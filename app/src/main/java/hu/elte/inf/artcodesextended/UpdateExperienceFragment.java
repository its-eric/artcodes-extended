package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import Services.ApiClient;
import Services.FunctionalInterfaces.IUpdateExperience;
import Services.IApiClient;
import Services.Models.Experience;
import Services.Models.ResponseModel;

public class UpdateExperienceFragment extends Fragment {

    private HashMap<String, List<String>> mapExperiences = new HashMap<>();
    private TextView updateExperienceCodeInput;
    private TextView updateExperienceURLInput;
    private Button buttonUpdateExperience;
    private String idExperience;
    private String[] tupleArray;
    public UpdateExperienceFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_experience, container, false);
        Bundle args = getArguments();
        if (args != null) {
            idExperience = args.getString("ExperienceID");
            tupleArray = args.getStringArray("Code_Url_UserID");
        }
        this.updateExperienceCodeInput = (TextView) view.findViewById(R.id.update_experience_code_input);
        this.updateExperienceURLInput = (TextView) view.findViewById(R.id.update_experience_url_input);
        this.buttonUpdateExperience = (Button) view.findViewById(R.id.update_experience_btn);

        // Set current data on text view
        updateExperienceCodeInput.setText(tupleArray[0]);
        updateExperienceURLInput.setText(tupleArray[1]);

        buttonUpdateExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUpdateExperience(idExperience, tupleArray);
            }
        });
        return view;
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
                    Toast.makeText(getContext(), "Experience updated successfully", Toast.LENGTH_LONG).show();
                    // Jump to ManageExperience Activity
                    //Intent intent_main = new Intent(getContext(), ManageExperiences.class);
                    //startActivity(intent_main);
                    //finish();
                    getActivity().onBackPressed();

                }else{
                    Toast.makeText(getContext(), responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_LONG).show();
            }
        };
        apiClient.updateExperience(id, editCode, editURL, userId, executable);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
