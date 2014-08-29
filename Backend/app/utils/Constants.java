package utils;

/**
 * Created by renatosierra on 7/24/14.
 */
public class  Constants {
    public static final int TRANSACTION_STATUS_IN_PROGRESS = 0;
    public static final int TRANSACTION_STATUS_FAILED = 1;
    public static final int TRANSACTION_STATUS_SUCCESS = 2;

    public static final String REQUEST_URL_GET_DAY_EXCHANGE_RATE="Services/getexchangerate";
    public static final String REQUEST_URL_GET_EXCHANGE_RATE_BY_DATE="Services/getexchangeratebydate";

    public static final long TRANSACTION_TYPE_GET_DAY_EXCHANGE_RATE=1;
    public static final long TRANSACTION_TYPE_GET_EXCHANGE_RATE_BY_DATE=2;
    public static final long TRANSACTION_TYPE_GET_VARIABLES=3;
    public static final long TRANSACTION_TYPE_LOG_IN=4;

    public static final long  ERROR_TYPE_NO_RESPONSE_FROM_SERVER=1;
    public static final int USER_STATES_BLOCKED=0;
    public static final int USER_STATES_ACTIVE=1;
    public static final int USER_STATES_NOT_ACTIVE = 2;
}
