package model;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dam Linh
 */
public class PersonTest {

    public PersonTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFullname method, of class Person. viewcontroller will make
     * sure there are at least first name and last name middlename may be empty
     */
    @Test
    public void testGetFullname() {
        Person instance = new Person("Dam", "Hong", "Linh", "damlinh@yahoo.com", "", null, null, null, true) {
        };
        assertEquals("Linh Hong Dam", instance.getFullname());

        instance = new Person("Dam", "Hong hong Hong", "Linh", "damlinh@yahoo.com", "", null, null, null, true) {
        };
        assertEquals("Linh Hong hong Hong Dam", instance.getFullname());

        instance = new Person("Dam", "", "Linh", "damlinh@yahoo.com", "", null, null, null, true) {
        };
        assertEquals("Linh Dam", instance.getFullname());
    }

    /**
     * Test of generateId method, of class Person.
     */
    @Test
    public void testGenerateId() {
        LocalDate someDate = new LocalDate();

        someDate = someDate.withMonthOfYear(2).withYear(2000);
        Person.setPreviousMonth(someDate);
        assertEquals("1305001", Person.generateId());

        someDate = someDate.withMonthOfYear(12).withYear(1998);
        Person.setPreviousMonth(someDate);
        assertEquals("1305001", Person.generateId());

        //current month is same as previous month reseting id is not called
        someDate = someDate.withMonthOfYear(4).withYear(2013);
        Person.setPreviousMonth(someDate);
        assertEquals("1305001", Person.generateId());
    }
}
