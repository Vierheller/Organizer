package de.grau.organizer.classes;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Christian on 13.10.2016.
 */
public class TagTest {
    @Test
    public void get_SetName() throws Exception {
        Tag tag = new Tag();
        tag.setName("This is a test");
        assertEquals("This is a test", tag.getName());
        Tag tag2 = new Tag("This is a second test");
        assertEquals("This is a second test", tag2.getName());
    }

}