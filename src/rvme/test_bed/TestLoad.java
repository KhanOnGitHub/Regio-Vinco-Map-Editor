/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rvme.data.DataManager;
import rvme.file.FileManager;
import saf.AppTemplate;

/**
 *
 * @author eyb0s
 */
public class TestLoad extends Application {
    AppTemplate app;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        testLoadAndorra(primaryStage);
    }
    
    public void testLoadAndorra(Stage primaryStage) throws Exception {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(primaryStage);
        FileManager fileManager = new FileManager();
        DataManager dataManager = new DataManager(app);
        fileManager.loadData(dataManager, "./andorra/Andorra.json");
        
        System.out.println(dataManager.getSubregions().get(0).getPoints());
    }
}
