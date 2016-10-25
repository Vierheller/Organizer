package de.grau.organizer.classes;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Represents a category
 *
 */

public class Tag extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;

    /**
     * Default constructor
     * Sets a unique id upon calling
     * @param name
     */
    public Tag(String name) {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }

        this.name = name;
    }

    /**
     * Default constructor
     * Sets a unique id upon calling
     */
    public Tag() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
