package projecthub;

/**
 * Created by Chris on 11/15/2015.
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
