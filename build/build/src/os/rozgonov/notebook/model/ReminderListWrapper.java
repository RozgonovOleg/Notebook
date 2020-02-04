package os.rozgonov.notebook.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "reminder")
public class ReminderListWrapper {

    private List<ReminderList> notes;
    
    @XmlElement(name = "notes")
    public List<ReminderList> getNotes() {
        return notes;
    }
    
    public void setNotes(List<ReminderList> notes) {
        this.notes = notes;
    }
}