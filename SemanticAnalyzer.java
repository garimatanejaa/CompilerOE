import java.io.*;
import java.util.*;

public class SemanticAnalyzer {
    private static final Set<String> dataTypes = new HashSet<>(Arrays.asList("int", "float", "char", "double"));
    private static final Map<String, String> symbolTable = new HashMap<>(); 
    public static void main(String[] args) {
        String inputFilename = "OutputParser"; 
        String outputFilename = "OutputSemantic";

        try {
            analyzeSemantics(inputFilename, outputFilename);
            System.out.println("Semantic analysis completed. Results saved in " + outputFilename);
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    private static void analyzeSemantics(String inputFilename, String outputFilename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));

        String lastDataType = null; 
        boolean insideFunction = false;
        boolean hasReturnStatement = false;
        String functionReturnType = null;

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": ", 2);
            if (parts.length < 2) {
                System.out.println("Invalid line format: " + line);
                continue;
            }
            String tokenType = parts[0].trim();
            String tokenValue = parts[1].split("\\(Line ")[0].trim();
            int lineNumber = Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); // Extract line number

            String output = analyzeToken(tokenType, tokenValue, lineNumber);
            if (dataTypes.contains(tokenValue)) {
                lastDataType = tokenValue;
            } else if (tokenType.equals("IDENTIFIER")) {
                if (lastDataType != null) {  
                    symbolTable.put(tokenValue, lastDataType);
                    writer.write("Declared variable '" + tokenValue + "' of type '" + lastDataType + "' at line " + lineNumber);
                    writer.newLine();
                    lastDataType = null; 
                } else if (!symbolTable.containsKey(tokenValue)) {
                    writer.write("ERROR: Variable '" + tokenValue + "' used without declaration at line " + lineNumber);
                    writer.newLine();
                }
            }

            if (tokenType.equals("OPERATOR") && tokenValue.equals("=")) {
                String variable = line.split(":")[1].trim().split(" ")[0];
                String expectedType = symbolTable.get(variable);
                if (expectedType != null) {
                    String assignedValue = line.split("= ")[1].trim();
                    if (assignedValue.matches("\\d+") && expectedType.equals("float")) {
                        writer.write("WARNING: Assigning integer to float variable '" + variable + "' at line " + lineNumber);
                        writer.newLine();
                    } else if (assignedValue.matches("\\d+\\.\\d+") && expectedType.equals("int")) {
                        writer.write("ERROR: Type mismatch! Cannot assign float to int '" + variable + "' at line " + lineNumber);
                        writer.newLine();
                    }
                }
            }

            if (tokenType.equals("KEYWORD") && tokenValue.equals("return")) {
                hasReturnStatement = true;
            } else if (tokenType.equals("PROGRAM")) {
                insideFunction = true;
                functionReturnType = symbolTable.get(tokenValue);
            } else if (tokenType.equals("OPERATOR") && tokenValue.equals("}")) {
                if (insideFunction) {
                    if (functionReturnType != null && !hasReturnStatement) {
                        writer.write("ERROR: Function '" + functionReturnType + "' is missing a return statement");
                        writer.newLine();
                    }
                    insideFunction = false;
                    hasReturnStatement = false;
                    functionReturnType = null;
                }
            }

            writer.write(output);
            writer.newLine();
        }

        reader.close();
        writer.close();
    }

    private static String analyzeToken(String tokenType, String tokenValue, int lineNumber) {
        switch (tokenType) {
            case "PROGRAM":
                return "Program '" + tokenValue + "' declared at line " + lineNumber;
            case "IDENTIFIER":
                return "Identifier '" + tokenValue + "' used at line " + lineNumber;
            case "COMMENT":
                return "Comment '" + tokenValue + "' at line " + lineNumber;
            case "KEYWORD":
                return "Keyword '" + tokenValue + "' used at line " + lineNumber;
            case "OPERATOR":
                return "Operator '" + tokenValue + "' used at line " + lineNumber;
            case "NUMBER": 
                return "Number '" + tokenValue + "' used at line " + lineNumber;
            case "LITERAL":
                return "Literal '" + tokenValue + "' used at line " + lineNumber;
            default:
                return "Unknown token type: " + tokenType + " at line " + lineNumber;
        }
    }
    
}
