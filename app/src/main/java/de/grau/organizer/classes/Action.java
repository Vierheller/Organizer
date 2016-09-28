package de.grau.organizer.classes;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 */

public class Action extends RealmObject {
    @PrimaryKey
    private String id;

    private String type;
    private String Data;

    public Action(){
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
