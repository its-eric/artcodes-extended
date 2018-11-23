package Services;

import java.util.List;

import Services.Authorization.AuthorizationHandler;
import Services.Authorization.BaseClient;
import Services.Models.Experience;
import Services.Models.Login;
import Services.Models.PublicExperience;
import Services.Models.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gxsha on 11/23/2018.
 */

public class ApiClient extends BaseClient implements IApiClient {

    private String result;

    public String login(String email, String password) {
        AuthorizationService authorizationService = retrofit.create(AuthorizationService.class);

        Login loginModel = new Login(email,password);
        Call<Object> call = authorizationService.login(loginModel);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    result = "Login was successful";
                    System.out.println("Body:" + response.body());
                }
                else {
                    result = "Not successful. Code: "+ response.code();
                    System.out.println("Code:" + response.code());
                    System.out.println("Body:" + response.body());
                    System.out.println("Message:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        return result;
    }

    public String register(String email, String password, String name, String country, String city) {
        AuthorizationService authorizationService = retrofit.create(AuthorizationService.class);

        Register registerModel = new Register(email,password, name, country, city);
        Call<String> call = authorizationService.register(registerModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    result = "Register was successful";
                    System.out.println("Body:" + response.body());
                }
                else {
                    result = "Not successful.";
                    //System.out.println("Code:" + response.code());
                    //System.out.println("Body:" + response.body());
                    //System.out.println("Message:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result = "Failed";
                System.out.println(t.getMessage());
            }
        });

        return result;
    }

    public List<PublicExperience> getAllExperiences() {
        return null;
    }

    public List<Experience> getExperiences() {
        ExperienceService experienceService = AuthorizationHandler.createService(ExperienceService.class);
        Call<List<Experience>> call = experienceService.getExperiences();

        call.enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(Call<List<Experience>> call, Response<List<Experience>> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<List<Experience>> call, Throwable t) {
                result = "Get experiences failed";
            }
        });

        return null;
    }

    public Experience getExperience(String id) {
        return null;
    }

    public String createExperience(String code, String url) {
        return null;
    }

    public String updateExperience(String id, String code, String url, String userId) {
        return null;
    }

    public String deleteExperience(String id) {
        return null;
    }
}
