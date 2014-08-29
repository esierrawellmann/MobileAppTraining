package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.Rol;
import models.User;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.libs.Crypto;
import play.mvc.Controller;

import java.io.*;
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
    public static  void editUsers(User data){
        User updatedUser = User.findById(data.id);
        updatedUser.save();
        renderJSON(updatedUser);
    }
    public static  void testFunction(User data){
        Logger.info(data.getUsername());
        Logger.info(Crypto.decryptAES(data.getPassword()));
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(request.body, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String theString = writer.toString();
        Logger.info(theString);
        renderJSON("{\"data\":\"olakease\"}");
    }
    public static void addUsers(String data){
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject)parser.parse(data);
        String name = object.get("name").getAsString();
        String password = object.get("password").getAsString();
        int status = object.get("status").getAsInt();
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
