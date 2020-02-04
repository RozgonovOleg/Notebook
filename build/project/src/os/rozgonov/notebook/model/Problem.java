package os.rozgonov.notebook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Problem {
	
	private StringProperty problems;
	private StringProperty selectedDate;
		
	public Problem() {this(null,null);}       
	
	public Problem(String problems, String selectedDate) {
		this.problems = new SimpleStringProperty(problems);
		this.selectedDate = new SimpleStringProperty(selectedDate);		
	}
	
	public String getproblems() {return problems.get();}
	
    public void setproblems(String problems) {this.problems.set(problems);}
    
    public StringProperty problemsProperty() {return problems;}
    
    public String getDate() {return selectedDate.get();}

    public void setDate(String selectedDate) {this.selectedDate.set(selectedDate);}

    public StringProperty selectedDateProperty() {return selectedDate;}
    
    public String toString(){return getDate();}
}
