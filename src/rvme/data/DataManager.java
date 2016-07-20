/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import java.io.File;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javax.imageio.ImageIO;
import rvme.audio.AudioManager;
import rvme.file.FileManager;
import rvme.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppDataComponent;
import saf.ui.AppChangeBorderThicknessSingleton;
import saf.ui.AppChangeDimensionsDialogSingleton;
import saf.ui.AppChangeMapBGDialogSingleton;
import saf.ui.AppChangeMapBorderColorDialogSingleton;
import saf.ui.AppGUI;
import saf.ui.AppMessageDialogSingleton;

/**
 *
 * @author eyb0s
 */
public class DataManager implements AppDataComponent {

    AppTemplate app;
    AppGUI gui;

    ObservableList<Subregion> subregions;
    ObservableList<ImageView> imageViews;
    ObservableList<Image> images;
    ObservableList<String> paths;
    String backgroundColor;
    int borderColorRed;
    int borderColorGreen;
    int borderColorBlue;
    int imageViewSelected;
    int mapWidth;
    int mapHeight;
    double mapScrollLocationX;
    double mapScrollLocationY;

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

    boolean converted;
    boolean playing;

    AudioManager audio;

    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;
        subregions = FXCollections.observableArrayList();
        images = FXCollections.observableArrayList();
        imageViews = FXCollections.observableArrayList();
        paths = FXCollections.observableArrayList();
        backgroundColor = "#FFFFFF";
        borderColorRed = 255;
        borderColorGreen = 255;
        borderColorBlue = 255;
        regionName = "?";
        audioName = "?";
        audioFileName = "?";
        map = new Pane();
        randomPane = new Pane();
        map.setPrefSize(802, 536);
        borderThickness = 1.0;
        parentDirectory = "?";
        mapZoom = 1.0;
        mapScrollLocationX = 0.0;
        mapScrollLocationY = 0.0;
        mapWidth = 802;
        mapHeight = 536;
        converted = false;
        playing = false;
        audio = new AudioManager();
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
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        backgroundColor = color;
        workspace.getMapPane().setBackground(new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public boolean getConverted() {
        return converted;
    }

    public void setConverted(boolean value) {
        converted = value;
    }

    public void addImagesToMap() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.imagesOnMap(imageViews);
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

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public double getMapScrollLocationX() {
        return mapScrollLocationX;
    }

    public void setMapScrollLocationX(double mapScrollLocationX) {
        this.mapScrollLocationX = mapScrollLocationX;
    }

    public double getMapScrollLocationY() {
        return mapScrollLocationY;
    }

    public void setMapScrollLocationY(double mapScrollLocationY) {
        this.mapScrollLocationY = mapScrollLocationY;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
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

    public void setLabelText(String text) {
        loadingLabel.setText(text);
    }

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

        regionName = "";
        audioName = "";
        audioFileName = "";
    }

    public void clearSubregions() {
        subregions.clear();
    }

    @Override
    public void changeMapName(String mapName) throws IOException {
        FileManager fileManager = (FileManager) app.getFileComponent();
        regionName = mapName;
        fileManager.updateFiles(regionName);
    }

    @Override
    public void addImage(String imagePath) {
        Image newImage = new Image("file:" + imagePath);
        ImageView newImageView = new ImageView(newImage);
        imageViews.add(newImageView);
        setupImageViewListener(newImageView);
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.imageOnMap(imageViews.get(imageViews.size() - 1));
    }

    @Override
    public void removeImage() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        imageViews.remove(imageViewSelected);
        workspace.removeImageOnMap();

    }

    @Override
    public void changeMapBG() {
        AppChangeMapBGDialogSingleton changeBGDialog = AppChangeMapBGDialogSingleton.getSingleton();
        ColorPicker mapBGColorPicker = new ColorPicker();
        GridPane colorPickerPane = new GridPane();
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            Color chosenColor = mapBGColorPicker.getValue();
            String hexString = String.format("#%02X%02X%02X",
                    (int) (chosenColor.getRed() * 255),
                    (int) (chosenColor.getGreen() * 255),
                    (int) (chosenColor.getBlue() * 255));
            setMapBackgroundColor(hexString);
            changeBGDialog.close();
        });
        colorPickerPane.add(mapBGColorPicker, 0, 0);
        colorPickerPane.add(okButton, 0, 1);

        Scene scene = new Scene(colorPickerPane);
        changeBGDialog.setScene(scene);
        changeBGDialog.showAndWait();

    }

    @Override
    public void changeBorderColor() {
        AppChangeMapBorderColorDialogSingleton changeBorderColorDialog = AppChangeMapBorderColorDialogSingleton.getSingleton();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ColorPicker mapBGColorPicker = new ColorPicker();
        GridPane colorPickerPane = new GridPane();
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            Color chosenColor = mapBGColorPicker.getValue();
            borderColorRed = (int) (chosenColor.getRed() * 255);
            borderColorGreen = (int) (chosenColor.getGreen() * 255);
            borderColorBlue = (int) (chosenColor.getBlue() * 255);
            for (int i = 0; i < subregions.size(); i++) {
                Subregion subregion = subregions.get(i);
                subregion.setRegion(subregion.constructRegion(borderThickness, borderColorRed, borderColorGreen, borderColorBlue, mapZoom));
                setupRegionListener(subregion);
            }
            workspace.redrawSubregions();
            changeBorderColorDialog.close();
        });
        colorPickerPane.add(mapBGColorPicker, 0, 0);
        colorPickerPane.add(okButton, 0, 1);

        Scene scene = new Scene(colorPickerPane);
        changeBorderColorDialog.setScene(scene);
        changeBorderColorDialog.showAndWait();
    }

    @Override
    public void changeBorderThickness() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ObservableList<Node> polygons = workspace.getSubregionGroup().getChildren();
        AppChangeBorderThicknessSingleton thicknessDialog = AppChangeBorderThicknessSingleton.getSingleton();
        Label borderThicknessLabel = new Label("Border Thickness Slider");
        Label borderThicknessValue = new Label();
        Label currentBorderThicknessLabel = new Label("Current Border Thickness: ");
        Label currentBorderThickness = new Label(Double.toString(borderThickness));
        Slider borderThicknessSlider = new Slider();

        borderThicknessSlider.setValue(1.0);
        borderThicknessSlider.setMin(0f);
        borderThicknessSlider.setMax(10f);
        borderThicknessSlider.setShowTickMarks(true);
        borderThicknessSlider.setMajorTickUnit(.25f);

        borderThicknessSlider.setBlockIncrement(.5);

        borderThicknessSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old, Number newValue) {
                borderThicknessValue.setText(String.format("%.2f", newValue));
                borderThickness = newValue.doubleValue();
                for (int i = 0; i < polygons.size(); i++) {
                    Polygon polygon = (Polygon) polygons.get(i);
                    polygon.setStrokeWidth(borderThickness / mapZoom);
                }
            }
        });
        
        GridPane borderThicknessGrid = new GridPane();
        borderThicknessGrid.add(borderThicknessLabel, 0,0);
        borderThicknessGrid.add(borderThicknessSlider, 1, 0);
        borderThicknessGrid.add(borderThicknessValue, 2,0);
        borderThicknessGrid.add(currentBorderThicknessLabel, 0, 1);
        borderThicknessGrid.add(currentBorderThickness, 1,1);
        
        Scene scene = new Scene(borderThicknessGrid,300,300);
        thicknessDialog.setScene(scene);
        thicknessDialog.showAndWait();
        
        
        
        

    }

    @Override
    public void reassignMapColors() {
        Color[] subregionColors = new Color[subregions.size()];
        int intervalGreyscale = 254 / subregions.size();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();

        for (int i = 0; i < subregions.size(); i++) {
            subregionColors[i] = Color.rgb(254 - (intervalGreyscale * i), 254 - (intervalGreyscale * i), 254 - (intervalGreyscale * i));
            subregions.get(i).setChanged(false);
        }

        for (int i = 0; i < subregions.size(); i++) {
            Subregion newColorSubregion = subregions.get((int) (Math.random() * subregions.size()));
            int newRed = (int) (subregionColors[i].getRed() * 255);
            int newGreen = (int) (subregionColors[i].getGreen() * 255);
            int newBlue = (int) (subregionColors[i].getBlue() * 255);
            while (newColorSubregion.getChanged()) {
                newColorSubregion = subregions.get((int) (Math.random() * subregions.size()));
            }
            newColorSubregion.setRGB(newRed, newGreen, newBlue);
            newColorSubregion.constructRegion(dataManager.getBorderThickness(), dataManager.getBorderColorRed(), dataManager.getBorderColorGreen(), dataManager.getBorderColorBlue(), newRed, newGreen, newBlue, mapZoom);
            newColorSubregion.setChanged(true);
            setupRegionListener(newColorSubregion);
        }

        workspace.redrawSubregions();
    }

    @Override
    public void playAnthem() {

        if (!playing) {
            try {
                audio.loadAudio(audioName, audioFileName);
                audio.play(audioName, false);
                playing = true;
            } catch (Exception e) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show("Audio playing error", "Incorrect path or audio file is not .wav or .mid");
            }
        } else {
            audio.stop(audioName);
            playing = false;
        }
    }

    public void highlightSubregion(int chosenSubregion) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Subregion subregion = subregions.get(chosenSubregion);
        subregion.setPrevRed(subregion.getRed());
        subregion.setPrevGreen(subregion.getGreen());
        subregion.setPrevBlue(subregion.getBlue());
        subregion.setRGB(255, 255, 0);
        subregion.setRegion(subregion.constructRegion(borderThickness, borderColorRed, borderColorGreen, borderColorBlue, 255, 255, 0, mapZoom));
        setupRegionListener(subregion);

        for (int i = 0; i < subregions.size(); i++) {
            if (i != chosenSubregion) {
                Subregion nonSelectedSubregion = subregions.get(i);
                int red = nonSelectedSubregion.getPrevRed();
                int green = nonSelectedSubregion.getPrevGreen();
                int blue = nonSelectedSubregion.getBlue();
                nonSelectedSubregion.setRGB(red, green, blue);
                nonSelectedSubregion.setRegion(nonSelectedSubregion.constructRegion(borderThickness, borderColorRed, borderColorGreen, borderColorBlue, red, green, blue, mapZoom));
                setupRegionListener(nonSelectedSubregion);
            }
        }
        workspace.redrawSubregions();
    }

    public void selectSubregionTableRow(int subregionIndex) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.getTableView().getSelectionModel().select(subregionIndex);
    }

    public void setupImageViewListener(ImageView imageView) {
        imageView.setOnMouseDragged(e -> {
            imageView.setEffect(new DropShadow(20.0, Color.YELLOW));
            imageViewSelected = imageViews.indexOf(imageView);
            for (int i = 0; i < imageViews.size(); i++) {
                if (i != imageViewSelected) {
                    imageViews.get(i).setEffect(null);
                }
            }
            imageView.setX(e.getX());
            imageView.setY(e.getY());
        });

        imageView.setOnMouseClicked(e -> {
            imageView.setEffect(new DropShadow(20.0, Color.YELLOW));
            imageViewSelected = imageViews.indexOf(imageView);
            for (int i = 0; i < imageViews.size(); i++) {
                if (i != imageViewSelected) {
                    imageViews.get(i).setEffect(null);
                }
            }
        });
    }

    public void setupRegionListener(Subregion subregion) {
        Polygon region = subregion.getRegion();
        region.setOnMouseClicked(e -> {
            int regionIndex = subregions.indexOf(subregion);
            highlightSubregion(regionIndex);
            selectSubregionTableRow(regionIndex);
        });
    }

    @Override
    public void changeMapDimensions() {
        AppChangeDimensionsDialogSingleton changeDimensionsDialog = AppChangeDimensionsDialogSingleton.getSingleton();
        AppMessageDialogSingleton dneDialog = AppMessageDialogSingleton.getSingleton();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        FileManager fileManager = (FileManager) app.getFileComponent();

        Label newWidth = new Label("New width for your exported image");
        Label newHeight = new Label("New height for your exported image");
        TextField widthValue = new TextField();
        TextField heightValue = new TextField();
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            mapWidth = Integer.parseInt(widthValue.getText());
            mapHeight = Integer.parseInt(heightValue.getText());
            workspace.getMapPane().setMinSize(Double.parseDouble(widthValue.getText()), Double.parseDouble(heightValue.getText()));
            changeDimensionsDialog.close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            changeDimensionsDialog.close();
        });

        GridPane dimensionsGrid = new GridPane();
        dimensionsGrid.add(newWidth, 0, 0);
        dimensionsGrid.add(widthValue, 1, 0);
        dimensionsGrid.add(newHeight, 0, 1);
        dimensionsGrid.add(heightValue, 1, 1);
        dimensionsGrid.add(okButton, 0, 2);
        dimensionsGrid.add(cancelButton, 1, 2);

        Scene scene = new Scene(dimensionsGrid);

        changeDimensionsDialog.setScene(scene);
        changeDimensionsDialog.showAndWait();
    }

    public void snapshotMap(String parentDirectory, String regionName, int width, int height) throws IOException {

        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        try {
            WritableImage widthHeight = new WritableImage(width, height);
            WritableImage regionMapImage = workspace.getMapPane().snapshot(new SnapshotParameters(), widthHeight);

            File exportedImage = new File(parentDirectory + "/" + regionName + "/" + regionName + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(regionMapImage, null), "png", exportedImage);
        } catch (IOException ex) {
            AppMessageDialogSingleton error = AppMessageDialogSingleton.getSingleton();
            error.show("Exporting failed", "Not a valid folder in the parent directory");

        }
    }
}
