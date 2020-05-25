import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileGrammarBuilder {

    private Grammar grammar;

    FileGrammarBuilder(String fileName) {
        List<String[]> rules = new ArrayList<>();
        readRulesFromFile(fileName, rules);
        this.grammar = new Grammar(rules);
    }

    private void readRulesFromFile(String fileName, List<String[]> rules) {
        try {
            FileReader fr = new FileReader(new File(fileName));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                rules.add(line.split(" "));
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Grammar build() {
        return this.grammar;
    }
}
