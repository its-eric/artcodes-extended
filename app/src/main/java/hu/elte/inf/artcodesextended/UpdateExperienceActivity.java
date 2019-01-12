package hu.elte.inf.artcodesextended;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Services.ApiClient;
import Services.FunctionalInterfaces.IUpdateExperience;
import Services.FunctionalInterfaces.IExecutable;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.IApiClient;
import Services.Models.Experience;
import Services.Models.ResponseModel;

public class UpdateExperienceActivity extends AppCompatActivity {

    private HashMap<String, List<String>> mapExperiences = new HashMap<>();
    private TextView updateExperienceCodeInput;
    private TextView updateExperienceURLInput;
    private Button buttonUpdateExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_experience);

        this.updateExperienceCodeInput = (TextView) findViewById(R.id.update_experience_code_input);
        this.updateExperienceURLInput = (TextView) findViewById(R.id.update_experience_url_input);
        this.buttonUpdateExperience = (Button) findViewById(R.id.update_experience_btn);

        // Get passing values from ManageExperience activity
        String idExperience = getIntent().getStringExtra("ExperienceID");
        String[] tupleArray = getIntent().getStringArrayExtra("Code_Url_UserID");

        // Set current data on text view
        updateExperienceCodeInput.setText(tupleArray[0]);
        updateExperienceURLInput.setText(tupleArray[1]);

        buttonUpdateExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUpdateExperience(idExperience, tupleArray);
            }
        });
    }

    public void processUpdateExperience(String id, String[] tupleArray){
        String editCode = updateExperienceCodeInput.getText().toString();
        String editURL = updateExperienceURLInput.getText().toString();
        String userId = tupleArray[2];

        IApiClient apiClient = new ApiClient();
        IUpdateExperience executable  = result -> {

            if(result.isSuccessful()){

                ResponseModel<Experience> responseModel = result.body();
                if(responseModel.success){
                    Toast.makeText(this, "Experience updated successfully", Toast.LENGTH_LONG).show();
                    // Jump to ManageExperience Activity
                    Intent intent_main = new Intent(this, ManageExperiences.class);
                    startActivity(intent_main);
                    finish();

                }else{
                    Toast.makeText(this, responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this, "Connection error!", Toast.LENGTH_LONG).show();
            }
        };
        apiClient.updateExperience(id, editCode, editURL, userId, executable);
    }
}
