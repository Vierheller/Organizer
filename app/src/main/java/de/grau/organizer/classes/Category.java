package de.grau.organizer.classes;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Represents a category of an event
 *
 */

public class Category extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private boolean defaultCategory;

    /**
     * Default constructor
     * Sets a unique id upon calling
     */
    public Category() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.defaultCategory = false;
    }

    public Category(String name) {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.defaultCategory = false;
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

    public void setDefaultCategory(boolean value) {
        this.defaultCategory = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Category)) {
            return false;
        } else if (this.id.equals(((Category) obj).getID())) {
            return true;
        } else {
            return false;
        }
    }
}
