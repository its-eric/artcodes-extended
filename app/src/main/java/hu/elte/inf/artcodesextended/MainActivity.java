package hu.elte.inf.artcodesextended;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.Authorization.AuthorizationHandler;
import Services.ExperienceService;
import Services.IApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Login();
        Register();

        this.context = this;
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

        // Set a button to open the Artcodes Scanner
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
        String result = apiClient.login("dorin@gmail.com", "1244");
        TextView resultTextView = (TextView) findViewById(R.id.textView2);
        if (resultTextView != null)
        {
            resultTextView.setText(result);
        }
    }

    private void Register() {
        IApiClient apiClient = new ApiClient();
        String result = apiClient.login("valera@gmail.com", "1234");
        TextView resultTextView = (TextView) findViewById(R.id.textView2);
        if (resultTextView != null)
        {
            resultTextView.setText(result);
        }
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
                }
            }
        }
    }

}
