
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadSurvey {
    
    /**
     * Reads questions from a CSV file and creates a survey with those questions.
     * 
     * CSV format per line:
     * type,statement,options,maximum
     * 
     * Where:
     * - type: 0-4 (question type)
     * - statement: The question text
     * - options: Options separated by | (for types 1, 2, 3)
     * - maximum: Maximum answers allowed (for type 3 only)
     */
    
    public static ArrayList<Question> createSurveyFromCSV(String csvFilePath) throws IOException {
        ArrayList<Question> questions = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                // Parse CSV line
                // Using custom parsing to handle quoted fields with commas
                List<String> fields = parseCSVLine(line);
                
                if (fields.size() < 2) {
                    System.err.println("Warning: Line " + lineNumber + " has insufficient fields: " + line);
                    continue;
                }
                
                try {
                    // Parse type
                    int type = Integer.parseInt(fields.get(0).trim());
                    
                    // Parse statement
                    String statement = fields.get(1).trim();
                    
                    // Parse options (if available)
                    String optionsStr = fields.size() > 2 ? fields.get(2).trim() : "";
                    
                    // Parse maximum (if available, for type 3)
                    String maxStr = fields.size() > 3 ? fields.get(3).trim() : "";
                    
                    // Create question based on type
                    Question question = createQuestionFromFields(type, statement, optionsStr, maxStr);
                    
                    if (question != null) {
                        questions.add(question);
                    } else {
                        System.err.println("Warning: Could not create question from line " + lineNumber);
                    }
                    
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }
        }

        return questions;
    }
    
    /**
     * Parse a CSV line, handling quoted fields
     */
    private static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == ',') {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        fields.add(currentField.toString());
        
        return fields;
    }
    
    /**
     * Create a Question object from parsed fields
     */
    private static Question createQuestionFromFields(int type, String statement, String optionsStr, String maxStr) {
        switch (type) {
            case 0: // Free number question
                return new Question.FreeNumberQuestion(statement);
                
            case 1: // Ordered single choice
                if (optionsStr.isEmpty()) {
                    System.err.println("Warning: Type 1 question needs options");
                    return null;
                }
                List<String> options1 = Arrays.asList(optionsStr.split("\\|"));
                return new Question.SingleChoiceQuestionOrdered(statement, options1);
                
            case 2: // Unordered single choice
                if (optionsStr.isEmpty()) {
                    System.err.println("Warning: Type 2 question needs options");
                    return null;
                }
                List<String> options2 = Arrays.asList(optionsStr.split("\\|"));
                return new Question.SingleChoiceQuestionUnordered(statement, options2);
                
            case 3: // Multiple choice
                if (optionsStr.isEmpty()) {
                    System.err.println("Warning: Type 3 question needs options");
                    return null;
                }
                List<String> options3 = Arrays.asList(optionsStr.split("\\|"));
                int max = -1; // Default: no limit
                if (!maxStr.isEmpty()) {
                    try {
                        max = Integer.parseInt(maxStr.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid maximum value, using -1 (no limit)");
                    }
                }
                return new Question.MultipleChoiceQuestion(statement, options3, max);
                
            case 4: // Free text question
                return new Question.FreeTextQuestion(statement);
                
            default:
                System.err.println("Warning: Unknown question type: " + type);
                return null;
        }
    }
}
