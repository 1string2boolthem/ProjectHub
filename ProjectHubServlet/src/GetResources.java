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

/**
 * Created by Chris on 12/11/2015.
 */
@WebServlet(name = "GetResources")
public class GetResources extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest) GsonWrapper.fromJson(in, HTTPRequest.class);
      Credentials credentials = httpRequest.getCredentials();
      LoginResult result = Authenticator.VerifyLogin(credentials.getEmail(), credentials.getPasswordSalted("ilBb1948"), db);
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
