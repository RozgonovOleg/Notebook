package os.rozgonov.notebook.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import os.rozgonov.notebook.MainApp;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import os.rozgonov.notebook.model.Problem;



public class overviewController {public static void main(String[] args) {
}

@FXML
private TableView<Problem> ProblemsTable=new TableView<Problem>();

@FXML private TextField dateField;

@FXML private DatePicker dp;

@FXML
private TableColumn<Problem, String> ProblemsColumn;

@FXML
private TableColumn<Problem, String> DateColumn;

@FXML private TextArea Problem;

@FXML
private Button Reminder;

@FXML
private Button AddNewBTN; 

@FXML
private void AddNew(){
	
	String n=dateField.getText();
	String m=Problem.getText();
	int a=n.length();
	int b=m.length();
	if(a>0&&b>0){			
	Problem problem= new Problem();
	problem.setproblems(Problem.getText());
	problem.setDate(dateField.getText());	
	mainApp.getProblemData().add(problem);	
	Problem.clear();
	
	}
	else {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Нет текста или даты");
		alert.setHeaderText("Нет текста или даты");
		alert.setContentText("Введите и дату и текст");
		alert.showAndWait();}	
 }

@FXML
private void Delete(ActionEvent event){    	
	Problem selectedItem = ProblemsTable.getSelectionModel().getSelectedItem();
	if (selectedItem != null) {
		mainApp.getProblemData().remove(selectedItem);
	
    } else {        
        Alert alert = new Alert(AlertType.WARNING);        		
        alert.setTitle("Не выбрано ничего");
        alert.setHeaderText("Не выбрано задание");
        alert.setContentText("Выберите задание сначала");
        alert.showAndWait();
    }	 
 }


private MainApp mainApp;

public overviewController() {	
}

@FXML
private void initialize() {   
			
	DateColumn.setCellValueFactory(cellData -> cellData.getValue().selectedDateProperty());
	DateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    ProblemsColumn.setCellValueFactory(cellData -> cellData.getValue().problemsProperty());
    ProblemsColumn.setCellFactory(TextFieldTableCell.forTableColumn());      
    Problem.setWrapText(true);
    Problem.setMaxWidth(ProblemsColumn.getPrefWidth()); 
    ProblemsTable.getSelectionModel().selectFirst();   
   
    
    }

@FXML
public void editablecolumn(){	
	
	ProblemsColumn.setOnEditCommit(
		    new EventHandler<CellEditEvent<Problem, String>>() {
		        @Override
		        public void handle(CellEditEvent<Problem, String> t) {
		            ((Problem) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setproblems(t.getNewValue());
		        }
		    }
		);
 }

public void setMainApp(MainApp mainApp) {
	this.mainApp=mainApp;
	
        ProblemsTable.setItems(mainApp.getProblemData());
        ProblemsTable.setOnKeyPressed( new EventHandler<KeyEvent>()
    	{
    	  @Override
    	  public void handle( final KeyEvent keyEvent )
    	  {
    		  Problem selectedItem = ProblemsTable.getSelectionModel().getSelectedItem();
    		  if (selectedItem != null) {
    				
    	      if ( keyEvent.getCode().equals( KeyCode.DELETE ) )
    	      {
    	    	  mainApp.getProblemData().remove(selectedItem);
    	      }
    	    }
    			}
    	  });
    	  
        FilteredList<Problem> filteredData = new FilteredList<>(mainApp.getProblemData(), e->true); 
        dateField.setOnMouseClicked(e->{
        	dateField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(problem -> {                
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }            
                String CaseFilter = newValue; 
                if (problem.getDate().contains(CaseFilter)) {
                    return true;           
                }
                return false; 
            });
          });
        });
        
        SortedList<Problem> sortedData = new SortedList<>(filteredData);        
        sortedData.comparatorProperty().bind(ProblemsTable.comparatorProperty());        
        ProblemsTable.setItems(sortedData);
 }

Date input = new Date();
LocalDate ld = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

public void setDateNow(){dp.setValue(ld);}

public void Date1(ActionEvent event) {
	Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
    dp.setDayCellFactory(dayCellFactory);
    
	LocalDate ld = dp.getValue();
	dateField.setText(ld.toString());	
 } 

private Callback<DatePicker, DateCell> getDayCellFactory() {
	
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                	String da = mainApp.getProblemData().toString();                 
                    super.updateItem(item, empty);                    
                    if (item.isBefore(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #e3ddff;");
                    }
                    if (item.toString().length()<=da.length() && da.toString().indexOf(item.toString()) !=-1) {                    	
                    	
                    	setDisable(false);
                        setStyle("-fx-background-color: #ff5d00;");
                    }                    
                }
            };
        }
    };
    return dayCellFactory;
 }

@FXML
public void Reminder(ActionEvent event) throws Exception {               
    try {
    	mainApp.showReminder(); 
    }
    catch(Exception e) {
    e.printStackTrace();
   }
 }
}
