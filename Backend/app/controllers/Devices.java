package controllers;

import models.Device;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renatosierra on 7/29/14.
 */
public class Devices extends Controller{
    public static void index() {
        List<Device> devices = Device.findAll();

        render(devices);

    }
    public static void getDevices(){

    }
}
