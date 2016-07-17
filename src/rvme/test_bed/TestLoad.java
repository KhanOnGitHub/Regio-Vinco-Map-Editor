/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
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
        Pane pane = new Pane();
        DataManager dataManager = testLoadAndorra(primaryStage);
        for (int i = 0; i < dataManager.getSubregions().size(); i++) {
           // pane.getChildren().add(dataManager.getSubregions().get(i).constructRegion());
        }
        Scene scene = new Scene(pane, 1000, 1000);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public DataManager testLoadAndorra(Stage primaryStage) throws Exception {
        /* FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(primaryStage);*/
        FileManager fileManager = new FileManager();
        DataManager dataManager = new DataManager(app);
        fileManager.loadDataTest(dataManager, "./andorra/Andorra.json");

        dataManager.printData();

        return dataManager;
    }
}
