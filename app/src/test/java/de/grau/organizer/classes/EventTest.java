package de.grau.organizer.classes;

import android.provider.CalendarContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;

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
    public void get_setNotes() throws Exception {
        Notes node = new Notes();
        RealmList<Notes> list = new RealmList<>();
        list.add(node);
        testEvent.setNotes(list);
        assertEquals(list, testEvent.getNotes());
    }


    @Test
    public void putNote() throws Exception {
        Notes node = new Notes();
        testEvent.putNote(node);
        assertEquals(node, testEvent.getNotes().get(0));
    }

    @Test
    public void get_setTags() throws Exception {
        Tag tag = new Tag();
        RealmList<Tag> list = new RealmList<>();
        list.add(tag);
        testEvent.setTags(list);
        assertEquals(list, testEvent.getTags());
    }

    @Test
    public void getCategory() throws Exception {
        Category category = new Category();
        testEvent.setCategory(category);
        assertEquals(category, testEvent.getCategory());
    }


    @Test
    public void addNote() throws Exception {
        Notes node = new Notes();
        testEvent.addNote(node);
        assertEquals(node, testEvent.getNotes().get(0));
    }

    @Test
    public void putNotes() throws Exception {
        Notes node1 = new Notes();
        Notes node2 = new Notes();
        Notes node3 = new Notes();
        List<Notes> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        testEvent.putNotes(list);
        assertEquals(list, testEvent.getNotes());

    }
    @Test
    public void testToString() throws Exception{
        testEvent.setDescription("This is a test");
        testEvent.setStart(startDate);
        testEvent.setEnd(endDate);
        testEvent.setName("Test");
        assertEquals("Test: This is a test starts at " + startDate + " and ends on " + endDate, testEvent.toString());
    }
    @Test
    public void testGetStartYearMonthDay(){
        testEvent.setStart(startDate);
        assertEquals(2016, testEvent.getStartYear());
        assertEquals(11, testEvent.getStartMonth());
        assertEquals(15, testEvent.getStartDay());
    }

}