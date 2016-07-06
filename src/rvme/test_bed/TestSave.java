/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import java.io.IOException;
import rvme.data.DataManager;
import rvme.file.FileManager;
import saf.AppTemplate;

/**
 *
 * @author eyb0s
 */
public class TestSave {
    
    AppTemplate app;
    public static void main(String[] args) {
        
    }
    
    public void createAndorra() throws IOException {
         DataManager dataManager = (DataManager) app.getDataComponent();
         FileManager fileManager = (FileManager) app.getFileComponent();
         String filePath = "file:./raw_map_data/Andorra.json";
         fileManager.loadMap(dataManager, filePath);
    }
}
