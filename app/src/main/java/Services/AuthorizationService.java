package Services;

import Services.Models.Login;
import Services.Models.Register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gxsha on 11/23/2018.
 */

public interface AuthorizationService {
    @POST("Account/Login")
    Call<Object> login(@Body Login login);

    @POST("Account/Register")
    Call<String> register(@Body Register register);
}
