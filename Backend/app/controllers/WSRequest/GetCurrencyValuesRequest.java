package controllers.WSRequest;

/**
 * Created by renatosierra on 7/31/14.
 */
public class GetCurrencyValuesRequest extends BaseRequest {
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    long userId;


}
