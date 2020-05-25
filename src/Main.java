import java.util.List;

public class Main {
    private static final String File_Name = "input.gr";
    public static void main(String[] args) {
        FileGrammarBuilder fileGrammarBuilder = new FileGrammarBuilder(File_Name);
        Grammar grammar = fileGrammarBuilder.build();

        //grammar.print();

        //this function returns the left hand side of ith rule
        String leftOfThird = grammar.getLeftHandSide(3);

        //this function returns the right hand side of ith rule as a list
        List<String> rightOfThird = grammar.getRightHandSide(3);

        System.out.println(leftOfThird);
        System.out.println(rightOfThird);
    }
}
