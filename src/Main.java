import java.util.List;

public class Main {
    private static final String File_Name = "test2.txt";
//    private static final String File_Name = "test1.txt";
    public static void main(String[] args) {
        FileGrammarBuilder fileGrammarBuilder = new FileGrammarBuilder(File_Name);
        Grammar grammar = fileGrammarBuilder.build();

        grammar.printFirsts();
        System.out.println("==============================");
        grammar.printFollows();
        System.out.println("==============================");
        grammar.isNullable(new Symbol("A"));
        System.out.println("==============================");
        grammar.printTable();

    }
}
