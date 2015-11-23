package csit.team3.projecthub;

import projecthub.*;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginHomeActivity extends AppCompatActivity {
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        this.controller = new Controller(this);
        setContentView(R.layout.activity_login_home);
        View btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(controller);
    }
    public void updateStatus(String status){
        TextView lblStatus = (TextView)findViewById(R.id.lblStatus);
        lblStatus.setText(status);
    }
}
class Controller implements View.OnClickListener {
    AppCompatActivity screen;
    public Controller(AppCompatActivity screen){
        this.screen = screen;
    }
    public void onClick(View view){
        EditText txtEmail = (EditText)screen.findViewById(R.id.txtEmail);
        EditText txtPassword = (EditText)screen.findViewById(R.id.txtPassword);
        LoginResult result = Authenticator.ClientLogin(txtEmail.getText().toString(), txtPassword.getText().toString());
        if(result == null)
            return;
        if(result.wasSuccessful()){
            TextView lblStatus = (TextView)screen.findViewById(R.id.lblStatus);
            lblStatus.setText("Login success!");
        }
        else{
            TextView lblStatus = (TextView)screen.findViewById(R.id.lblStatus);
            lblStatus.setText("Login failed...");
        }
    }
}
