package projecthub; /**
 * Created by Chris on 11/15/2015.
 */
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
}
