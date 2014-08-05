package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by renatosierra on 7/24/14.
 */
@Entity
public class TransactionType extends Model{
    @Column(nullable=false)
    String name;
    @Column(nullable=false)
    long code;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
