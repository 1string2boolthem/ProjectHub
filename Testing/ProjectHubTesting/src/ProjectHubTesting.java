import org.junit.Test;
import org.junit.Assert;
import projecthub.*;
import projecthub.Account.Creation.CreationAttempt;
import projecthub.Account.Creation.CreationResult;

/**
 * Created by Chris on 12/14/2015.
 */
public class ProjectHubTesting {

   //testAuthenticator will fully exercise the Authenticator code.
   @Test
   public void testAuthentication(){
      //Test a known good username and password on a known good server.
      LoginAttempt attempt = new LoginAttempt("crendall@csumb.edu", "whereami", null);
      HTTPRequest request = new HTTPRequest(null, null, "http://freetheheap.net:8080/ProjectHubServlet/login", attempt);
      HTTPResult result = new HTTPResult(null, HTTPClient.doRequest(request));
      LoginResult loginResult = (LoginResult)GsonWrapper.fromJson(result.getResponse(), LoginResult.class);
      Assert.assertTrue(loginResult.wasSuccessful());

      //Test a known bad username and password
      attempt = new LoginAttempt("crendall@csumb.edu", "notmypassword", null);
      request.setData(attempt);
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      loginResult = (LoginResult)GsonWrapper.fromJson(result.getResponse(), LoginResult.class);
      Assert.assertFalse(loginResult.wasSuccessful());

      //Test a null username and password
      attempt = new LoginAttempt(null, null, null);
      request.setData(attempt);
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      loginResult = (LoginResult)GsonWrapper.fromJson(result.getResponse(), LoginResult.class);
      Assert.assertFalse(loginResult.wasSuccessful());
   }

   @Test
   public void testRegistration(){

      //These values must be changed each time the test is ran.
      String firstName = "Christopher";
      String lastName = "Rendall";
      String username = "cjrendall8";
      String password = "password";
      String confirmPassword = "password";
      String email = "myemail8@email.com";

      //Delete the account associated with the test parameters
      Database db = new Database("crendall", "whereami", "tigerlily.arvixe.com");
      db.setDatabase("peacebuildingdevelopment");
      db.exQuery("DELETE FROM users WHERE E_Mail='" + email + "'");

      //Test known good registration values.
      projecthub.Account.Creation.CreationAttempt attempt = new CreationAttempt(firstName, lastName, username, email, password, confirmPassword);
      HTTPRequest request = new HTTPRequest(null, null, "http://freetheheap.net:8080/ProjectHubServlet/register", attempt);
      HTTPResult result = new HTTPResult(null, HTTPClient.doRequest(request));
      projecthub.Account.Creation.CreationResult creationResult = (CreationResult) GsonWrapper.fromJson(result.getResponse(), CreationResult.class);
      Assert.assertTrue(creationResult.wasSuccessful());

      //Test the registration attempt again with the same values. It should fail.
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      creationResult = (CreationResult) GsonWrapper.fromJson(result.getResponse(), CreationResult.class);
      Assert.assertFalse(creationResult.wasSuccessful());

      //Test the registration attempt with a different firstName, lastName, and email, but with the same username.
      firstName = "NotChristopher";
      lastName = "NotRendall";
      String newEmail = "notmyemail@email.com";
      attempt = new CreationAttempt(firstName, lastName, username, newEmail, password, confirmPassword);
      request.setData(attempt);
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      creationResult = (CreationResult) GsonWrapper.fromJson(result.getResponse(), CreationResult.class);
      Assert.assertFalse(creationResult.wasSuccessful());

      //Test logging in with the username and password used above.
      LoginAttempt loginAttempt = new LoginAttempt(email, password, null);
      request.setData(loginAttempt);
      request.setUrl("http://freetheheap.net:8080/ProjectHubServlet/login");
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      LoginResult loginResult = (LoginResult) GsonWrapper.fromJson(result.getResponse(), LoginResult.class);
      Assert.assertTrue(loginResult.wasSuccessful());

      //Test the registration attempt with valid data, but with passwords that do not match.
      db.exQuery("DELETE FROM users WHERE E_Mail='" + email + "'");
      attempt = new CreationAttempt(firstName, lastName, username, email, password, "notpassword");
      request.setData(attempt);
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      creationResult = (CreationResult) GsonWrapper.fromJson(result.getResponse(), CreationResult.class);
      Assert.assertFalse(creationResult.wasSuccessful());

      //Test account creation with all null values.
      request.setUrl("http://freetheheap.net:8080/ProjectHubServlet/register");
      attempt = new CreationAttempt(null, null, null, null, null, null);
      request.setData(attempt);
      result = new HTTPResult(null, HTTPClient.doRequest(request));
      creationResult = (CreationResult) GsonWrapper.fromJson(result.getResponse(), CreationResult.class);
      Assert.assertFalse(creationResult.wasSuccessful());

      db.exQuery("DELETE FROM users WHERE E_Mail='" + email + "'");
      db.closeConnection();

   }

}
