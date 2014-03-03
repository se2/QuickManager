/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import org.joda.time.LocalDate;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ta Nhut Minh
 */
public class ValidatorTest {

    private static Class c1;
    private static Class c2;
    private static Class c3;
    private static HashMap<Integer, Integer> times;
    private static School school;
    private static ClassSession cs1;
    private static ClassSession cs2;
    private static ClassSession cs3;
    private static ClassSession cs4;
    private static ClassSession cs5;
    private static ClassSession cs6;
    private static LocalDate start = new LocalDate(2013, 5, 5);
    private static LocalDate end = new LocalDate(2013, 5, 5);
    private static ClassType ct1 = new ClassType("Piano", ClassType.SINGLE, 1, 12000, 16000, null);
    private static ClassType ct2 = new ClassType("Organ", ClassType.DUAL, 2, 0, 0, null);

    public ValidatorTest() {
    }

    //<editor-fold defaultstate="collapsed" desc="clearDatabase">
    private static void clearDatabase() {
        HashMap<String, Teacher> teachers;
        HashMap<String, Student> students;
        HashMap<String, Class> classes;
        HashMap<Integer, Room> rooms;
        HashMap<String, User> users;
        HashMap<String, ClassSession> sessions;
        HashMap<String, TeacherClass> teacherClass;
        HashMap<String, StudentClass> studentClass;
        ArrayList<Invoice> invoices;

        teachers = new HashMap<>();
        students = new HashMap<>();
        classes = new HashMap<>();
        rooms = new HashMap<>();
        users = new HashMap<>();
        sessions = new HashMap<>();
        teacherClass = new HashMap<>();
        studentClass = new HashMap<>();
        invoices = new ArrayList<>();

        school.setClasses(classes);
        school.setInvoices(invoices);
        school.setRooms(rooms);
        school.setSessions(sessions);
        school.setStudentClass(studentClass);
        school.setStudents(students);
        school.setTeacherClass(teacherClass);
        school.setTeachers(teachers);
        school.setUsers(users);

        Person.setCurrentId(1);
        Class.setCurrentId(1);
        ClassSession.setCurrentId(1);
        TeacherClass.setCurrentId(1);
        StudentClass.setCurrentId(1);

        school.saveData();
    }
    //</editor-fold>

    @BeforeClass
    public static void setUpClass() {
        school = new School();
        clearDatabase();

        c1 = new Class("Class 1", start, end, "", ct1, 48000);
        c2 = new Class("Class 2", start, end, "", ct2, 96000);
        c3 = new Class("Class 3", start, end, "", ct1, 100000);

        school.addClass(c1);
        school.addClass(c2);
        school.addClass(c3);

        //<editor-fold defaultstate="collapsed" desc="init Class 1 sessions times: 1, 2 | 11, 12 | 16, 17, 18: Room 1">
        times = new HashMap<>();
        times.put(1, 1);
        times.put(2, 2);
        cs1 = new ClassSession(c1.getClassId(), 1, times, true);
        school.addSession(cs1);

        times = new HashMap<>();
        times.put(11, 11);
        times.put(12, 12);
        cs2 = new ClassSession(c1.getClassId(), 1, times, false);
        school.addSession(cs2);

        times = new HashMap<>();
        times.put(16, 16);
        times.put(17, 17);
        times.put(18, 18);
        cs3 = new ClassSession(c1.getClassId(), 1, times, true);
        school.addSession(cs3);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="init Class 2 sessions times: 11, 12 | 15, 16: Room 1">
        times = new HashMap<>();
        times.put(11, 11);
        times.put(12, 12);
        cs4 = new ClassSession(c2.getClassId(), 1, times, true);

        times = new HashMap<>();
        times.put(15, 15);
        times.put(16, 16);
        cs5 = new ClassSession(c2.getClassId(), 1, times, false);

        school.addSession(cs4);
        school.addSession(cs5);
        //</editor-fold>

        // time same as Class 2 but different room
        //<editor-fold defaultstate="collapsed" desc="init Class 3 sessions times: 10, 11 | Room 2">
        times = new HashMap<>();
        times.put(10, 10);
        times.put(11, 11);
        cs6 = new ClassSession(c3.getClassId(), 2, times, true);

        school.addSession(cs6);
        //</editor-fold>
    }

    @Test
    public void testValidateClassTime() {
        // class 1 has time conflict with class 2
        assertFalse(Validator.validateClassTime(c1, c2, school));

        // class 3 has time conflict with class 2 but different room (we care about time only)
        assertFalse(Validator.validateClassTime(c2, c3, school));

        // class 3 has time conflict with class 1 and different room (we care abiyt time only)
        assertFalse(Validator.validateClassTime(c1, c3, school));
    }

    @Test
    public void testValidateSessionTime() {
        // cs1 not conflit cs2
        assertTrue(Validator.validateSessionTime(cs1, cs2));

        // cs3 conflits cs5
        assertFalse(Validator.validateSessionTime(cs3, cs5));

        // cs4 conflit cs6
        assertFalse(Validator.validateSessionTime(cs4, cs6));
    }

    @Test
    public void testValidateTeacherAndClassTime() {
        Teacher t = new Teacher(null, "Dam", "", "Linh", "", "", null, "", null, true);
        school.addTeacer(t);
        // make this teacher teaches class c1
        TeacherClass tc = new TeacherClass(t.getId(), c1.getClassId());
        school.addTeacherClass(tc);

        // cs2 is part of the class this teacher is teaching
        assertTrue(Validator.validateTeacherAndClassTime(school, t, cs2, start, end));

        // cs6 has different room but same time as this teacher's class time
        assertFalse(Validator.validateTeacherAndClassTime(school, t, cs6, start, end));

        // cs4 has same time as this teacher's class time
        assertFalse(Validator.validateTeacherAndClassTime(school, t, cs4, start, end));
    }

    /**
     * Test of emailValidate method, of class Validator.
     */
    @Test
    public void testEmailValidate() {
        System.out.println("emailValidate");

        /**
         * empty string
         */
        assertEquals(false, Validator.emailValidate(""));
        /**
         * inputs with characters and dots
         */
        assertEquals(true, Validator.emailValidate("tanhutminh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("ta.nhutminh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("ta.nhut.minh@rmit.edu.vn"));

        assertEquals(true, Validator.emailValidate("tanhutminh@rmitedu.vn"));
        assertEquals(true, Validator.emailValidate("ta.nhutminh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("ta.nhut.minh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("t.anhutminh@rmit.edu.vn"));

        /**
         * input contains digits
         */
        assertEquals(true, Validator.emailValidate("1234567@123.12"));
        assertEquals(true, Validator.emailValidate("minh1234567@123.12"));
        assertEquals(true, Validator.emailValidate("1m23i45n6h7@nhut.ta"));

        /**
         * inputs with incorrect format
         */
        assertEquals(false, Validator.emailValidate("tanhutminh@"));
        assertEquals(false, Validator.emailValidate("@rmit.edu.vn"));
        assertEquals(false, Validator.emailValidate("tanhutminhrmit.edu.vn"));
        assertEquals(false, Validator.emailValidate("tanhutminh@rmiteduv.n"));

        /**
         * other inputs contains non-alphanumeric character
         */
        assertEquals(false, Validator.emailValidate("ta*nhutminh@rmit.edu.vn"));
        assertEquals(false, Validator.emailValidate("ta?nhutminh@rmit.edu.vn"));
        assertEquals(false, Validator.emailValidate("ta!nhutminh@rmit.edu.vn"));
        assertEquals(false, Validator.emailValidate("ta nhutminh@rmit.edu.vn"));

        /**
         * dash and underscore signs
         */
        assertEquals(true, Validator.emailValidate("ta_nhut_minh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("ta-nhut-minh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("ta-nhut_minh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("_tanhutminh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("_ta-nhut-minh_@rmit.edu.vn"));

        assertEquals(true, Validator.emailValidate("ta_nhut_minh1@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("1ta-nhut-minh@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("1ta-nhut_minh1@rmit.edu.vn"));
        assertEquals(true, Validator.emailValidate("_tanhutminh@1rmit.1edu.1vn"));
    }

    /**
     * Test of usernameValidate method, of class Validator.
     */
    @Test
    public void testUsernameValidate() {
        System.out.println("usernameValidate");
        /**
         * empty string
         */
        assertEquals(false, Validator.usernameValidate(""));
        System.out.println("\tPASS with empty string case.");

        /**
         * only letters
         */
        assertEquals(true, Validator.usernameValidate("a"));
        assertEquals(true, Validator.usernameValidate("abcdefghijklmnopqrstuvwxyz"));
        System.out.println("\tPASS with only characters.");
        /**
         * only digits
         */
        assertEquals(true, Validator.usernameValidate("0"));
        assertEquals(true, Validator.usernameValidate("0123456789"));
        System.out.println("\tPASS with only digits");
        /**
         * only alphanumerics
         */
        assertEquals(true, Validator.usernameValidate("a0bcdefg9"));
        assertEquals(true, Validator.usernameValidate("4a0bcdefg9"));
        assertEquals(true, Validator.usernameValidate("a0bcdefg"));
        assertEquals(true, Validator.usernameValidate("4a0bcdefg"));
        System.out.println("\tPASS with only alphnumerics");
        /**
         * other characters than alphanumerics
         */
        assertEquals(false, Validator.usernameValidate(".a0"));
        assertEquals(false, Validator.usernameValidate("-a0"));
        assertEquals(false, Validator.usernameValidate("_a0"));
        assertEquals(false, Validator.usernameValidate("*a0"));
        assertEquals(false, Validator.usernameValidate(".-_*a0"));
        System.out.println("\tPASS with non-alphanumerics characters.");
        System.out.println("\t--->PASS ALL");
    }

    /**
     * Test of passwordValidate method, of class Validator.
     */
    @Test
    public void testPasswordValidate() {
        System.out.println("passwordValidate");

        /**
         * contains only a type of characters
         */
        assertEquals(false, Validator.passwordValidate("tanhutminh"));
        assertEquals(false, Validator.passwordValidate("12345678"));
        assertEquals(false, Validator.passwordValidate("!@#$%^&*("));
        assertEquals(false, Validator.passwordValidate("TANHUTMINH"));
        System.out.println("\tnot allow only 1 type of characters:\t\tPASS.");

        /**
         * contains two types of characters
         */
        System.out.println("\tnot allow only 2 types of characters");
        /**
         * lower case and upper case letters
         */
        assertEquals(false, Validator.passwordValidate("Nhutminh"));
        assertEquals(false, Validator.passwordValidate("NHutminh"));
        assertEquals(false, Validator.passwordValidate("NhutMinh"));
        assertEquals(false, Validator.passwordValidate("nhutminH"));
        assertEquals(false, Validator.passwordValidate("nhutmiNH"));
        assertEquals(false, Validator.passwordValidate("nhuTMinh"));
        System.out.println("\t\tupper and lower case letters\t\tPASS");

        /**
         * lower case letters and digits
         */
        assertEquals(false, Validator.passwordValidate("1nhutminh"));
        assertEquals(false, Validator.passwordValidate("12nhutminh"));
        assertEquals(false, Validator.passwordValidate("1nhut2minh3"));
        assertEquals(false, Validator.passwordValidate("nhutminh1"));
        assertEquals(false, Validator.passwordValidate("nhutminh12"));
        assertEquals(false, Validator.passwordValidate("nhuTMinh"));
        System.out.println("\t\tlower case letters and digits\t\tPASS");

        /**
         * lower case letters and special characters
         */
        assertEquals(false, Validator.passwordValidate("*nhutminh"));
        assertEquals(false, Validator.passwordValidate("!@nhutminh"));
        assertEquals(false, Validator.passwordValidate("#nhut minh&"));
        assertEquals(false, Validator.passwordValidate("nhutminh%"));
        assertEquals(false, Validator.passwordValidate("nhutminh@^"));
        assertEquals(false, Validator.passwordValidate("nhu >inh"));
        System.out.println("\t\tlowercase letters and special characters\t\tPASS");

        /**
         * upper case letters and digits
         */
        assertEquals(false, Validator.passwordValidate("1NHUTMINH"));
        assertEquals(false, Validator.passwordValidate("12NHUTMINH"));
        assertEquals(false, Validator.passwordValidate("1NHUT2MINH3"));
        assertEquals(false, Validator.passwordValidate("NHUTMINH1"));
        assertEquals(false, Validator.passwordValidate("NHUTMINH12"));
        assertEquals(false, Validator.passwordValidate("NHU45INH"));
        System.out.println("\t\tupper case letters and digits\t\tPASS");

        /**
         * upper case letters and special characters
         */
        assertEquals(false, Validator.passwordValidate("!NHUTMINH"));
        assertEquals(false, Validator.passwordValidate(" ?NHUTMINH"));
        assertEquals(false, Validator.passwordValidate("{NHUT;MINH."));
        assertEquals(false, Validator.passwordValidate("NHUTMINH|"));
        assertEquals(false, Validator.passwordValidate("NHUTMINH_+"));
        assertEquals(false, Validator.passwordValidate("NHU^&INH"));
        System.out.println("\t\tupper case letters and special characters\t\tPASS");

        /**
         * digits and special characters
         */
        assertEquals(false, Validator.passwordValidate("!123456789"));
        assertEquals(false, Validator.passwordValidate(" ?123456789"));
        assertEquals(false, Validator.passwordValidate("{12345;67890."));
        assertEquals(false, Validator.passwordValidate("12345678|"));
        assertEquals(false, Validator.passwordValidate("12345678_+"));
        assertEquals(false, Validator.passwordValidate("1234^&1234"));
        System.out.println("\t\tdigits and special characters\t\tPASS");

        /**
         * contains three type of characters
         */
        System.out.println("\tnot allow only 3 types of characters");
        /**
         * uppercase letters, digits and special characters
         */
        assertEquals(false, Validator.passwordValidate("ABC123!@#"));
        assertEquals(false, Validator.passwordValidate("ABC!@#123"));
        assertEquals(false, Validator.passwordValidate("!@#ABC123"));
        assertEquals(false, Validator.passwordValidate("!@#123ABC"));
        assertEquals(false, Validator.passwordValidate("123!@#ABC"));
        assertEquals(false, Validator.passwordValidate("123ABC!@#"));

        assertEquals(false, Validator.passwordValidate("A1!B2@C3#"));
        assertEquals(false, Validator.passwordValidate("A!1B@2C#3"));
        assertEquals(false, Validator.passwordValidate("1A!2B@3C#"));
        assertEquals(false, Validator.passwordValidate("1!A2@B3#C"));
        assertEquals(false, Validator.passwordValidate("!A1@B2#C3"));
        assertEquals(false, Validator.passwordValidate("!1A@2B#3C"));

        System.out.println("\t\tuppercase letters, digits and special characters \t\tPASS");

        /**
         * lowercase letters, digits and special characters
         */
        assertEquals(false, Validator.passwordValidate("abc123!@#"));
        assertEquals(false, Validator.passwordValidate("abc!@#123"));
        assertEquals(false, Validator.passwordValidate("!@#abc123"));
        assertEquals(false, Validator.passwordValidate("!@#123abc"));
        assertEquals(false, Validator.passwordValidate("123!@#abc"));
        assertEquals(false, Validator.passwordValidate("123abc!@#"));

        assertEquals(false, Validator.passwordValidate("a1!b2@c3#"));
        assertEquals(false, Validator.passwordValidate("a!1b@2c#3"));
        assertEquals(false, Validator.passwordValidate("1a!2b@3c#"));
        assertEquals(false, Validator.passwordValidate("1!a2@b3#c"));
        assertEquals(false, Validator.passwordValidate("!a1@b2#c3"));
        assertEquals(false, Validator.passwordValidate("!1a@2b#3c"));

        System.out.println("\t\tlowercase letters, digits and special characters \t\tPASS");

        /**
         * lowercase letters, uppercase letters and special characters
         */
        assertEquals(false, Validator.passwordValidate("abcABC!@#"));
        assertEquals(false, Validator.passwordValidate("abc!@#ABC"));
        assertEquals(false, Validator.passwordValidate("!@#abcABC"));
        assertEquals(false, Validator.passwordValidate("!@#ABCabc"));
        assertEquals(false, Validator.passwordValidate("ABC!@#abc"));
        assertEquals(false, Validator.passwordValidate("ABCabc!@#"));

        assertEquals(false, Validator.passwordValidate("aA!bB@cC#"));
        assertEquals(false, Validator.passwordValidate("a!Ab@Bc#C"));
        assertEquals(false, Validator.passwordValidate("Aa!Bb@Cc#"));
        assertEquals(false, Validator.passwordValidate("A!aB@bC#c"));
        assertEquals(false, Validator.passwordValidate("!aA@bB#cC"));
        assertEquals(false, Validator.passwordValidate("!Aa@Bb#Cc"));

        System.out.println("\t\tlowercase letters, uppercase letters and special characters \t\tPASS");

        /**
         * lowercase letters, uppercase letters and digits
         */
        assertEquals(false, Validator.passwordValidate("abcABC123"));
        assertEquals(false, Validator.passwordValidate("abc123ABC"));
        assertEquals(false, Validator.passwordValidate("123abcABC"));
        assertEquals(false, Validator.passwordValidate("123ABCabc"));
        assertEquals(false, Validator.passwordValidate("ABC123abc"));
        assertEquals(false, Validator.passwordValidate("ABCabc123"));

        assertEquals(false, Validator.passwordValidate("aA1bB2cC3"));
        assertEquals(false, Validator.passwordValidate("a1Ab2Bc3C"));
        assertEquals(false, Validator.passwordValidate("Aa1Bb2Cc3"));
        assertEquals(false, Validator.passwordValidate("A1aB2bC3c"));
        assertEquals(false, Validator.passwordValidate("1aA2bB3cC"));
        assertEquals(false, Validator.passwordValidate("1Aa2Bb3Cc"));

        System.out.println("\t\tlowercase letters, uppercase letters and special digits \t\tPASS");

        /**
         * Tests with the expected number of types of password.
         */
        System.out.println("\tall required types of characters");

        /**
         * at least 8 characters
         */
        assertEquals(false, Validator.passwordValidate("Minh12@"));
        System.out.println("\t\tnot allow less than 8 characters\t\tPASS");
        assertEquals(true, Validator.passwordValidate("Minh123@"));
        assertEquals(true, Validator.passwordValidate("Minh123@2"));
        System.out.println("\t\trequires at least 8 characters\t\tPASS");
        System.out.println("\t--->PASS ALL");
    }

    /**
     * Test of teacherClassValidate method, of class Validator. whether the
     * teacher is assigned in the corrected class. does not need to test the
     * spelling of the skill (guitar vs Guitar) because the system allows only
     * options to choose by clicking, not typing.
     */
    @Test
    public void testTeacherClassValidate() {
        System.out.println("teacherClassValidate");

        ClassType ct3 = new ClassType("organ", ClassType.DUAL, 2, 0, 0, null);
        ClassType ct4 = new ClassType("violin", ClassType.DUAL, 2, 0, 0, null);
        ClassType ct5 = new ClassType("drum", ClassType.DUAL, 2, 0, 0, null);

        String skills2[][] = {{"guitar", "100"}, {"organ", "200"}, {"violin", "300"}, {"hip hop", "400"}};
        String skills4[][] = {{"organ", "15"}, {"violin", "30"}, {"hip hop", "45"}, {"guitar", "60"}};
        String skills5[][] = {{"drum", "12"}, {"organ", "24"}, {"violin", "48"}, {"hip hop", "96"}};

        assertEquals(true, Validator.teacherClassValidate(skills2, ct3));
        assertEquals(true, Validator.teacherClassValidate(skills4, ct4));
        assertEquals(true, Validator.teacherClassValidate(skills5, ct5));
        System.out.println("\t\t--->teacher with that skill\t\tPASS");

        assertEquals(false, Validator.teacherClassValidate(skills5, ct1));
        System.out.println("\t\t--->teacher without that skill\t\tPASS");

        System.out.println("\t--->teacherClassValidate(String skills[], Class c)\t\tPASS");
    }
}
