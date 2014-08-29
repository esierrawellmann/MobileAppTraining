package controllers;

import controllers.WSRequest.LogInRequest;
import models.Device;
import models.Transaction;
import models.TransactionType;
import models.User;
import org.codehaus.jackson.map.ObjectMapper;
import play.Logger;
import play.libs.Crypto;
import play.mvc.Controller;
import utils.Constants;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by renatosierra on 7/23/14.
 */
public class WSLogIn extends Controller{
    public static void logIn(String data){
        long now = Calendar.getInstance().getTimeInMillis();
        boolean loginSucceed = false;

        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setStatus(Constants.TRANSACTION_STATUS_IN_PROGRESS);
        //Finding Transaction Type by Transaction code
        TransactionType transactionType = TransactionType.find("code = ?",Constants.TRANSACTION_TYPE_LOG_IN).first();
        transaction.setTransactionType(transactionType);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LogInRequest logInRequest = objectMapper.readValue(data, LogInRequest.class);
            List<User> users = User.find("username = ? and password = ?", logInRequest.getUsername(), Crypto.encryptAES(logInRequest.getPassword())).fetch();
            loginSucceed = users.size() >0 ;

            Device device = Device.find("deviceId = ? ",logInRequest.getDeviceUID()).first();

            if(users.size() >0 && device == null){
                Device newDevice = new Device();
                newDevice.setDeviceId(logInRequest.getDeviceUID());
                newDevice.setOs(logInRequest.getDeviceOS());
                newDevice.setAppVersion(logInRequest.getAppVersion());
                newDevice.setDeviceOSVersion(logInRequest.getDeviceOSVersion());
                newDevice.setUser(users.get(0));
                transaction.setDevice(newDevice);
                newDevice.save();
            }else{
                if(device !=null){
                    transaction.setDevice(device);
                }
            }
            transaction.setRequest(data);
            transaction.setResponseTime(Calendar.getInstance().getTimeInMillis() - now);
            transaction.setStatus(Constants.TRANSACTION_STATUS_SUCCESS);
            if(loginSucceed){
                long userId = users.get(0).getId();
                transaction.setUser(users.get(0));
                transaction.setResponse("{\n" +
                        "    \"successful\":"+true+",\n" +
                        "    \"data\":{\n" +
                        "        \"loginSucceed\":"+loginSucceed+"\n" +
                        "        , \"userId\":"+userId+"\n" +
                        "    }\n" +
                        "}");
                transaction.save();
                render(loginSucceed,userId);
            }else{
                transaction.setResponse("{\n" +
                        "    \"successful\":"+true+",\n" +
                        "    \"data\":{\n" +
                        "        \"loginSucceed\":"+loginSucceed+"\n" +
                        "    }\n" +
                        "}");

                transaction.save();
                render(loginSucceed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
