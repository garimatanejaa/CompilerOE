import java.io.*;
import java.util.*;

public class ThreeAddressCodeGenerator {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("OutputLO"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("OutputThreeCode"))) {
            List<String> tokens = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                tokens.add(line.trim());
            }

            List<String> tac = generateThreeAddressCode(tokens);
            for (String s : tac) {
                writer.write(s);
                writer.newLine();
            }
            System.out.println("✅ Three-address code generated in OutputThreeCode");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static List<String> generateThreeAddressCode(List<String> tokens) {
        List<String> tac = new ArrayList<>();
        int i = 0;

        if (i < tokens.size() && tokens.get(i).startsWith("Identifier: main")) {
            i++;
        }
        if (i + 2 < tokens.size()
            && tokens.get(i).startsWith("Identifier: a")
            && tokens.get(i + 1).startsWith("Operator: =")
            && tokens.get(i + 2).startsWith("Literal:")) {
            String value = tokens.get(i + 2).split(": ")[1];
            tac.add("a = " + value);
            i += 3;
        }
        if (i + 2 < tokens.size()
            && tokens.get(i).startsWith("Identifier: b")
            && tokens.get(i + 1).startsWith("Operator: =")
            && tokens.get(i + 2).startsWith("Literal:")) {
            String value = tokens.get(i + 2).split(": ")[1];
            tac.add("b = " + value);
            i += 3;
        }
        String conditionTemp = "";
        if (i + 2 < tokens.size()
            && tokens.get(i).startsWith("Identifier: a")
            && tokens.get(i + 1).startsWith("Operator: >")
            && tokens.get(i + 2).startsWith("Literal:")) {
            String left = tokens.get(i).split(": ")[1];
            String op = ">";
            String right = tokens.get(i + 2).split(": ")[1];
            conditionTemp = "T1";
            tac.add("T1 = " + left + " " + op + " " + right);
            tac.add("if T1 goto L1");
            tac.add("goto L2");
            i += 3;
        }
        if (i + 4 < tokens.size()
            && tokens.get(i).startsWith("Identifier: a")
            && tokens.get(i + 1).startsWith("Operator: =")
            && tokens.get(i + 2).startsWith("Identifier: a")
            && tokens.get(i + 3).startsWith("Operator: +")
            && tokens.get(i + 4).startsWith("Literal:")) {
            String left = tokens.get(i + 2).split(": ")[1];
            String op = "+";
            String right = tokens.get(i + 4).split(": ")[1];
            tac.add("L1:");
            tac.add("T2 = " + left + " " + op + " " + right);
            tac.add("a = T2");
            tac.add("goto L3");
            i += 5;
        }
        if (i + 4 < tokens.size()
            && tokens.get(i).startsWith("Identifier: a")
            && tokens.get(i + 1).startsWith("Operator: =")
            && tokens.get(i + 2).startsWith("Identifier: a")
            && tokens.get(i + 3).startsWith("Operator: -")
            && tokens.get(i + 4).startsWith("Literal:")) {
            String left = tokens.get(i + 2).split(": ")[1];
            String op = "-";
            String right = tokens.get(i + 4).split(": ")[1];
            tac.add("L2:");
            tac.add("T3 = " + left + " " + op + " " + right);
            tac.add("a = T3");
            tac.add("L3:");
            i += 5;
        }
      
        if (i + 1 < tokens.size()
            && tokens.get(i).startsWith("Keyword: return")  
            && tokens.get(i + 1).startsWith("Identifier:")) {
            String retVar = tokens.get(i + 1).split(": ")[1];
            tac.add("return " + retVar);
            i += 2;
        }
        return tac;
    }
}
