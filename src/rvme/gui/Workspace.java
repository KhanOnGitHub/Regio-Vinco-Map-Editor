/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.io.IOException;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import rvme.controller.MapEditorController;



/**
 *
 * @author eyb0s
 */
public class Workspace extends AppWorkspaceComponent {
    
    AppTemplate app;
    
    AppGUI gui;
    
    MapEditorController mapEditorController;
    
    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
    }
    
    @Override
    public void initStyle() {
        
    }
    
    @Override 
    public void reloadWorkspace() {
        
    }
    
    
}
