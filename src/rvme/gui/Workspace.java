/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
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
    Label sliderLabel;
    Label mapLabel;
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
        DataManager data = (DataManager) app.getDataComponent();

        subregionsTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mapEditorController.processEditSubregion(subregionsTable.getSelectionModel().getSelectedItem());
            }
        });

        /*subregionsPane.setOnMouseClicked(e -> {
            System.out.println("aylmao");
        });*/
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
        Group subregionGroup = new Group();
        for (int i = 0; i < subregions.size(); i++) {
            Polygon polygon = subregions.get(i).getRegion();
            subregionGroup.getChildren().add(polygon);
        }

        subregionGroup.setScaleX(subregionGroup.getScaleX() * 200);
        subregionGroup.setScaleY(subregionGroup.getScaleY() * 200);
        subregionsPane.getChildren().add(subregionGroup);
        subregionsPane.setBackground(Background.EMPTY);
    }

    public void imagesOnMap(ObservableList<ImageView> imageViews) {
        for (int i = 0; i < imageViews.size(); i++) {
            imagesPane.getChildren().add(imageViews.get(i));
        }
    }

    public void imageOnMap(ImageView imageView) {
        imagesPane.getChildren().add(imageView);

    }

    public void removeImageOnMap() {
        DataManager dataManager = (DataManager) app.getDataComponent();
        imagesPane.getChildren().clear();
        dataManager.addImagesToMap();
    }

    public void redrawSubregions() {
        DataManager dataManager = (DataManager) app.getDataComponent();
        ObservableList<Subregion> subregions = dataManager.getSubregions();
        subregionsPane.setPrefSize(802, 536);
        subregionsPane.getChildren().clear();

        Group subregionGroup = new Group();
        for (int i = 0; i < subregions.size(); i++) {
            Polygon polygon = subregions.get(i).getRegion();
            subregionGroup.getChildren().add(polygon);
        }

        subregionGroup.setScaleX(subregionGroup.getScaleX() * 200);
        subregionGroup.setScaleY(subregionGroup.getScaleY() * 200);
        subregionsPane.getChildren().add(subregionGroup);
        subregionsPane.setBackground(Background.EMPTY);

    }

    public StackPane getMapPane() {
        return mapPane;
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

}
