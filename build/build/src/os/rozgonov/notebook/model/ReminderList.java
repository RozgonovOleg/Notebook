package os.rozgonov.notebook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReminderList {
	
	private StringProperty daytime;
	private StringProperty selectedProblem;
		
	public ReminderList() {this(null,null);}       
	
	public ReminderList(String daytime, String selectedProblem) {
		this.daytime = new SimpleStringProperty(daytime);
		this.selectedProblem = new SimpleStringProperty(selectedProblem);		
	}
	
	public String getdaytime() {return daytime.get();}
	
    public void setdaytime(String daytime) {this.daytime.set(daytime);}
    
    public StringProperty daytimeProperty() {return daytime;}
    
    public String getselectedProblem() {return selectedProblem.get();}

    public void setselectedProblem(String selectedProblem) {this.selectedProblem.set(selectedProblem);}

    public StringProperty selectedProblemProperty() {return selectedProblem;}
    
    public String toString(){return getdaytime()+" "+getselectedProblem();}
}
