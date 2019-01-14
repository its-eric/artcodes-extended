package hu.elte.inf.artcodesextended;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.ApiClient;
import Services.Authorization.AuthorizationHandler;
import Services.FunctionalInterfaces.IGetAll;
import Services.IApiClient;
import Services.Models.PublicExperience;
import Services.Models.ResponseModel;
import uk.ac.horizon.artcodes.model.Action;
import uk.ac.horizon.artcodes.model.Experience;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    public static Map<String, String> data = new HashMap<>();
    public static Experience experience = null;

    private DrawerLayout mDrawerLayout;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        mDrawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        FetchExperiences();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new MainFragment()).commit();



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        mItem = menuItem;
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                        Fragment fragment = null;

                        if(mItem == null){
                            return;
                        }
                        else{
                            mItem.setChecked(false);
                        }
                        switch (mItem.getItemId()){
                            case R.id.menu_home:
                                fragment = new MainFragment();
                                break;
                            case R.id.menu_login:
                                fragment = new LoginFragment();
                                break;
                            case R.id.menu_register:
                                fragment = new RegisterFragment();
                                break;
                            case R.id.menu_create:
                                if(checkPermission()){
                                    fragment = new CreateExperienceFragment();
                                }
                                else{
                                    Toast.makeText(context, "Login first", Toast.LENGTH_SHORT).show();
                                    fragment = new LoginFragment();
                                }
                                break;
                            case R.id.menu_browse:
                                fragment = new BrowseExperienceFragment();
                                break;
                            case R.id.menu_manage:
                                if(checkPermission()){
                                    fragment = new ManageExperiencesFragment();
                                }
                                else{
                                    Toast.makeText(context, "Login first", Toast.LENGTH_SHORT).show();
                                    fragment = new LoginFragment();
                                }
                                break;
                            case R.id.menu_about:
                                fragment = new AboutFragment();
                            default:
                                break;
                        }
                        handleMenuClick(fragment);
                        mItem = null;
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }
    private void handleMenuClick(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private boolean checkPermission(){
        return AuthorizationHandler.authToken != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new MainFragment()).commit();
        } else {
            getFragmentManager().popBackStack();
        }
        final FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.fab);
        if (cameraButton != null)
        {
            cameraButton.setEnabled(true);
        }
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

    public static void FetchExperiences(){
        IGetAll executable = (result) ->{
            if(result == null){
                Toast t = Toast.makeText(context, "Connection error", Toast.LENGTH_LONG);
                t.show();
            }
            else if (!result.isSuccessful()){
                Toast t = Toast.makeText(context, "Couldn't fetch experiences", Toast.LENGTH_LONG);
                t.show();
            }
            else {
                ResponseModel<List<PublicExperience>> responseModel = result.body();
                for (PublicExperience i : responseModel.result){
                    MainActivity.data.put(i.code,i.url);
                    System.out.println("Code: " + i.code + " Url:" + i.url);
                }
                // Create and configure an Artcode experience
                experience = new Experience();
                for (String code : MainActivity.data.keySet()) {
                    // Create Actions for the Artcodes you want to scan
                    Action action = new Action();
                    action.getCodes().add(code);
                    experience.getActions().add(action);
                    System.out.println("Found:" + code);
                }
                Toast.makeText(context, "Fetched " + responseModel.result.size(), Toast.LENGTH_LONG).show();
            }
        };
        IApiClient apiClient = new ApiClient();
        apiClient.getAllExperiences(executable);
    }
}
