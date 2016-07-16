/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme;

import java.util.Locale;
import rvme.data.DataManager;
import rvme.file.FileManager;
import rvme.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppComponentsBuilder;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import saf.components.AppWorkspaceComponent;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

/**
 *
 * @author eyb0s
 */
public class MapEditorApp extends AppTemplate {
    
    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        return new AppComponentsBuilder() {
            
            @Override
            public AppDataComponent buildDataComponent() throws Exception {
                return new DataManager(MapEditorApp.this);
            }
            
            @Override
	    public AppFileComponent buildFileComponent() throws Exception {
		return new FileManager();
	    }
            
            @Override
	    public AppWorkspaceComponent buildWorkspaceComponent() throws Exception {
		return new Workspace(MapEditorApp.this);
	    }
        };
    }
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }
}
