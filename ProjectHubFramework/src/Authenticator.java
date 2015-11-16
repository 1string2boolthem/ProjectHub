/**
 * Created by Chris on 11/15/2015.
 */
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.sql.*;
public abstract class Authenticator {
   public static LoginResult ClientLogin(String email, String password){
      password = DigestUtils.sha1Hex("ilBb1948" + password);
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      LoginResult result = gson.fromJson(CommunicationHandler.SendMessage("http://freetheheap.net:8080/ServletTesting/servlet", new LoginAttempt(email, password)), LoginResult.class);
      return result;
   }
   public static LoginResult VerifyLogin(String email, String password, Database db){
      ResultSet results = null;
      results = db.doQuery("SELECT * FROM users WHERE E_Mail='" + email + "' AND Password='" + password + "'");
      try{
         if(results.next() == false)
            return new LoginResult(false, "");
      }catch(Exception e){ return new LoginResult(false, ""); }
      return new LoginResult(true, "1");
   }
   //public boolean VerifyCredentials(String email, String sessionID, Database db){
   //
  // }
}