package Services.FunctionalInterfaces;

import java.util.List;

import Services.Models.PublicExperience;
import Services.Models.ResponseModel;
import retrofit2.Response;

/**
 * Created by gxsha on 11/25/2018.
 */

@FunctionalInterface
public interface IGetAll extends IExecutable<Response<ResponseModel<List<PublicExperience>>>>{
}
