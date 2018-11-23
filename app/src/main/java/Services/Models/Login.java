package Services.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gxsha on 11/23/2018.
 */

public class Login {
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;

    public Login(String email, String password){
        this.email = email;
        this.password = password;
    }
}
