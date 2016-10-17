package de.grau.organizer.classes;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ubuntu on 9/22/16.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;

    public Category() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public Category(String name) {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }

        this.name = name;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
