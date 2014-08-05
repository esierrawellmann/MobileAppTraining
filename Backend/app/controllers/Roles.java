package controllers;

import models.Rol;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by renatosierra on 7/29/14.
 */
public class Roles extends Controller{
    public static void index() {

        render();
    }
    public static void getRoles(){
        List<Rol> roles = Rol.findAll();
        renderJSON(roles);
    }
    public static void addNewRoleAction(String rolName){
        Rol rol = new Rol();
        rol.setName(rolName);
        rol.save();
        renderJSON(rol);
    }

}
