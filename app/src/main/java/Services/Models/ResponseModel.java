package Services.Models;

/**
 * Created by gxsha on 11/23/2018.
 */

public class ResponseModel<T> {
    public boolean success;
    public T result;
    public String errors;
}
