import java.io.*;
import java.util.*;

class ASTNode {
    String type;
    String value;
    int lineNumber;
    List<ASTNode> children;

    public ASTNode(String type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
        this.children = new ArrayList<>();
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }
}

public class ASTBuilder {
    public static void main(String[] args) {
        ASTNode root = new ASTNode("PROGRAM", "Main", 1);

        try (BufferedReader br = new BufferedReader(new FileReader("OutputLexer"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("OutputParser"))) {

            List<ASTNode> nodes = parseTokens(br);
            for (ASTNode node : nodes) {
                root.addChild(node);
            }

            writeASTToFile(root, bw);
            System.out.println("AST written to OutputParser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<ASTNode> parseTokens(BufferedReader br) throws IOException {
        List<ASTNode> nodes = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.replaceAll("[\\[\\]]", "").split(", ");
            if (parts.length < 3) continue;

            String type = parts[0].trim();
            String value = parts[1].trim();
            int lineNumber = Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));

            ASTNode node = new ASTNode(type, value, lineNumber);
            nodes.add(node);
        }
        return nodes;
    }

    private static void writeASTToFile(ASTNode node, BufferedWriter bw) throws IOException {
        writeNodeToFile(node, bw, 0);
        bw.close();
    }

    private static void writeNodeToFile(ASTNode node, BufferedWriter bw, int depth) throws IOException {
        for (int i = 0; i < depth; i++) {
            bw.write("  ");
        }
        bw.write(node.type + ": " + node.value + " (Line " + node.lineNumber + ")\n");

        for (ASTNode child : node.children) {
            writeNodeToFile(child, bw, depth + 1);
        }
    }
}

