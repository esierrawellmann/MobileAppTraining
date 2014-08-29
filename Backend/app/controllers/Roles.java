package controllers;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import models.Rol;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import play.Logger;
import play.mvc.Controller;

import javax.persistence.PersistenceException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by renatosierra on 729/14.
 */
public class Roles extends Controller{
    public static void index() {

        render();
    }
    public static void getRoles(){
        List<Rol> roles = Rol.findAll();
        render(roles);
    }
    public static void deleteRol(){
        ObjectMapper objectMapper = new ObjectMapper();
        Rol rol=null;
        try{
            rol = objectMapper.readValue(request.body,Rol.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Rol updatedRol = Rol.findById(rol.id);
        try{
            updatedRol.delete();
            renderJSON("{\"success\":true}");
        }catch(PersistenceException exception){
            renderJSON("{\"success\":false}");
        }


    }
    public static void addNewRoleAction(String rolName){
        Rol rol = new Rol();
        rol.setName(rolName);
        rol.save();
        renderJSON(rol);
    }
    public static void updateRol(){
        ObjectMapper objectMapper = new ObjectMapper();
        Rol rol=null;
        try{
            rol = objectMapper.readValue(request.body,Rol.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Rol updatedRol = Rol.findById(rol.id);
        updatedRol.setName(rol.getName());
        updatedRol.save();
        renderJSON(updatedRol);

    }

}
