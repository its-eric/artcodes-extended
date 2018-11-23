package Services;

import java.util.List;

import Services.Models.CreateExperience;
import Services.Models.Experience;
import Services.Models.PublicExperience;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by gxsha on 11/23/2018.
 */

public interface ExperienceService {

    @GET("api/Experiences/getAll")
    Call<List<PublicExperience>> getAllExperiences();

    @GET("api/Experiences")
    Call<List<Experience>> getExperiences();

    @POST("api/Experiences")
    Call<Object> createExperience(@Body CreateExperience experience);

    @GET("api/Experiences/{id}")
    Call<Experience> getExperience(@Path("id") String id);

    @PUT("api/Experiences/{id}")
    Call<Object> updateExperience(@Path("id") String id, @Body Experience experience);

    @DELETE("api/Experiences/{id}")
    Call<Object> removeExperience(@Path("id") String id);
}
