import java.util.List;

public class Main {
    private static final String File_Name = "input.gr";
    public static void main(String[] args) {
        FileGrammarBuilder fileGrammarBuilder = new FileGrammarBuilder(File_Name);
        Grammar grammar = fileGrammarBuilder.build();

        grammar.printFirsts();
        grammar.printFollows();
        grammar.printTable();

    }
}
