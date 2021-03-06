/**
 * Created by Chris on 11/15/2015.
 *
 * This class is a logical abstraction of a SQL database instance.
 * It contains functions for identification, connection, and querying:
 */

package projecthub; 

import java.sql.*;

public class Database {
   private String username, password, database = "", server, salt = "";
   private boolean driverLoaded;
   private Connection c;
   private String error;
   public Database(String username, String password, String server){
      int test = 0;
      System.out.println(test);
      //Try loading the MySQL driver.
      try {
         // The newInstance() call is a work around for some
         // broken Java implementations

         Class.forName("com.mysql.jdbc.Driver").newInstance();
         this.username = username;
         this.password = password;
         this.server = server;
         this.driverLoaded = true;
      } catch (Exception ex) {
         this.driverLoaded = false;
         this.error = ex.getLocalizedMessage();
      }
   }
   public String getError(){
      return this.error;
   }
   // Salt is random data which is used as an additional input
   // to hashing (encrypting) plaintext passwords:
   public void setSalt(String salt){
      this.salt = salt;
   }
   public String getSalt(){
      return this.salt;
   }
	
   // Retrieves current connection based on credentials:
   private Connection getConnection(){
      try {
         Connection c = DriverManager.getConnection("jdbc:mysql://" + this.server + "/" + this.database, this.username, this.password);
         c.setAutoCommit(true);
         return c;
      }
      catch(Exception e){
         return null;
      }
   }
   // If credentials are non-existent or connection is bogus, 
   // Database cannot be set. 
>>>>>>> origin/master
   public boolean setDatabase(String database){
      if(database == "" || username == "" || password == "")
         return false;
      this.database = database;
      Connection c = this.getConnection();
      if(c == null){
         this.database = "";
         return false;
      }
      this.c = c;
      return true;
   }
   // Method for executing SQL Queries,
   // But does not return query data:
   public void exQuery(String query){
      try{
         Statement s = c.createStatement();
         s.execute(query);
      }catch(Exception e){}
   }
<<<<<<< HEAD
   public int insert(String query){
      try{
         Statement s = c.createStatement();
         s.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
         ResultSet rs = s.getGeneratedKeys();
         if(rs.next())
            return rs.getInt(1);
         else
            return -1;
      }catch(Exception e){
         error = e.getLocalizedMessage();
      }
      return -1;
   }
   public ResultSet getResults(String query){
=======
   // This retrieves the results of a Query:
   public ResultSet doQuery(String query){
>>>>>>> origin/master
      ResultSet results;
      if(this.database == "")
         return null;
      //Try to perform the query.
      try{
         if(c == null || c.isClosed())
            return null;
         Statement s = c.createStatement();
         results = s.executeQuery(query);
      }catch(Exception e){results = null; error = e.getLocalizedMessage();}
      return results;
   }
   public void closeConnection(){
      try{
         this.c.close();
      }catch(Exception e){}
   }
   public boolean wasDriverLoaded(){
      return this.driverLoaded;
   }
}
