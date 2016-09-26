package de.grau.organizer.Classes;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 * Base Class of an Event, it holds all needed Data to work with an particular Event
 * Both actions and notes are lists of RealmObjects (1..n)
 */

public class Event extends RealmObject{
    @PrimaryKey
    private long id;
    private String name, description;
    private int priority;

    private Date start,end;
    private int notificationTime; //Seconds before event.start to notify

    private RealmList<Action> actions;  //List of actions corresponding to an Event instance
    private RealmList<Notes> notes;     //List of all notes corresponding to an Event instance
    private RealmList<Tag> tags;        //""
    private Category category;

}
