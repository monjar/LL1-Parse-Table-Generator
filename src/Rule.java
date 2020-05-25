import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rule {
    private String leftHandSide;
    private List<String> rightHandSide;

    public Rule(String[] ruleString) {
        rightHandSide = new ArrayList<>();
        this.leftHandSide = ruleString[0];
        rightHandSide.addAll(Arrays.asList(ruleString).subList(2, ruleString.length));
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public List<String> getRightHandSide() {
        return rightHandSide;
    }

    public void print(){
        System.out.print(leftHandSide + " ==> ");
        rightHandSide.forEach((string)->{
            System.out.print(string+ " ");
        });
        System.out.print("\n");
    }
}
