package hu.elte.inf.artcodesextended;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.Authorization.AuthorizationHandler;
import Services.FunctionalInterfaces.IGetAll;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.IApiClient;
import Services.Models.PublicExperience;
import Services.Models.ResponseModel;
import uk.ac.horizon.artcodes.model.Experience;
import uk.ac.horizon.artcodes.model.Action;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.horizon.artcodes.scanner.ScannerActivity;

public class MainActivity extends AppCompatActivity {

    private final int ARTCODE_REQUEST = 12345; // Range: 0-65535
    private Map<String, String> data = new HashMap<>();
    private Experience experience = null;
    private final Context context = this;
    private Button login;
    private Button register;
    private Button getExperiencesButton;
    private TextView experienceResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.login = findViewById(R.id.login);
        this.register = findViewById(R.id.register);
        this.getExperiencesButton = findViewById(R.id.getExperiences);
        this.experienceResult = findViewById(R.id.textView4);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        getExperiencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetExperiences();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        FetchExperiences();

        // Set a login to open the Artcodes Scanner
        final FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.fab);
        if (cameraButton != null) {
            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    synchronized (context) {
                        cameraButton.setEnabled(false);

                        // Create and setup the intent that will launch the Artcode scanner
                        Intent intent = new Intent(context, ScannerActivity.class);

                        // Put experience in intent
                        Gson gson = new GsonBuilder().create();
                        intent.putExtra("experience", gson.toJson(experience));

                        // Start artcode reader activity
                        startActivityForResult(intent, ARTCODE_REQUEST);
                    }
                }
            });
        }
    }

    private void FetchExperiences(){
        IGetAll executable = (result) ->{
            if(result == null){
                Toast t = Toast.makeText(context, "Connection error", Toast.LENGTH_LONG);
                t.show();
            }
            else if (!result.isSuccessful()){
                    Toast t = Toast.makeText(context, "Couldn't fetch experiences", Toast.LENGTH_LONG);
                    t.show();
            }
            else {
                ResponseModel<List<PublicExperience>> responseModel = result.body();
                for (PublicExperience i : responseModel.result){
                    data.put(i.code,i.url);
                    System.out.println("Code: " + i.code + " Url:" + i.url);
                }
                // Create and configure an Artcode experience
                experience = new Experience();
                for (String code : this.data.keySet()) {
                    // Create Actions for the Artcodes you want to scan
                    Action action = new Action();
                    action.getCodes().add(code);
                    experience.getActions().add(action);
                    System.out.println("Found:" + code);
                }
            }
        };
        IApiClient apiClient = new ApiClient();
        apiClient.getAllExperiences(executable);
    }

    private void Login() {
        Intent login_activity = new Intent(this, LoginActivity.class);
        startActivity(login_activity);
    }

    private void GetExperiences() {

        IGetExperiences executable = (result) ->
        {
            ResponseModel<List<Services.Models.Experience>> resultModel = result.body();
            if(result.isSuccessful()){
                //for example show the result;
                //then navigate to another activity;
                //you can setup how much to wait before navigating
                experienceResult.setTextColor(Color.GREEN);
                experienceResult.setText("Found results: " +resultModel.result.size());

            }
            else if(result.code() == 401){
                //redirect to login page
                experienceResult.setTextColor(Color.RED);
                experienceResult.setText("You are not authorized");
            }
            else if(!resultModel.success){
                //set it with some different colors showing that there is an error;
                //don't navigate to another activity or
                experienceResult.setTextColor(Color.RED);
                experienceResult.setText(resultModel.errors);
            }
        };

        IApiClient apiClient = new ApiClient();
        apiClient.getExperiences(executable);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        synchronized (context)
        {
            final FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.fab);
            if (cameraButton != null)
            {
                cameraButton.setEnabled(true);
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.print("Request Code:" + requestCode);
        System.out.print("Result Code:" + resultCode);
        if (requestCode == ARTCODE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                // This is where you will receive a response from the
                // Artcode scanner if an Artcode was found

                // This is the code of the Artcode that was found (e.g. "1:1:1:1:2")
                String code = data.getStringExtra("marker");

                // Do any logic based on the result here, for example display the code in a TextView
                TextView resultTextView = (TextView) findViewById(R.id.textView2);
                if (resultTextView != null)
                {
                    resultTextView.setText("Found code " + code + ": " + this.data.get(code) + "!");
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(this.data.get(code)));
                    startActivity(intent);
                }
            }
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
            case R.id.manage:
                Toast.makeText(this, "manage experience", Toast.LENGTH_LONG).show();
                Intent intent_manage = new Intent(this, ManageExperiences.class);
                startActivity(intent_manage);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }
}
