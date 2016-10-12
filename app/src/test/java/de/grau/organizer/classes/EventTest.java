package de.grau.organizer.classes;

import android.provider.CalendarContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Christian on 12.10.2016.
 */
public class EventTest {
    private Event testEvent;
    private Date startDate;
    private Date endDate;
    @Mock
    Event mockedEvent;
    @Before
    public void setUp(){
        testEvent = new Event();
        startDate = new Date();
        endDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(2016,11,15,12,15,10);
        startDate.setTime(cal.getTime().getTime());
        cal.set(2016,11,15,12,20,10);
        endDate.setTime(cal.getTime().getTime());
    }

    @Test
    public void get_setName() throws Exception {
        testEvent.setName("abba");
        assertEquals("abba", testEvent.getName());
    }

    @Test
    public void get_setDescription() throws Exception {
        testEvent.setDescription("This is a test Description");
        assertEquals("This is a test Description", testEvent.getDescription());
    }

    @Test
    public void get_setPriority() throws Exception {
        testEvent.setPriority(10);
        assertEquals(10, testEvent.getPriority());
    }

    @Test
    public void get_setStart() throws Exception {
        testEvent.setStart(startDate);
        assertEquals(startDate, testEvent.getStart());
    }


    @Test
    public void get_setEnd() throws Exception {
        testEvent.setEnd(endDate);
        assertEquals(endDate, testEvent.getEnd());
    }

    @Test
    public void getNotificationTime() throws Exception {
        testEvent.setNotificationTime(10);
        assertEquals(10, testEvent.getNotificationTime());
    }

    @Test
    public void getAction() throws Exception {
    Action action = new Action();
        testEvent.setAction( action);
        assertEquals(action, testEvent.getAction());
    }

    @Test
    public void setAction() throws Exception {

    }

    @Test
    public void getNotes() throws Exception {

    }

    @Test
    public void setNotes() throws Exception {

    }

    @Test
    public void putNote() throws Exception {

    }

    @Test
    public void getTags() throws Exception {

    }

    @Test
    public void setTags() throws Exception {

    }

    @Test
    public void getCategory() throws Exception {

    }

    @Test
    public void setCategory() throws Exception {

    }

    @Test
    public void addNote() throws Exception {

    }

    @Test
    public void putNotes() throws Exception {

    }

}