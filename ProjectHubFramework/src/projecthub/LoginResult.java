package projecthub;

/**
 * Created by Chris on 11/15/2015.
 * 
 * This object represents the result of an attempted login
 * and assists in providing a record stored for auditing purposes.
 */
public class LoginResult {
   private boolean Success;
   private String SessionID;
   public LoginResult(boolean success, String sessionID){
      this.Success = success;
      this.SessionID = sessionID;
   }
   public boolean wasSuccessful(){
      return this.Success;
   }
   public String getSessionID(){
      return this.SessionID;
   }
}
