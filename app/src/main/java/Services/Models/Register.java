package Services.Models;

/**
 * Created by gxsha on 11/23/2018.
 */

public class Register {
    public String email;
    public String password;
    public String name;
    public String country;
    public String city;

    public Register(String email, String password, String name, String country, String city){
        this.email = email;
        this.password = password;
        this.name = name;
        this.country = country;
        this.city = city;
    }
}
