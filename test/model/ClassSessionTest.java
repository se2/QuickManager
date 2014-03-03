/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author SVC
 */
public class ClassSessionTest {

    public ClassSessionTest() {
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
     * Test of getTimeString method, of class ClassSession. viewcontroller makes
     * sure ClassSession last in only 1 day and cannot be empty
     */
    @Test
    public void testGetTimeString() {
        HashMap<Integer, Integer> times = new HashMap<>();
        times.put(5, 5);
        times.put(6, 6);
        times.put(7, 7);
        times.put(8, 8);
        times.put(9, 9);

        ClassSession cs = new ClassSession("Class 1", 1, times, true);
        assertEquals("17:15 - 18:30, Monday", cs.getTimeString());

        times = new HashMap<>();
        times.put(180, 180);
        times.put(181, 181);
        times.put(182, 182);

        cs.setTimes(times);
        assertEquals("17:00 - 17:45, Friday", cs.getTimeString());
    }

    @Test
    public void testPrintDetail() {
        HashMap<Integer, Integer> times = new HashMap<>();
        times.put(5, 5);
        times.put(6, 6);
        times.put(7, 7);
        times.put(8, 8);
        times.put(9, 9);
        
        ClassSession cs = new ClassSession("class 1", 1, times, true);
        assertEquals("class 1	,1	,Session2	,true\t,5\n", cs.printDetail());
        System.out.println("PASS ClassSessionPrintDetail");
    }
}
