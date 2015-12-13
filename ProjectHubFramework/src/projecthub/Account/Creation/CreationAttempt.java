/**
 * Created by Chris on 11/24/2015.
 *
 * This class is an abstraction of an instance of an 
 * attempt to create a user account. 
 */

package projecthub.Account.Creation;

public class CreationAttempt {
   private String firstName, lastName, username, email, password, confirmPassword;

   public CreationAttempt(String firstName, String lastName, String username, String email, String password, String confirmPassword) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.email = email;
      this.password = password;
      this.confirmPassword = confirmPassword;
   }

   public String getConfirmPassword() {
      return confirmPassword;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getUsername() {
      return username;
   }

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }
}
