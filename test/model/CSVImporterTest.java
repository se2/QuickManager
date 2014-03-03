/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.HashMap;
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
public class CSVImporterTest {

    private CSVImporter importer;

    public CSVImporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        School school = new School();
        importer = new CSVImporter(new File("test,csv"), school);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of startImport method, of class CSVImporter.
     */
    @Test
    public void testStartImport() throws Exception {
        System.out.println("startImport");
        School school = new School();
        File file = new File("test.csv");
        CSVImporter instance = new CSVImporter(file, school);
        instance.startImport();
        System.out.println("PASS startImport");
    }

    /**
     * Test of importUsers method, of class CSVImporter.
     */
    @Test
    public void testImportUsers() throws Exception {
        String s = "manager	,Manager1*	,Manager	,Manager Default	,true	,2012-12-21	,defaultmanager@themusicschool.edu.vn	,08 - 1111 - 1111	\n";
        importer.importUsers(s);
        HashMap<String, User> users = importer.getUsers();
        assertTrue(users.isEmpty());//duplicate user
    }

    /**
     * Test of importClassType method, of class CSVImporter.
     */
    @Test
    public void testImportClassType() throws Exception {
        String s = "Piano	,0	,0	,12	,15	,";
        importer.importClassType(s);
        assertTrue(importer.getClassTypes().isEmpty());//duplicate
    }

    /**
     * Test of importTeacher method, of class CSVImporter.
     */
    @Test
    public void testImportTeacher() throws Exception {
        String s = "1305001	,Quang Tran	,true	,2001-05-09	,Piano-12	,quangtran@gmail.com	,08 - 1230 - 4567	,hcmc";
        importer.importTeacher(s);
        assertTrue(importer.getTeachers().isEmpty());//duplicate
    }

    /**
     * Test of importClass method, of class CSVImporter.
     */
    @Test
    public void testImportClass() throws Exception {
        String s = "Class1	,Piano	,2013-05-09	,2014-05-09	,1	,0	,text	,Piano-0";
        importer.importClass(s);
        assertTrue(importer.getClasses().isEmpty());
    }

    /**
     * Test of importSession method, of class CSVImporter.
     */
    @Test
    public void testImportSession() throws Exception {
        String s = "Class1	,8  ,Session1   ,true   ,5";
        importer.importSession(s);
        assertTrue(importer.getSessions().isEmpty());
    }

    /**
     * Test of importTeacherClass method, of class CSVImporter.
     */
    @Test
    public void testImportTeacherClass() throws Exception {
        String s = "TC1	,1305001	,Class1";
        importer.importTeacherClass(s);
        assertTrue(importer.getTeacherClasses().isEmpty());
    }

    /**
     * Test of importStudentClass method, of class CSVImporter.
     */
    @Test
    public void testImportStudentClass() throws Exception {
        String s = "Enrollment1	,Class1	,1305002	,false	,true";
        importer.importStudentClass(s);
        assertTrue(importer.getStudentClasses().isEmpty());
    }

    /**
     * Test of importInvoice method, of class CSVImporter.
     */
    @Test
    public void testImportInvoice() throws Exception {
        String s = "1305002-130509	,Enrollment1	,	,	,	,12	,0";
        importer.importInvoice(s);
        assertTrue(importer.getInvoices().isEmpty());
    }

    /**
     * Test of importPaySlip method, of class CSVImporter.
     */
    @Test
    public void testImportPaySlip() throws Exception {
        //importPaySlip() is empty
    }
}
