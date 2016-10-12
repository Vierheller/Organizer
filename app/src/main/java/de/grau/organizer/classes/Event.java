package de.grau.organizer.classes;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private String id;
    private String name, description;
    private int priority;

    private Date start,end;
    private int notificationTime; //Seconds before event.start to notify

    private Action action;  //List of actions corresponding to an Event instance
    private RealmList<Notes> notes;     //List of all notes corresponding to an Event instance
    private RealmList<Tag> tags;        //""
    private Category category;

    public Event(){
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.notes = new RealmList<Notes>();
        this.priority = 4;      // default value
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public RealmList<Notes> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Notes> notes) {
        this.notes = notes;
    }

    public void putNote(Notes note){
        notes.add(note);
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public void addNote(Notes note){
        this.notes.add(note);
    }


    public void putNotes(List<Notes> notes) {
        this.notes.addAll(notes);
    }
}
