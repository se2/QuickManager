/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import org.joda.time.LocalDate;
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
public class PaySlipTest {
    
    public PaySlipTest() {
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
     * Test of printDetail method, of class PaySlip.
     */
    @Test
    public void testPrintDetail() {
        System.out.println("printDetail");
        String[][] skills = {{"piano", "100"}, {"guitar", "125"}};
        Teacher tc1 = new Teacher(skills, "quang", "", "tran", "quang.tran@rmit.edu.vn", "0908872263", new LocalDate(1989, 1, 1), "hcmc", null, true);
        
        HashMap<String, Long> listClasses = new HashMap<>();
        listClasses.put("class 1", (long) 100000);
        listClasses.put("class 2", (long) 200000);
        listClasses.put("class 3", (long) 300000);
        
        PaySlip instance = new PaySlip(tc1.getId(), new LocalDate(2013, 5, 8), listClasses);
        String expResult = "null	,1305001	,2013-05-08	,class 2-200000 class 3-300000 class 1-100000\n";
        String result = instance.printDetail();
        assertEquals(expResult, result);
        System.out.println("PASS PaySlip PrintDetail");
    }
}
