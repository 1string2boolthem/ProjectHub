package projecthub;

/**
 * Created by Chris on 11/15/2015.
 */
public class LoginResult {
   private boolean Success;
   private String AuthString;
   public LoginResult(boolean success, String authString){
      this.Success = success;
      this.AuthString = authString;
   }
   public boolean wasSuccessful(){
      return this.Success;
   }
   public String getAuthString(){
      return this.AuthString;
   }
}
