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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import properties_manager.PropertiesManager;
import rvme.PropertyType;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import rvme.controller.MapEditorController;
import rvme.data.DataManager;
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
        workspace = new FlowPane();
        mapPane = createMapPane();
        subregionsBox = createTableView();
        mapTable = createSplitPane(mapPane, subregionsBox);
        mapTable.setPrefSize(app.getGUI().getPrimaryScene().getWidth(), app.getGUI().getPrimaryScene().getHeight() );
        workspace.getChildren().add(mapTable);
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
        
        mapPane.setBackground(new Background (new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        mapPane.getChildren().addAll(subregionsPane, sliderBox);
        
                
        return mapPane;
    }
    
    private VBox createTableView() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //CREATE THE BOX AND LABEL FOR THE TABLEVIEW
        subregionsBox = new VBox();
        subregionsLabel = new Label(props.getProperty(PropertyType.SUBREGIONS_HEADING_LABEL));
        
        subregionsTable = new TableView();
        
        subregionsBox.getChildren().add(subregionsLabel);
        subregionsBox.getChildren().add(subregionsTable);
        
        //SETUP COLUMN HEADINGS
        subregionNameColumn = new TableColumn(props.getProperty(PropertyType.NAME_COLUMN_HEADING));
        subregionCapitalColumn = new TableColumn(props.getProperty(PropertyType.CAPITAL_COLUMN_HEADING));
        subregionLeaderColumn = new TableColumn(props.getProperty(PropertyType.LEADER_COLUMN_HEADING));
        
        //LINK COLUMNS TO DATA
        subregionNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("subregionName"));
        subregionCapitalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("subregionCapital"));
        subregionLeaderColumn.setCellValueFactory(new PropertyValueFactory<String, String>("subregionLeader"));
        
        //RESIZE AND EDIT COLUMS PROPERTIES
        subregionNameColumn.prefWidthProperty().bind(subregionsTable.widthProperty().multiply(.33));
        subregionCapitalColumn.prefWidthProperty().bind(subregionsTable.widthProperty().multiply(.33));
        subregionLeaderColumn.prefWidthProperty().bind(subregionsTable.widthProperty().multiply(.34));
        subregionNameColumn.setResizable(false);
        subregionCapitalColumn.setResizable(false);
        subregionLeaderColumn.setResizable(false);
        
        
        //ADD COLUMNS TO THE TAB;E
        subregionsTable.getColumns().add(subregionNameColumn);
        subregionsTable.getColumns().add(subregionCapitalColumn);
        subregionsTable.getColumns().add(subregionLeaderColumn);
        
        //SET THE HEIGHT OF THE TABLE TO PROPERLY MATCH THE SCREEN HEIGHT
        subregionsTable.setPrefHeight(app.getGUI().getPrimaryScene().getHeight() - subregionsLabel.getHeight());
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        subregionsTable.setItems(dataManager.getSubregions());
        
        return subregionsBox;
    }
    
    private SplitPane createSplitPane(StackPane mapPane, VBox subregionsBox) {
        mapTable = new SplitPane();
        
        mapTable.getItems().addAll(mapPane, subregionsBox);
        mapTable.setDividerPositions(.5f);
        
        return mapTable;
    }
    
    @Override
    public void initStyle() {
        
    }
    
    @Override 
    public void reloadWorkspace() {
        
    }
    
    
}
