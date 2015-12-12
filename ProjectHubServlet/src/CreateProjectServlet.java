import projecthub.*;
import projecthub.Project.ProjectCreationAttempt;
import projecthub.Project.ProjectCreationResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 * Created by Chris on 12/11/2015.
 */
@WebServlet(name = "CreateProjectServlet")
public class CreateProjectServlet extends HttpServlet {
   private String verifyProjectData(ProjectCreationAttempt attempt, Database db){
      String name = attempt.getName();
      String description = attempt.getDescription();
      String owner = attempt.getOwnerUsername();
      if(name.isEmpty() || description.isEmpty() || owner.isEmpty())
         return "One or more fields are empty.";
      ResultSet userResults = db.getResults("SELECT * FROM users WHERE Username='" + owner + "'");
      try{
         if(!userResults.next())
            return "The owner selected does not exist.";
      } catch(Exception e){

      }
      if(projectExists(name, db))
         return "A project with the same name already exists.";
      return null;
   }
   private boolean projectExists(String projectName, Database db){
      ResultSet projects = db.getResults("SELECT * FROM projects WHERE Name='" + projectName + "'");
      try{
         if(projects.next())
            return true;
         else
            return false;
      } catch(Exception e){
         return true;
      }
   }
   private int getUserIDFromEmail(String email, Database db){
      ResultSet users = db.getResults("SELECT * FROM users WHERE E_Mail='" + email + "'");
      try{
         users.next();
         return users.getInt("ID");
      } catch(Exception e){}
      return -1;
   }
   private int getUserID(String username, Database db){
      ResultSet users = db.getResults("SELECT * FROM users WHERE Username='" + username + "'");
      try{
         users.next();
         return users.getInt("ID");
      } catch(Exception e){}
      return -1;
   }
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest) GsonWrapper.fromJson(in, HTTPRequest.class);
      Credentials credentials = httpRequest.getCredentials();
      LoginResult result = Authenticator.VerifyLogin(credentials.getEmail(), credentials.getPasswordSalted("ilBb1948"), db);
      if(!result.wasSuccessful())
         return;
      ProjectCreationAttempt attempt = (ProjectCreationAttempt)GsonWrapper.fromJson(httpRequest.getData(), ProjectCreationAttempt.class);
      ProjectCreationResult creationResult = null;
      String error = verifyProjectData(attempt, db);
      if(error != null){
         creationResult  = new ProjectCreationResult(false, -1);
         creationResult.addError(error);
      }
      else{
         int row = db.insert("INSERT INTO projects (Name, Category, Description, Active) VALUES ('" + attempt.getName() +
               "', 0, '" + attempt.getDescription() + "', 1)");
         if(row != -1) {
            creationResult = new ProjectCreationResult(true, row);
            db.insert("INSERT INTO `users->projects` (User_ID, Project_ID) VALUES (" + getUserIDFromEmail(credentials.getEmail(), db) + ", " + row + ")");
            for (int i = 0; i < attempt.getResourceIDCount(); i++)
               db.insert("INSERT INTO `resource->project` (Resource_ID, Project_ID) VALUES (" + attempt.getResourceID(i) + ", " + row + ")");
            for (int i = 0; i < attempt.getParticipantCount(); i++) {
               int userID = getUserID(attempt.getParticipant(i), db);
               if (userID != -1 && userID != getUserIDFromEmail(credentials.getEmail(), db) && userID != getUserID(attempt.getOwnerUsername(), db))
                  db.insert("INSERT INTO `users->projects` (User_ID, Project_ID) VALUES (" + attempt.getResourceID(i) + ", " + row + ")");
            }
         }
         else {
            error = db.getError();
            creationResult = new ProjectCreationResult(false, -1);
            creationResult.addError("An unknown error has occurred.");
         }
      }
      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(creationResult, out);
      db.closeConnection();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
