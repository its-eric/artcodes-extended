package Services;

import Services.FunctionalInterfaces.ICreateExperience;
import Services.FunctionalInterfaces.IExecutable;
import Services.FunctionalInterfaces.IGetAll;
import Services.FunctionalInterfaces.IGetExperience;
import Services.FunctionalInterfaces.IGetExperiences;
import Services.FunctionalInterfaces.IRemoveExperience;
import Services.FunctionalInterfaces.IUpdateExperience;

/**
 * Created by gxsha on 11/23/2018.
 */

public interface IApiClient {
    void login(String email, String password, IExecutable executable);
    void register(String email, String password, String name, String country, String city, IExecutable executable);
    void getAllExperiences(IGetAll executable);
    void getExperiences(IGetExperiences executable);
    void getExperience(String id, IGetExperience executable);
    void createExperience(String code, String url, ICreateExperience executable);
    void updateExperience(String id, String code, String url, String userId, IUpdateExperience executable);
    void deleteExperience(String id, IRemoveExperience executable);
}
