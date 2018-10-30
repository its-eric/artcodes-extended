package hu.elte.inf.artcodesextended;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Toast.makeText(this, "about_us", Toast.LENGTH_LONG).show();
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
