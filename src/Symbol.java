import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Symbol {

    private String symbolStr;
    private SymbolType type;

    public Symbol(String symbolStr) {
        this.symbolStr = symbolStr;
        if (Character.isUpperCase(symbolStr.charAt(0)))
            this.type = SymbolType.NonTerminal;
        else if (symbolStr.charAt(0) == '#')
            this.type = SymbolType.Epsilon;
        else
            this.type = SymbolType.Terminal;

    }

    public String getSymbolStr() {
        return symbolStr;
    }

    public boolean isTerminal() {
        return this.type == SymbolType.Terminal;
    }

    public boolean isEpsilon() {
        return this.type == SymbolType.Epsilon;
    }

    public boolean isStart(){
        return this.symbolStr.equals("S");
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(symbolStr, symbol.symbolStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbolStr, type);
    }

    @Override
    public String toString() {
        return symbolStr;
    }


}
