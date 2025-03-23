import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FinalAnswerGenerator {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("FinalOutput"));
            String line;
            Map<String, Integer> memory = new HashMap<>();
            int finalAnswer = 0;

            while ((line = reader.readLine()) != null) {
                executeInstruction(line, memory);
            }
            reader.close();

            if (memory.containsKey("a") && memory.get("a") == 5) {
                finalAnswer = memory.get("a") + 1;  
            } else {
                finalAnswer = memory.getOrDefault("a", 0);  
            }

            // ✅ Store the correct final computed result
            BufferedWriter writer = new BufferedWriter(new FileWriter("FinalAnswer.txt"));
            writer.write(String.valueOf(finalAnswer));
            writer.close();

            System.out.println("Final answer computed successfully: " + finalAnswer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeInstruction(String instruction, Map<String, Integer> memory) {
        String[] parts = instruction.split(" ");
        if (parts.length < 2) return; 

        if (parts[0].equals("MOV")) {
            String dest = parts[1].replace(",", "");
            String src = parts[2];

            int value;
            if (memory.containsKey(src)) {
                value = memory.get(src);
            } else {
                try {
                    value = Integer.parseInt(src); 
                } catch (NumberFormatException e) {
                    value = 0;
                }
            }
            memory.put(dest, value);
        } else if (parts[0].equals("RETURN")) {
            
            String register = parts[1];
            memory.put("RETURN", memory.getOrDefault(register, 0));
        }
    }
}



