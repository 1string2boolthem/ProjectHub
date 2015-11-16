/**
 * Created by Chris on 11/15/2015.
 */
import java.sql.*;
public class Testing {
   public static void main(String[] args){
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      ResultSet results = null;
      results = db.doQuery("SELECT * FROM users WHERE E_Mail='crendall@csumb.edu' AND Password='51d026d07902a0c061d2de0d26a8a9028f6b8ba1'");
      try{
         System.out.println(results.next());
      }catch(Exception e){
         System.out.println(e.toString());
      }
      LoginResult result = Authenticator.ClientLogin("crendall@csumb.edu", "whereami");
      System.out.println(result.wasSuccessful());

   }
}
