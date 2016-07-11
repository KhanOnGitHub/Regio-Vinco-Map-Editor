/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rvme.data.DataManager;
import rvme.data.Subregion;
import rvme.file.FileManager;
import saf.AppTemplate;

/**
 *
 * @author eyb0s
 */
public class TestSave extends Application {

    AppTemplate app;
    ObservableList<Subregion> subregions;

    public static void main(String[] args) throws Exception {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        testSaveAndorra(primaryStage);
    }

    public void testSaveAndorra(Stage primaryStage) throws Exception {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(primaryStage);
        FileManager fileManager = new FileManager();
        fileManager.saveData(createAndorra(selectedFile), "./andorra/Andorra.json");
    }

    public DataManager createAndorra(File selectedFile) throws IOException, Exception {
        DataManager dataManager = new DataManager(app);
        FileManager fileManager = new FileManager();

        //LOAD RAW MAP DATA, IE MAP GEOGRAPHY
        //String filePath = "/raw_map_data/Andorra.json";
        dataManager.setBorderThickness(1.0);
        dataManager.setParentDirectory("./andorra/");
        fileManager.loadMap(dataManager, selectedFile.getAbsolutePath());

        //LOAD THE NATIONAL ANTHEM
        dataManager.setRegionName("Andorra");
        dataManager.setAudioName("Andorra National Anthem");
        dataManager.setAudioFileName("./andorra/Andorra National Anthem.mid");

        subregions = FXCollections.observableArrayList();
        subregions = dataManager.getSubregions();

        //HARD CODE THE SUBREGION NAME CAPITAL AND LEADERS
        subregions.get(0).setRegionName("Ordino");
        subregions.get(0).setRegionCapital("Ordino (town)");
        subregions.get(0).setRegionLeader("Ventura Espot");

        subregions.get(1).setRegionName("Canillo");
        subregions.get(1).setRegionCapital("Canillo (town)");
        subregions.get(1).setRegionLeader("Enric Casadevall Medrano");

        subregions.get(2).setRegionName("Encamp");
        subregions.get(2).setRegionCapital("Encamp (town)");
        subregions.get(2).setRegionLeader("Miquel Alís Font");

        subregions.get(3).setRegionName("Escaldes-Engordany");
        subregions.get(3).setRegionCapital("Escaldes-Engordany (town)");
        subregions.get(3).setRegionLeader("Montserrat Capdevila Pallarés");

        subregions.get(4).setRegionName("Ordino");
        subregions.get(4).setRegionCapital("Ordino");
        subregions.get(4).setRegionLeader("Ventura Espot");

        subregions.get(5).setRegionName("La Massana");
        subregions.get(5).setRegionCapital("La Massana (town)");
        subregions.get(5).setRegionLeader("Josep Areny");

        subregions.get(6).setRegionName("Andorra la Vella");
        subregions.get(6).setRegionCapital("Andorra la Vella (city)");
        subregions.get(6).setRegionLeader("Maria Rosa Ferrer Obiols");

        //GIVE EACH OF THE SUBREGIONS PATH TO THEIR RESPECTIVE FLAGS AND LEADERS
        for (int i = 0; i < subregions.size(); i++) {
            Subregion subregion = subregions.get(i);

            String flagPath = "file:./andorra/" + subregion.getSubregionName() + " Flag.png";
            subregion.setFlagPath(flagPath);

            String leaderPath = "file:./andorra/" + subregion.getSubregionLeader() + ".png";
            subregion.setLeaderPath(leaderPath);
        }

        //START ADDING THINGS TO MAP
        dataManager.setMapBackgroundColor("orange");
        dataManager.addImagetoMap("file:./andorra/Andorra Flag.png", 581, 390);
        //dataManager.addSubregionsToPane();

        return dataManager;

    }
}
