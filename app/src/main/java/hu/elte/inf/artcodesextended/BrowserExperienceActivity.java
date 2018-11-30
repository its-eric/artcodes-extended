package hu.elte.inf.artcodesextended;


import Services.ApiClient;
import Services.IApiClient;
import Services.Models.Experience;
import Services.ApiClient;
import Services.FunctionalInterfaces.IGetAll;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.IApiClient;
import Services.FunctionalInterfaces.IExecutable;
import Services.Models.PublicExperience;
import Services.Models.ResponseModel;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class BrowserExperienceActivity extends ListActivity {
    private HashMap<String, String> mapAllExperience = new HashMap<>();
    private ListView resultsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_experience);

        this.resultsListView = (ListView) findViewById(R.id.result_listview);
        listALLExperience();
    }

    public void printAllExperience(HashMap<String,String> mapAllExperience){
        //==== Print CODEs as list: This code runs ok
//        List<String> listOfCodes = new ArrayList(mapAllExperience.keySet());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_dropdown_item_1line, listOfCodes);
//        getListView().setAdapter(adapter);
        // ========

        /*Print codes and urls */
        List<HashMap<String, String>> listItems = new ArrayList<HashMap<String,String>>();
//        Iterator it = mapAllExperience.entrySet().iterator();
//        while(it.hasNext())
//        {
//            HashMap<String, String> resultsMap = new HashMap<>();
//            Map.Entry pair = (Map.Entry) it.next();
//            resultsMap.put("First Line", pair.getKey().toString());
//            resultsMap.put("Second Line", pair.getValue().toString());
//            listItems.add(resultsMap);
//        }
        for(Map.Entry<String,String> entry : mapAllExperience.entrySet()){
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("First Line", entry.getKey().toString());
            resultsMap.put("Second Line", entry.getValue().toString());
            listItems.add(resultsMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listItems,R.layout.list_items,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text_display_code, R.id.text_display_url});
        resultsListView.setAdapter(adapter);
    }

    public void listALLExperience(){
        IApiClient apiClient = new ApiClient();
        IGetAll executable = (result) -> {

            if(result.isSuccessful()){

                ResponseModel<List<PublicExperience>> responseModel = result.body();
                if(responseModel.success){
                    for (PublicExperience i : responseModel.result){
                        mapAllExperience.put(i.code,i.url);
                    }
                    // print
                    printAllExperience(mapAllExperience);
                }else{
                    Toast.makeText(this, responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else if(result.code() == 401)
            {
                Toast.makeText(this, "Login please!", Toast.LENGTH_LONG).show();

                IExecutable<String> executable1 = (result1) -> {};
                apiClient.login("dorin@gmail.com", "1234", executable1);
            }
            else
            {
                Toast.makeText(this, "Connection error!", Toast.LENGTH_LONG).show();
            }
        };

        apiClient.getAllExperiences(executable);
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
