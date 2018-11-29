package Services.Models;

/**
 * Created by gxsha on 11/23/2018.
 */

public class Experience extends BaseExperience {
    public String id;
    public String userId;

    public Experience(String id, String code, String url, String userId) {
        super(code, url);
        this.id = id;
        this.userId = userId;
    }
}
