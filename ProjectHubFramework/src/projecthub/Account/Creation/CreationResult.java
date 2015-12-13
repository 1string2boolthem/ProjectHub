/**
 * Created by Chris on 12/10/2015.
 *
 * This class is an abstraction of a single result of
 * a user creating a persistent account.
 */

package projecthub.Account.Creation;

import java.util.ArrayList;


public class CreationResult {
   private boolean success;
   private String email;
   private String password;
   private ArrayList<String> errors;
   public CreationResult(boolean success, String email, String password){
      this.success = success;
      this.email = email;
      this.password = password;
      this.errors = new ArrayList<String>();
   }
   public void addError(String error){
      this.errors.add(error);
   }
   public int getErrorCount(){
      return this.errors.size();
   }
   public String getError(int index){
      return this.errors.get(index);
   }
   public boolean wasSuccessful() {
      return success;
   }

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }
}
