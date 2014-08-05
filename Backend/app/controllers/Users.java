package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.Rol;
import models.User;
import org.codehaus.jackson.map.ObjectMapper;
import play.Logger;
import play.libs.Crypto;
import play.mvc.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renatosierra on 7/28/14.
 */
public class Users extends Controller {
    public static void index(){
        render();
    }
    public static void getUsers(){
        List<User> users = User.findAll();
        List<Rol> roles = Rol.findAll();
        Map<String,Object>  initialData = new HashMap<String, Object>();
        initialData.put("users",users);
        initialData.put("roles",roles);

        renderJSON(initialData);
    }
    public static void addUsers(String data){
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject)parser.parse(data);
        String name = object.get("name").getAsString();
        String password = object.get("password").getAsString();
        String status = object.get("status").getAsString();
        JsonObject rol = object.get("rol").getAsJsonObject();
        long rolId = rol.get("id").getAsLong();

        User user = new User();
        user.setUsername(name);
        user.setPassword(Crypto.encryptAES(password));
        user.setStatus(status);

        Rol userRol = Rol.findById(rolId);
        user.setRol(userRol);
        user.save();


        renderJSON(user);
    }


}
