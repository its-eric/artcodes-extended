package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Services.ApiClient;
import Services.FunctionalInterfaces.IExecutable;
import Services.IApiClient;

public class RegisterFragment extends Fragment {

    private Button register_button;
    private AutoCompleteTextView name;
    private AutoCompleteTextView email;
    private AutoCompleteTextView city;
    private AutoCompleteTextView country;
    private EditText password;
    private EditText confirm_password;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        this.register_button = view.findViewById(R.id.register_button);
        this.name = view.findViewById(R.id.name);
        this.email = view.findViewById(R.id.email);
        this.city = view.findViewById(R.id.city);
        this.country = view.findViewById(R.id.country);
        this.password = view.findViewById(R.id.password);
        this.confirm_password = view.findViewById(R.id.confirm_password);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        return view;
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
            Toast t = Toast.makeText(getContext(), "You must enter a name!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if(!isEmail(email)){
            Toast t = Toast.makeText(getContext(), "Email Invalid", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (isEmpty(country)) {
            Toast t = Toast.makeText(getContext(), "You must enter a country!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (isEmpty(city)) {
            Toast t = Toast.makeText(getContext(), "You must enter a city!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if(isEmpty(password)){
            Toast t = Toast.makeText(getContext(), "You must enter a password!", Toast.LENGTH_SHORT);
            t.show();
        }
        else if(!password.getText().toString().equals(confirm_password.getText().toString())){
            Toast t = Toast.makeText(getContext(), "Password and Confirmation should match!", Toast.LENGTH_SHORT);
            t.show();
        }
        else{
            IExecutable<String> executable = (result) ->{

                if(result == null){
                    Toast t = Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT);
                    t.show();
                }else if(result.equals("Registration was successful")) {
                    Toast t = Toast.makeText(getContext(), "Registration was succesfully completed", Toast.LENGTH_SHORT);
                    t.show();
                    getActivity().onBackPressed();
                }else{
                    Toast t = Toast.makeText(getContext(), result, Toast.LENGTH_SHORT);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
