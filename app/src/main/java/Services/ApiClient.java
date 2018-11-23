package Services;

import java.util.List;

import Services.Authorization.AuthorizationHandler;
import Services.Authorization.BaseClient;
import Services.Models.CreateExperience;
import Services.Models.Experience;
import Services.Models.Login;
import Services.Models.PublicExperience;
import Services.Models.Register;
import Services.Models.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gxsha on 11/23/2018.
 */

public class ApiClient extends BaseClient implements IApiClient {

    private Object result;
    private AuthorizationService authService;
    private ExperienceService experienceService;

    public ApiClient(){
        this.authService = retrofit.create(AuthorizationService.class);
        this.experienceService = AuthorizationHandler.createService(ExperienceService.class);
    }

    public String login(String email, String password) {
        Login loginModel = new Login(email,password);
        Call<ResponseModel<String>> call = authService.login(loginModel);
        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
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
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        return (String)result;
    }

    public String register(String email, String password, String name, String country, String city) {
        Register registerModel = new Register(email,password, name, country, city);
        Call<ResponseModel<String>> call = authService.register(registerModel);
        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
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
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                result = "Failed";
                System.out.println(t.getMessage());
            }
        });

        return (String)result;
    }

    public List<PublicExperience> getAllExperiences() {
        Call<ResponseModel<List<PublicExperience>>> call = experienceService.getAllExperiences();

        call.enqueue(new Callback<ResponseModel<List<PublicExperience>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<PublicExperience>>> call, Response<ResponseModel<List<PublicExperience>>> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel<List<PublicExperience>>> call, Throwable t) {

            }
        });
        return (List<PublicExperience>)result;
    }

    public List<Experience> getExperiences() {
        Call<ResponseModel<List<Experience>>> call = experienceService.getExperiences();

        call.enqueue(new Callback<ResponseModel<List<Experience>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<Experience>>> call, Response<ResponseModel<List<Experience>>> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel<List<Experience>>> call, Throwable t) {

            }
        });

        return (List<Experience>)result;
    }

    public Experience getExperience(String id) {
        Call<ResponseModel<Experience>> call = experienceService.getExperience(id);
        call.enqueue(new Callback<ResponseModel<Experience>>() {
            @Override
            public void onResponse(Call<ResponseModel<Experience>> call, Response<ResponseModel<Experience>> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel<Experience>> call, Throwable t) {

            }
        });

        return (Experience)result;
    }

    public String createExperience(String code, String url)
    {
        CreateExperience model = new CreateExperience(code, url);
        Call<ResponseModel<Experience>> call = experienceService.createExperience(model);
        call.enqueue(new Callback<ResponseModel<Experience>>() {
            @Override
            public void onResponse(Call<ResponseModel<Experience>> call, Response<ResponseModel<Experience>> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel<Experience>> call, Throwable t) {

            }
        });
        return (String)result;
    }

    public String updateExperience(String id, String code, String url, String userId) {
        Experience experience = new Experience(id, code, url, userId);
        Call<ResponseModel<String>> call = experienceService.updateExperience(id, experience);
        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {

            }
        });
        return (String)result;
    }

    public String deleteExperience(String id) {
        Call<ResponseModel<Experience>> call = experienceService.removeExperience(id);
        call.enqueue(new Callback<ResponseModel<Experience>>() {
            @Override
            public void onResponse(Call<ResponseModel<Experience>> call, Response<ResponseModel<Experience>> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel<Experience>> call, Throwable t) {

            }
        });
        return (String)result;
    }
}
