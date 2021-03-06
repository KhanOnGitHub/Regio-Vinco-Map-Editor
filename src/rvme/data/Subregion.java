/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rvme.MapEditorApp;
import saf.AppTemplate;

/**
 *
 * @author eyb0s
 */
public class Subregion {

    public static final String DEFAULT_NAME = "?";
    public static final String DEFAULT_CAPITAL = "?";
    public static final String DEFAULT_LEADER = "?";

    final StringProperty subregionName;
    final StringProperty subregionCapital;
    final StringProperty subregionLeader;
    String flagPath;
    String leaderPath;

    ArrayList<Double> polyPoints;
    Polygon region;

    int red;
    int prevRed;
    int blue;
    int prevBlue;
    int green;
    int prevGreen;

    boolean changed;
    boolean selected;

    MapEditorApp app;
    
    AppTemplate dataApp;

    public Subregion() {
        subregionName = new SimpleStringProperty(DEFAULT_NAME);
        subregionCapital = new SimpleStringProperty(DEFAULT_CAPITAL);
        subregionLeader = new SimpleStringProperty(DEFAULT_LEADER);

        flagPath = "?";
        leaderPath = "?";

        polyPoints = new ArrayList<>();

        region = new Polygon();
        red = 124;
        green = 252;
        blue = 0;

        prevRed = 0;
        prevGreen = 0;
        prevBlue = 0;

        changed = false;
        selected = false;
    }

    public Subregion(String initName, String initCapital, String initLeader, String flagPath, String leaderPath, int red, int green, int blue) {
        this();
        subregionName.set(initName);
        subregionCapital.set(initCapital);
        subregionLeader.set(initLeader);
        this.flagPath = flagPath;
        this.leaderPath = leaderPath;
        this.red = red;
        this.blue = blue;
        this.green = green;

    }

    public StringProperty subregionName() {
        return subregionName;
    }

    public StringProperty subregionCapital() {
        return subregionCapital;
    }

    public StringProperty subregionLeader() {
        return subregionLeader;
    }

    public String getSubregionName() {
        return subregionName.get();
    }

    public String getSubregionCapital() {
        return subregionCapital.get();
    }

    public String getSubregionLeader() {
        return subregionLeader.get();
    }

    public Polygon getRegion() {
        return region;
    }

    public String getFlagPath() {
        return flagPath;
    }

    public String getLeaderPath() {
        return leaderPath;
    }

    public void setRegion(Polygon region) {
        this.region = region;
    }

    public ArrayList<Double> getPoints() {
        return polyPoints;
    }

    public void setRegionName(String value) {
        subregionName.set(value);
    }

    public void setRegionCapital(String value) {
        subregionCapital.set(value);
    }

    public void setRegionLeader(String value) {
        subregionLeader.set(value);
    }

    public void setLeaderPath(String leaderPath) {
        this.leaderPath = leaderPath;
    }

    public void setFlagPath(String flagPath) {
        this.flagPath = flagPath;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setChanged(boolean value) {
        changed = value;
    }

    public int getPrevRed() {
        return prevRed;
    }

    public void setPrevRed(int prevRed) {
        this.prevRed = prevRed;
    }

    public int getPrevBlue() {
        return prevBlue;
    }

    public void setPrevBlue(int prevBlue) {
        this.prevBlue = prevBlue;
    }

    public int getPrevGreen() {
        return prevGreen;
    }

    public void setPrevGreen(int prevGreen) {
        this.prevGreen = prevGreen;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void printProperties() {
        System.out.println("Subregion Name: " + subregionName);
        System.out.println("Subregion Capital: " + subregionCapital);
        System.out.println("Subregion Leader: " + subregionLeader);

        System.out.println("Flag Path: " + flagPath);
        System.out.println("Leader Path: " + leaderPath);
        System.out.println("Subregion RGB Values :" + red + ", " + green + ", " + blue);
    }

    public void addPoints(double X, double Y) {
        polyPoints.add(X);
        polyPoints.add(Y);
    }

    public Polygon constructRegion(double strokeWidth, int borderColorRed, int borderColorGreen, int borderColorBlue, double mapZoom) {
        region = new Polygon();
        region.getPoints().addAll(polyPoints);
        region.setFill(Color.rgb(red, green, blue));
        region.setStrokeWidth(1 / mapZoom);
        region.setStroke(Color.rgb(borderColorRed, borderColorGreen, borderColorBlue));
        return region;
    }

    public Polygon constructRegion(double strokeWidth, int borderColorRed, int borderColorGreen, int borderColorBlue, int newRed, int newGreen, int newBlue, double mapZoom) {
        region = new Polygon();
        region.getPoints().addAll(polyPoints);
        region.setFill(Color.rgb(newRed, newGreen, newBlue));
        region.setStrokeWidth(1 / mapZoom);
        region.setStroke(Color.rgb(borderColorRed, borderColorGreen, borderColorBlue));
        return region;
    }
    


}
