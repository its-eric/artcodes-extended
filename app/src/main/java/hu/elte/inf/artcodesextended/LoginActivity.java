package hu.elte.inf.artcodesextended;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Services.ApiClient;
import Services.FunctionalInterfaces.IExecutable;
import Services.IApiClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private Button login_button;
    private AutoCompleteTextView email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        this.login_button = findViewById(R.id.login);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }


    private void validate() {

        if (!isEmailValid(email.getText().toString())) {
            Toast t = Toast.makeText(this, "Email Invalid", Toast.LENGTH_SHORT);
            t.show();
        } else if (!isPasswordValid(password.getText().toString())) {
            Toast t = Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT);
            t.show();
        } else {
            IExecutable<String> executable = (result) -> {
                if (result == null) {
                    Toast t = Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT);
                    t.show();
                } else if (result.equals("Login was successful")) {
                    Toast t = Toast.makeText(this, "Login was succesfully", Toast.LENGTH_SHORT);
                    t.show();
                    finish();
                } else {
                    Toast t = Toast.makeText(this, result, Toast.LENGTH_SHORT);
                    t.show();
                }
            };
            IApiClient apiClient = new ApiClient();
            apiClient.login(email.getText().toString(),
                    password.getText().toString(),
                    executable);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".com");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }
}