/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.joda.time.LocalDate;
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
public class TeacherTest {

    public TeacherTest() {
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
     * Test of getSkillString method, of class Teacher. viewcontroller makes
     * sure there is at least one skill teacher has
     */
    @Test
    public void testGetSkillString() {
        String skills[][] = {{"Piano", "100"}, {"Guitar", "200"}, {"Hip-hop", "300"}};

        Teacher instance = new Teacher(skills, null, null, null, null, null,
                null, null, null, true);
        String expResult = "Piano, Guitar, Hip-hop";
        String result = instance.getSkillString();
        assertEquals(expResult, result);
        System.out.println("PASS ALL");
    }

    @Test
    public void testPrintDetail() {
        String[][] skills = {{"piano", "100"}, {"guitar", "125"}};
        Teacher instance = new Teacher(skills, "quang", "", "tran", "quang.tran@rmit.edu.vn", "0908872263", new LocalDate(1989, 1, 1), "hcmc", null, true);
        assertEquals("1305002	,tran quang	,true	,1989-01-01	,piano-100 guitar-125	,quang.tran@rmit.edu.vn	,0908872263	,hcmc\n", instance.printDetail());
        System.out.println("PASS teacherPrintDetail");
    }
}
