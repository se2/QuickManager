/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SVC
 */
public class StudentTest {

    public StudentTest() {
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

    @Test
    public void testConstructor() {
        LocalDate today = new LocalDate();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyMM");
        String todayString = dtf.print(today);
        Student.setCurrentId(1);
        
        Student s = new Student("", "", "", "", "", "", "", "", "", "", today, "", null, true);
        assertEquals(todayString + "001", s.getId());

        s = new Student("", "", "", "", "", "", "", "", "", "", today, "", null, true);
        assertEquals(todayString + "002", s.getId());

        // force Person to reset currentId
        LocalDate prevMonth = new LocalDate(2012, 1, 1);//any date in the past
        Person.setPreviousMonth(prevMonth);

        s = new Student("", "", "", "", "", "", "", "", "", "", today, "", null, true);
        assertEquals(todayString + "001", s.getId());
        System.out.println("PASS testConstructor");
    }

    @Test
    public void testToString() {
        Student instance = new Student("0909941192", "ductu@zing.vn", "tu dad", "binh duong", "dad", "duc", "", "tu", "ductu@gmail.com", "0901928376", new LocalDate(1992, 1, 1), "hcmc", null, true);
        assertEquals(instance.getId() + " tu duc ductu@gmail.com 0901928376 01 01 1992 hcmc tu dad ductu@zing.vn 0909941192 binh duong null", instance.toString());
        System.out.println("PASS toString");
    }

    @Test
    public void testPrintDetail() {
        Student instance = new Student("0909941192", "ductu@zing.vn", "tu dad", "binh duong", "dad", "duc", "", "tu", "ductu@gmail.com", "0901928376", new LocalDate(1992, 1, 1), "hcmc", null, true);
        
        assertEquals("1305002	,tu duc	,true	,1992-01-01	,ductu@gmail.com	,0901928376	,hcmc\t,tu dad	,null	,ductu@zing.vn	,0909941192	,binh duong\n", instance.printDetail());
        System.out.println("PASS prinDetail");
        
    }
}
