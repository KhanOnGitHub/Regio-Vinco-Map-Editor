/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppDataComponent;
import saf.ui.AppGUI;

/**
 *
 * @author eyb0s
 */
public class DataManager implements AppDataComponent {

    AppTemplate app;

    ObservableList<Subregion> subregions;
    ObservableList<ImageView> imageViews;
    ObservableList<Image> images;
    ArrayList<String> paths;
    String backgroundColor;
    String borderColor;

    Pane map;

    String regionName;
    String audioName;
    String audioFileName;

    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;
        subregions = FXCollections.observableArrayList();
        imageViews = FXCollections.observableArrayList();
        paths = new ArrayList();
        backgroundColor = "";
        borderColor = "";
        regionName = "";
        audioName = "";
        audioFileName = "";
        map = new Pane();
        map.setPrefSize(802, 536);
    }

    public ObservableList<Subregion> getSubregions() {
        return subregions;
    }

    public void addSubregion(Subregion subregion) {
        subregions.add(subregion);
    }

    public void removeSubregion(Subregion subregion) {
        subregions.remove(subregion);
    }

    public void addSubregionsToPane() {
        for (int i = 0; i < subregions.size(); i++) {
            map.getChildren().add(subregions.get(i).constructRegion());
        }
    }

    public void setMapBackgroundColor(String color) {
        map.setStyle("-fx-background-color: " + color);
    }

    public void addImagetoMap(String imageName, double x, double y) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:./andorra/" + imageName;
        Image newImage = new Image(imagePath);
        paths.add(imagePath);
        ImageView newImageView = new ImageView(newImage);

        newImageView.setX(x);
        newImageView.setY(y);

        imageViews.add(newImageView);

        map.getChildren().add(imageViews.get(imageViews.indexOf(newImageView)));
    }
    
    public ArrayList<String> getPaths() {
        return paths;
    }

    public Subregion getSubregionWithName(String name) {
        Subregion returnedSubregion = null;
        for (int i = 0; i < subregions.size(); i++) {
            if (subregions.get(i).getSubregionName().equals(name)) {
                returnedSubregion = subregions.get(i);
            }
        }
        return returnedSubregion;
    }

    public double convertLong(double x) {
        //SOME CONVERSION
        AppGUI gui = app.getGUI();
        double screenWidth = gui.getPrimaryScene().getWidth();
        x = (x + 180.0) * (screenWidth / 360);
        return x;
    }
    
    public ObservableList<ImageView> getImageViews() {
        return imageViews;
    }

    public double convertLat(double y) {
        //SOME CONVERSION
        AppGUI gui = app.getGUI();
        double screenHeight = gui.getPrimaryScene().getHeight();
        y = (90.0 - y) * (screenHeight / 180);
        return y;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAudioName() {
        return audioName;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }
    
    public Pane getMap() {
        return map;
    }

    @Override
    public void reset() {

    }

    public void clearSubregions() {
        subregions.clear();
    }
}
