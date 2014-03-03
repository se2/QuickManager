/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ResourceBundle;
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
public class ClassTypeTest {
    
    public ClassTypeTest() {
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
     * Test of getLessonPerWeekString method, of class ClassType.
     */
    @Test
    public void testPrintDetail() {
        System.out.println("printDetail");
        ClassType instance = new ClassType("Dance", 2, 2, 1000, 1500, null);
        String exp = "Dance	,2	,2	,1000	,1500	,Empty Remark\n";
        assertEquals(exp, instance.printDetail());
        System.out.println("PASS ClassType PrintDetail");
    }

}
