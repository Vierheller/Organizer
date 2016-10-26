package de.grau.organizer.classes;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 * Represents an action which is embedded in an event
 * Contains action type and data to perform an action
 */

public class Action extends RealmObject {
    public static final String TYPE_CALL = "phone-call";
    @PrimaryKey
    private String id;

    private String type;
    private String Data;

    /**
     * Default constructor
     * set id to an unique identifier
     */
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
