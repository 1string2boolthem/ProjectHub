package projecthub;

/**
 * Created by Chris on 11/15/2015.
 */
public class LoginAttempt {
   private String email;
   private String password;
   LoginAttempt(String email, String password){
      this.email = email;
      this.password = password;
   }
   public String getEmail(){
      return this.email;
   }
   public String getPassword(){
      return this.password;
   }
}
