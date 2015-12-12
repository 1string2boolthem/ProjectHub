package projecthub;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 11/16/2015.
 *
 * This is a Junit unit testing routines for testing 
 * The authentication function of the project:
 */
public class AuthenticatorTest {

   @Test
   public void testClientLogin() throws Exception {
      LoginResult result = Authenticator.ClientLogin("", "", "10.0.0.3:8080");
      assertNotNull(result);
      assertFalse(result.wasSuccessful());
      result = Authenticator.ClientLogin("crendall@csumb.edu", "whereami", "10.0.0.3:8080");
      assertTrue(result.wasSuccessful());
      result = Authenticator.ClientLogin("crendall@csumb.edu", "nope");
      assertFalse(result.wasSuccessful());
      result = Authenticator.ClientLogin("crendall@csumb.edu", "");
      assertFalse(result.wasSuccessful());
      result = Authenticator.ClientLogin("", "wherami");
      assertFalse(result.wasSuccessful());
      result = Authenticator.ClientLogin("crendall@csumb.edu", "WHEREAMI");
      assertFalse(result.wasSuccessful());
   }

   @Test
   public void testVerifyLogin() throws Exception {

   }

   @Test
   public void testGenerateSessionID() throws Exception {

   }
}