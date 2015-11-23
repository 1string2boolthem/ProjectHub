package projecthub;
/**
 * Created by Chris on 11/15/2015.
 */
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.sql.*;
public abstract class Authenticator {
   public static LoginResult ClientLogin(String email, String password, String server){
      password = new String(Hex.encodeHex(DigestUtils.sha1("ilBb1948" + password)));
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      LoginResult result = gson.fromJson(CommunicationHandler.SendMessage("http://" + server + "/ProjectHubServlet/login", new LoginAttempt(email, password)), LoginResult.class);
      if(result == null)
         result = new LoginResult(false, "");
      return result;
   }
   public static LoginResult ClientLogin(String email, String password){
      password = new String(Hex.encodeHex(DigestUtils.sha1("ilBb1948" + password)));
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      LoginResult result = gson.fromJson(CommunicationHandler.SendMessage("http://10.0.0.3:8080/ProjectHubServlet/login", new LoginAttempt(email, password)), LoginResult.class);
      if(result == null)
         result = new LoginResult(false, "");
      return result;
   }
   public static LoginResult VerifyLogin(String email, String password, Database db){
      ResultSet results = null;
      results = db.doQuery("SELECT * FROM users WHERE E_Mail='" + email + "' AND Password='" + password + "'");
      try{
         if(results.next() == false)
            return new LoginResult(false, "");
      }catch(Exception e){ return new LoginResult(false, ""); }
      return new LoginResult(true, GenerateSessionID());
   }
   public static String GenerateSessionID(){
      StringBuilder builder = new StringBuilder();
      for(char c = 'a'; c <= 'z'; c++)
         builder.append(c);
      for(char c = 'A'; c <= 'Z'; c++){
         builder.append(c);
      }
      for(char c = '0'; c <= '9'; c++)
         builder.append(c);
      String charChoices = builder.toString();
      builder = new StringBuilder();
      for(int i = 0; i < 20; i++)
         builder.append(charChoices.charAt((int)Math.round(Math.random()*(charChoices.length()-1))));
      return builder.toString();
   }
   //public boolean VerifyCredentials(String email, String sessionID, projecthub.Database db){
   //
  // }
}