package Services;
import java.util.List;

import Services.Authorization.AuthorizationHandler;
import Services.Authorization.BaseClient;
import Services.FunctionalInterfaces.ICreateExperience;
import Services.FunctionalInterfaces.IExecutable;
import Services.FunctionalInterfaces.IGetAll;
import Services.FunctionalInterfaces.IGetExperience;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.FunctionalInterfaces.IRemoveExperience;
import Services.FunctionalInterfaces.IUpdateExperience;
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

    private AuthorizationService authService;
    private ExperienceService experienceService;

    public ApiClient(){
        this.authService = retrofit.create(AuthorizationService.class);
        this.experienceService = AuthorizationHandler.createService(ExperienceService.class);
    }

    private <T> void ExecuteCall(Call<T> call, final IExecutable executable){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                executable.Execute(response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                executable.Execute(null);
            }
        });
    }

    public void login(String email, String password, final IExecutable executable) {
        final Login loginModel = new Login(email,password);

        Call<ResponseModel<String>> call = authService.login(loginModel);
        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                ResponseModel<String> responseModel = response.body();
                if(response.isSuccessful() && responseModel.success){
                    AuthorizationHandler.authToken = responseModel.result;
                    executable.Execute("Login was successful");
                }
                else {
                    executable.Execute(responseModel.errors);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                executable.Execute(null);
            }
        });
    }

    public void register(String email, String password, String name, String country, String city, final IExecutable executable) {
        Register registerModel = new Register(email,password, name, country, city);
        Call<ResponseModel<String>> call = authService.register(registerModel);
        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                ResponseModel<String> responseModel = response.body();
                if(response.isSuccessful() && responseModel.success){
                    AuthorizationHandler.authToken = responseModel.result;
                    executable.Execute("Registration was successful");
                }
                else {
                    executable.Execute(responseModel.errors);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                executable.Execute(null);
            }
        });
    }

    public void getAllExperiences(IGetAll executable) {
        Call<ResponseModel<List<PublicExperience>>> call = experienceService.getAllExperiences();
        ExecuteCall(call, executable);
    }

    public void getExperiences(IGetExperiences executable) {
        Call<ResponseModel<List<Experience>>> call = experienceService.getExperiences();
        ExecuteCall(call, executable);
    }

    public void getExperience(String id, IGetExperience executable) {
        Call<ResponseModel<Experience>> call = experienceService.getExperience(id);
        ExecuteCall(call, executable);
    }

    public void createExperience(String code, String url, ICreateExperience executable)
    {
        CreateExperience model = new CreateExperience(code, url);
        Call<ResponseModel<Experience>> call = experienceService.createExperience(model);
        ExecuteCall(call, executable);
    }

    public void updateExperience(String id, String code, String url, String userId, IUpdateExperience executable) {
        Experience experience = new Experience(id, code, url, userId);
        Call<ResponseModel<String>> call = experienceService.updateExperience(id, experience);
        ExecuteCall(call, executable);
    }

    public void deleteExperience(String id, IRemoveExperience executable) {
        Call<ResponseModel<Experience>> call = experienceService.removeExperience(id);
        ExecuteCall(call, executable);
    }
}
