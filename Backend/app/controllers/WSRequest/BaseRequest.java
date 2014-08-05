package controllers.WSRequest;

/**
 * Created by renatosierra on 7/21/14.
 */
public class BaseRequest {
    String deviceOS;
    String deviceOSVersion;
    String deviceUID;
    String appVersion;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getDeviceUID() {
        return deviceUID;
    }

    public void setDeviceUID(String deviceUID) {
        this.deviceUID = deviceUID;
    }


}
