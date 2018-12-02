package hu.elte.inf.artcodesextended;
import Services.ApiClient;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.text.TextUtils;
import android.util.Patterns;

import Services.FunctionalInterfaces.ICreateExperience;
import Services.FunctionalInterfaces.IExecutable;
import Services.IApiClient;
import Services.Models.Experience;
import Services.Models.ResponseModel;

public class ExperienceActivity extends AppCompatActivity {

    private TextView addExperienceCodeInput;
    private TextView addExperienceURLInput;
    private Button buttonCreateExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set Content View to activity
        setContentView(R.layout.activity_experience);

        this.addExperienceCodeInput = (TextView) findViewById(R.id.add_experience_code_input);
        this.addExperienceURLInput = (TextView) findViewById(R.id.add_experience_url_input);
        this.buttonCreateExperience = (Button) findViewById(R.id.add_experience_btn);

        buttonCreateExperience.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createExperience();
            }
        });

        /*Handle "Not Register" link*/
        registerLink();
    }

    /*Check valid url*/
    public boolean isValidURL(String url){
        return (!TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches());
    }

    /*Check valid code, only number and :*/
    public boolean isValidCode(String code){
        return (!TextUtils.isEmpty(code) && code.matches("^[0-9:]+$"));
    }

    /*Handle when user click "Not register?" link*/
    public void registerLink(){
        final TextView experience_not_registered = (TextView) findViewById(R.id.experience_not_registered);
        experience_not_registered.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_login = new Intent(ExperienceActivity.this, RegisterActivity.class);
                startActivity(intent_login);
            }
        });
    }

    private boolean checkValidate(String CodeInput, String URLInput){
        // check validate
        if(TextUtils.isEmpty(CodeInput) || TextUtils.isEmpty(URLInput)){
            Toast.makeText(this, "Please fill your code and url", Toast.LENGTH_LONG).show();
            return false;
        }else if(!isValidCode(CodeInput)){
            Toast.makeText(this, "Invalid code, please try again", Toast.LENGTH_LONG).show();
            return false;
        }else if(!isValidURL(URLInput)){
            Toast.makeText(this, "Invalid url, please try again", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void createExperience(){
        String CodeInput = addExperienceCodeInput.getText().toString();
        String URLInput = addExperienceURLInput.getText().toString();

        if(!checkValidate(CodeInput,URLInput)){
            return;
        }

        IApiClient apiClient = new ApiClient();
        ICreateExperience executable  = result -> {

            if(result.isSuccessful()){

                ResponseModel<Experience> responseModel = result.body();
                if(responseModel.success){
                    Toast.makeText(this, "Experience created successfully", Toast.LENGTH_LONG).show();
                    // Jump to MainActivity
                    Intent intent_main = new Intent(this, MainActivity.class);
                    startActivity(intent_main);
                    finish();

                }else{
                    Toast.makeText(this, responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else if(result.code() == 401)
            {
                Toast.makeText(this, "Login please!", Toast.LENGTH_LONG).show();

                // Using to test -> should remove soon
                IExecutable<String> executable1 = (result1) -> {};
                apiClient.login("dorin@gmail.com", "1234", executable1);
            }
            else
            {
                Toast.makeText(this, "Connection error!", Toast.LENGTH_LONG).show();
            }
        };
        apiClient.createExperience(CodeInput, URLInput, executable);
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
