package os.rozgonov.notebook.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "pers")
public class ProblemListWrapper {

    private List<Problem> problems;
    
    @XmlElement(name = "problem")
    public List<Problem> getProblems() {
        return problems;
    }
    
    public void setProblems(List<Problem> problems1) {
        this.problems = problems1;
    }
}