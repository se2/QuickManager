/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class ClassTest {

    public ClassTest() {
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
     * Test of getTuitionFeeString method, of class Class.
     */
    @Test
    public void testGetTuitionFeeString() {
        ClassType ct1 = new ClassType("Piano", ClassType.SINGLE, 1, 12000, 16000, null);
        ClassType ct2 = new ClassType("Organ", ClassType.DUAL, 2, 0, 0, null);

        model.Class c = new Class("", null, null, "text", ct1, 28000);
        assertEquals("VND 28,000", c.getTuitionFeeString());

        c = new Class("", null, null, "text", ct2, 0);
        assertEquals("VND 0", c.getTuitionFeeString());
    }

    @Test
    public void testPrintDetail() {
        ClassType ct = new ClassType("piano", 1, 2, 12000, 15000, null);
        Class cl = new Class("class 1", new LocalDate(2013, 5, 1), new LocalDate(2013, 7, 1), "intro", ct, 60000);
        assertEquals(cl.getClassId() + "	,class 1	,2013-05-01	,2013-07-01	,0	,60000	,intro	,piano-1\n", cl.printDetail());
        System.out.println("PASS ClassPrintDetail");
    }
}
