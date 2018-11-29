package hu.elte.inf.artcodesextended;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.FunctionalInterfaces.IGetAll;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.IApiClient;
import Services.FunctionalInterfaces.IExecutable;
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
    private Context context;
    private Button login;
    private Button register;
    private TextView textView3;

    private Button getExperiencesButton;
    private TextView experienceResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        this.textView3 = findViewById(R.id.textView3);
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

        // Load your data from somewhere
        this.data.put("1:1:1:1:2", "Artcode 1");
        this.data.put("1:1:2:4:4", "Artcode 2");
        this.data.put("1:1:1:3:3", "Artcode 3");
        this.data.put("1:1:1:1:5", "Artcode 4");
        this.data.put("1:2:2:3", "From squares");
        this.data.put("1:2:2", "From circles");

        // Create and configure an Artcode experience
        experience = new Experience();
        for (String code : this.data.keySet()) {
            // Create Actions for the Artcodes you want to scan
            Action action = new Action();
            action.getCodes().add(code);
            experience.getActions().add(action);
        }

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

    private void Login() {
        IApiClient apiClient = new ApiClient();
        IExecutable<String> executable = (result) ->
        {
            if(result != null){
                //for example show the result;
                //then navigate to another activity;
                //you can setup how much to wait before navigating
                textView3.setTextColor(Color.GREEN);
                textView3.setText(result);
            }
            else {
                //set it with some different colors showing that there is an error;
                //don't navigate to another activity or
                textView3.setTextColor(Color.RED);
                textView3.setText("Connection error");
            }
        };

        apiClient.login("dorin@gmail.com", "1234", executable);
    }

    private void GetExperiences() {
        IApiClient apiClient = new ApiClient();
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
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.stackoverflow.com"));
                    startActivity(intent);
                }
            }
        }
    }

}
