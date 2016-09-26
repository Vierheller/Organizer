package de.grau.organizer.Classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 */

public class Notes extends RealmObject{
    @PrimaryKey
    private long id;
    private String text;
}
