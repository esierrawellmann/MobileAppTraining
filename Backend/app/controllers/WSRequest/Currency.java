package controllers.WSRequest;

/**
 * Created by renatosierra on 7/31/14.
 */
public class Currency extends BaseRequest{
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    long userId;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    int option;
}
