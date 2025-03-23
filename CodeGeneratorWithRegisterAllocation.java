import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CodeGeneratorWithRegisterAllocation {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("OutputCodeOptimize"));
            String line;
            StringBuilder generatedCode = new StringBuilder();
            int[] registerIndex = new int[]{1}; 
            Map<String, String> registerMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
               
                String generatedLine = generateCodeWithRegisterAllocation(line, registerMap, registerIndex);
                generatedCode.append(generatedLine).append("\n");
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("FinalOutput"));
            writer.write(generatedCode.toString());
            writer.close();

            System.out.println("Code generation completed. Check FinalOutput file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateCodeWithRegisterAllocation(String line, Map<String, String> registerMap, int[] registerIndex) {
        
        if (line.startsWith("return ")) {
            String variable = line.split(" ")[1];
            return "MOV R" + registerIndex[0] + ", " + variable + "\nRETURN R" + registerIndex[0];
        }

        if (line.startsWith("if")) {
            return line.replace("if ", "JUMP_IF ");
        }

        if (line.startsWith("goto")) {
            return line.replace("goto ", "JUMP ");
        }
        if (line.endsWith(":")) {
            return line; 
        }

        String[] parts = line.split(" = ");
        if (parts.length < 2) {
            return line; 
        }
        String variable = parts[0].trim();
        String value = parts[1].trim();

       
        String register = registerMap.get(value);
        if (register == null) {
            register = "R" + registerIndex[0];
            registerMap.put(value, register);
            registerIndex[0]++;
        }

        return "MOV " + register + ", " + value + "\n" +
               "MOV " + variable + ", " + register;
    }
}