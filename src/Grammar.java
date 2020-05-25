import java.util.List;
import java.util.stream.Collectors;

public class Grammar {
    private List<Rule> rulesList;
    Grammar(List<String[]> rules ){

        rulesList = rules.stream().map(Rule::new).collect(Collectors.toList());
    }

    public void print(){
        rulesList.forEach(Rule::print);
    }

    public List<String> getRightHandSide(int index){
        return rulesList.get(index).getRightHandSide();
    }

    public String getLeftHandSide(int index){
        return rulesList.get(index).getLeftHandSide();
    }
}

