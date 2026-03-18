import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    //list with all surveys
    private List<Survey> surveyList;
    private Scanner scanner;

    //CONSTRUCTORS

    //Empty constructor
    public App() {
        surveyList = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void initializeApp(Survey[] surveys) {
        surveyList = new ArrayList<>();
        for (Survey survey : surveys) {
            surveyList.add(survey);
        }
    }

    //FUNCTIONS

    //Display and Interaction

    //Writes List of Surveys on console
    public void displaySurveyList() {
        if (surveyList == null || surveyList.isEmpty()) {
            System.out.println("No surveys available.");
            return;
        }

        System.out.println("Survey List:");
        for (int i = 0; i < surveyList.size(); i++) {
            Survey survey = surveyList.get(i);
            System.out.println(i + ": " + survey.title + " - " + survey.description);
        }
    }

    //Shows options
    public void showMenu() {
        System.out.println("\nSurvey App");
        System.out.println("1. Manage survey");
        System.out.println("2. Answer survey");
        System.out.println("3. Analyze survey");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    //Calls methods depending on the option chosen
    public void interactWithUser(){
        boolean running = true;

        while (running) {
            showMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    SurveyManagement();
                    break;
                case 2:
                    try {
                        SurveyAnswer();
                    } catch (IOException e) {
                        System.out.println("Error answering survey: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("\nChoose: ");
                    System.out.println("1. Analyze existing survey.");
                    System.out.println("2. Import survey for analysis.");
                    int opt = scanner.nextInt();
                    if (opt == 1) SurveyAnalysis();
                    else {
                        try {
                            KMeansDriver.main(new String[]{});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("End");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //Shows survey management options
    public void showMenuSurveyManagement() throws IOException{
        System.out.println("\n Survey Management");
        System.out.println("1. New survey");
        System.out.println("2. Modify survey");
        System.out.println("3. Delete survey");
        System.out.print("Choose an option: ");
    }

    //Calls methods depending on the option chosen
    public void interactWithUserManagement(){
        try {
            showMenuSurveyManagement();
        } catch (IOException e) {
            System.out.println("Error showing menu survey: " + e.getMessage());
        }
        

        int choice = scanner.nextInt();
        scanner.nextLine();


        switch (choice) {
            case 1:
                SurveyCreation();
                break;
            case 2:
                SurveyModification();
                break;
            case 3:
                SurveyDeletion();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    //Calls interactWithUserManagement() to choose an option
    public void SurveyManagement() {
        interactWithUserManagement();
    }

    //METHODS

    //Creates new survey
    public void SurveyCreation() {
        try {
            //Get survey details
            System.out.print("Enter survey title: ");
            String title = scanner.nextLine();
            
            System.out.print("Enter survey description: ");
            String description = scanner.nextLine();
            
            //Create new survey with unique ID
            int newId = surveyList.size();
            Survey newSurvey = new Survey(newId, description, title);
            
            newSurvey.addQuestions();
            
            //Add to the list
            surveyList.add(newSurvey);
            
            System.out.println("Survey created successfully. ID: " + newId);
            
        } catch (Exception e) {
            System.out.println("Error creating survey: " + e.getMessage());
        }
    }

    //Calls ModifySurvey() from Class Survey
    public void SurveyModification() {
        Survey selectedSurvey = selectSurvey();
        if (selectedSurvey != null) {
            try {
                selectedSurvey.ModifySurvey();
            } catch (Exception e) {
                System.out.println("Error modifying survey: " + e.getMessage());
            }
        } else {
            System.out.println("No survey selected.");
        }
    }

    //Deletes Survey from surveyList
    public void SurveyDeletion() {
        Survey selectedSurvey = selectSurvey();
        if (selectedSurvey != null) {
            surveyList.remove(selectedSurvey);
            System.out.println("Survey '" + selectedSurvey.title + "' deleted successfully.");
        }
    }    

    //Calls SurveyAnswer() from Class Survey
    public void SurveyAnswer() throws IOException{
        Survey selectedSurvey = selectSurvey();
        if (selectedSurvey != null) {
            selectedSurvey.SurveyAnswer();
        } else {
            System.out.println("No survey selected.");
        }
    }

    //Calls SurveyAnalysis() from Class Survey
    public void SurveyAnalysis() {
        Survey selectedSurvey = selectSurvey();
        if (selectedSurvey != null) {
            try {
                //Input k for the algorithm
                System.out.println("Input k for k-means++ algorithm:");
                int k = scanner.nextInt();

                //Perform analysis with the determined k
                selectedSurvey.SurveyAnalysis(k);
            } catch (Exception e) {
                System.out.println("Error analyzing survey: " + e.getMessage());
            }
        } else {
            System.out.println("No survey selected.");
        }
    }

    //Select a survey by index
    public Survey selectSurvey() {
        if (surveyList.isEmpty()) {
            System.out.println("No surveys available.");
            return null;
        }

        displaySurveyList();
        System.out.print("Select survey index: ");
        int selectedIndex = scanner.nextInt();
        scanner.nextLine(); //consume newline

        if (selectedIndex >= 0 && selectedIndex < surveyList.size()) {
            return surveyList.get(selectedIndex);
        } else {
            System.out.println("Invalid index.");
            return null;
        }
    }

    public static void main(String[] args) {
        App app = new App();

        //Start interaction
        app.interactWithUser();
    }
}
