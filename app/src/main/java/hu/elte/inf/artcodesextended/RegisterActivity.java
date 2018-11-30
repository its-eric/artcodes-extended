package hu.elte.inf.artcodesextended;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        }
        return(super.onOptionsItemSelected(item));
    }


}
