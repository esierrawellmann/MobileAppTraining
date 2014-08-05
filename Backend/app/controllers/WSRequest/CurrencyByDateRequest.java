package controllers.WSRequest;

/**
 * Created by renatosierra on 7/21/14.
 */
public class CurrencyByDateRequest extends BaseRequest{
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    int option;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    long userId;
}
