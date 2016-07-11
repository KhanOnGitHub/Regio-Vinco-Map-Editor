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
import rvme.gui.Workspace;
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
    ObservableList<String> paths;
    String backgroundColor;
    int borderColorRed;
    int borderColorGreen;
    int borderColorBlue;
    
    String parentDirectory;
    
    double borderThickness;
    
    Pane map;

    String regionName;
    String audioName;
    String audioFileName;

    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;
        subregions = FXCollections.observableArrayList();
        imageViews = FXCollections.observableArrayList();
        paths = FXCollections.observableArrayList();
        backgroundColor = "";
        int borderColorRed = 0;
        int borderColorGreen = 0;
        int borderColorBlue = 0;
        regionName = "";
        audioName = "";
        audioFileName = "";
        map = new Pane();
        map.setPrefSize(802, 536);
        borderThickness = 1.0;
        parentDirectory = "";
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
    
    public String getParentDirectory() {
        return parentDirectory;
    }
    
    public void setParentDirectory(String directory) {
        parentDirectory = directory;
    }

    public void addSubregionsToPane() throws Exception {
        for (int i = 0; i < subregions.size(); i++) {
            map.getChildren().add(subregions.get(i).constructRegion());
        }
        
        Workspace workspace = new Workspace(app);
        workspace.getMapPane().getChildren().add(map);
    }

    public void setMapBackgroundColor(String color) {
        map.setStyle("-fx-background-color: " + color);
        backgroundColor = color;
    }
    
    public String getBackgroundColor() {
        return backgroundColor;
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
    
    public ObservableList<String> getPaths() {
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
    
    public double getBorderThickness() {
        return borderThickness;
    }
    
    public void setBorderThickness(double borderThickness) {
        this.borderThickness = borderThickness;
    }
    
    public void setBorderColorRed(int red) {
        borderColorRed = red;
    }
    
    public void setBorderColorGreen(int green) {
        borderColorGreen = green;
    }
    
    public void setBorderColorBlue(int blue) {
        borderColorBlue = blue;
    }
    
    public int getBorderColorRed() {
        return borderColorRed;
    }
    
    public int getBorderColorBlue() {
        return borderColorBlue;
    }
    
    public int getBorderColorGreen() {
        return borderColorGreen;
    }
    
    public void printData() {
        System.out.println("Region Name: " + regionName);
        System.out.println("Audio Name: " + audioName);
        System.out.println("Audio File Name: " + audioFileName);
        System.out.println("Border Thickness: " + borderThickness);
        
        for(int i = 0; i < paths.size(); i ++)
            System.out.println("Image Path: " + paths.get(i));
        System.out.println("Border Color: " + borderColorRed + ", " + borderColorGreen + ", " +borderColorBlue);
        System.out.println("Background Color: " + backgroundColor);
        
        for(int i = 0; i < subregions.size(); i++) {
            subregions.get(i).printProperties();
        }
        
    }

    @Override
    public void reset() {

    }

    public void clearSubregions() {
        subregions.clear();
    }
}
