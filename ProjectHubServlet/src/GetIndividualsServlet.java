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
@WebServlet(name = "GetIndividualsServlet")
public class GetIndividualsServlet extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest) GsonWrapper.fromJson(in, HTTPRequest.class);
      Credentials credentials = httpRequest.getCredentials();
      LoginResult result = Authenticator.VerifyLogin(credentials.getEmail(), credentials.getPasswordSalted("ilBb1948"), db);
      if(!result.wasSuccessful())
         return;
      ResultSet individuals = db.getResults("SELECT Username, First_Name, Last_Name FROM users");
      IndividualsList individualsList = new IndividualsList();
      try{
         while(individuals.next()){
            individualsList.add(individuals.getString("Username"), individuals.getString("First_Name") + " " + individuals.getString("Last_Name"));
         }
      } catch(Exception e){

      }
      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(individualsList, out);
      db.closeConnection();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
