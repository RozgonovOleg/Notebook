package os.rozgonov.notebook;


import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import java.io.File;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import os.rozgonov.notebook.model.Problem;
import os.rozgonov.notebook.model.ProblemListWrapper;
import os.rozgonov.notebook.model.ReminderList;
import os.rozgonov.notebook.model.ReminderListWrapper;
import os.rozgonov.notebook.view.ReminderController;
import os.rozgonov.notebook.view.overviewController;

public class MainApp extends Application {
	
	private Stage primaryStage;
    private BorderPane border;    
    
    private ObservableList<Problem> problemData = FXCollections.observableArrayList(); 
    
    private ObservableList<ReminderList> RList = FXCollections.observableArrayList(); 
    
    private ObservableList<ReminderList> filteredRList = FXCollections.observableArrayList();
    
    public MainApp() {
        problemData.add(new Problem("A","2016-10-27"));        
    }    	
    public ObservableList<Problem> getProblemData() {
            return this.problemData;
    }    
    public ObservableList<ReminderList> getRList() {
        return this.RList;
    }
    private List<ReminderList> getfilteredRList() {
    	return this.filteredRList;
    }
	@Override
	public void start(Stage primaryStage) {
		Platform.setImplicitExit(false);
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Notebook");
        toTray();
        initborder();
        showoverview();        
        File file1=new File("ReminderFile.xml");
        loadReminderFromFile(file1);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	File file1=new File("ReminderFile.xml");
            	saveReminderListToFile(file1);
            	}
            });
        startTasks();
	}
	
	public void initborder() {
        try {            
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("os/rozgonov/notebook/view/border.fxml"));
            border = (BorderPane) loader.load();
            
            Scene scene = new Scene(border);
            primaryStage.setScene(scene);                       
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = getProblemFilePath();
        if (file != null) {
            loadProblemDataFromFile(file);
        }
    }
	
	public void showoverview() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("os/rozgonov/notebook/view/overview.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();
            overviewController controller = loader.getController();
            controller.setMainApp(this);
            
            border.setCenter(overview);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	
	public void showReminder() {
	    try {
	        
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/Reminder.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        ReminderController controller = loader.getController();
            controller.setMainApp(this);
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Напоминание");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);	               
	        dialogStage.showAndWait();
	        	       
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	        
	    }
	}

	public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
		launch(args);
	}

 public File getProblemFilePath() {
    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
    String filePath = prefs.get("filePath", null);
    if (filePath != null) {
        return new File(filePath);
    } else {
        return null;
   }
 }  
  
 public void setProblemFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());	        
	        primaryStage.setTitle("Notebook");
	    } else {
	        prefs.remove("filePath");	        
	        primaryStage.setTitle("Notebook");
	    }
	}

	
 public void loadProblemDataFromFile(File file) {
	    try {  	  
	    	JAXBContext context = JAXBContext
	                .newInstance(ProblemListWrapper.class);	        
	        Unmarshaller um = context.createUnmarshaller();	        
	        ProblemListWrapper wrap = (ProblemListWrapper)um.unmarshal(file);
	        
	        problemData.clear();	        
	        problemData.addAll(wrap.getProblems());
	        
	        setProblemFilePath(file);	              

	    } catch (Exception e) { 
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not load data");
	        alert.setContentText("Could not load data from file:\n" + file.getPath());
	        alert.showAndWait();
	    }	    
	}
 
 public void saveProblemDataToFile(File file) {
	try{ 
	       JAXBContext context = JAXBContext
	                .newInstance(ProblemListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        ProblemListWrapper wrapper = new ProblemListWrapper();	              
	        wrapper.setProblems(problemData);	        
	        m.marshal(wrapper, file);	       
	        setProblemFilePath(file);
	        
	    } catch (Exception e) { 
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());
	        alert.showAndWait();
	    }
	}
 
 public void loadReminderFromFile(File file1) {
	    try {  	  
	    	JAXBContext context = JAXBContext
	                .newInstance(ReminderListWrapper.class);	        
	        Unmarshaller um = context.createUnmarshaller();	        
	        ReminderListWrapper wrap = (ReminderListWrapper)um.unmarshal(file1);	        	        
	        
	        RList.clear();
	        filteredRList.addAll(wrap.getNotes());
	        int l = filteredRList.size();
	    	int i;
	    	for( i=0; i<l ; i++){
	    		String s = getfilteredRList().get(i).getdaytime();
	    		String s1 =s.substring(0, 10);
	    		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	 try
	    	     {
	    	         Date date = simpleDateFormat.parse(s1);
	    	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    	         LocalDate ld = LocalDate.now().minusDays(1);
	    	         String ld1 =ld.format(formatter);
	    	         Date d = simpleDateFormat.parse(ld1);	    	         
		    		 boolean after = date.after(d);		    		 
		    		 if(after){		    			 
						RList.add(getfilteredRList().get(i));
		    		 }
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
	        } 
	        catch (Exception e) { 	        
	    }	    
	}

public void saveReminderListToFile(File file1) {
	try{ 
	       JAXBContext context = JAXBContext
	                .newInstance(ReminderListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        ReminderListWrapper wrapper = new ReminderListWrapper();
	        filteredRList.clear();
	        int l = RList.size();
	    	int i;
	    	for( i=0; i<l ; i++){
	    		String s = getRList().get(i).getdaytime();	    		
	    		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    	 try
	    	     {
	    	         Date date = simpleDateFormat.parse(s);	 
	    	         LocalDateTime ld = LocalDateTime.now();
	    	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");	    	         
	    	         String ld1= ld.format(formatter);	    	         	         
	    	         Date d = simpleDateFormat.parse(ld1);
	    	         boolean after = date.after(d);		
		    		 if(after){		    			 
		    			 filteredRList.add(getRList().get(i));
		    		 }
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
	        
	        wrapper.setNotes(filteredRList);	        
	        m.marshal(wrapper, file1);	       
	        
	    } catch (Exception e) { 
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file1.getPath());
	        alert.showAndWait();
	    }
	}
public void startTasks() {
	int l = RList.size();
	int i;
	for( i=0; i<l ; i++){
	String notes = getRList().get(i).getselectedProblem();
    TimerTask task = new TimerTask() {
    
      public void run() {		
        Platform.runLater(() -> {
       	 Alert alert = new Alert(AlertType.INFORMATION);        		
            alert.setTitle("Напоминание");
            alert.setHeaderText("Напоминание");
            alert.setContentText(notes);
            alert.showAndWait();	
        });        
	}      
  };        
    String s1 = getRList().get(i).getdaytime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 try
     {
         Date date = simpleDateFormat.parse(s1);         
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
  }
public void toTray(){
	 if (SystemTray.isSupported()) {         
         SystemTray tray = SystemTray.getSystemTray();
         Image image = Toolkit.getDefaultToolkit().getImage(
         		"resources/images/Notebook-symbol-white64.png");
         PopupMenu popup = new PopupMenu();
         MenuItem exitItem = new MenuItem("Выход");
         
         MenuItem openItem = new java.awt.MenuItem("Развернуть");
         
         openItem.addActionListener(event -> Platform.runLater(this::initborder));
         openItem.addActionListener(event -> Platform.runLater(this::showoverview));
         
         popup.add(openItem);
         popup.addSeparator();
         popup.add(exitItem);
         
         TrayIcon trayIcon = new TrayIcon(image, "Notebook", popup); 
         trayIcon.setImageAutoSize(true);
         trayIcon.addActionListener(event -> Platform.runLater(this::initborder));
         trayIcon.addActionListener(event -> Platform.runLater(this::showoverview));
         
         exitItem.addActionListener(event -> {             
             Platform.exit();
             tray.remove(trayIcon);
         });    
         
         Font defaultFont = java.awt.Font.decode(null);
         Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
         openItem.setFont(boldFont);

         try{
           tray.add(trayIcon);
         }catch (Exception e) {
           System.err.println("Can't add to tray");
         }
       } else {
         System.err.println("Tray unavailable");
       }      
 }
}

