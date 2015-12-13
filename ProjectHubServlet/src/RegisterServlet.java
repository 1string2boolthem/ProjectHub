 /**
 * Created by Chris on 12/9/2015.
 * This class represents the servlet used to 
 * register new users into the project database. 
 *
 * The version of Apache Tomcat used is 7.0.52.
 */

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import projecthub.Account.Creation.CreationAttempt;
import projecthub.Account.Creation.CreationResult;
import projecthub.Database;
import projecthub.GsonWrapper;
import projecthub.HTTPRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;


@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
   
   // This helper method verifies that there are no similar (duplicate) values in the DB:
   private boolean verifyUniqueValue(Database db, String key, String value){
      ResultSet results = db.getResults("SELECT * FROM users WHERE " + key + "='" + value + "'");
      System.out.println(db.getError());
      try {
         if (results.next()) {
            return false;
         }
         else
            return true;
      } catch(Exception e){
         System.out.println(e.getMessage());
      }
      return false;
   }
   
   // This returns an error message if for some reason the information and credentials
   // supplied are valid. Otherwise, it returns null:
   private String verifyAttempt(CreationAttempt attempt, Database db){
      if(attempt.getFirstName() == null || attempt.getLastName() == null || attempt.getEmail() == null || attempt.getUsername() == null || attempt.getPassword() == null || attempt.getConfirmPassword() == null)
         return "Please complete all fields.";
      if(attempt.getFirstName().isEmpty() || attempt.getLastName().isEmpty() || attempt.getEmail().isEmpty() || attempt.getUsername().isEmpty() || attempt.getPassword().isEmpty() || attempt.getConfirmPassword().isEmpty())
         return "Please complete all fields.";
      if(!verifyUniqueValue(db, "Username", attempt.getUsername()))
         return "Username is not unique";
      if(!verifyUniqueValue(db, "E_Mail", attempt.getEmail()))
         return "A user with that e-mail address already exists.";
      if(!attempt.getPassword().equals(attempt.getConfirmPassword()))
         return "Password and Confirm Password do not match.";
      return null;
   }
   
   // This verifies the user registry, then adds the new user into the DB 
   // if the verification is successful:
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Settings settings = Settings.getInstance();
      String dbUsername = Settings.getDBUsername();
      String dbPassword = Settings.getDBPassword();
      String dbHost = Settings.getDBHost();
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase(Settings.getDBName());
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest)GsonWrapper.fromJson(in, HTTPRequest.class);
      CreationAttempt attempt = (CreationAttempt)GsonWrapper.fromJson(httpRequest.getData(), CreationAttempt.class);
      String error = verifyAttempt(attempt, db);
      CreationResult result;
      
	  // If error != null, there's a problem:
	  if(error != null){
         result = new CreationResult(false, "", "");
         result.addError(error);
      }
      
	  // Otherwise, add the new user to the DB:
	  else{
         int row = db.insert("INSERT INTO users (Username, Password, E_Mail, Individual_ID, Level, First_Name, Last_Name) VALUES ('" + attempt.getUsername() +
               "', '" +  new String(Hex.encodeHex(DigestUtils.sha1("ilBb1948" + attempt.getPassword()))) + "', '" + attempt.getEmail() + "', 0, 0, '" + attempt.getFirstName() +
               "', '" + attempt.getLastName() + "')");
         if(row != -1)
            result = new CreationResult(true, attempt.getEmail(), attempt.getPassword());
         else{
            result = new CreationResult(false, "", "");
            result.addError(db.getError());
         }
      }

      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(result, out);
      db.closeConnection();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
