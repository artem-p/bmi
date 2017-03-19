package ru.artempugachev.bmi;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test for bmi computing
 */

public class PersonTest {
    @Test
    public void bmi_wrong_parameters() {
        Person person = new Person(0, 0);
        assertEquals(person.getBmi(), "0");
    }


    @Test
    public void bmi_normal() {
        Person person = new Person(178, 73);
        assertEquals(person.getBmi(), "23.0");
    }
}
