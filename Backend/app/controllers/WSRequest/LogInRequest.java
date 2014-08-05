package controllers.WSRequest;

/**
 * Created by renatosierra on 7/24/14.
 */
public class LogInRequest extends BaseRequest {
    String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String password;

}
