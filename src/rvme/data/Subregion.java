/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.Polygon;
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
    
    ArrayList<Double> polyPoints;
    Polygon region;
    
    public Subregion() {
        subregionName = new SimpleStringProperty(DEFAULT_NAME);
        subregionCapital = new SimpleStringProperty(DEFAULT_CAPITAL);
        subregionLeader = new SimpleStringProperty(DEFAULT_LEADER);
        
        polyPoints = new ArrayList<>();
        
        region = new Polygon();
    }
    
    public Subregion(String initName, String initCapital, String initLeader) {
        this();
        subregionName.set(initName);
        subregionCapital.set(initCapital);
        subregionLeader.set(initLeader);
    }
    
    public Polygon getRegion() {
        return region;
    }
    
    public void setRegion(Polygon region) {
        this.region = region;
    }
    
    public ArrayList<Double> getPoints() {
        return polyPoints;
    }
    
    public void addPoints(double X, double Y) {
        polyPoints.add(X);
        polyPoints.add(Y);
    }
    
    public Polygon constructRegion() {
        region = new Polygon();
        region.getPoints().addAll(polyPoints);
        
        return region;
    }
    
}
