package com.mycompany.companyb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class Questions { //Abstract class to store the template of the quenstion classes 
    protected String ID; //All variables are set to protected to prevent outside access
    protected String Question;
    protected boolean Compulsory;
    protected boolean valid;
    protected String SectionType;

    public abstract boolean validateAnswer(); //an undefiened method to check if the answer is valid

    public boolean isValid() {
        return valid;
    }
}

class Rate_type_question extends Questions { //rate type question class
    private int rating;

    public void setRating(int rating) { //sets tha rating for each answer
        this.rating = rating;
    }

    public int getRating() { //gets and returns the rating the user inputs
        return rating;
    }

    @Override
    public boolean validateAnswer() { //checks if the number typed in is 1,2,3,4 or 5 repeat otherwise
        valid = (rating >= 1 && rating <= 5);
        return valid;
    }
}

class MCQ extends Questions {
    private String choice;

    public void setChoice(String choice) { //checks for an answer by the user and converts the input into lowercase + remove spaces
        if (choice == null) {
            this.choice = null;
        } else {
            this.choice = choice.toLowerCase().replaceAll("\\s+", "");
        }
    }

    public String getChoice() {
        return choice;
    }

    @Override
    public boolean validateAnswer() { //checks if the input letter is equal to a,b,c or d returns valid if it is
        valid = "a".equals(choice) || "b".equals(choice) || "c".equals(choice) || "d".equals(choice);     
        return valid;
    }
}

class Text_question extends Questions {
    private String answer;

    public void setAnswer(String answer) {
        if (answer == null) { //check if the answer is empty and returns null if it is
            this.answer = null;
        } 
        else {
            this.answer = answer.trim(); //removes empty strings on the sides of the answer
        }
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean validateAnswer() { //if the answer is null or exceeds or doesn't meet the word limit it returns a false for valid 
        if (answer == null) {
            valid = false;
            return false;
        }
        int len = answer.length();
        valid = (len >= 3 && len <= 250);
        return valid;
    }
}

abstract class UserType { //Template for user types in the program
    protected String ID;
    protected String Name;
    protected String Password;
    protected String Email;
    protected String salt; //a unique code generated upon login
    protected boolean loggedin;

    protected UserType(String ID, String Name, String Password, String Email, String salt, boolean loggedin) { //contructor checks if the feilds are null
        this.ID = Objects.requireNonNull(ID, "ID cannot be null");                                             //if they are null it throws and exception "xxx"
        this.Name = Objects.requireNonNull(Name, "Name cannot be null");
        this.Password = Objects.requireNonNull(Password, "Password cannot be null");
        this.Email = Objects.requireNonNull(Email, "Email cannot be null");
        this.salt = Objects.requireNonNull(salt, "Salt cannot be null");
        this.loggedin = loggedin;
    }

    public void Login() { //A emthod to login
        this.loggedin = true;
    }

    public void Logout() {//A method to logout
        this.loggedin = false;
    }

    public boolean Loggedin() { //A method to check if the user is logged in
        return loggedin;
    }

    public String Get_ID() { //A method to get the user ID
        return ID;
    }

    public String Get_Name() {//A method to get the User name
        return Name;
    }

    public String Get_Password() {// a method to get the user password
        return Password;
    }

    public void setPassword(String password) { //A method to set the user password
        this.Password = Objects.requireNonNull(password, "Password cannot be null"); //Throws and exception if the feild is empty
    }

    public String getEmail() { //A method to get the User Email
        return Email;
    }

    public void setEmail(String email) {//A method to set the user emeial
        this.Email = Objects.requireNonNull(email, "Email cannot be null");
    }

    public String getSalt() { //A mehtod ot get the user salt code
        return salt;
    }

    public void setSalt(String salt) { //A method to set the salt code foe that specific user
        this.salt = Objects.requireNonNull(salt, "Salt cannot be null");
    }
}

class Admin extends UserType { //A class for the admin
    private String AdminID;

    public Admin(String ID, String Name, String Password, String Email, String salt, String AdminId) {
        super(ID, Name, Password, Email, salt, false);
        this.AdminID = Objects.requireNonNull(AdminId, "AdminID cannot be null");
    }

    public String getAdminID() { //A method to get the admin ID
        return AdminID;
    }

    public void setAdminID(String adminId) { //A method to set the admin ID
        this.AdminID = Objects.requireNonNull(adminId, "AdminID cannot be null");
    }

    public void addQuestion(List<Questions> questions, Questions question) {    //A method to add an question to the form
        ensureAuthenticated();                                                  //Checks if the user is authenticated to prevent unaurthorized access
        Objects.requireNonNull(questions, "Question list cannot be null");      //throws and exception the Question type is empty
        Objects.requireNonNull(question, "Question cannot be null");            //Checks if the question being added is empty
        questions.add(question);                                                //A method to add the question to the list
    }

    public boolean removeQuestionById(List<Questions> questions, String questionId) {//Mehtod to remove questions
        ensureAuthenticated();                                                  //Checks if the user is authenticated
        Objects.requireNonNull(questions, "Question list cannot be null");      //Cjecks if the question being removed has an empty fied in the list
        Objects.requireNonNull(questionId, "Question ID cannot be null");       //Checks if the questionID is empty
        return questions.removeIf(q -> questionId.equals(q.ID));                //Removes the question from the list if the ID matches          
    }

    private void ensureAuthenticated() { //A method to check if the user is authenticated
        if (!Loggedin()) {               //checks for the previous "loggedin" status to be true
            throw new IllegalStateException("Admin must be logged in to perform this action.");
        }
    }
}

class Dean extends UserType { //Class for dean
    private String deanId;

    public Dean(String ID, String Name, String Password, String Email, String salt, String deanId) {
        super(ID, Name, Password, Email, salt, false);
        this.deanId = Objects.requireNonNull(deanId, "Dean ID cannot be null");
    }

    public String getDeanId() { //a method to get the dean ID
        return deanId;
    }

    public void setDeanId(String deanId) { //a methof to set the dean ID
        this.deanId = Objects.requireNonNull(deanId, "Dean ID cannot be null");
    }

    public boolean Approve(String reportId, String comment) {                   //A method to approve  report
        ensureAuthenticated();                                                  //Checks if dean is auntenticated
        Objects.requireNonNull(reportId, "Report ID cannot be null");           //throws exception if reportID is null
        return true;                                                            //approved
    }

    public boolean rejectReport(String reportId, String comment) {              //A method to reject a report
        ensureAuthenticated();                                                  //Checks if dean is auntenticated
        Objects.requireNonNull(reportId, "Report ID cannot be null");           //throws exception if reportID is null
        if (comment == null || comment.isBlank()) {                             //checks if the comment is empty
            throw new IllegalArgumentException("A written comment is required when rejecting a report."); //Exception thrown if it is
        }
        return true;                                                            //rejected
    }

    private void ensureAuthenticated() { //A method to check if the dean is autenticated
        if (!Loggedin()) {
            throw new IllegalStateException("Dean must be logged in to perform this action.");
        }
    }
}

class LIC extends UserType { //class for LIC
    private String licId;
    private final List<String> assignedModules; //A list for assigned modules for that LIC

    public LIC(String ID, String Name, String Password, String Email, String salt, String licId) {
        super(ID, Name, Password, Email, salt, false);
        this.licId = Objects.requireNonNull(licId, "LIC ID cannot be null");
        this.assignedModules = new ArrayList<>();
    }

    public String getLicId() { //A method to get the licID
        return licId;
    }

    public void setLicId(String licId) { //checks if the LICID is null else throws and exception
        this.licId = Objects.requireNonNull(licId, "LIC ID cannot be null");
    }

    public List<String> getAssignedModules() { //gets the assigned modules for the LIC
        return assignedModules;
    }

    public void assignModule(String moduleCode) {                               //A method to add modules to that LIC
        Objects.requireNonNull(moduleCode, "Module code cannot be null");       //checks if the module code is null and throws a exception if it is
        if (!assignedModules.contains(moduleCode)) {                            //If the assigned modules code list does not have the module code netered it will append it to the list
            assignedModules.add(moduleCode);
        }
    }

    public void recieve_report(String reportId) { //A method for the LIC to get a report of the Module
        Objects.requireNonNull(reportId, "Report ID cannot be null"); //Checks if the report ID is null and throws an exception if it is
    }
}

public class CompanyB {
    public static void main(String[] args) {}
     
}