/**
 * Annotated by NF on 12/12/2015
 *
 * This View represents a screen in which a user can 
 * add a project to the database of projects. 
 *
 */

package csit.team3.projecthub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Credentials;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projecthub.GsonWrapper;
import projecthub.HTTPRequest;
import projecthub.Project.ProjectCreationAttempt;
import projecthub.Project.ProjectCreationResult;

public class AddProject extends AppCompatActivity implements OnHTTPTaskCompleted {
    
	// Data structures used to store project participants, resources,
	// and other data:
	ArrayList<String> participants = new ArrayList<String>();
    private static final String HOST = "freetheheap.net";
    ArrayList<String> participantUsernames = new ArrayList<String>();
    ArrayList<String> resources = new ArrayList<String>();
    ArrayList<String> resourceIDs = new ArrayList<String>();
    ArrayAdapter<String> resourceAdapter;
    ArrayAdapter<String> participantAdapter;
    Controller controller;
    ProgressDialog progressDialog;
    private String owner;
    @Override
    
	// View creation code:
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        this.setTitle("Add Project");
        this.controller = new Controller(this);
        Button btnAddParticipant = (Button)findViewById(R.id.btnAddParticipant);
        btnAddParticipant.setOnClickListener(controller);
        Button btnChooseOwner = (Button)findViewById(R.id.btnChooseOwner);
        btnChooseOwner.setOnClickListener(controller);
        Button btnAddResource = (Button)findViewById(R.id.btnAddResource);
        btnAddResource.setOnClickListener(controller);
        ListView lstParticipants = (ListView)findViewById(R.id.lstParticipants);
        participantAdapter = new ArrayAdapter<String>(this, R.layout.user_home_project_row, participants);
        lstParticipants.setAdapter(participantAdapter);
        lstParticipants.setOnItemLongClickListener(controller);
        ListView lstResources = (ListView)findViewById(R.id.lstResources);
        resourceAdapter = new ArrayAdapter<String>(this, R.layout.user_home_project_row, resources);
        lstResources.setAdapter(resourceAdapter);
        lstResources.setOnItemLongClickListener(controller);
        Button btnCreateProject = (Button)findViewById(R.id.btnCreateProject);
        btnCreateProject.setOnClickListener(controller);

    }
    private void adjustListViewHeight(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    public void onHTTPTaskCompleted(HTTPResult result){
        ProjectCreationResult creationResult = (ProjectCreationResult)GsonWrapper.fromJson(result.getResponse(), ProjectCreationResult.class);
        if(creationResult == null || !creationResult.wasSuccessful()){
            NotificationDialog.show("Creation Failed", creationResult.getError(0), this);
        }
        else{
            progressDialog.hide();
            NotificationDialog.show("Project Created", "This project has been added to the ProjectHub.", this);
            finish();
        }
        progressDialog.hide();
    }
    @Override
    
	// Project/participant/resource management code:
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            String caller = data.getExtras().getString("caller");
            if(caller.equals("owner")){
                TextView txtOwner = (TextView)findViewById(R.id.txtOwner);
                txtOwner.setText(data.getExtras().getString("name"));
                this.owner = data.getExtras().getString("username");
            }
            else if(caller.equals("participant")){
                for(String username : this.participantUsernames)
                    if(username == data.getExtras().getString("username"))
                        return;
                this.participantAdapter.add(data.getExtras().getString("name"));
                this.participantUsernames.add(data.getExtras().getString("username"));
                this.participantAdapter.notifyDataSetChanged();
                this.adjustListViewHeight((ListView)findViewById(R.id.lstParticipants));
            }
            else if(caller.equals("resource")){
                for(String resourceID : this.resourceIDs)
                    if(resourceID == data.getExtras().getString("id"))
                        return;
                this.resourceAdapter.add(data.getExtras().getString("name"));
                this.resourceIDs.add(data.getExtras().getString("id"));
                this.resourceAdapter.notifyDataSetChanged();
                this.adjustListViewHeight((ListView)findViewById(R.id.lstResources));
            }
        }
    }
	
	// Controller is an intermediate class which is responsible for processing and 
	// sending data to/from the App(UI) and DB:
    class Controller implements View.OnClickListener, ListView.OnItemLongClickListener {
        AddProject screen;
        public Controller(AddProject screen){
            this.screen = screen;
        }
        public void doChooseOwner(){
            Intent intent = new Intent(screen, AddProjectChooseOwner.class);
            intent.putExtra("caller", "owner");
            startActivityForResult(intent, 1);
        }
        public void doChooseParticipant(){
            Intent intent = new Intent(screen, AddProjectChooseOwner.class);
            intent.putExtra("caller", "participant");
            startActivityForResult(intent, 1);
        }
        public void doChooseResource(){
            Intent intent = new Intent(screen, AddProjectChooseResource.class);
            intent.putExtra("caller", "resource");
            startActivityForResult(intent, 1);
        }
        
		// Project creation code:
		public void doCreateProject(){
            EditText txtName = (EditText)findViewById(R.id.txtProjectName);
            EditText txtDescription = (EditText)findViewById(R.id.txtDescription);
            TextView txtOwner = (TextView)findViewById(R.id.txtOwner);
            projecthub.Credentials credentials = new projecthub.Credentials();
            credentials.setEmail(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, ""));
            credentials.setPassword(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, ""));
            ProjectCreationAttempt attempt = new ProjectCreationAttempt();
            attempt.setName(txtName.getText().toString());
            attempt.setDescription(txtDescription.getText().toString());
            attempt.setOwnerUsername(txtOwner.getText().toString().split(" -")[0]);
            for(String resourceID : screen.resourceIDs)
                attempt.addResourceID(Integer.parseInt(resourceID));
            for(String username : screen.participantUsernames)
                attempt.addParticipant(username);
            HTTPRequest request = new HTTPRequest("CREATEPROJECT", credentials, "http://" + HOST + ":8080/ProjectHubServlet/createProject", attempt);
            HTTPTask task = new HTTPTask(screen);
            task.execute(request);
            screen.progressDialog = ProgressDialog.show(screen, "", "Creating project...");
        }
        public void onClick(View view){
            Button btnChooseOwner = (Button)findViewById(R.id.btnChooseOwner);
            Button btnAddParticipant = (Button)findViewById(R.id.btnAddParticipant);
            Button btnAddResource = (Button)findViewById(R.id.btnAddResource);
            Button btnCreateProject = (Button)findViewById(R.id.btnCreateProject);
            if(view == btnChooseOwner)
                doChooseOwner();
            else if(view == btnAddParticipant)
                doChooseParticipant();
            else if(view == btnAddResource)
                doChooseResource();
            else if(view == btnCreateProject)
                doCreateProject();
        }
        
		// Long-press (long-click) removes items from list:
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long row_id){
            ListView lstParticipants = (ListView)findViewById(R.id.lstParticipants);
            ListView lstResources = (ListView)findViewById(R.id.lstResources);
            if(parent == lstParticipants) {
                screen.participantAdapter.remove(screen.participantAdapter.getItem(position));
                screen.participantUsernames.remove(position);
                screen.participantAdapter.notifyDataSetChanged();
                screen.adjustListViewHeight((ListView) findViewById(R.id.lstParticipants));
            }
            else if(parent == lstResources){
                screen.resourceAdapter.remove(screen.resourceAdapter.getItem(position));
                screen.resourceIDs.remove(position);
                screen.resourceAdapter.notifyDataSetChanged();
                screen.adjustListViewHeight(lstResources);
            }

            return true;
        }
    }
}
