package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class LoginFragment extends Fragment {

    private Button login_button;
    private AutoCompleteTextView email;
    private EditText password;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.login_button = view.findViewById(R.id.login);
        this.email = view.findViewById(R.id.email);
        this.password = view.findViewById(R.id.password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void validate() {

        if (!isEmailValid(email.getText().toString())) {
            Toast t = Toast.makeText(getContext(), "Email Invalid", Toast.LENGTH_SHORT);
            t.show();
        } else if (!isPasswordValid(password.getText().toString())) {
            Toast t = Toast.makeText(getContext(), "Password must be at least 4 characters", Toast.LENGTH_SHORT);
            t.show();
        } else {
            IExecutable<String> executable = (result) -> {
                if (result == null) {
                    Toast t = Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT);
                    t.show();
                } else if (result.equals("Login was successful")) {
                    Toast t = Toast.makeText(getContext(), "Login was succesfully", Toast.LENGTH_SHORT);
                    t.show();
                    getActivity().onBackPressed();
                } else {
                    Toast t = Toast.makeText(getContext(), result, Toast.LENGTH_SHORT);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
