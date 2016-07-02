/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import saf.AppTemplate;
import saf.components.AppDataComponent;


/**
 *
 * @author eyb0s
 */
public class DataManager implements AppDataComponent {
    
    AppTemplate app;
    
    ObservableList<Subregion> subregions;
    
    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;
        subregions = FXCollections.observableArrayList();
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
    
    @Override
    public void reset() {
        
    }
}
