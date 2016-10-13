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

    public String getID() {
        return this.id;
    }
}
