package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by renatosierra on 7/23/14.
 */
@Entity
public class Device extends Model {
    @Required
    @Column(nullable=false)
    String os;
    @Required
    @Column(nullable=false)
    String deviceId;

    String deviceOSVersion;

    String appVersion;
    @Required
    @ManyToOne
    @JoinColumn(nullable=false)
    User user;


    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
