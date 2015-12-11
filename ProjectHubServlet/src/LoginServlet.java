/**
 * Created by Chris on 11/15/2015.
 * 
 * This class represents the servlet used as
 * the gateway to the project database. 
 *
 * The version of Apache Tomcat used is 7.0.52.
 */

import java.io.IOException;
import projecthub.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class LoginServlet extends javax.servlet.http.HttpServlet {
   @Override
   
   // Note: This is where the DB credentials are set in ProjectHub,
   // using a Settings class object as "storage:"
   
   public void init(ServletConfig config) throws ServletException {
      Settings settings = Settings.getInstance();
      settings.setDBUsername("crendall");
      settings.setDBPassword("whereami");
      settings.setDBHost("tigerlily.arvixe.com");
      settings.setDBName("peacebuildingdevelopment");
   }
   protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
      Database db = new Database(Settings.getDBUsername(), Settings.getDBPassword(), Settings.getDBHost());
      System.out.println(db.getError());
      boolean connected = db.setDatabase(Settings.getDBName());
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      LoginAttempt attempt = (LoginAttempt)GsonWrapper.fromJson(in, LoginAttempt.class);
      LoginResult result = Authenticator.VerifyLogin(attempt.getEmail(), attempt.getPassword(), db);
      if(result.wasSuccessful()){
         db.exQuery("UPDATE users SET Session_ID='" + result.getSessionID() + "' WHERE E_Mail='" + attempt.getEmail() + "' AND Password='" + attempt.getPassword() + "'");
      }
      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(result, out);
      db.closeConnection();
   }

   protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
      PrintWriter out = new PrintWriter(response.getWriter());
      out.write("DB Name: " + Settings.getDBName());
   }
}
