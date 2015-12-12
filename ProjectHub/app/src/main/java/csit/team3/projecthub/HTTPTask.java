package csit.team3.projecthub;
import org.apache.commons.io.IOUtils;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import projecthub.GsonWrapper;
import projecthub.HTTPRequest;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chris on 12/9/2015.
 */
public class HTTPTask extends AsyncTask<HTTPRequest, Void, HTTPResult> {
    private OnHTTPTaskCompleted onCompleted = null;
    public HTTPTask(OnHTTPTaskCompleted onCompleted){
        this.onCompleted = onCompleted;
    }
    protected HTTPResult doInBackground(HTTPRequest... requests){
        String result = null;
        try {
            result = doRequest(requests[0]);
        }catch(Exception e){
            return null;
        }
        return new HTTPResult(requests[0].getRequestIdentifier(), result);
    }
    protected void onPostExecute(HTTPResult result){
        this.onCompleted.onHTTPTaskCompleted(result);
    }
    private String doRequest(HTTPRequest request) throws Exception{
        String response = null;
        HttpURLConnection connection;
        InputStreamReader inputStream;
        try{
            URL url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(request.getMethod());
            if(request.getMethod() == "POST"){
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                GsonWrapper.toJson(request, out);
                out.close();
            }
            inputStream = new InputStreamReader(connection.getInputStream());
            response = IOUtils.toString(inputStream);
        }catch(Exception e){
            return null;
        }
        return response;
    }
}
class HTTPResult{
    private String requestIdentifier;
    private String response;
    public HTTPResult(String requestIdentifier, String response){
        this.requestIdentifier = requestIdentifier;
        this.response = response;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
