package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.EditText;
import java.util.HashMap;
import java.util.Map;

import uk.ac.horizon.artcodes.model.Experience;
import uk.ac.horizon.artcodes.model.Action;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.horizon.artcodes.scanner.ScannerActivity;

public class MainActivity extends AppCompatActivity {

    private final int ARTCODE_REQUEST = 12345; // Range: 0-65535
    private Map<String,String> data = new HashMap<>();
    private Experience experience = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        for (String code : this.data.keySet())
        {
            // Create Actions for the Artcodes you want to scan
            Action action = new Action();
            action.getCodes().add(code);
            experience.getActions().add(action);
        }

        // Set a button to open the Artcodes Scanner
        final FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.fab);
        if (cameraButton != null)
        {
            cameraButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    synchronized (context)
                    {
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


//        setContentView(R.layout.activity_experience);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

}
