package hu.elte.inf.artcodesextended;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Services.ApiClient;
import Services.FunctionalInterfaces.IExecutable;
import Services.IApiClient;

public class RegisterActivity extends AppCompatActivity {

    private Button register_button;
    private AutoCompleteTextView name;
    private AutoCompleteTextView email;
    private AutoCompleteTextView city;
    private AutoCompleteTextView country;
    private EditText password;
    private EditText confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.register_button = findViewById(R.id.register_button);
        this.name = findViewById(R.id.name);
        this.email = findViewById(R.id.email);
        this.city = findViewById(R.id.city);
        this.country = findViewById(R.id.country);
        this.password = findViewById(R.id.password);
        this.confirm_password = findViewById(R.id.confirm_password);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void validate() {
        if (isEmpty(name)) {
            Toast t = Toast.makeText(this, "You must enter a name!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if(!isEmail(email)){
            Toast t = Toast.makeText(this, "Email Invalid", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (isEmpty(country)) {
            Toast t = Toast.makeText(this, "You must enter a country!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (isEmpty(city)) {
            Toast t = Toast.makeText(this, "You must enter a city!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if(isEmpty(password)){
            Toast t = Toast.makeText(this, "You must enter a password!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if(!password.getText().toString().equals(confirm_password.getText().toString())){
            Toast t = Toast.makeText(this, "Password and Confirmation should match!", Toast.LENGTH_SHORT);
            t.show();
        }
        else{
            IExecutable<String> executable = (result) ->{

                if(result == null){
                    Toast t = Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT);
                    t.show();
                }else if(result.equals("Registration was successful")) {
                    Toast t = Toast.makeText(this, "Registration was succesfully completed", Toast.LENGTH_SHORT);
                    t.show();
                    finish();
                }else{
                    Toast t = Toast.makeText(this, result, Toast.LENGTH_SHORT);
                    t.show();
                }
            };

            IApiClient apiClient = new ApiClient();
            apiClient.register(email.getText().toString(),
                    password.getText().toString(),
                    name.getText().toString(),
                    country.getText().toString(),
                    city.getText().toString(),
                    executable );
        }
    }

    /* Menu method*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.login:
                Toast.makeText(this, "login", Toast.LENGTH_LONG).show();
                Intent intent_login = new Intent(this, LoginActivity.class);
                startActivity(intent_login);
                return(true);
            case R.id.register:
                Toast.makeText(this, "register", Toast.LENGTH_LONG).show();
                Intent intent_register = new Intent(this, RegisterActivity.class);
                startActivity(intent_register);
                return(true);
            case R.id.about_us:
                Toast.makeText(this, "experience", Toast.LENGTH_LONG).show();
                Intent intent_experience = new Intent(this, ExperienceActivity.class);
                startActivity(intent_experience);
                return(true);
            case R.id.browser:
                Toast.makeText(this, "browser experience", Toast.LENGTH_LONG).show();
                Intent intent_browser = new Intent(this, BrowserExperienceActivity.class);
                startActivity(intent_browser);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }


}
