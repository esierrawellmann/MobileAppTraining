package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by renatosierra on 7/16/14.
 */
@Entity
public class User extends Model {
    @Column(nullable=true)
    String username;
    @Column(nullable=true)
    String password;
    @Column(nullable=true)
    int status;
    @Required
    @ManyToOne
    @JoinColumn(nullable=false)
    Rol rol;

    public List<User_Permission> getPermission() {
        return permission;
    }

    public void setPermission(List<User_Permission> permission) {
        this.permission = permission;
    }

    @OneToMany(mappedBy = "permission",cascade = CascadeType.PERSIST )
    List<User_Permission> permission;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
