package de.grau.organizer.classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ubuntu on 9/22/16.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;

    public long getID() {
        return this.id;
    }
}
