 /**
 * Created by Chris on 12/11/2015.
 * This class represents the servlet used to 
 * retrieve User projects from the project database. 
 *
 * The version of Apache Tomcat used is 7.0.52.
 */

import projecthub.*;
import projecthub.Project.ProjectList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;


@WebServlet(name = "GetUserProjectsServlet")
public class GetUserProjectsServlet extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // Server Login section of code:
	  Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest) GsonWrapper.fromJson(in, HTTPRequest.class);
      Credentials credentials = httpRequest.getCredentials();
      LoginResult result = Authenticator.VerifyLogin(credentials.getEmail(), credentials.getPasswordSalted("ilBb1948"), db);
      if(!result.wasSuccessful())
         return;
      String email = credentials.getEmail();
	  
	  // User project records are retrieved from the database as a resultset:
      ResultSet projects = db.getResults("SELECT p.Project_ID, Name FROM projects p JOIN `users->projects` up ON p.Project_ID=up.Project_ID JOIN users u ON up.User_ID=u.ID WHERE E_Mail='" + credentials.getEmail() + "'");
      ProjectList projectList = new ProjectList();
      try{
         while(projects.next()){
            String id = new String(projects.getString("Project_ID"));
            String name = projects.getString("Name");
            projectList.add(projects.getString("Project_ID"), projects.getString("Name"));
         }
      }catch(Exception e){

      }
      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(projectList, out);
      db.closeConnection();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
