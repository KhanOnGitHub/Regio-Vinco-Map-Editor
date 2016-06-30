/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
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
        createSplitPane();
    }
    
    private void setupHandlers() {
        
    }
    
    private StackPane createMapPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //SETUP MAP PANE COMPONENTS
        mapPane = new StackPane();
        subregionsPane = new Pane();
        
        //SETUP MAP ZOOM SLIDER
        mapZoomSlider = new Slider(0, 1, .5);
        mapZoomSlider.setShowTickMarks(true);
        mapZoomSlider.setMajorTickUnit(.25f);
        mapZoomSlider.setBlockIncrement(.1f);
        
        sliderLabel = new Label();
        
        
                
        return null;
    }
    
    private TableView createTableView() {
        return null;
    }
    
    private void createSplitPane() {
        
    }
    
    @Override
    public void initStyle() {
        
    }
    
    @Override 
    public void reloadWorkspace() {
        
    }
    
    
}
