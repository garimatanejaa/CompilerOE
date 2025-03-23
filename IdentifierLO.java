import java.io.*;

public class IdentifierLO {
    public static void main(String[] args) {
        String inputFileName = "OutputSemantic"; 
        String outputFileName = "OutputLO"; 

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, writer);
            }

            System.out.println("Identifiers, Operators, and Literals extracted into OutputLO");

        } catch (IOException e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }

    private static void processLine(String line, BufferedWriter writer) throws IOException {
        String[] parts = line.split("'");

        if (parts.length < 2) return; 

        if (line.startsWith("Identifier")) {
            writer.write("Identifier: " + parts[1] + "\n");
        } else if (line.startsWith("Operator")) {
            writer.write("Operator: " + parts[1] + "\n");
        } else if (line.startsWith("Number")) { 
            writer.write("Literal: " + parts[1] + "\n");
        }
    }
}

