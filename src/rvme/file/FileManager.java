/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;


import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import saf.ui.AppGUI;

/**
 *
 * @author eyb0s
 */
public class FileManager implements AppFileComponent {
    
    @Override
    public void saveData(AppDataComponent data, String filePath) {
        
    }
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        
    }
    
   public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
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
