/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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
public class InvoiceTest {

    public InvoiceTest() {
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
     * Test of getTotalFeeString method, of class Invoice.
     */
    @Test
    public void testGetTotalFeeString() {
        Invoice instance = new Invoice("", null, null, "", "", 3200000, 0);
        assertEquals("VND 3,200,000", instance.getTotalFeeString());

        instance = new Invoice("", null, null, "", "", 0, 0);
        assertEquals("VND 0", instance.getTotalFeeString());

        instance = new Invoice("", null, null, "", "", 30, 0);
        assertEquals("VND 30", instance.getTotalFeeString());

        instance = new Invoice("", null, null, "", "", 999999999, 0);
        assertEquals("VND 999,999,999", instance.getTotalFeeString());

        instance = new Invoice("", null, null, "", "", 1000, 0);
        assertEquals("VND 1,000", instance.getTotalFeeString());
    }

    @Test
    public void testGetBalanceString() {
        Invoice instance = new Invoice("", null, null, "", "", 0, 25000000);
        assertEquals("VND 25,000,000", instance.getBalanceString());

        instance = new Invoice("", null, null, "", "", 0, 3000000);
        assertEquals("VND 3,000,000", instance.getBalanceString());

        instance = new Invoice("", null, null, "", "", 0, 0);
        assertEquals("VND 0", instance.getBalanceString());

        instance = new Invoice("", null, null, "", "", 0, 30);
        assertEquals("VND 30", instance.getBalanceString());
    }
}
