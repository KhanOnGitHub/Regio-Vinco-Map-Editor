/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import java.io.File;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.ui.AppEditDialogSingleton;
import rvme.PropertyType;
import rvme.data.DataManager;
import rvme.data.Subregion;
import rvme.gui.Workspace;
import saf.ui.AppGUI;

/**
 *
 * @author eyb0s
 */
public class MapEditorController {

    AppTemplate app;
    AppGUI gui;
    AppEditDialogSingleton dialog;

    private Label subregionNameLabel;
    private Label subregionCapitalLabel;
    private Label subregionLeaderLabel;
    private Label flagPictureLabel;
    private Label leaderPictureLabel;

    private TextField nameText;
    private TextField capitalText;
    private TextField leaderText;
    private TextField flagPictureText;
    private TextField leaderPictureText;

    private Button okButton;
    private Button cancelButton;
    private Button prevButton;
    private Button nextButton;

    private Image flagImage;
    private Image leaderImage;

    private ImageView flagImageView;
    private ImageView leaderImageView;
    
    private HBox imageViewBox;

    private GridPane editPane;

    private Scene scene;

    public MapEditorController(AppTemplate initApp) {
        app = initApp;
    }

    public void processEditSubregion(Subregion subregion) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        DataManager data = (DataManager) app.getDataComponent();
        layoutEditSubregionGUI(subregion);
    }

    public void processHighlightSubregion(Subregion subregion) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        DataManager dataManager = (DataManager) app.getDataComponent();
        ObservableList<Subregion> subregions = dataManager.getSubregions();
        int chosenSubregion = subregions.indexOf(subregion);
        dataManager.highlightSubregion(chosenSubregion);
    }

    public void handleKeyPress(KeyEvent event) {
        event.consume();
        switch (event.getCode()) {
            case UP:
                processMoveUp();
                break;
            case DOWN:
                processMoveDown();
                break;
            case LEFT:
                processMoveLeft();
                break;
            case RIGHT:
                processMoveRight();
                break;
            default:
                break;
        }
    }

    public void processMoveUp() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        workspace.getSubregionGroup().setTranslateY(workspace.getSubregionGroup().getTranslateY() - 10);
        dataManager.setMapScrollLocationY(workspace.getSubregionGroup().getTranslateY());

    }

    public void processMoveDown() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        workspace.getSubregionGroup().setTranslateY(workspace.getSubregionGroup().getTranslateY() + 10);
        dataManager.setMapScrollLocationY(workspace.getSubregionGroup().getTranslateY());
    }

    public void processMoveLeft() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        workspace.getSubregionGroup().setTranslateX(workspace.getSubregionGroup().getTranslateX() - 10);
        dataManager.setMapScrollLocationX(workspace.getSubregionGroup().getTranslateX());
    }

    public void processMoveRight() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        workspace.getSubregionGroup().setTranslateX(workspace.getSubregionGroup().getTranslateX() + 10);
        dataManager.setMapScrollLocationX(workspace.getSubregionGroup().getTranslateX());
    }

    public void layoutEditSubregionGUI(Subregion subregion) {
        dialog = AppEditDialogSingleton.getSingleton();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        //INSTANTIATE THE LABELS AND GIVE THEM TEXT
        subregionNameLabel = new Label();
        subregionNameLabel.setText(props.getProperty(PropertyType.NAME_COLUMN_HEADING));
        subregionCapitalLabel = new Label();
        subregionCapitalLabel.setText(props.getProperty(PropertyType.CAPITAL_COLUMN_HEADING));
        subregionLeaderLabel = new Label();
        subregionLeaderLabel.setText(props.getProperty(PropertyType.LEADER_COLUMN_HEADING));
        flagPictureLabel = new Label();
        flagPictureLabel.setText(props.getProperty(PropertyType.FLAG_LABEL));
        leaderPictureLabel = new Label();
        leaderPictureLabel.setText(props.getProperty(PropertyType.LEADER_LABEL));

        //TEXT FIELDS FOR SOME LABELS
        nameText = new TextField();
        nameText.setText(subregion.getSubregionName());
        capitalText = new TextField();
        capitalText.setText(subregion.getSubregionCapital());
        leaderText = new TextField();
        leaderText.setText(subregion.getSubregionLeader());

        File flagFile = new File(dataManager.getParentDirectory() + "/" + dataManager.getRegionName() + "/" +  subregion.getSubregionName() + " Flag.png");
        if (flagFile.exists()) {
            flagImage = new Image("file:" + dataManager.getParentDirectory() + "/" + dataManager.getRegionName() + "/" +  subregion.getSubregionName() + " Flag.png");
            subregion.setFlagPath(flagFile.getPath());
        } else {
            flagImage = new Image("file:./images/NoImage.png");
        }

        File leaderFile = new File(dataManager.getParentDirectory() + "/" + dataManager.getRegionName() + "/" + subregion.getSubregionLeader() + ".png");
        if (leaderFile.exists()) {
            leaderImage = new Image("file:" + dataManager.getParentDirectory() + "/" + dataManager.getRegionName() + "/" + subregion.getSubregionLeader() + ".png");
            subregion.setLeaderPath(leaderFile.getPath());
        } else {
            leaderImage = new Image("file:./images/NoImage.png");
        }

        flagImageView = new ImageView(flagImage);
        flagImageView.setFitWidth(200);
        flagImageView.setFitHeight(200);
        
        leaderImageView = new ImageView(leaderImage);
        leaderImageView.setFitWidth(200);
        leaderImageView.setFitHeight(200);

        flagPictureText = new TextField();
        flagPictureText.setText(subregion.getFlagPath());
        flagPictureText.setEditable(false);
        flagPictureText.setMouseTransparent(true);
        flagPictureText.setFocusTraversable(false);
        leaderPictureText = new TextField();
        leaderPictureText.setText(subregion.getLeaderPath());
        leaderPictureText.setEditable(false);
        leaderPictureText.setMouseTransparent(true);
        leaderPictureText.setFocusTraversable(false);

        //INSTANTIATE AND GIVE TEXT TO BUTTONS
        okButton = new Button();
        okButton.setText(props.getProperty(PropertyType.OK_BUTTON));
        okButton.setOnAction(mouseClick -> {
            editSubregion(subregion);
        });
        cancelButton = new Button();
        cancelButton.setText(props.getProperty(PropertyType.CANCEL_BUTTON));
        cancelButton.setOnAction(mouseClick -> {
            editSubregion(subregion);
            promptCancel();
        });

        prevButton = new Button("Previous");
        prevButton.setOnAction(mouseClick -> {
            ObservableList<Subregion> subregions = dataManager.getSubregions();
            int currSubregionIndex = subregions.indexOf(subregion);
            if (currSubregionIndex != 0) {
                editSubregion(subregion);
                layoutEditSubregionGUI(subregions.get(currSubregionIndex - 1));
                dataManager.highlightSubregion(currSubregionIndex - 1);
                workspace.getTableView().getSelectionModel().select(currSubregionIndex - 1);

            }
        });
        nextButton = new Button("Next");
        nextButton.setOnAction(mouseClick -> {
            ObservableList<Subregion> subregions = dataManager.getSubregions();
            int currSubregionIndex = subregions.indexOf(subregion);
            if (currSubregionIndex != subregions.size() - 1) {
                editSubregion(subregion);
                layoutEditSubregionGUI(subregions.get(currSubregionIndex + 1));
                dataManager.highlightSubregion(currSubregionIndex + 1);
                workspace.getTableView().getSelectionModel().select(currSubregionIndex + 1);
            }
        });

        GridPane gridPane = new GridPane();

        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(15, 12, 15, 12));
        gridPane.add(subregionNameLabel, 0, 0);
        gridPane.add(nameText, 1, 0);

        gridPane.add(subregionCapitalLabel, 0, 1);
        gridPane.add(capitalText, 1, 1);

        gridPane.add(subregionLeaderLabel, 0, 2);
        gridPane.add(leaderText, 1, 2);

        gridPane.add(flagPictureLabel, 0, 3);
        gridPane.add(flagPictureText, 1, 3);

        HBox okCancel = new HBox();
        okCancel.setSpacing(10);
        okCancel.setPadding(new Insets(0,0, 0, 50));
        okCancel.getChildren().addAll(okButton, cancelButton);

        gridPane.add(leaderPictureLabel, 0, 4);
        gridPane.add(leaderPictureText, 1, 4);
        
        imageViewBox = new HBox();
        imageViewBox.setSpacing(10);
        imageViewBox.getChildren().addAll(leaderImageView, flagImageView);
        gridPane.add(imageViewBox, 1, 5);

        gridPane.add(okCancel, 1, 6);
        gridPane.add(prevButton, 0, 6);
        gridPane.add(nextButton, 2, 6);
        GridPane.setHalignment(imageViewBox, HPos.CENTER);
        GridPane.setHalignment(okCancel, HPos.CENTER);

        scene = new Scene(gridPane, 700, 500);
        scene.getStylesheets().add("rvme/css/rvme_style.css");
        gridPane.getStyleClass().add("gridPane");
        dialog.setScene(scene);
        dialog.setTitle(props.getProperty(PropertyType.EDIT_DIALOG));
        dialog.show();
    }

    public void editSubregion(Subregion subregion) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        subregion.setRegionName(nameText.getText());
        subregion.setRegionCapital(capitalText.getText());
        subregion.setRegionLeader(leaderText.getText());
        workspace.updateTableView();
        dialog.close();

    }

    public void promptCancel() {
        dialog.close();
    }

}
