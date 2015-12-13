/**
 * Annotated by NF on 12/12/2015
 *
 * This View represents the "project owner" selection process
 * as a sub-task of creating a project:
 *
 */

package csit.team3.projecthub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Set;

import projecthub.Credentials;
import projecthub.GsonWrapper;
import projecthub.HTTPRequest;
import projecthub.IndividualsList;

public class AddProjectChooseOwner extends AppCompatActivity implements OnHTTPTaskCompleted {
    
	// Setup data strucures and other variables:
	private Controller controller;
    private static final String HOST = "freetheheap.net";
    private ArrayList<String> individuals = new ArrayList<String>();
    private ArrayList<String> keys = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private ProgressDialog progressDialog;
    private String caller;
    private void loadIndividuals(){
        Credentials credentials = new Credentials();
        credentials.setEmail(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, ""));
        credentials.setPassword(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, ""));
        HTTPRequest request = new HTTPRequest("LOADINDIVIDUALS", credentials, "http://" + HOST + ":8080/ProjectHubServlet/getIndividuals", null);
        HTTPTask task = new HTTPTask(this);
        task.execute(request);
    }
    @Override
    
	// View creation code:
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_choose_owner);
        this.caller = getIntent().getExtras().getString("caller");
        this.controller = new Controller(this);
        ListView lstOwner = (ListView)findViewById(R.id.lstOwner);
        lstOwner.setOnItemClickListener(controller);
        this.listAdapter = new ArrayAdapter<String>(this, R.layout.user_home_project_row, this.individuals);
        lstOwner.setAdapter(this.listAdapter);
        this.progressDialog = ProgressDialog.show(this, "", "Loading individuals...", true);
        loadIndividuals();
    }
    public void onHTTPTaskCompleted(HTTPResult result){
        IndividualsList individuals = (IndividualsList)GsonWrapper.fromJson(result.getResponse(), IndividualsList.class);
        Set<String> keys = individuals.getKeys();
        for(String key : keys){
            this.keys.add(key);
            this.listAdapter.add(key + " - " + individuals.getValue(key));
        }
        this.listAdapter.notifyDataSetChanged();
        this.progressDialog.hide();
    }
    
	// Selects project owner based on list index:
	private void returnIndividual(int keyPosition){
        Intent returnData = new Intent();
        returnData.putExtra("caller", caller);
        returnData.putExtra("username", keys.get(keyPosition));
        returnData.putExtra("name", individuals.get(keyPosition));
        setResult(RESULT_OK, returnData);
        finish();
    }
    
	// Controller is an intermediate class which processes and passes data
	// to/from the App(UI) and the DB:
	class Controller implements View.OnClickListener, ListView.OnItemClickListener {
        AddProjectChooseOwner screen;

        public Controller(AddProjectChooseOwner screen) {
            this.screen = screen;
        }

        public void onClick(View view) {
            NotificationDialog.show("Test", "Click detected!", screen);
        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            screen.returnIndividual(position);
        }
    }
}
