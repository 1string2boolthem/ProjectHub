package csit.team3.projecthub;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import projecthub.Account.Creation.CreationAttempt;
import projecthub.Credentials;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import projecthub.GsonWrapper;
import projecthub.HTTPRequest;
import projecthub.Project.ProjectList;

public class UserHome extends AppCompatActivity implements OnHTTPTaskCompleted {
    ArrayList<String> projectNames = new ArrayList<String>();
    private static final String HOST = "freetheheap.net";
    boolean loadInProcess = false;
    Controller controller;
    ProgressDialog progressDialog;
    ArrayAdapter<String> listAdapter;
    private void loadProjectNames(){
        if(loadInProcess)
            return;
        loadInProcess = true;
        this.progressDialog = ProgressDialog.show(this, "", "Loading projects...", true);
        Credentials credentials = new Credentials();
        credentials.setEmail(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, ""));
        credentials.setPassword(SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, ""));
        HTTPRequest request = new HTTPRequest("LOADPROJECTS", credentials, "http://" + HOST + ":8080/ProjectHubServlet/getUserProjects", null);
        HTTPTask task = new HTTPTask(this);
        task.execute(request);
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadProjectNames();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        this.controller = new Controller(this);
        ListView lstProjects = (ListView)findViewById(R.id.lstProjects);
        listAdapter = new ArrayAdapter<String>(this, R.layout.user_home_project_row, projectNames);
        lstProjects.setAdapter(listAdapter);
        Button btnNavigate = (Button)findViewById(R.id.btnNavigate);
        btnNavigate.setOnClickListener(this.controller);
        loadProjectNames();
    }
    public void onHTTPTaskCompleted(HTTPResult result){
        ProjectList projectList = (ProjectList) GsonWrapper.fromJson(result.getResponse(),ProjectList.class);
        this.listAdapter.clear();
        Set<String> keys = projectList.getKeys();
        for(int i = 0; i < keys.size(); i++){
            projectNames.add(projectList.getValue((String)keys.toArray()[i]));
        }
        listAdapter.notifyDataSetChanged();
        this.progressDialog.hide();
        loadInProcess = false;
    }
    class Controller implements View.OnClickListener {
        UserHome screen;
        public Controller(UserHome screen){
            this.screen = screen;
        }
        public void onClick(View view){
            String[] menuItems = {"Add Project", "Global Resources", "My Profile", "Logout"};
            AlertDialog.Builder builder = new AlertDialog.Builder(screen);
            builder.setTitle("Navigate to");
            builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        Intent intent = new Intent(screen, AddProject.class);
                        startActivity(intent);
                    }
                    else if(which == 3){
                        SharedPrefsUtils.save(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, "");
                        SharedPrefsUtils.save(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, "");
                        Intent intent = new Intent(screen, LoginHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            });
            builder.create();
            builder.show();
        }
    }
}
