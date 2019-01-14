package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.FunctionalInterfaces.IGetAll;
import Services.IApiClient;
import Services.Models.PublicExperience;
import Services.Models.ResponseModel;

public class BrowseExperienceFragment extends Fragment {

    private HashMap<String, String> mapAllExperience = new HashMap<>();
    private ListView resultsListView;

    public BrowseExperienceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_browse_experience, container, false);

        this.resultsListView = (ListView) view.findViewById(R.id.results_listview);
        listALLExperience(view);

        return view;
    }

    public void listAllExperienceWithPersonalTemplate(HashMap<String,String> mapAllExperience, View view){
        List<HashMap<String, String>> listItems = new ArrayList<>();

        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems,R.layout.list_items,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text_display_code, R.id.text_display_url});

        for(Map.Entry<String,String> entry : mapAllExperience.entrySet()){
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("First Line", entry.getKey().toString());
            resultsMap.put("Second Line", entry.getValue().toString());
            listItems.add(resultsMap);
        }
        this.resultsListView = (ListView) view.findViewById(R.id.results_listview);
        resultsListView.setAdapter(adapter);
    }

    public void listALLExperience(View view){
        IApiClient apiClient = new ApiClient();
        IGetAll executable = (result) -> {

            if(result.isSuccessful()){

                ResponseModel<List<PublicExperience>> responseModel = result.body();
                if(responseModel.success){
                    for (PublicExperience i : responseModel.result){
                        mapAllExperience.put(i.code,i.url);
                    }
                    // print
                    listAllExperienceWithPersonalTemplate(mapAllExperience, view);
                }else{
                    Toast.makeText(getContext(), responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_LONG).show();
            }
        };

        apiClient.getAllExperiences(executable);
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
