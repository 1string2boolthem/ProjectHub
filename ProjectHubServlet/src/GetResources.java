 /**
 * Created by Chris on 12/11/2015.
 * This class represents the servlet used to 
 * retrieve project resources from the project database. 
 *
 * The version of Apache Tomcat used is 7.0.52.
 */

import projecthub.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;


@WebServlet(name = "GetResources")
public class GetResources extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
	  // Login code section:
	  Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest) GsonWrapper.fromJson(in, HTTPRequest.class);
      Credentials credentials = httpRequest.getCredentials();
      LoginResult result = Authenticator.VerifyLogin(credentials.getEmail(), credentials.getPasswordSalted("ilBb1948"), db);
      
	  // Project resources will be returned as a resultset, but only public
	  // resources will be returned if the authentication attempt fails:
	  ResultSet resources;
      if(!result.wasSuccessful())
         resources = db.getResults("SELECT ID, Name FROM resources WHERE Public=1");
      else
         resources = db.getResults("SELECT ID, Name FROM resources");
      ResourceList resourceList = new ResourceList();
      try{
         while(resources.next()) {
            resourceList.add(resources.getString("ID"), resources.getString("Name"));
         }
      } catch(Exception e){

      }
      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(resourceList, out);
      db.closeConnection();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
