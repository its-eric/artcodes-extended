package hu.elte.inf.artcodesextended;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.FunctionalInterfaces.IExecutable;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.IApiClient;
import Services.Models.PublicExperience;
import Services.Models.ResponseModel;
import Services.Models.Experience;

public class ManageExperiences extends AppCompatActivity {

    private HashMap<String, List<String>> mapExperiences = new HashMap<>();
    private ListView resultsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_experiences);

        this.resultsLV = (ListView) findViewById(R.id.tv_manageEx);
        listExperiences();
    }

    public void listAllExperienceWithPersonalTemplate(HashMap<String,List<String>> mapExperiences){
        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems,R.layout.list_items,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text_display_code, R.id.text_display_url});

        for(Map.Entry<String,List<String>> entry : mapExperiences.entrySet()){
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("First Line", entry.getKey());
            resultsMap.put("Second Line", entry.getValue().toString());
            listItems.add(resultsMap);
        }
        resultsLV.setAdapter(adapter);
    }

    public void listExperiences(){

        IApiClient apiClient = new ApiClient();
        IGetExperiences executable = (result) -> {

            if(result.isSuccessful()){

                ResponseModel<List<Experience>> responseModel = result.body();
                if(responseModel.success){
                    for (Experience i : responseModel.result){
                        List<String> parts = new ArrayList<>();
                        parts.add(i.code);
                        parts.add(i.url);
                        parts.add(i.userId);
                        // map contains <id,[code, url, userId]>
                        mapExperiences.put(i.id,parts);
                    }
                    // print
                    listAllExperienceWithPersonalTemplate(mapExperiences);
                }else{
                    Toast.makeText(this, responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else if(result.code() == 401)
            {
                Toast.makeText(this, "Login please!", Toast.LENGTH_LONG).show();

                // Test
                IExecutable<String> executable1 = (result1) -> {};
                apiClient.login("dorin@gmail.com", "1234", executable1);
                // End Test
            }
            else
            {
                Toast.makeText(this, "Connection error!", Toast.LENGTH_LONG).show();
            }
        };

        apiClient.getExperiences(executable);
    }
}
