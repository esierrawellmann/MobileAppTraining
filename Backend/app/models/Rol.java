package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

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
}
