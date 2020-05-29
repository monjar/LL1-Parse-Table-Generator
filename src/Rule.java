import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rule {
    private int id;
    private Symbol leftHandSide;
    private List<Symbol> rightHandSide;

    public Rule(String[] ruleString, int id) {
        List<Symbol> list = Arrays.stream(ruleString).map(Symbol::new).collect(Collectors.toList());
        this.leftHandSide = list.get(0);
        rightHandSide = list.subList(2,list.size());
        this.id = id;
    }

    public Symbol getLeftHandSide() {
        return leftHandSide;
    }

    public List<Symbol> getRightHandSide() {
        return rightHandSide;
    }

    public void print(){
        System.out.print(leftHandSide + " ==> ");
        rightHandSide.forEach((symbol)->{
            System.out.print(symbol.getSymbolStr()+ " ");
        });
        System.out.print("\n");
    }

    @Override
    public String toString() {
        return "[" + String.valueOf(id) + "]";
    }
}
