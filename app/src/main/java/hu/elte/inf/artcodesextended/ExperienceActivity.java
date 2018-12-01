package hu.elte.inf.artcodesextended;

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

public class ExperienceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        final Button buttonExperience = (Button) findViewById(R.id.add_experience_btn);
        buttonExperience.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                TextView addExperienceTextDisplay = (TextView) findViewById(R.id.add_experience_text_display);
                TextView addExperienceTextInput = (TextView) findViewById(R.id.add_experience_text_input);
                addExperienceTextDisplay.setText(addExperienceTextInput.getText());
            }
        });

        final TextView experience_not_registered = (TextView) findViewById(R.id.experience_not_registered);
        experience_not_registered.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_login = new Intent(ExperienceActivity.this, RegisterActivity.class);
                startActivity(intent_login);
            }
        });

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
