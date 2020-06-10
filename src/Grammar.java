import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private List<Rule> rulesList;
    private Map<Symbol, List> firstSets = new HashMap<>();
    private Map<Symbol, List> followSets = new HashMap<>();
    private Set<Symbol> terminals;
    private Set<Symbol> nonTerminals;

    Grammar(List<String[]> rules) {
        allocateMemory();
        for (int index = 0; index < rules.size(); index++) {
            Rule rule = new Rule(rules.get(index), index);
            nonTerminals.add(rule.getLeftHandSide());
            rule.getRightHandSide().stream().filter(Symbol::isTerminal).forEach(terminals::add);
            terminals.add(new Symbol("$"));
            rulesList.add(rule);
        }
        isDependent = new HashMap<>();
        nonTerminals.forEach(t -> isDependent.put(t, new HashMap<>()));
    }

    private void allocateMemory() {
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();
        rulesList = new ArrayList<>();
    }

    public List<Symbol> getRightHandSide(int index) {
        return rulesList.get(index).getRightHandSide();
    }

    public List<Rule> getRulesList() {
        return rulesList;
    }

    public Symbol getLeftHandSide(int index) {
        return rulesList.get(index).getLeftHandSide();
    }


    private List<Rule> getRulesWithSymbolLHS(Symbol symbol) {
        List<Rule> rulesWithSymbolLHS = new ArrayList<>();
        for (Rule rule : rulesList) {
            if (rule.getLeftHandSide().equals(symbol))
                rulesWithSymbolLHS.add(rule);
        }
        return rulesWithSymbolLHS;
    }

    public List<Symbol> findFirstOfSymbol(Symbol symbol) {

        List<Symbol> firstOfList = new ArrayList<>();
        if (symbol.isTerminal()) { // if symbol is terminal, then add it to its firstOfList and return
            firstOfList.add(symbol);
            return firstOfList;
        }

        List<Rule> rules = getRulesWithSymbolLHS(symbol); //productions with symbol as LHS

        for (Rule rule : rules) {

            List<Symbol> rhsSymbolList = rule.getRightHandSide();

            for (int i = 0; i < rhsSymbolList.size(); i++) {

                //if rhs is epsilon => first symbol is epsilon ( a -> # )
                Symbol rhsSymbol = rhsSymbolList.get(i);
                if (rhsSymbol.isEpsilon()) {
                    firstOfList.add(new Symbol("#"));
                    break;
                }

                //otherwise go find the first of the rhs
                List<Symbol> firstOfRHSList = findFirstOfSymbol(rhsSymbol);
                boolean containEpsilon = false;
                int epsilonIndex = -1;

                if (i == rhsSymbolList.size() - 1) {
                    // rhsSymbol is the last symbol in rhs. even if contains epsilon it must be added to the list. no need to check
                    // a-> bc , b->epsilon , c->epsilon
                    mergeLists(firstOfList, firstOfRHSList);
                    continue;
                }

                for (int j = 0; j < firstOfRHSList.size(); j++) {
                    Symbol firstofRHS = firstOfRHSList.get(j);
                    if (firstofRHS.isEpsilon()) {
                        epsilonIndex = j;
                        containEpsilon = true;
                        break;
                    }
                }

                if (!containEpsilon) {
                    mergeLists(firstOfList, firstOfRHSList);
                    break;
                }
                //a-> bc , b-> epsilon => first a = first b - epsilon + first c
                firstOfRHSList.remove(epsilonIndex);
                mergeLists(firstOfList, firstOfRHSList);

            }


        }
        return firstOfList;
    }

    public List<Symbol> getFirstWithoutEpsilon(Symbol symbol) {
        return this.findFirstOfSymbol(symbol).stream().filter(symb -> !symb.isEpsilon()).collect(Collectors.toList());
    }

    Map<Symbol, Map<Symbol, Boolean>> isDependent;

    public List<Symbol> findFollowSymbol1(Symbol symbol) {

        List<Rule> rulesWithSymbolList = rulesList.stream()
                .filter(rule -> rule.getRightHandSide().contains(symbol))
                .collect(Collectors.toList());

        Set<Symbol> followOfList = new HashSet<>();
        if (symbol.isStart())
            followOfList.add(new Symbol("$"));
        for (Rule rule : rulesWithSymbolList)
            findFollowInRule(symbol, followOfList, rule);

        return new ArrayList<>(followOfList);

    }

    public List<Symbol> findFollow(Symbol symbol) {
        isDependent = new HashMap<>();
        nonTerminals.forEach(t -> isDependent.put(t, new HashMap<>()));
        return findFollowSymbol1(symbol);

    }


    private void findFollowInRule(Symbol symbol, Set<Symbol> followOfList, Rule rule) {
        List<Symbol> rhsList = rule.getRightHandSide();
        int symbolIndexInRhs = rhsList.indexOf(symbol);
        if (symbolIndexInRhs < rhsList.size() - 1)
            addUntilNonNull(followOfList, rule, rhsList, symbolIndexInRhs);
        else if (!isDependencyAdded(symbol, rule) && !symbol.equals(rule.getLeftHandSide())) {
            isDependent.get(symbol).put(rule.getLeftHandSide(), true);
            followOfList.addAll(findFollowSymbol1(rule.getLeftHandSide()));
        }
    }

    private boolean isDependencyAdded(Symbol symbol, Rule rule) {
        return isDependent.get(symbol).containsKey(rule.getLeftHandSide()) && isDependent.get(symbol).get(rule.getLeftHandSide());
    }

    private void addUntilNonNull(Set<Symbol> followOfList, Rule rule, List<Symbol> rhsList, int symbolIndexInRhs) {
        for (int index = symbolIndexInRhs + 1; index < rhsList.size(); index++) {
            Symbol next = rhsList.get(index);
            followOfList.addAll(getFirstWithoutEpsilon(next));
            if (!isNullable(next))
                break;
            if (index == rhsList.size() - 1)
                followOfList.addAll(findFollowSymbol1(rule.getLeftHandSide()));
        }
    }

    private void mergeLists(List<Symbol> list1, List<Symbol> list2) {
        for (Symbol secondListSymbol : list2) {
            int symIndex = searchForSymbolInList(list1, secondListSymbol);
            if (symIndex == -1)
                list1.add(secondListSymbol);
        }
    }

    private int searchForSymbolInList(List<Symbol> firstOfNext, Symbol symbol) {
        String symStr = symbol.getSymbolStr();
        int symIndex = -1;
        for (int j = 0; j < firstOfNext.size(); j++) {
            if (firstOfNext.get(j).getSymbolStr().equals(symStr)) {
                symIndex = j;
                break;
            }
        }
        return symIndex;
    }


    public boolean isNullable(Symbol symbol) {
        return symbol.isEpsilon() || (!symbol.isTerminal() && rulesList
                .stream().filter(w -> w.getLeftHandSide().equals(symbol))
                .map(Rule::getRightHandSide).anyMatch(right -> right.stream().allMatch(this::isNullable)));
    }

    public List<Symbol> makePredictions(Rule rule) {
        Set<Symbol> ans = new HashSet<>();
        for (Symbol symbol : rule.getRightHandSide())
            if (predictRuleForSymbol(rule, ans, symbol)) break;
        return new ArrayList<>(ans);
    }

    private boolean predictRuleForSymbol(Rule rule, Set<Symbol> ans, Symbol symbol) {
        if (isNullable(symbol)) {
            if (symbol.isEpsilon())
                ans.addAll(findFollow(rule.getLeftHandSide()));
            else
                ans.addAll(findFirstOfSymbol(symbol));
        } else {
            if (symbol.isTerminal())
                ans.add(symbol);
            else
                ans.addAll(findFirstOfSymbol(symbol));
            return true;
        }
        return false;
    }

    public String buildTable() {
        Rule[][] arr = new Rule[nonTerminals.size()][terminals.size()];
        List<Symbol> rows = new ArrayList<>(nonTerminals);
        List<Symbol> cols = new ArrayList<>(terminals);
        for (var pr : this.rulesList)
            makePredictionAndAddToTable(arr, rows, cols, pr);
        TableStringFormatter formatter = new TableStringFormatter();
        return formatter.stringifyTable(arr, rows, cols);
    }

    private void makePredictionAndAddToTable(Rule[][] arr, List<Symbol> rows, List<Symbol> cols, Rule pr) {
        List<Symbol> prodRulePredict = makePredictions(pr);
        Symbol left = pr.getLeftHandSide();
        for (var symbol : prodRulePredict) {
            if (!symbol.isEpsilon())
                arr[rows.indexOf(left)][cols.indexOf(symbol)] = pr;
        }
    }

    public void printTable() {
        System.out.println(this.buildTable());
    }

    public void printFollows() {
        nonTerminals.forEach(symbol -> {
            System.out.print("Follow(" + symbol + ") :");
            System.out.println(findFollow(symbol));
        });
    }

    public void printFirsts() {
        nonTerminals.forEach(symbol -> {
            System.out.print("First(" + symbol + ") :");
            System.out.println(findFirstOfSymbol(symbol));
        });
    }

}

