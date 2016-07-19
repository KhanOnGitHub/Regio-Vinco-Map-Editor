/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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

    ProgressBar loadingProgress;

    //THIS HAS OUR SUBREGION MAP
    VBox mapBox;
    StackPane mapPane;
    Pane subregionsPane;
    Pane imagesPane;
    Group imagesGroup;
    Label sliderLabel;
    Label mapLabel;
    Slider mapZoomSlider;
    HBox sliderBox;
    ImageView mapImages;
    Group subregionGroup;
    Label zoomLevel;

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
        setupHandlers();
    }

    private void layoutGUI() {
        workspace = new FlowPane();
        mapBox = createMapPane();
        subregionsBox = createTableView();
        loadingProgress = new ProgressBar();
        mapTable = createSplitPane(mapBox, subregionsBox);
        mapTable.setPrefSize(app.getGUI().getPrimaryScene().getWidth(), app.getGUI().getPrimaryScene().getHeight());
        workspace.getChildren().add(mapTable);
    }

    private void setupHandlers() {
        mapEditorController = new MapEditorController(app);
        DataManager dataManager = (DataManager) app.getDataComponent();

        subregionsTable.setRowFactory(e -> {
            TableRow<Subregion> row = new TableRow<>();
            row.setOnMouseClicked(mouseClick -> {
                Subregion rowData = row.getItem();
                mapEditorController.processHighlightSubregion(rowData);
                if (mouseClick.getClickCount() == 2 && (!row.isEmpty())) {
                    rowData = row.getItem();
                    mapEditorController.processEditSubregion(rowData);
                    mapEditorController.processHighlightSubregion(rowData);
                }
            });
            return row;
        });

        mapZoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old, Number newValue) {
                subregionGroup.setScaleX(newValue.doubleValue());
                subregionGroup.setScaleY(newValue.doubleValue());
                zoomLevel.setText(String.format("%.2f", newValue));
                dataManager.setMapZoom(newValue.doubleValue());
            }
        });

        gui.getPrimaryScene().setOnKeyPressed(e -> {
            mapEditorController.handleKeyPress(e);
        });

    }

    public void updateTableView() {
        subregionsTable.getColumns().get(0).setVisible(false);
        subregionsTable.getColumns().get(0).setVisible(true);
        subregionsTable.getColumns().get(1).setVisible(false);
        subregionsTable.getColumns().get(1).setVisible(true);
        subregionsTable.getColumns().get(2).setVisible(false);
        subregionsTable.getColumns().get(2).setVisible(true);
    }

    private VBox createMapPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        mapBox = new VBox();

        mapLabel = new Label();
        mapLabel.setText(props.getProperty(PropertyType.MAP_LABEL));

        //SETUP MAP PANE COMPONENTS
        mapPane = new StackPane();
        subregionsPane = new Pane();
        imagesPane = new Pane();
        imagesGroup = new Group();
        sliderBox = new HBox();

        mapPane.setPrefSize(802, 536);
        mapPane.setMaxSize(802, 536);

        //SETUP MAP ZOOM SLIDER
        mapZoomSlider = new Slider();
        mapZoomSlider.setMin(0);
        mapZoomSlider.setMax(1000);
        mapZoomSlider.setValue(200);
        mapZoomSlider.setShowTickMarks(true);
        mapZoomSlider.setMajorTickUnit(100);
        mapZoomSlider.setMinorTickCount(50);
        mapZoomSlider.setBlockIncrement(20);

        //LABEL FOR SLIDER
        sliderLabel = new Label();
        sliderLabel.setText(props.getProperty(PropertyType.ZOOM_SLIDER_LABEL));

        zoomLevel = new Label();
        zoomLevel.setText(Double.toString(mapZoomSlider.getValue()));
        //SETUP THE HBOX CONTAINIG THE SLIDERS
        sliderBox.getChildren().addAll(sliderLabel, mapZoomSlider, zoomLevel);

        Group sliderBoxGroup = new Group();
        sliderBoxGroup.getChildren().add(sliderBox);

        //mapPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        mapPane.getChildren().add(subregionsPane);
        mapPane.getChildren().add(imagesPane);
        mapPane.getChildren().add(sliderBoxGroup);

        sliderBoxGroup.setPickOnBounds(false);
        imagesPane.setPickOnBounds(false);
        StackPane.setAlignment(sliderBoxGroup, Pos.BOTTOM_LEFT);
        mapBox.getChildren().addAll(mapLabel, mapPane);

        return mapBox;
    }

    private VBox createTableView() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        //CREATE THE BOX AND LABEL FOR THE TABLEVIEW
        subregionsBox = new VBox();
        subregionsLabel = new Label(props.getProperty(PropertyType.SUBREGIONS_HEADING_LABEL));

        subregionsTable = new TableView();

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

        DataManager dataManager = (DataManager) app.getDataComponent();
        subregionsTable.setItems(dataManager.getSubregions());

        subregionsBox.getChildren().add(subregionsLabel);
        subregionsBox.getChildren().add(subregionsTable);

        return subregionsBox;
    }

    private SplitPane createSplitPane(VBox mapBox, VBox subregionsBox) {
        mapTable = new SplitPane();

        mapTable.getItems().addAll(mapBox, subregionsBox);
        mapTable.setDividerPositions(.5f);

        return mapTable;
    }

    @Override
    public void initStyle() {
        // FIRST THE WORKSPACE PANE
        workspace.getStyleClass().add(CLASS_BORDERED_PANE);

        subregionsBox.getStyleClass().add(CLASS_BORDERED_PANE);
        mapTable.getStyleClass().add(CLASS_BORDERED_PANE);

        subregionsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        mapLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);

    }

    public void drawOnMap(ObservableList<Subregion> subregions) {
        DataManager dataManager = (DataManager) app.getDataComponent();
        subregionsPane.setPrefSize(802, 536);
        subregionsPane.setMaxSize(802, 536);

        subregionGroup = new Group();
        for (int i = 0; i < subregions.size(); i++) {
            Polygon polygon = subregions.get(i).getRegion();
            subregionGroup.getChildren().add(polygon);
        }

        subregionGroup.setScaleX(dataManager.getMapZoom());
        subregionGroup.setScaleY(dataManager.getMapZoom());
        subregionsPane.getChildren().add(subregionGroup);
        subregionsPane.setBackground(Background.EMPTY);
    }

    public void imagesOnMap(ObservableList<ImageView> imageViews) {
        for (int i = 0; i < imageViews.size(); i++) {
            imagesGroup.getChildren().add(imageViews.get(i));
        }
        
        imagesPane.getChildren().add(imagesGroup);
    }

    public void imageOnMap(ImageView imageView) {
        imagesGroup.getChildren().add(imageView);
        imagesPane.getChildren().clear();
        imagesPane.getChildren().add(imagesGroup);

    }

    public void removeImageOnMap() {
        DataManager dataManager = (DataManager) app.getDataComponent();
        imagesGroup.getChildren().clear();
        dataManager.addImagesToMap();
    }

    public void redrawSubregions() {
        DataManager dataManager = (DataManager) app.getDataComponent();
        ObservableList<Subregion> subregions = dataManager.getSubregions();
        subregionsPane.setPrefSize(802, 536);
        subregionsPane.setMaxSize(802, 536);
        subregionsPane.getChildren().clear();

        subregionGroup = new Group();
        for (int i = 0; i < subregions.size(); i++) {
            Polygon polygon = subregions.get(i).getRegion();
            subregionGroup.getChildren().add(polygon);
        }

        subregionGroup.setScaleX(dataManager.getMapZoom());
        subregionGroup.setScaleY(dataManager.getMapZoom());
        Rectangle clipRectangle = new Rectangle();
        clipRectangle.setHeight(subregionsPane.getHeight() - mapLabel.getHeight());
        clipRectangle.setWidth(subregionsPane.getWidth());
        subregionGroup.setClip(clipRectangle);
        subregionsPane.getChildren().add(subregionGroup);

    }

    public StackPane getMapPane() {
        return mapPane;
    }

    public TableView getTableView() {
        return subregionsTable;
    }

    @Override
    public void reloadWorkspace() {
        DataManager dataManager = (DataManager) app.getDataComponent();
        /* 
        //recreate tables
        layoutGUI();
        setupHandlers();
        initStyle();
        
        try {
            dataManager.addSubregionsToPane();
        } catch (Exception ex) {
            Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataManager.addImagesToMap();
         */
    }

    public Group getSubregionGroup() {
        return subregionGroup;
    }
    
    public Group getImagesGroup() {
        return imagesGroup;
    }
    
    

}
