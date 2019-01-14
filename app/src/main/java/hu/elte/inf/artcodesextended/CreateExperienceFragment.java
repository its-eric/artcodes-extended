package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Services.ApiClient;
import Services.FunctionalInterfaces.ICreateExperience;
import Services.IApiClient;
import Services.Models.Experience;
import Services.Models.ResponseModel;

public class CreateExperienceFragment extends Fragment {

    private TextView addExperienceCodeInput;
    private TextView addExperienceURLInput;
    private Button buttonCreateExperience;

    public CreateExperienceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_experience, container, false);
        this.addExperienceCodeInput = (TextView) view.findViewById(R.id.add_experience_code_input);
        this.addExperienceURLInput = (TextView) view.findViewById(R.id.add_experience_url_input);
        this.buttonCreateExperience = (Button) view.findViewById(R.id.add_experience_btn);

        buttonCreateExperience.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createExperience();
            }
        });

        /*Handle "Not Logged in" link*/
        loginLink(view);

        return view;
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
    public void loginLink(View view){
        final TextView experience_not_registered = (TextView) view.findViewById(R.id.experience_not_registered);
        experience_not_registered.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getFragmentManager().beginTransaction().replace(R.id.flContent, new LoginFragment()).commit();
            }
        });
    }

    private boolean checkValidate(String CodeInput, String URLInput){
        // check validate
        if(TextUtils.isEmpty(CodeInput) || TextUtils.isEmpty(URLInput)){
            Toast.makeText(getContext(), "Please fill your code and url", Toast.LENGTH_LONG).show();
            return false;
        }else if(!isValidCode(CodeInput)){
            Toast.makeText(getContext(), "Invalid code, please try again", Toast.LENGTH_LONG).show();
            return false;
        }else if(!isValidURL(URLInput)){
            Toast.makeText(getContext(), "Invalid url, please try again", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "Experience created successfully", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();

                }else{
                    Toast.makeText(getContext(), responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else if(result.code() == 401)
            {

            }
            else
            {
                Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_LONG).show();
            }
        };
        apiClient.createExperience(CodeInput, URLInput, executable);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
