package Services.FunctionalInterfaces;

import java.util.List;

import Services.Models.Experience;
import Services.Models.ResponseModel;
import retrofit2.Response;


/**
 * Created by gxsha on 11/25/2018.
 */

@FunctionalInterface
public interface IGetExperiences extends IExecutable<Response<ResponseModel<List<Experience>>>>  {

}
