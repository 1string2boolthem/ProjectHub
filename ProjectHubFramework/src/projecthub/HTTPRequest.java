package projecthub;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.OutputStream;

/**
 * Created by Chris on 12/10/2015.
 */
public class HTTPRequest{
   private String requestIdentifier;
   private String credentials;
   private String url;
   private String method;
   private String data;

   public String getRequestIdentifier() {
      return requestIdentifier;
   }

   public void setRequestIdentifier(String requestIdentifier) {
      this.requestIdentifier = requestIdentifier;
   }

   public HTTPRequest(String requestIdentifier, Credentials credentials, String url, Object data){
      this.setMethod("POST");
      this.setRequestIdentifier(requestIdentifier);
      this.setCredentials(credentials);
      this.setUrl(url);
      this.setData(data);
   }
   public Credentials getCredentials() {
      return (Credentials)GsonWrapper.fromJson(this.credentials, Credentials.class);
   }

   public void setCredentials(Credentials credentials) {
      this.credentials = GsonWrapper.toJson(credentials);
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getMethod() {
      return method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public String getData() {
      return data;
   }

   public void setData(Object data) {
      this.data = GsonWrapper.toJson(data);
   }
}
