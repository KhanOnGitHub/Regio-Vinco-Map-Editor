/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import properties_manager.PropertiesManager;
import rvme.MapEditorApp;
import rvme.data.DataManager;
import rvme.data.Subregion;

import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import saf.ui.AppMessageDialogSingleton;

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
    static final String JSON_REGION = "REGION NAME";
    static final String JSON_BORDER = "BORDER_COLOR";
    static final String JSON_MAP_COLOR = "MAP_COLOR";
    static final String JSON_THICKNESS = "BORDER_THICKNESS";
    static final String JSON_RED = "R";
    static final String JSON_GREEN = "G";
    static final String JSON_BLUE = "B";
    static final String JSON_DIRECTORY = "PARENT_DIRECTORY";
    static final String JSON_CONVERTED = "CONVERTED";

    static final String JSON_REGION_NAME = "REGION_NAME";
    static final String JSON_REGION_CAPITAL = "REGION_CAPITAL";
    static final String JSON_REGION_LEADER = "REGION_LEADER";
    static final String JSON_FLAG_PATH = "FLAG_PATH";
    static final String JSON_LEADER_PATH = "LEADER_PATH";
    static final String JSON_AUDIO = "AUDIO_NAME";
    static final String JSON_AUDIO_FILE = "AUDIO_FILE_NAME";
    static final String JSON_IMAGE = "IMAGE_PATH";
    static final String JSON_IMAGES = "IMAGE_PATHS";
    static final String JSON_ZOOM = "MAP_ZOOM";

    MapEditorApp app;

    File currentMapFile;
    File folderInParent;

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;

        //FIRST NAME, BORDER COLOR, AND THICKNESS
        String regionName = dataManager.getRegionName();
        String parentRegionDirectory = dataManager.getParentDirectory();
        int borderColorRed = dataManager.getBorderColorRed();
        int borderColorGreen = dataManager.getBorderColorGreen();
        int borderColorBlue = dataManager.getBorderColorBlue();
        double borderThickness = dataManager.getBorderThickness();
        double mapZoom = dataManager.getMapZoom();
        String backgroundColor = dataManager.getBackgroundColor();
        String audioName = dataManager.getAudioName();
        String audioFileName = dataManager.getAudioFileName();
        boolean converted = dataManager.getConverted();

        //JSON FOR THE PATHS OF IMAGES THAT ARENT LEADERS OR FLAGS
        ObservableList<String> paths = dataManager.getPaths();
        ObservableList<ImageView> imageViews = dataManager.getImageViews();
        JsonArrayBuilder imagePathBuilder = Json.createArrayBuilder();
        for (int l = 0; l < paths.size(); l++) {
            JsonObject imageFilePath = Json.createObjectBuilder()
                    .add(JSON_IMAGE, paths.get(l))
                    .add(JSON_X, imageViews.get(l).getX())
                    .add(JSON_Y, imageViews.get(l).getY()).build();
            imagePathBuilder.add(imageFilePath);
        }
        JsonArray imagePaths = imagePathBuilder.build();

        //BUILD A JSON ARRAY BUILDER FOR NECESSARY ARRAYS
        ObservableList<Subregion> subregions = dataManager.getSubregions();
        JsonObject dataManagerJSO;
        JsonArrayBuilder subregionBuilder = Json.createArrayBuilder();
        for (int i = 0; i < subregions.size(); i++) {
            Subregion currentSubregion = subregions.get(i);
            ArrayList<Double> currentSubregionCoordinates = currentSubregion.getPoints();

            //MAKE THE LIST OF SUBREGION PROPERTIES
            JsonArrayBuilder subregionPropertiesBuilder = Json.createArrayBuilder();

            JsonObject subregionPropertiesJson = Json.createObjectBuilder()
                    .add(JSON_REGION_NAME, currentSubregion.getSubregionName())
                    .add(JSON_REGION_CAPITAL, currentSubregion.getSubregionCapital())
                    .add(JSON_REGION_LEADER, currentSubregion.getSubregionLeader())
                    .add(JSON_FLAG_PATH, currentSubregion.getFlagPath())
                    .add(JSON_LEADER_PATH, currentSubregion.getLeaderPath())
                    .add(JSON_RED, currentSubregion.getRed())
                    .add(JSON_GREEN, currentSubregion.getGreen())
                    .add(JSON_BLUE, currentSubregion.getBlue()).build();
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

                if (j == currentSubregionCoordinates.size() - 2) {
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

        JsonArrayBuilder borderColorBuilder = Json.createArrayBuilder();

        JsonObject borderColorObject = Json.createObjectBuilder()
                .add(JSON_RED, borderColorRed)
                .add(JSON_GREEN, borderColorGreen)
                .add(JSON_BLUE, borderColorBlue).build();

        borderColorBuilder.add(borderColorObject);

        JsonArray borderColorArray = borderColorBuilder.build();

        dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_REGION, regionName)
                .add(JSON_DIRECTORY, parentRegionDirectory)
                .add(JSON_AUDIO, audioName)
                .add(JSON_AUDIO_FILE, audioFileName)
                .add(JSON_THICKNESS, borderThickness)
                .add(JSON_IMAGES, imagePaths)
                .add(JSON_BORDER, borderColorArray)
                .add(JSON_MAP_COLOR, backgroundColor)
                .add(JSON_ZOOM, mapZoom)
                .add(JSON_CONVERTED, converted)
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
    public void loadData(AppDataComponent data, String filePath, File chosenFile) throws IOException, Exception {
        JsonObject json = loadJSONFile(filePath);

        DataManager dataManager = (DataManager) data;

        JsonString jsonRegionName = json.getJsonString(JSON_REGION);
        dataManager.setRegionName(jsonRegionName.getString());

        JsonString jsonAudioName = json.getJsonString(JSON_AUDIO);
        dataManager.setAudioName(jsonAudioName.getString());

        JsonString jsonAudioFileName = json.getJsonString(JSON_AUDIO_FILE);
        dataManager.setAudioFileName(jsonAudioFileName.getString());

        JsonString jsonParentRegionDirectory = json.getJsonString(JSON_DIRECTORY);
        dataManager.setParentDirectory(jsonParentRegionDirectory.getString());

        dataManager.setConverted(json.getBoolean(JSON_CONVERTED));

        JsonArray jsonImagePaths = json.getJsonArray(JSON_IMAGES);
        for (int i = 0; i < jsonImagePaths.size(); i++) {
            JsonObject jsonImagePath = jsonImagePaths.getJsonObject(i);
            JsonString jsonImageString = jsonImagePath.getJsonString(JSON_IMAGE);
            double x = getDataAsDouble(jsonImagePath, JSON_X);
            double y = getDataAsDouble(jsonImagePath, JSON_Y);
            dataManager.getPaths().add(jsonImageString.getString());
            Image newImage = new Image("file:" + jsonImageString.getString());
            ImageView newImageView = new ImageView(newImage);
            newImageView.setX(x);
            newImageView.setY(y);
            dataManager.setupImageViewListener(newImageView);
            dataManager.getImageViews().add(newImageView);
        }

        JsonNumber jsonBorderThickness = json.getJsonNumber(JSON_THICKNESS);
        dataManager.setBorderThickness(jsonBorderThickness.doubleValue());

        JsonArray jsonBorderColorsArray = json.getJsonArray(JSON_BORDER);
        JsonObject jsonBorderColors = jsonBorderColorsArray.getJsonObject(0);
        dataManager.setBorderColorBlue(getDataAsInt(jsonBorderColors, JSON_BLUE));
        dataManager.setBorderColorRed(getDataAsInt(jsonBorderColors, JSON_RED));
        dataManager.setBorderColorGreen(getDataAsInt(jsonBorderColors, JSON_GREEN));

        JsonString jsonMapColor = json.getJsonString(JSON_MAP_COLOR);
        dataManager.setMapBackgroundColor(jsonMapColor.getString());

        JsonNumber jsonMapZoom = json.getJsonNumber(JSON_ZOOM);
        dataManager.setMapZoom(jsonMapZoom.doubleValue());

        loadMap(data, filePath, chosenFile);

        dataManager.addSubregionsToPane();
        dataManager.addImagesToMap();

        currentMapFile = chosenFile;
        folderInParent = new File(dataManager.getParentDirectory() + "/" + dataManager.getRegionName());

    }

    public void loadDataTest(AppDataComponent data, String filePath) throws IOException, Exception {
        JsonObject json = loadJSONFile(filePath);

        DataManager dataManager = (DataManager) data;

        JsonString jsonRegionName = json.getJsonString(JSON_REGION);
        dataManager.setRegionName(jsonRegionName.getString());

        JsonString jsonAudioName = json.getJsonString(JSON_AUDIO);
        dataManager.setAudioName(jsonAudioName.getString());

        JsonString jsonAudioFileName = json.getJsonString(JSON_AUDIO_FILE);
        dataManager.setAudioName(jsonAudioFileName.getString());

        JsonArray jsonImagePaths = json.getJsonArray(JSON_IMAGES);
        for (int i = 0; i < jsonImagePaths.size(); i++) {
            JsonObject jsonImagePath = jsonImagePaths.getJsonObject(i);
            JsonString jsonImageString = jsonImagePath.getJsonString(JSON_IMAGE);
            double x = getDataAsDouble(jsonImagePath, JSON_X);
            double y = getDataAsDouble(jsonImagePath, JSON_Y);
            dataManager.getPaths().add(jsonImageString.getString());
            Image newImage = new Image(jsonImageString.getString());
            ImageView newImageView = new ImageView(newImage);
            newImageView.setX(x);
            newImageView.setY(y);
            dataManager.getImageViews().add(newImageView);
        }

        JsonNumber jsonBorderThickness = json.getJsonNumber(JSON_THICKNESS);
        dataManager.setBorderThickness(jsonBorderThickness.doubleValue());

        JsonArray jsonBorderColorsArray = json.getJsonArray(JSON_BORDER);
        JsonObject jsonBorderColors = jsonBorderColorsArray.getJsonObject(0);
        dataManager.setBorderColorBlue(getDataAsInt(jsonBorderColors, JSON_BLUE));
        dataManager.setBorderColorRed(getDataAsInt(jsonBorderColors, JSON_RED));
        dataManager.setBorderColorGreen(getDataAsInt(jsonBorderColors, JSON_GREEN));

        JsonString jsonMapColor = json.getJsonString(JSON_MAP_COLOR);
        dataManager.setMapBackgroundColor(jsonMapColor.getString());

        JsonNumber jsonMapZoom = json.getJsonNumber(JSON_ZOOM);
        dataManager.setMapZoom(jsonMapZoom.doubleValue());

        // loadMap(data, filePath, chosenFile);
        //dataManager.addSubregionsToPane();
    }

    public double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    public int getDataAsInt(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigIntegerValue().intValue();
    }

    public String getDataAsString(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonString string = (JsonString) value;
        return string.getString();
    }

    public void loadMap(AppDataComponent data, String filePath, File chosenFile) throws IOException {
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
            Subregion subregion = new Subregion();
            JsonArray jsonSubregionProperties = jsonItemForSubregionPolygons.getJsonArray(JSON_SUBREGION_PROPERTIES);
            JsonObject jsonSubregionPropertiesObject = jsonSubregionProperties.getJsonObject(0);

            JsonString regionNameString = jsonSubregionPropertiesObject.getJsonString(JSON_REGION_NAME);

            JsonString regionCapitalString = jsonSubregionPropertiesObject.getJsonString(JSON_REGION_CAPITAL);
            JsonString regionLeaderString = jsonSubregionPropertiesObject.getJsonString(JSON_REGION_LEADER);
            JsonString regionFlagString = jsonSubregionPropertiesObject.getJsonString(JSON_FLAG_PATH);
            JsonString regionLeaderPathString = jsonSubregionPropertiesObject.getJsonString(JSON_LEADER_PATH);

            String regionName = regionNameString.getString();
            String regionCapital = regionCapitalString.getString();
            String regionLeader = regionLeaderString.getString();
            String regionFlag = regionFlagString.getString();
            String regionLeaderPath = regionLeaderPathString.getString();

            int red = getDataAsInt(jsonSubregionPropertiesObject, JSON_RED);
            int green = getDataAsInt(jsonSubregionPropertiesObject, JSON_GREEN);
            int blue = getDataAsInt(jsonSubregionPropertiesObject, JSON_BLUE);

            subregion.setRegionName(regionName);
            subregion.setRegionCapital(regionCapital);
            subregion.setRegionLeader(regionLeader);
            subregion.setFlagPath(regionFlag);
            subregion.setLeaderPath(regionLeaderPath);
            subregion.setRGB(red, green, blue);

            JsonArray jsonSubregionPolygons = jsonItemForSubregionPolygons.getJsonArray(JSON_SUBREGION_POLYGONS);
            for (int j = 0; j < jsonSubregionPolygons.size(); j++) { // json 2
                JsonArray jsonCoordinates = jsonSubregionPolygons.getJsonArray(j);
                if (!dataManager.getConverted()) {
                    for (int k = 0; k < jsonCoordinates.size(); k++) {
                        JsonObject jsonCoordinatesObject = jsonCoordinates.getJsonObject(k);
                        double x = getDataAsDouble(jsonCoordinatesObject, JSON_X);
                        double y = getDataAsDouble(jsonCoordinatesObject, JSON_Y);
                        x = dataManager.convertLong(x);
                        y = dataManager.convertLat(y);
                        subregion.addPoints(x, y);
                        if (k == jsonCoordinates.size() - 1) {
                            subregion.setRegion(subregion.constructRegion(dataManager.getBorderThickness(), dataManager.getBorderColorRed(), dataManager.getBorderColorGreen(), dataManager.getBorderColorBlue()));
                            dataManager.addSubregion(subregion);
                            dataManager.setupRegionListener(subregion);
                        }
                    }
                } else {
                    for (int k = 0; k < jsonCoordinates.size(); k++) {
                        JsonObject jsonCoordinatesObject = jsonCoordinates.getJsonObject(k);
                        double x = getDataAsDouble(jsonCoordinatesObject, JSON_X);
                        double y = getDataAsDouble(jsonCoordinatesObject, JSON_Y);
                        subregion.addPoints(x, y);
                        if (k == jsonCoordinates.size() - 1) {
                            subregion.setRegion(subregion.constructRegion(dataManager.getBorderThickness(), dataManager.getBorderColorRed(), dataManager.getBorderColorGreen(), dataManager.getBorderColorBlue()));
                            dataManager.addSubregion(subregion);
                            dataManager.setupRegionListener(subregion);
                        }
                    }

                }
            }
        }

        //LOAD ALL IMAGES
        /*JsonArray jsonImages = json.getJsonArray(JSON_IMAGES);
        for(int i = 0; i < jsonImages.size(); i++)
        {
            JsonObject jsonImageProperties = jsonImages.getJsonObject(i);
            String imagePath = jsonImageProperties.getString(JSON_IMAGE);
            double x = getDataAsDouble(jsonImageProperties, JSON_X);
            double y = getDataAsDouble(jsonImageProperties, JSON_Y);
            x = dataManager.convertLong(x);
            y = dataManager.convertLat(y);
            Image newImage = new Image(imagePath);
            ImageView newImageView = new ImageView(newImage);
            newImageView.setX(x);
            newImageView.setY(y);
            dataManager.getImageViews().add(newImageView);
        }*/
        dataManager.setConverted(true);

    }

    public void loadRawMap(AppDataComponent data, String filePath) throws IOException {
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
                if (!dataManager.getConverted()) {
                    for (int k = 0; k < jsonCoordinates.size(); k++) {
                        JsonObject jsonCoordinatesObject = jsonCoordinates.getJsonObject(k);
                        double x = getDataAsDouble(jsonCoordinatesObject, JSON_X);
                        double y = getDataAsDouble(jsonCoordinatesObject, JSON_Y);
                        x = dataManager.convertLong(x);
                        y = dataManager.convertLat(y);
                        subregion.addPoints(x, y);
                        if (k == jsonCoordinates.size() - 1) {
                            subregion.setRegion(subregion.constructRegion(dataManager.getBorderThickness(), dataManager.getBorderColorRed(), dataManager.getBorderColorGreen(), dataManager.getBorderColorBlue()));
                            subregion.getRegion().setStrokeWidth(1); //PUT ANOTHER JSON FIELD HERE
                            subregion.getRegion().setStroke(Color.BLACK); //PUT ANOTHER JSON FIELD HERE WHEN IMPLEMENTED
                            dataManager.addSubregion(subregion);
                            dataManager.setupRegionListener(subregion);
                        }
                    }
                } else {
                    for (int k = 0; k < jsonCoordinates.size(); k++) {
                        JsonObject jsonCoordinatesObject = jsonCoordinates.getJsonObject(k);
                        double x = getDataAsDouble(jsonCoordinatesObject, JSON_X);
                        double y = getDataAsDouble(jsonCoordinatesObject, JSON_Y);
                        subregion.addPoints(x, y);
                        if (k == jsonCoordinates.size() - 1) {
                            subregion.setRegion(subregion.constructRegion(dataManager.getBorderThickness(), dataManager.getBorderColorRed(), dataManager.getBorderColorGreen(), dataManager.getBorderColorBlue()));
                            subregion.getRegion().setStrokeWidth(1); //PUT ANOTHER JSON FIELD HERE
                            subregion.getRegion().setStroke(Color.BLACK); //PUT ANOTHER JSON FIELD HERE WHEN IMPLEMENTED
                            dataManager.addSubregion(subregion);
                            dataManager.setupRegionListener(subregion);
                        }
                    }

                }
            }
        }
        dataManager.setConverted(true);
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

    @Override
    public void newMap(AppDataComponent data, String regionName, String filePath, String directoryPath) throws IOException, Exception {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        DataManager dataManager = (DataManager) data;
        dataManager.reset();
        dataManager.setConverted(false);
        dataManager.setRegionName(regionName);
        loadRawMap(data, filePath);
        currentMapFile = new File("./work/", regionName + ".json");
        currentMapFile.createNewFile();
        folderInParent = new File(directoryPath + "/" + regionName);
        folderInParent.mkdirs();
        dataManager.setParentDirectory(directoryPath);
        saveData(data, currentMapFile.getPath());
        dataManager.addSubregionsToPane();
    }

    @Override
    public File getMapFile() {
        return currentMapFile;
    }

    @Override
    public void setMapFile(File file) {
        currentMapFile = file;
    }

    public void updateFiles(String fileName) throws IOException {
        AppMessageDialogSingleton updateFilesMessage = AppMessageDialogSingleton.getSingleton();
        Files.move(currentMapFile.toPath(), currentMapFile.toPath().resolveSibling(fileName + ".json"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        folderInParent.renameTo(new File(folderInParent.getParent() + "/" + fileName));
        updateFilesMessage.show("Region Name Change", "Region name has been changed along with the work file and directory names.");

    }

}
