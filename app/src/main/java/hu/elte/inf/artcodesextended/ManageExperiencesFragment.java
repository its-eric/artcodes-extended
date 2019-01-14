package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.FunctionalInterfaces.IRemoveExperience;
import Services.IApiClient;
import Services.Models.Experience;
import Services.Models.ResponseModel;

public class ManageExperiencesFragment extends Fragment {

    private HashMap<String, List<String>> mapExperiences = new HashMap<>();
    private ListView resultsLV;
    private ImageView update_btn;
    private ImageView delete_btn;

    public ManageExperiencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_manage_experiences, container, false);

        this.resultsLV = (ListView) view.findViewById(R.id.lv_manageEx);
        listExperiences();
        return view;
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

        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems,R.layout.list_manage_experiences,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text_display_code, R.id.text_display_url})
        {
            @Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                // Handle delete button
                delete_btn = (ImageView) v.findViewById(R.id.btn_delete);
                delete_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        processDeleteExperience(idExperience.get(position));
                    }
                });

                // Handle update button
                update_btn = (ImageView) v.findViewById(R.id.btn_update);
                update_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg1) {
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

        Fragment fragment =  new UpdateExperienceFragment();
        Bundle args = new Bundle();
        args.putString("ExperienceID", id);
        args.putStringArray("Code_Url_UserID", tupleArray);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public void processDeleteExperience(String id){
        IApiClient apiClient = new ApiClient();
        IRemoveExperience executable = (result) ->{
            if(result.isSuccessful()){

                Toast.makeText(getContext(), "Deleted successfully!", Toast.LENGTH_LONG).show();
                // Refresh page
                Fragment fragment =  new ManageExperiencesFragment();
                getFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
            }
            else if(result.code() == 401)
            {
                Toast.makeText(getContext(), "Login please!", Toast.LENGTH_LONG).show();
                // Go to login page
                Fragment fragment =  new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
            }
            else
            {
                Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_LONG).show();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                apiClient.deleteExperience(id,executable);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setMessage("Are you sure you want to delete this experience?");

        AlertDialog d = builder.create();
        d.show();
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

                        mapExperiences.put(i.id,parts);
                    }
                    // print
                    listAllExperienceWithPersonalTemplate(mapExperiences);
                }else{
                    Toast.makeText(getContext(), responseModel.errors, Toast.LENGTH_LONG).show();
                }
            }
            else if(result.code() == 401)
            {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                try {
                    fragmentTransaction.replace(R.id.flContent, LoginFragment.class.newInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
                fragmentTransaction.commit();
            }
            else
            {
                Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_LONG).show();
            }
        };

        apiClient.getExperiences(executable);
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
