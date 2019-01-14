package hu.elte.inf.artcodesextended;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.horizon.artcodes.scanner.ScannerActivity;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {

    private final int ARTCODE_REQUEST = 12345; // Range: 0-65535
    private View view;
    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        this.view = view;

        if(MainActivity.data == null){
            MainActivity.FetchExperiences();
        }
        final ImageView refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.FetchExperiences();
            }
        });

        // Set a login to open the Artcodes Scanner
        final FloatingActionButton cameraButton = (FloatingActionButton) view.findViewById(R.id.fab);
        if (cameraButton != null) {
            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    synchronized (MainActivity.context) {
                        cameraButton.setEnabled(false);

                        // Create and setup the intent that will launch the Artcode scanner
                        Intent intent = new Intent(MainActivity.context, ScannerActivity.class);

                        // Put experience in intent
                        Gson gson = new GsonBuilder().create();
                        intent.putExtra("experience", gson.toJson(MainActivity.experience));

                        // Start artcode reader activity
                        startActivityForResult(intent, ARTCODE_REQUEST);
                    }
                }
            });
        }

        return  view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.print("Request Code:" + requestCode);
        System.out.print("Result Code:" + resultCode);
        if (requestCode == ARTCODE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                // This is the code of the Artcode that was found (e.g. "1:1:1:1:2")
                String code = data.getStringExtra("marker");

                // Do any logic based on the result here, for example display the code in a TextView
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MainActivity.data.get(code)));
                startActivity(intent);
                final FloatingActionButton cameraButton = (FloatingActionButton) this.view.findViewById(R.id.fab);
                if (cameraButton != null)
                {
                    cameraButton.setEnabled(true);
                }
            }
        }
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
