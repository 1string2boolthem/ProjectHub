package csit.team3.projecthub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import projecthub.Credentials;
import projecthub.GsonWrapper;
import projecthub.HTTPRequest;
import projecthub.IndividualsList;
import projecthub.ResourceList;

public class AddProjectChooseResource extends AppCompatActivity implements OnHTTPTaskCompleted {
    ArrayList<String> resources = new ArrayList<String>();
    ArrayList<String> keys = new ArrayList<String>();
    private static final String HOST = "freetheheap.net";
    ArrayAdapter<String> listAdapter;
    Controller controller;
    ProgressDialog progressDialog;
    private void loadResources(){
        Credentials credentials = new Credentials();
        credentials.setEmail(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, ""));
        credentials.setPassword(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, ""));
        HTTPRequest request = new HTTPRequest("LOADRESOURCES", credentials, "http://" + HOST + ":8080/ProjectHubServlet/getResources", null);
        HTTPTask task = new HTTPTask(this);
        task.execute(request);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_choose_resource);
        this.controller = new Controller(this);
        ListView lstResources = (ListView)findViewById(R.id.lstResources);
        lstResources.setOnItemClickListener(controller);
        this.listAdapter = new ArrayAdapter<String>(this, R.layout.user_home_project_row, this.resources);
        lstResources.setAdapter(this.listAdapter);
        this.progressDialog = ProgressDialog.show(this, "", "Loading resources...", true);
        loadResources();
    }
    private void returnResource(int keyPosition){
        Intent returnData = new Intent();
        returnData.putExtra("caller", "resource");
        returnData.putExtra("id", keys.get(keyPosition));
        returnData.putExtra("name", resources.get(keyPosition));
        setResult(RESULT_OK, returnData);
        finish();
    }
    public void onHTTPTaskCompleted(HTTPResult result){
        ResourceList resources = (ResourceList) GsonWrapper.fromJson(result.getResponse(), ResourceList.class);
        Set<String> keys = resources.getKeys();
        for(String key : keys){
            this.keys.add(key);
            this.listAdapter.add(resources.getValue(key));
        }
        this.listAdapter.notifyDataSetChanged();
        this.progressDialog.hide();
    }
    class Controller implements View.OnClickListener, ListView.OnItemClickListener {
        AddProjectChooseResource screen;

        public Controller(AddProjectChooseResource screen) {
            this.screen = screen;
        }

        public void onClick(View view) {
            NotificationDialog.show("Test", "Click detected!", screen);
        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            screen.returnResource(position);
        }
    }
}
