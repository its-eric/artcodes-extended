package Services;

import java.util.List;

import Services.Models.Experience;
import Services.Models.PublicExperience;

/**
 * Created by gxsha on 11/23/2018.
 */

public interface IApiClient {
    String login(String email, String password);
    String register(String email, String password, String name, String country, String city);
    List<PublicExperience> getAllExperiences();
    List<Experience> getExperiences();
    Experience getExperience(String id);
    String createExperience(String code, String url);
    String updateExperience(String id, String code, String url, String userId);
    String deleteExperience(String id);
}
