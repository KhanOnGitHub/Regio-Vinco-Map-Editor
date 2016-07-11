/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import rvme.MapEditorApp;
import rvme.data.DataManager;
import rvme.data.Subregion;

import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 *
 * @author eyb0s
 */
public class FileManager implements AppFileComponent {

    static final String JSON_NUMBER_OF_SUBREGIONS = "NUMBER_OF_SUBREGIONS";
    static final String JSON_SUBREGIONS = "SUBREGIONS";
    static final String JSON_NUMBER_OF_SUBREGION_POLYGONS = "NUMBER_OF_SUBREGION_POLYGONS";
    static final String JSON_SUBREGION_POLYGONS = "SUBREGION_POLYGONS";
    static final String JSON_SUBREGION_PROPERTIES = "SUBREGION_PROPERTIES";
    static final String JSON_X = "X";
    static final String JSON_Y = "Y";

    static final String JSON_REGION_NAME = "REGION_NAME";
    static final String JSON_REGION_CAPITAL = "REGION_CAPITAL";
    static final String JSON_REGION_LEADER = "REGION_LEADER";

    MapEditorApp app;

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;

        //FIRST NAME, BORDER COLOR, AND THICKNESS
        String regionName = dataManager.getRegionName();
        int borderColorRed = dataManager.getBorderColorRed();
        int borderColorGreen = dataManager.getBorderColorGreen();
        int borderColorBlue = dataManager.getBorderColorBlue();
        double borderThickness = dataManager.getBorderThickness();

        //BUILD A JSON ARRAY BUILDER FOR NECESSARY ARRAYS
        ObservableList<Subregion> subregions = dataManager.getSubregions();
        JsonObject dataManagerJSO;
        JsonArrayBuilder subregionBuilder = Json.createArrayBuilder();
        for (int i = 0; i < subregions.size(); i++) {
            Subregion currentSubregion = subregions.get(i);
            ArrayList<Double> currentSubregionCoordinates = currentSubregion.getPoints();
           
            //MAKE THE LIST OF SUBREGION PROPERTIES
            JsonArrayBuilder subregionPropertiesBuilder = Json.createArrayBuilder();
            String testSubregionLeader = currentSubregion.getSubregionLeader();
            System.out.println(testSubregionLeader);
            JsonObject subregionPropertiesJson = Json.createObjectBuilder()
                    .add(JSON_REGION_NAME, currentSubregion.getSubregionName())
                    .add(JSON_REGION_CAPITAL, currentSubregion.getSubregionCapital())
                    .add(JSON_REGION_LEADER, currentSubregion.getSubregionLeader()).build();
            subregionPropertiesBuilder.add(subregionPropertiesJson);
            
            //THE ARRAY TO ADD TO THE LARGER OBJECT LATER
            JsonArray subregionPropertiesArray = subregionPropertiesBuilder.build();
            
            //FINALLY CREATE THE SUBREGION PROPERTIES OBJECT
            //JsonObject subregionPropertiesFinalObject = Json.createObjectBuilder()
             //       .add(JSON_SUBREGION_PROPERTIES, subregionPropertiesArray).build();
            
            
            JsonArrayBuilder subregionCoordinatesBuilder = Json.createArrayBuilder();
            JsonArrayBuilder subregionCoordinatesArrayBuilder = Json.createArrayBuilder();
            
            for (int j = 0; j < currentSubregionCoordinates.size(); j += 2) {
                JsonObject coordinatePair = Json.createObjectBuilder()
                        .add(JSON_X, currentSubregionCoordinates.get(j))
                        .add(JSON_Y, currentSubregionCoordinates.get(j + 1)).build();
                subregionCoordinatesArrayBuilder.add(coordinatePair);
                
                if(j == currentSubregionCoordinates.size()-2) {
                    JsonArray subregionCoordinatesInnerArray = subregionCoordinatesArrayBuilder.build();
                    subregionCoordinatesBuilder.add(subregionCoordinatesInnerArray);  
                }
            }
            
            //ARRAY WITH COORDINATES TO ADD TO LARGER OBJECT LATER
            JsonArray subregionCoordinatesArray = subregionCoordinatesBuilder.build(); 
            
            JsonObject subregionObject = Json.createObjectBuilder()
                    .add(JSON_SUBREGION_PROPERTIES, subregionPropertiesArray)
                    .add(JSON_SUBREGION_POLYGONS, subregionCoordinatesArray).build();
            subregionBuilder.add(subregionObject);
        }
        
        JsonArray subregionsArray = subregionBuilder.build();
        
        dataManagerJSO = Json.createObjectBuilder()
                
                .add(JSON_SUBREGIONS, subregionsArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
                

    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {

    }

    public double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    public void loadMap(AppDataComponent data, String filePath) throws IOException {
        //CLEAR OLD DATA
        DataManager dataManager = (DataManager) data;
        dataManager.clearSubregions();

        // LOAD JSON FILE WITH THE DATA 
        JsonObject json = loadJSONFile(filePath);

        //LOAD ALL REGIONS
        JsonArray jsonSubRegions = json.getJsonArray(JSON_SUBREGIONS);

        for (int i = 0; i < jsonSubRegions.size(); i++) //json 1 
        {
            JsonObject jsonItemForSubregionPolygons = jsonSubRegions.getJsonObject(i);
            JsonArray jsonSubregionPolygons = jsonItemForSubregionPolygons.getJsonArray(JSON_SUBREGION_POLYGONS);
            for (int j = 0; j < jsonSubregionPolygons.size(); j++) { // json 2
                JsonArray jsonCoordinates = jsonSubregionPolygons.getJsonArray(j);
                Subregion subregion = new Subregion();
                for (int k = 0; k < jsonCoordinates.size(); k++) {
                    JsonObject jsonCoordinatesObject = jsonCoordinates.getJsonObject(k);
                    double x = getDataAsDouble(jsonCoordinatesObject, JSON_X);
                    double y = getDataAsDouble(jsonCoordinatesObject, JSON_Y);
//                    x = dataManager.convertLong(x);
                    //                  y = dataManager.convertLat(y);
                    subregion.addPoints(x, y);
                    if (k == jsonCoordinates.size() - 1) {
                        subregion.setRegion(subregion.constructRegion());
                        subregion.getRegion().setStrokeWidth(1); //PUT ANOTHER JSON FIELD HERE
                        subregion.getRegion().setStroke(Color.BLACK); //PUT ANOTHER JSON FIELD HERE WHEN IMPLEMENTED
                        dataManager.addSubregion(subregion);
                    }
                }
            }
        }
        //dataManager.drawMap();
        System.out.println(dataManager.getSubregions().size());
    }

    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {

    }

}
