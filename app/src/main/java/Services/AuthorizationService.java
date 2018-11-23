package Services;

import Services.Models.Login;
import Services.Models.Register;
import Services.Models.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gxsha on 11/23/2018.
 */

public interface AuthorizationService {
    @POST("Account/Login")
    Call<ResponseModel<String>> login(@Body Login login);

    @POST("Account/Register")
    Call<ResponseModel<String>> register(@Body Register register);
}
