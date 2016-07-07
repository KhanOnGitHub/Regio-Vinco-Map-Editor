/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
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
    static final String JSON_X = "X";
    static final String JSON_Y = "Y";
    
    MapEditorApp app;

    @Override
    public void saveData(AppDataComponent data, String filePath) {
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
