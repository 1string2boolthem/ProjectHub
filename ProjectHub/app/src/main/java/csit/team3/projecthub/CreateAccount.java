package csit.team3.projecthub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import projecthub.Account.Creation.*;
import projecthub.GsonWrapper;
import projecthub.HTTPRequest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccount extends AppCompatActivity implements OnHTTPTaskCompleted {
    private Controller controller;
    private static final String HOST = "freetheheap.net";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.controller = new Controller(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this.controller);
    }
    public void onHTTPTaskCompleted(HTTPResult result){
        CreationResult creationResult = (CreationResult) GsonWrapper.fromJson(result.getResponse(), CreationResult.class);
        if(!creationResult.wasSuccessful())
            NotificationDialog.show("Register Failed", creationResult.getError(0), this);
        else{
            SharedPrefsUtils.save(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, creationResult.getEmail());
            SharedPrefsUtils.save(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, creationResult.getPassword());
            Intent intent = new Intent(this, UserHome.class);
            startActivity(intent);
        }
    }
    class Controller implements View.OnClickListener {
        CreateAccount screen;
        public Controller(CreateAccount screen){
            this.screen = screen;
        }
        public void doRegister(){
            String firstName = ((EditText)findViewById(R.id.txtFirst)).getText().toString();
            String lastName = ((EditText)findViewById(R.id.txtLast)).getText().toString();
            String username = ((EditText)findViewById(R.id.txtUsername)).getText().toString();
            String email = ((EditText)findViewById(R.id.txtEmail)).getText().toString();
            String password = ((EditText)findViewById(R.id.txtPassword)).getText().toString();
            String confirmPassword = ((EditText)findViewById(R.id.txtConfirmPassword)).getText().toString();
            CreationAttempt attempt = new CreationAttempt(firstName, lastName, username, email, password, confirmPassword);
            HTTPRequest request = new HTTPRequest("REGISTER", null, "http://" + HOST + ":8080/ProjectHubServlet/register", attempt);
            HTTPTask task = new HTTPTask(screen);
            task.execute(request);

        }
        public void onClick(View view){
            doRegister();
        }
    }
}
