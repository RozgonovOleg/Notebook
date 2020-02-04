package os.rozgonov.notebook.view;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import os.rozgonov.notebook.MainApp;
import os.rozgonov.notebook.model.ReminderList;

public class ReminderController {public static void main(String[] args) {
}

@FXML
private TextArea RText;

@FXML
private Button Ok;

@FXML
private Button Cancel;

@FXML 
private DatePicker dpR;

@FXML 
private ComboBox<String> ChosenHour = new ComboBox<String>();
ObservableList<String> Hours = (FXCollections.observableArrayList(
		"00","01", "02", "03","04", "05", "06","07", "08", "09","10", "11", "12","13", "14", "15","16", "17", "18","19", "20", "21","22", "23"));

@FXML 
private ComboBox<String> ChosenMin = new ComboBox<String>();
ObservableList<String> Mins = (FXCollections.observableArrayList(
		"00","01", "02", "03","04", "05", "06","07", "08", "09","10", "11", "12","13", "14", "15","16", "17", "18","19", "20", "21","22", "23", 
		"24", "25","26", "27", "28","29", "30", "31","32", "33", "34", "35","36", "37", "38","39", "40", "41","42", "43", "44", "45","46", "47",
		"48","49", "50", "51","52", "53", "54", "55","56", "57", "58","59"));

@FXML
private void initialize(){
	
	RText.setWrapText(true);
	ChosenHour.setValue("00");
	ChosenHour.setItems(Hours);
	ChosenHour.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
	      @Override
	      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {	       
	      }
	    });
	ChosenMin.setValue("00");
	ChosenMin.setItems(Mins);
	ChosenMin.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
	      @Override
	      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {	       
	      }
	    });
	}
@FXML
private Callback<DatePicker, DateCell> getDayCellFactory() {
	
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {                	               	
                    super.updateItem(item, empty);                    
                    if (item.isBefore(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #e3ddff;");
                    }                                  
                }
            };
        }
    };
    return dayCellFactory;
 }

@FXML
public void setDateNow(){dpR.setValue(LocalDate.now());}

@FXML
public void Date1(ActionEvent event) {
	Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
    dpR.setDayCellFactory(dayCellFactory);
   } 

@FXML
private void handleCancel() {	
	
	 Stage stage = (Stage) Cancel.getScene().getWindow();
	    stage.close();
 }

MainApp mainApp = new MainApp();

public void setMainApp(MainApp mainApp2) {
	this.mainApp=mainApp2;	
}

@FXML
private void handleOk() {	
	
	String dd = dpR.getValue().toString()+" "+ChosenHour.getValue()+":"+ChosenMin.getValue();
	String text = RText.getText();
    if(dd.length()>0&text.length()>0){    	
    	ReminderList ReminderList= new ReminderList();
    	ReminderList.setdaytime(dd);
    	ReminderList.setselectedProblem(text);    	    	
    	mainApp.getRList().add(ReminderList);    	
    	Stage stage = (Stage) Cancel.getScene().getWindow();
        stage.close();
        
        TimerTask task = new TimerTask() {
            
        	@Override
              public void run() {
        		
                Platform.runLater(() -> {               	               	
                	Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
                	alert.setTitle("Напоминание");
                    alert.setHeaderText("Напоминание");
                	DialogPane root = alert.getDialogPane();
                	Stage dialogStage = new Stage(StageStyle.UTILITY);

                	for (ButtonType buttonType : root.getButtonTypes()) {
                	    ButtonBase button = (ButtonBase) root.lookupButton(buttonType);
                	    button.setOnAction(evt -> {
                	        root.setUserData(buttonType);
                	        dialogStage.close();
                	    });
                	}
                	root.getScene().setRoot(new Group());
                	root.setPadding(new Insets(10, 0, 10, 0));
                	Scene scene = new Scene(root);
                	
                	dialogStage.setScene(scene);
                	dialogStage.initModality(Modality.APPLICATION_MODAL);
                	dialogStage.setAlwaysOnTop(true);
                	dialogStage.setResizable(false);
                	dialogStage.showAndWait();                	                	
                });        
        	}      
          };                            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        	 try
             {
                 Date date = simpleDateFormat.parse(dd);
                 Timer timer = new Timer();                 
                 timer.schedule (task, date) ;                 
             }
             catch (ParseException ex)
             {
            	 Alert alert = new Alert(AlertType.WARNING);        		
                 alert.setTitle("Дата не парсится");
                 alert.setHeaderText("");
                 alert.setContentText("Введите правильную дату");
                 alert.showAndWait();	
             }	
          }       	
    else
    	{Alert alert = new Alert(AlertType.WARNING);        		
        alert.setTitle("Не введен текст");
        alert.setHeaderText("Не введен текст");
        alert.setContentText("Введите текст напоминания");
        alert.showAndWait();
        }       
   }
}

