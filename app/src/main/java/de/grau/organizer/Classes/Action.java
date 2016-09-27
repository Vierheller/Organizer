package de.grau.organizer.classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 */

public class Action extends RealmObject {
    @PrimaryKey
    private long id;
    private String type;
    private String Data;

}
