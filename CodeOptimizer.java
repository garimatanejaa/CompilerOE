import java.io.*;
import java.util.*;
import java.util.regex.*;

public class CodeOptimizer {
    public static void main(String[] args) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("OutputThreeCode"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {  
                    lines.add(line);
                }
            }
            reader.close();
            
            List<String> optimizedLines = optimizeLines(lines);
            
            boolean hasReturn = false;
            for (String l : lines) {
                if (l.trim().startsWith("return")) {
                    hasReturn = true;
                    break;
                }
            }
            boolean optimizedHasReturn = false;
            for (String l : optimizedLines) {
                if (l.trim().startsWith("return")) {
                    optimizedHasReturn = true;
                    break;
                }
            }
        
            if (hasReturn && !optimizedHasReturn) {
                for (int i = lines.size() - 1; i >= 0; i--) {
                    if (lines.get(i).trim().startsWith("return")) {
                        optimizedLines.add(lines.get(i).trim());
                        break;
                    }
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("OutputCodeOptimize"));
            for (String optimizedLine : optimizedLines) {
                writer.write(optimizedLine);
                writer.newLine();
            }
            writer.close();
            
            System.out.println("✅ Optimization completed. Check OutputCodeOptimize file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // This method looks for a pattern:
    //   Tn = expression
    //   var = Tn
    // and replaces them with:
    //   var = expression
    private static List<String> optimizeLines(List<String> lines) {
        List<String> optimized = new ArrayList<>();
        Pattern tempAssignPattern = Pattern.compile("^(T\\d+) = (.+)$");
        Pattern propagatePattern = Pattern.compile("^(.+) = (T\\d+)$");
        
        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i);
            Matcher m = tempAssignPattern.matcher(currentLine);
            if (m.matches() && i + 1 < lines.size()) {
                String tempVar = m.group(1); 
                String expr = m.group(2);    
                String nextLine = lines.get(i + 1);
                Matcher m2 = propagatePattern.matcher(nextLine);
                if (m2.matches()) {
                    String var = m2.group(1).trim();
                    String usedTemp = m2.group(2).trim();
                    if (usedTemp.equals(tempVar)) {
                        
                        optimized.add(var + " = " + expr);
                        i++; 
                        continue;
                    }
                }
            }
           
            optimized.add(currentLine);
        }
        return optimized;
    }
}
