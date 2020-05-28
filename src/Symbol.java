import java.util.ArrayList;
import java.util.List;

public class Symbol {

    private String symbolStr;

    private String type ="non-Terminal" ;

    public Symbol(String symbolStr) {
        this.symbolStr = symbolStr;


    }

    public String getSymbolStr() {
        return symbolStr;
    }

    public String getType() {

        List<Character> terminalList = new ArrayList<>();
        terminalList.add('+');
        terminalList.add('-');
        terminalList.add('(');
        terminalList.add(')');
        terminalList.add('/');
        terminalList.add('*');

        if (((int)(symbolStr.charAt(0)) <= 122 && (int)(symbolStr.charAt(0)) >= 97 )|| terminalList.contains(symbolStr.charAt(0)) ) {
            this.type = "Terminal";
        }
        else if ( symbolStr.charAt(0) == '#')
            this.type = "Epsilon";
        else
            this.type = "non-Terminal";

        return type;
    }


}
