 /**
 * Created by Chris on 11/15/2015.
 *
 * This class allows for simple conversion of classes
 * to and from GSON to JSON.
 */
 
package projecthub;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.Writer;

public class GsonWrapper {
   public static Object fromJson(Reader json, Class theClass){
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      return gson.fromJson(json, theClass);
   }
   public static void toJson(Object source, Writer out){
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      gson.toJson(source, out);
   }
   public static String toJson(Object source){
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      return gson.toJson(source);
   }
   public static Object fromJson(String json, Class theClass){
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      return gson.fromJson(json, theClass);
   }
}
