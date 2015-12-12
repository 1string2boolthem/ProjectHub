package csit.team3.projecthub;

import projecthub.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Reader;

public class LoginHomeActivity extends AppCompatActivity implements OnHTTPTaskCompleted {
    private Controller controller;
    private static final String HOST = "freetheheap.net";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.controller = new Controller(this);
        setContentView(R.layout.activity_login_home);
        View btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(controller);
        View btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(controller);
        String email = SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, "");
        String password = SharedPrefsUtils.get(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, "");
        EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtEmail.setText(email);
        EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtPassword.setText(password);
        if(!txtPassword.getText().toString().equals("") && !txtEmail.getText().toString().equals(""))
            controller.doLogin();
    }
    public void updateStatus(String status){
        TextView lblStatus = (TextView)findViewById(R.id.lblStatus);
        lblStatus.setText(status);
    }
    public void showProgressDialog(String text){
        this.progressDialog = ProgressDialog.show(this, "", text, true);
    }
    public void hideProgressDialog(){
        this.progressDialog.hide();
    }
    public void onHTTPTaskCompleted(HTTPResult result){
        hideProgressDialog();
        //The result will always be a LoginResult.
        LoginResult loginResult = (LoginResult)GsonWrapper.fromJson(result.getResponse(), LoginResult.class);

        if(loginResult == null)
            return;
        if(loginResult.wasSuccessful()){
            TextView lblStatus = (TextView)findViewById(R.id.lblStatus);
            String email = ((EditText)findViewById(R.id.txtEmail)).getText().toString();
            String password = ((EditText)findViewById(R.id.txtPassword)).getText().toString();
            SharedPrefsUtils.save(getApplicationContext(), SharedPrefsUtils.EMAIL_KEY, email);
            SharedPrefsUtils.save(getApplicationContext(), SharedPrefsUtils.PASSWORD_KEY, password);
            Intent intent = new Intent(this, UserHome.class);
            startActivity(intent);
        }
        else{
            NotificationDialog.show("Login Failed", "You have entered an incorrect e-mail or password.", this);
        }
    }
    class Controller implements View.OnClickListener {
        LoginHomeActivity screen;
        public Controller(LoginHomeActivity screen){
            this.screen = screen;
        }
        public void doLogin(){
            screen.showProgressDialog("Logging in...");
            EditText txtEmail = (EditText)screen.findViewById(R.id.txtEmail);
            EditText txtPassword = (EditText)screen.findViewById(R.id.txtPassword);
            HTTPRequest request = new HTTPRequest("login", null, "http://" + HOST + ":8080/ProjectHubServlet/login", new LoginAttempt(txtEmail.getText().toString(), txtPassword.getText().toString(), "123"));
            HTTPTask loginTask = new HTTPTask(screen);
            loginTask.execute(request);
        }
        public void doRegister(){
            Intent intent = new Intent(screen, CreateAccount.class);
            startActivity(intent);
        }
        public void onClick(View view){
            if(view == (View)screen.findViewById(R.id.btnLogin))
                doLogin();
            else if(view == (View)screen.findViewById(R.id.btnRegister)){
                doRegister();
            }
        }
    }
}

