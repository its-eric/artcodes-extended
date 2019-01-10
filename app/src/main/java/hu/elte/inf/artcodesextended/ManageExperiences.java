package hu.elte.inf.artcodesextended;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.*;

import Services.ApiClient;
import Services.FunctionalInterfaces.IExecutable;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.FunctionalInterfaces.IRemoveExperience;
import Services.IApiClient;
import Services.Models.ResponseModel;
import Services.Models.Experience;

public class ManageExperiences extends AppCompatActivity {

    private HashMap<String, List<String>> mapExperiences = new HashMap<>();
    private ListView resultsLV;
    private Button update_btn;
    private Button delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_experiences);

        this.resultsLV = (ListView) findViewById(R.id.lv_manageEx);
        listExperiences();
    }

    public void listAllExperienceWithPersonalTemplate(HashMap<String,List<String>> mapExperiences){

        List<HashMap <String, String>> listItems = new ArrayList<>();
        List<String> idExperience = new ArrayList<>();

        for(Map.Entry<String,List<String>> entry : mapExperiences.entrySet()){
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("First Line", entry.getValue().get(0)); //code
            resultsMap.put("Second Line", entry.getValue().get(1)); //url
            listItems.add(resultsMap);

            idExperience.add(entry.getKey());
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems,R.layout.list_manage_experiences,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text_display_code, R.id.text_display_url})
        {
            @Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                // Handle delete button
                delete_btn = (Button)v.findViewById(R.id.btn_delete);
                delete_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        processDeleteExperience(idExperience.get(position));
                        Toast.makeText(ManageExperiences.this,"deleted" + position + " " + idExperience.get(position),Toast.LENGTH_SHORT).show();
                    }
                });

                // Handle update button
                update_btn = (Button)v.findViewById(R.id.btn_update);
                update_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg1) {
                        Toast.makeText(ManageExperiences.this,"updated" + position + " " + idExperience.get(position),Toast.LENGTH_SHORT).show();
                        processUpdateExperience(idExperience.get(position), mapExperiences);
                    }
                });
                return v;
            }
        };

        resultsLV.setAdapter(adapter);
    }

    public void processUpdateExperience(String id, HashMap<String,List<String>> mapExperiences){

        List<String> tupleList = mapExperiences.get(id); //code, url, userID
        String[] tupleArray = tupleList.toArray(new String[tupleList.size()]);

        Intent intent_update_experience = new Intent(this, UpdateExperienceActivity.class);
        intent_update_experience.putExtra("ExperienceID", id);
        intent_update_experience.putExtra("Code_Url_UserID",tupleArray);
        startActivity(intent_update_experience);
    }

    public void processDeleteExperience(String id){
        IApiClient apiClient = new ApiClient();
        IRemoveExperience executable = (result) ->{
            if(result.isSuccessful()){

                Toast.makeText(this, "Deleted successfully!", Toast.LENGTH_LONG).show();
                // Refresh page
                Intent intent_main = new Intent(this, ManageExperiences.class);
                startActivity(intent_main);
                finish();
            }
            else if(result.code() == 401)
            {
                Toast.makeText(this, "Login please!", Toast.LENGTH_LONG).show();
                // Go to login page
                Intent intent_main = new Intent(this, LoginActivity.class);
                startActivity(intent_main);
                finish();
            }
            else
            {
                Toast.makeText(this, "Connection error!", Toast.LENGTH_LONG).show();
            }
        };
        apiClient.deleteExperience(id,executable);
    }

    public void listExperiences(){

        IApiClient apiClient = new ApiClient();
        IGetExperiences executable = (result) -> {

            if(result.isSuccessful()){

                ResponseModel<List<Experience>> responseModel = result.body();
                if(responseModel.success){
                    //List<String> idExperience = new ArrayList<>();
                    for (Experience i : responseModel.result){
                        List<String> parts = new ArrayList<>();
                        parts.add(i.code);
                        parts.add(i.url);
                        parts.add(i.userId);
                        //idExperience.add(i.code);

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
