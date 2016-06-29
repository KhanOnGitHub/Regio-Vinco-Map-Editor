/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import saf.AppTemplate;
import saf.components.AppDataComponent;


/**
 *
 * @author eyb0s
 */
public class DataManager implements AppDataComponent {
    
    AppTemplate app;
    
    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;
    }
    
    @Override
    public void reset() {
        
    }
}
