/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
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
public class ReportTest {

    private Report report;
    private ArrayList<String> paidStudentsInfo;
    private ArrayList<String> unPaidStudentsInfo;
    private ArrayList<Long> paidAmount;
    private ArrayList<Long> unPaidAmount;
    private ArrayList<Invoice> paidInvoice;
    private ArrayList<Invoice> unPaidInvoice;

    public ReportTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        paidStudentsInfo = new ArrayList<>();
        unPaidStudentsInfo = new ArrayList<>();
        paidAmount = new ArrayList<>();
        unPaidAmount = new ArrayList<>();
        paidInvoice = new ArrayList<>();
        unPaidInvoice = new ArrayList<>();

        paidAmount.add(5000000l);//5 000 000
        paidAmount.add(1000000l);//1 000 000
        paidAmount.add(3000000l);//3 000 000

        unPaidAmount.add(2000000l);//2 000 000
        unPaidAmount.add(1000000l);//1 000 000
        unPaidAmount.add(5000000l);//5 000 000

        report = new Report(paidStudentsInfo, unPaidStudentsInfo, paidAmount, unPaidAmount, paidInvoice, unPaidInvoice, null, null);
    }

    /**
     * Test of getPaidAmountString method, of class Report.
     */
    @Test
    public void testGetPaidAmountString() {
        ArrayList<String> expect = new ArrayList<>(3);
        expect.add("VND 5,000,000");
        expect.add("VND 1,000,000");
        expect.add("VND 3,000,000");

        ArrayList result = report.getPaidAmountString();

        for (int i = 0; i < expect.size(); i++) {
            assertEquals(expect.get(i), result.get(i));
        }
    }

    /**
     * Test of getUnPaidAmountString method, of class Report.
     */
    @Test
    public void testGetUnPaidAmountString() {
        ArrayList<String> expect = new ArrayList<>(3);
        expect.add("VND 2,000,000");
        expect.add("VND 1,000,000");
        expect.add("VND 5,000,000");

        ArrayList result = report.getUnPaidAmountString();

        for (int i = 0; i < expect.size(); i++) {
            assertEquals(expect.get(i), result.get(i));
        }
    }

    /**
     * Test of getTotalPaidAmountString method, of class Report.
     */
    @Test
    public void testGetTotalPaidAmountString() {
        assertEquals("VND 9,000,000", report.getTotalPaidAmountString());
    }

    /**
     * Test of getTotalUnpaidAmountString method, of class Report.
     */
    @Test
    public void testGetTotalUnpadAmountString() {
        assertEquals("VND 8,000,000", report.getTotalUnpaidAmountString());
    }
}
