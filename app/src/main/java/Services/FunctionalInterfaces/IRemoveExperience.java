package Services.FunctionalInterfaces;

import Services.Models.ResponseModel;
import retrofit2.Response;

/**
 * Created by gxsha on 11/25/2018.
 */

@FunctionalInterface
public interface IRemoveExperience extends  IExecutable<Response<ResponseModel<String>>>{
}
