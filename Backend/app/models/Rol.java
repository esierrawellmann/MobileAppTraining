package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by renatosierra on 7/23/14.
 */
@Entity
public class Rol extends Model {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required
    @Column(nullable=false)
    String name;

    public List<Rol_Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Rol_Permission> permissions) {
        this.permissions = permissions;
    }
    @OneToMany(mappedBy = "rol",cascade = CascadeType.PERSIST )
    List<Rol_Permission> permissions;

}
