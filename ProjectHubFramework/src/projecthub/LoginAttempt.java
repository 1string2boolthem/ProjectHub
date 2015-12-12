package projecthub;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Chris on 11/15/2015.
 */
public class LoginAttempt {
   private String email;
   private String password;
   private String androidID;
   public LoginAttempt(String email, String password, String androidID){
      this.email = email;
      this.password = password;
      this.androidID = androidID;
   }
   public String getEmail(){
      return this.email;
   }
   public String getPassword(){
      return this.password;
   }
   public String getPasswordSalted(String salt){
      return new String(Hex.encodeHex(DigestUtils.sha1(salt + this.getPassword())));
   }
   public String getAndroidID(){
      return this.androidID;
   }
}
