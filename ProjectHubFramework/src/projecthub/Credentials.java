/**
 * Created by Chris on 12/10/2015.
 *
 * This class is an encapsulation of user credentials - 
 * email/password
 */

package projecthub;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;


public class Credentials{
   private String email;
   private String password;

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }
   public String getPasswordSalted(String salt){
      return new String(Hex.encodeHex(DigestUtils.sha1(salt + this.getPassword())));
   }
   public void setPassword(String password) {
      this.password = password;
   }
}