package Services.FunctionalInterfaces;

import Services.Models.Experience;
import Services.Models.ResponseModel;
import retrofit2.Response;

/**
 * Created by gxsha on 11/25/2018.
 */

@FunctionalInterface
public interface IUpdateExperience extends IExecutable<Response<ResponseModel<Experience>>> {
}
