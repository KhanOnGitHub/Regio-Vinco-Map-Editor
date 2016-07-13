/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import rvme.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppDataComponent;
import saf.ui.AppProgressBarDialogSingleton;

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
    double mapZoom;

    Pane map;
    Pane randomPane;

    String regionName;
    String audioName;
    String audioFileName;

    ProgressBar loadingProgress;
    Label loadingLabel;
    GridPane loadingGrid;

    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;
        subregions = FXCollections.observableArrayList();
        images = FXCollections.observableArrayList();
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
        randomPane = new Pane();
        map.setPrefSize(802, 536);
        borderThickness = 1.0;
        parentDirectory = "";
        mapZoom = 1.0;
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
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.drawOnMap(subregions);
    }

    public void setMapBackgroundColor(String color) {
        randomPane.setStyle("-fx-background-color: " + color);
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
        x = (x + 180.0) * (802 / 360);
        return x;
    }

    public ObservableList<ImageView> getImageViews() {
        return imageViews;
    }

    public double convertLat(double y) {
        //SOME CONVERSION

        y = (90.0 - y) * (536 / 180);
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

    public double getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(double mapZoom) {
        this.mapZoom = mapZoom;
    }

    public void printData() {
        System.out.println("Region Name: " + regionName);
        System.out.println("Audio Name: " + audioName);
        System.out.println("Audio File Name: " + audioFileName);
        System.out.println("Border Thickness: " + borderThickness);

        for (int i = 0; i < paths.size(); i++) {
            System.out.println("Image Path: " + paths.get(i));
        }
        System.out.println("Border Color: " + borderColorRed + ", " + borderColorGreen + ", " + borderColorBlue);
        System.out.println("Background Color: " + backgroundColor);
        System.out.println("Map Zoom: " + mapZoom);

        for (int i = 0; i < subregions.size(); i++) {
            subregions.get(i).printProperties();
        }

    }

    public void loadingProgress() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        AppProgressBarDialogSingleton loadingDialog = AppProgressBarDialogSingleton.getSingleton();
        loadingProgress = new ProgressBar();
        loadingProgress.setProgress(0f);
        loadingLabel = new Label();
        //REPLACE WITH XML PROPERTY
        loadingLabel.setText("Loading of work in progress");
        loadingGrid = new GridPane();
        loadingGrid.add(loadingLabel, 0, 0);
        loadingGrid.add(loadingProgress, 1, 0);
        Scene scene = new Scene(loadingGrid, 300, 300);
        scene.getStylesheets().add("rvme/css/rvme_style.css");
        loadingGrid.getStyleClass().add("gridPane");
        loadingDialog.setScene(scene);
        //REPLACE WITH XML PROPERTY
        loadingDialog.setTitle("Loading");
        loadingDialog.show();
    }

    public void setLoadingProgress(double value) {
        loadingProgress.setProgress(value);
    }

    public void setLabelText(String text) {
        loadingLabel.setText(text);
    }

    /* public void drawMap() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        AppGUI gui = app.getGUI();
        for (int i = 0; i < polygons.size(); i++) {
            Polygon poly = polygons.get(i).reconstructRegion();
            poly.setFill(Color.GREEN);
            workspace.getWorkspace().getChildren().add(poly);
        }

    }*/
    @Override
    public void reset() {
        subregions.clear();
        imageViews.clear();
        images.clear();
        paths.clear();
        backgroundColor = "";
        borderColorRed = borderColorGreen = borderColorBlue = 0;

        parentDirectory = "";

        borderThickness = 1.0f;
        mapZoom = 1.0f;

        map.getChildren().clear();
        randomPane.getChildren().clear();

        regionName="";
        audioName="";
        audioFileName="";
    }

    public void clearSubregions() {
        subregions.clear();
    }

}
