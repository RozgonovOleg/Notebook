package os.rozgonov.notebook.view;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import os.rozgonov.notebook.MainApp;

public class borderController {

    private os.rozgonov.notebook.MainApp mainApp;
   
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleNew() {
        mainApp.getProblemData().clear();
        mainApp.setProblemFilePath(null);
    }
    
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadProblemDataFromFile(file);
        }
    }
    
    @FXML
    private void handleSave() {
        File problemFile = mainApp.getProblemFilePath();
        if (problemFile != null) {
            mainApp.saveProblemDataToFile(problemFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
          
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveProblemDataToFile(file);
        }
    }
    
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author Rozgonov Oleg");

        alert.showAndWait();
    }
    
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}