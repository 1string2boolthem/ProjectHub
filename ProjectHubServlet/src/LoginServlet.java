import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import projecthub.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 * Created by Chris on 11/15/2015.
 */
public class LoginServlet extends javax.servlet.http.HttpServlet {
   @Override
   public void init(ServletConfig config) throws ServletException {
      Settings settings = Settings.getInstance();
      settings.setDBUsername("crendall");
      settings.setDBPassword("whereami");
      settings.setDBHost("tigerlily.arvixe.com");
      settings.setDBName("peacebuildingdevelopment");
   }
   protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
      Settings settings = Settings.getInstance();
      String dbUsername = Settings.getDBUsername();
      String dbPassword = Settings.getDBPassword();
      String dbHost = Settings.getDBHost();
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db = new Database(Settings.getDBUsername(), Settings.getDBPassword(), Settings.getDBHost());
      System.out.println(db.getError());
      boolean connected = db.setDatabase(Settings.getDBName());
      InputStreamReader in = new InputStreamReader(request.getInputStream());
      HTTPRequest httpRequest = (HTTPRequest)GsonWrapper.fromJson(in, HTTPRequest.class);
      LoginAttempt attempt = (LoginAttempt)GsonWrapper.fromJson(httpRequest.getData(), LoginAttempt.class);
      LoginResult result = Authenticator.VerifyLogin(attempt.getEmail(), new String(Hex.encodeHex(DigestUtils.sha1("ilBb1948" + attempt.getPassword()))), db);
      ResultSet deviceEntry = db.getResults("SELECT * FROM user->devices ud JOIN users u WHERE u.ID=ud.UserID AND ud.DeviceID=" + attempt.getAndroidID());
      //try {
       //  deviceEntry.next();
     // }
      //catch(Exception e){
      //   db.exQuery("INSERT INTO user->devices ")
     // }
      if(result.wasSuccessful()){
         db.exQuery("UPDATE users SET Session_ID='" + result.getAuthString() + "' WHERE E_Mail='" + attempt.getEmail() + "' AND Password='" + attempt.getPassword() + "'");
      }
      PrintWriter out = new PrintWriter(response.getWriter());
      GsonWrapper.toJson(result, out);
      db.closeConnection();
   }

   protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
      PrintWriter out = new PrintWriter(response.getWriter());
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      out.write(db.testFunction());
   }
}
