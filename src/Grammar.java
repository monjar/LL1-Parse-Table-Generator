import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private List<Rule> rulesList;
    private Map<Symbol , List> firstSets = new HashMap<>();
    private Map<Symbol , List> followSets = new HashMap<>();

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


    private List<Rule> getRulesWithSymbolLHS(Symbol symbol){
        List<Rule> rulesWithSymbolLHS = new ArrayList<>();
        for (Rule rule : rulesList) {
            if (rule.getLeftHandSide().equals(symbol.getSymbolStr()))
                rulesWithSymbolLHS.add(rule);
        }
       return rulesWithSymbolLHS;
    }

    public List<Symbol> findFirstOfSymbol(Symbol symbol){
        List<Symbol> firstOfList = new ArrayList<>();

        if(symbol.getType().equals("Terminal")){ // if symbol is terminal, then add it to its firstOfList and return
            firstOfList.add(symbol);
            return firstOfList;
        }

        List<Rule> rules = getRulesWithSymbolLHS(symbol); //productions with symbol as LHS
        for(Rule rule: rules) {

            List<String> rhsSymbolList = rule.getRightHandSide();

            for (int i = 0; i < rhsSymbolList.size(); i++) {
                //if rhs is epsilon => first symbol is epsilon ( a -> # )
                String rhsSymbol = rhsSymbolList.get(i);
                if (rhsSymbol.equals("#")) {
                    firstOfList.add(new Symbol("#"));
                    break;
                }

                //otherwise go find the first of the rhs
                List<Symbol> firstOfRHSList = findFirstOfSymbol(new Symbol(rhsSymbol));
                boolean containEpsilon = false;
                int epsilonIndex = -1;

                if (i == rhsSymbolList.size() - 1) {
                    // rhsSymbol is the last symbol in rhs. even if contains epsilon it must be added to the list. no need to check
                    // a-> bc , b->epsilon , c->epsilon
                    firstOfList.addAll(firstOfRHSList);
                    continue;
                }

                for (int j = 0; j < firstOfRHSList.size(); j++) {
                    Symbol firstofRHS = firstOfRHSList.get(j);
                    if (firstofRHS.getSymbolStr().equals("#")) {
                        epsilonIndex = j;
                        containEpsilon = true;
                        break;
                    }
                }

                if (!containEpsilon) {
                    firstOfList.addAll(firstOfRHSList);
                    break;
                }
                //a-> bc , b-> epsilon => first a = first b - epsilon + first c
                firstOfRHSList.remove(epsilonIndex);
                firstOfList.addAll(firstOfRHSList);

            }


        }
        return firstOfList;
    }



    public List<Symbol> findFollowSymbol1(Symbol symbol){
        List<Rule> rulesWithSymbolList = new ArrayList<>();
        List<Symbol> followOfList = new ArrayList<>();

        rulesWithSymbolList = findRulesWithSymbol(symbol);

        for(Rule rule : rulesWithSymbolList){
            List<String> rhsList = rule.getRightHandSide();

            int symbolIndexInRhs = getSymbolIndexInRhs(symbol, rhsList);
            for (int i = symbolIndexInRhs + 1; i <= rhsList.size() ; i++) {
                //if (i== rhsList.size())
                   // graph[symbol][rule.getLeftHandSide()] = true;
                List<Symbol> firstOfNext = findFirstOfSymbol(new Symbol(rhsList.get(i)));
                int epsIndex = searchForEps(firstOfNext);
               if (epsIndex != -1) {  //first contains epsilon
                firstOfNext.remove(epsIndex);
                followOfList.addAll(firstOfNext);
                continue;
               }

               followOfList.addAll(firstOfNext);
               break;

            }
        }
return followOfList;

    }

    private int searchForEps(List<Symbol> firstOfNext) {
        int epsIndex = -1;
        for (int j = 0; j < firstOfNext.size(); j++) {
            if (firstOfNext.get(j).equals("#")){
                epsIndex = j;
                break;
            }
        }
        return epsIndex ;
    }

    private int getSymbolIndexInRhs(Symbol symbol, List<String> rhsList) {
        for(int i = 0; i<rhsList.size(); i++){
            if (rhsList.get(i).equals(symbol.getSymbolStr())) {
              return  i;
            }
        }
        return -1;
    }

    public  List<Rule> findRulesWithSymbol(Symbol symbol){
        List<Rule> rulesWithSymbolList = new ArrayList<>();
        for(Rule rule: rulesList){
         //search symbol in the right hand side
            if(SearchInHandSides(symbol, rule))
                rulesWithSymbolList.add(rule);
        }
        return rulesWithSymbolList;
    }

    private boolean SearchInHandSides(Symbol symbol, Rule rule) {
        //check left hand_side
            List<String> rhsList = rule.getRightHandSide();
            for (String rhs : rhsList)
                if (rhs.contains(symbol.getSymbolStr()))
                {
                    return true;
                }
        return false;
    }
}

