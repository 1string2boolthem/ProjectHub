/**
 * Created by Chris on 11/23/2015.
 */
public class Settings {
   private static Settings ourInstance = new Settings();
   private static String DBUsername = "";
   private static String DBPassword = "";
   private static String DBHost = "";
   private static String DBName = "";
   public static Settings getInstance() {
      return ourInstance;
   }

   private Settings() {
   }
   public static void setDBUsername(String username){
      Settings.DBUsername = username;
   }
   public static void setDBPassword(String password){
      Settings.DBPassword = password;
   }
   public static void setDBHost(String host){
      Settings.DBHost = host;
   }
   public static void setDBName(String name){
      Settings.DBName = name;
   }
   public static String getDBUsername(){
      return Settings.DBUsername;
   }
   public static String getDBPassword(){
      return Settings.DBPassword;
   }
   public static String getDBHost() {
      return Settings.DBHost;
   }
   public static String getDBName(){
      return Settings.DBName;
   }
}
