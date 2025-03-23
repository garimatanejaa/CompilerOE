import java.io.*;
import java.util.*;

public class CLexer {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "int", "float", "if", "else", "return"));
    
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList(
            "+", "-", "*", "/", "=", ">", "<", ">=", "<="));

    public static void main(String[] args) {
        String filename = "input.c";
        try {
            List<Token> tokens = tokenize(filename);
            writeTokensToFile(tokens);
            System.out.println("Tokens written to OutputLexer");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static List<Token> tokenize(String filename) throws IOException {
        List<Token> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int lineNumber = 1;

        while ((line = reader.readLine()) != null) {
            tokens.addAll(tokenizeLine(line, lineNumber));
            lineNumber++;
        }
        reader.close();
        return tokens;
    }

    private static List<Token> tokenizeLine(String line, int lineNumber) {
        List<Token> tokens = new ArrayList<>();
        String[] words = line.split("\\s+|(?=[(){};,])|(?<=[(){};,])");

        for (String word : words) {
            word = word.trim();
            if (!word.isEmpty()) {
                if (KEYWORDS.contains(word)) {
                    tokens.add(new Token("KEYWORD", word, lineNumber));
                } else if (OPERATORS.contains(word)) {
                    tokens.add(new Token("OPERATOR", word, lineNumber));
                } else if (word.matches("\\d+\\.\\d+") || word.matches("\\d+")) {  // ✅ Supports floats & integers
                    tokens.add(new Token("NUMBER", word, lineNumber));
                } else if (word.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    tokens.add(new Token("IDENTIFIER", word, lineNumber));
                }
            }
        }
        return tokens;
    }

    private static void writeTokensToFile(List<Token> tokens) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("OutputLexer"));
        for (Token token : tokens) {
            writer.write(token.toString());
            writer.newLine();
        }
        writer.close();
    }

    static class Token {
        String type, value;
        int lineNumber;

        Token(String type, String value, int lineNumber) {
            this.type = type;
            this.value = value;
            this.lineNumber = lineNumber;
        }

        public String toString() {
            return "[" + type + ", " + value + ", Line " + lineNumber + "]";
        }
    }
}
