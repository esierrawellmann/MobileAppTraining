package controllers;

import controllers.WSRequest.LogInRequest;
import models.Device;
import models.User;
import org.codehaus.jackson.map.ObjectMapper;
import play.libs.Crypto;
import play.mvc.Controller;


import java.io.IOException;
import java.util.List;

/**
 * Created by renatosierra on 7/23/14.
 */
public class WSLogIn extends Controller{
    public static void logIn(String data){
        boolean loginSucceed = false;
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
                newDevice.save();
            }

            if(loginSucceed){
                long userId = users.get(0).getId();
                render(loginSucceed,userId);
            }else{
                render(loginSucceed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
