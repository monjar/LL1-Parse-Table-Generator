import java.util.List;

public class TableStringFormatter {
    private StringBuilder sb;
    TableStringFormatter(){
        sb = new StringBuilder();
    }

    public String stringifyTable(Rule[][] arr, List<Symbol> rows, List<Symbol> cols){
        stringifyColumnLabels(cols);
        stringifyTableValues(arr,rows,cols);
        return sb.toString();
    }

    private void stringifyColumnLabels(List<Symbol> cols) {
        sb.append("          \t\t\t");
        for (Symbol s: cols)
            sb.append(this.formatTerminalString(s)).append("\t\t\t");
    }
    private void stringifyTableValues(Rule[][] arr, List<Symbol> rows, List<Symbol> cols) {
        sb.append("\n");
        for (  Symbol left : rows )
            stringifyRowValues(arr[rows.indexOf(left)], cols, left);

    }

    private void stringifyRowValues(Rule[] rules, List<Symbol> cols,  Symbol left) {
        sb.append(this.formatNonTerminalString(left)).append("\t\t\t");
        for (Symbol terminal :cols ) {
            var pr = rules[cols.indexOf(terminal)];
            sb.append(this.formatRuleString(pr)).append("\t\t\t");
        }
        sb.append("\n");
    }

    private String formatNonTerminalString(Symbol nonTerminal){
        int addSpace = 10 - nonTerminal.getSymbolStr().length();
        return nonTerminal.getSymbolStr() + " ".repeat(Math.max(0, addSpace));
    }
    private String formatTerminalString(Symbol terminal){
        int addSpace = 4 - terminal.getSymbolStr().length();
        return terminal.getSymbolStr() + " ".repeat(Math.max(0, addSpace));
    }
    private String formatRuleString(Rule rule){
        if (rule ==null)
            return "[ ]   ";
        int addSpace = 6 - rule.toString().length();
        return rule.toString() + " ".repeat(Math.max(0, addSpace));
    }
}
