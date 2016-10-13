package de.grau.organizer.classes;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 */

public class Notes extends RealmObject{
    @PrimaryKey
    private String id;
    private String text;
    public Notes() {
        if(this.id == null){
            this.id = UUID.randomUUID().toString();
        }
    }

    public String getId() { return id; }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
