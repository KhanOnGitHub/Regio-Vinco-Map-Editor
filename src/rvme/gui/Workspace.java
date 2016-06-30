/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import properties_manager.PropertiesManager;
import rvme.PropertyType;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import rvme.controller.MapEditorController;
import rvme.data.Subregion;



/**
 *
 * @author eyb0s
 */
public class Workspace extends AppWorkspaceComponent {
    
    AppTemplate app;
    
    AppGUI gui;
    
    MapEditorController mapEditorController;
    
    //OUR SPLIT PANE
    SplitPane mapTable;
    
    //THIS HAS OUR SUBREGION MAP
    StackPane mapPane;
    Pane subregionsPane;
    Label sliderLabel;
    Slider mapZoomSlider;
    HBox sliderBox;
    ImageView mapImages;
    
    //OUR REGION FOR VIEWING AND MANAGING SUBREGIONS
    VBox subregionsBox;
    Label subregionsLabel;
    HBox subregionsToolbar;
    TableView<Subregion> subregionsTable;
    TableColumn subregionNameColumn;
    TableColumn subregionCapitalColumn;
    TableColumn subregionLeaderColumn;
    
    
    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
        layoutGUI();        
    }
    
    private void layoutGUI() {
        createSplitPane(createMapPane(), createTableView());
    }
    
    private void setupHandlers() {
        
    }
    
    private StackPane createMapPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //SETUP MAP PANE COMPONENTS
        mapPane = new StackPane();
        subregionsPane = new Pane();
        sliderBox = new HBox();
        
        //SETUP MAP ZOOM SLIDER
        mapZoomSlider = new Slider(0, 1, .5);
        mapZoomSlider.setShowTickMarks(true);
        mapZoomSlider.setMajorTickUnit(.25f);
        mapZoomSlider.setBlockIncrement(.1f);
        
        //LABEL FOR SLIDER
        sliderLabel = new Label();
        sliderLabel.setText(props.getProperty(PropertyType.ZOOM_SLIDER_LABEL));
        
        //SETUP THE HBOX CONTAINIG THE SLIDERS
        sliderBox.getChildren().addAll(sliderLabel, mapZoomSlider);
        
        mapPane.setBackground(new Background (new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        mapPane.getChildren().addAll(subregionsPane, sliderBox);
        
                
        return mapPane;
    }
    
    private TableView createTableView() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        //SETUP COLUMN HEADINGS
        subregionNameColumn = new TableColumn(props.getProperty(PropertyType.NAME_COLUMN_HEADING));
        subregionCapitalColumn = new TableColumn(props.getProperty(PropertyType.CAPITAL_COLUMN_HEADING));
        subregionLeaderColumn = new TableColumn(props.getProperty(PropertyType.LEADER_COLUMN_HEADING));
        
        
        
        return subregionsTable;
    }
    
    private void createSplitPane(StackPane mapPane, TableView subregionData) {
        
    }
    
    @Override
    public void initStyle() {
        
    }
    
    @Override 
    public void reloadWorkspace() {
        
    }
    
    
}
