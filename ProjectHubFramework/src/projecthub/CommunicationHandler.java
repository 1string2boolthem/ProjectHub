package projecthub; /**
 * Created by Chris on 11/15/2015.
 */
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
public class CommunicationHandler {
   public static String SendMessage(String httpUrl, Object object){
      String response = null;
      URL url;
      HttpURLConnection c;
      InputStreamReader r;

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      //Try to handle the transaction
      try {
         url = new URL(httpUrl);
         c = (HttpURLConnection) url.openConnection();
         c.setDoOutput(true);
         c.setRequestMethod("POST");
         PrintWriter out = new PrintWriter(c.getOutputStream());
         gson.toJson(object, out);
         out.close();
         r = new InputStreamReader(c.getInputStream());
         response = IOUtils.toString(r);

      }catch(Exception e){
         return null; }

      return response;
   }
}
