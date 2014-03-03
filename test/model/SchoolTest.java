/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import model.Class;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author SVC
 */
public class SchoolTest {

    private HashMap<String, Teacher> teachers;
    private HashMap<String, Student> students;
    private HashMap<String, Class> classes;
    private HashMap<Integer, Room> rooms;
    private HashMap<String, User> users;
    private HashMap<String, ClassSession> sessions;
    private HashMap<String, TeacherClass> teacherClass;
    private HashMap<String, StudentClass> studentClass;
    private ArrayList<ClassType> classTypes;
    private ArrayList<Invoice> invoices;
    private Student student1;
    private Student student2;
    private Student student3;
    private Class class1;
    private Class class2;
    private StudentClass sc11;
    private StudentClass sc12;
    private StudentClass sc21;
    private StudentClass sc22;
    private Teacher teacher;
    private Teacher teacher2;
    private Teacher teacher3;
    private TeacherClass tc1;
    private TeacherClass tc2;
    private Invoice invoice1;
    private ClassType ct1;
    private ClassType ct2;
    private ClassType ct3;

    public SchoolTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        School instance = new School();

        this.classes = instance.getClasses();
        this.invoices = instance.getInvoices();
        this.rooms = instance.getRooms();
        this.sessions = instance.getSessions();
        this.studentClass = instance.getStudentClass();
        this.students = instance.getStudents();
        this.teacherClass = instance.getTeacherClass();
        this.teachers = instance.getTeachers();
        this.users = instance.getUsers();
        this.classTypes = instance.getClassTypes();

        instance.saveData();
        instance = new School();
        Iterator<Map.Entry<String, TeacherClass>> iter = this.teacherClass.entrySet().iterator();
        while (iter.hasNext()) {
            TeacherClass tc = iter.next().getValue();
            assertEquals(this.teacherClass.get(tc.getId()).getClassId(),
                    instance.getTeacherClass().get(tc.getId()).getClassId());
            assertEquals(this.teacherClass.get(tc.getId()).getTeacherId(),
                    instance.getTeacherClass().get(tc.getId()).getTeacherId());
        }

        Iterator<Map.Entry<String, Class>> iter2 = this.classes.entrySet().iterator();
        while (iter2.hasNext()) {
            Class c = iter2.next().getValue();

            assertEquals(this.classes.get(c.getClassId()).getClassName(),
                    instance.getClasses().get(c.getClassId()).getClassName());

            assertEquals(this.classes.get(c.getClassId()).getCurrentNumberOfStudent(),
                    instance.getClasses().get(c.getClassId()).getCurrentNumberOfStudent());

            assertEquals(this.classes.get(c.getClassId()).getEndDate(),
                    instance.getClasses().get(c.getClassId()).getEndDate());

            assertEquals(this.classes.get(c.getClassId()).getTuitionFeeString(),
                    instance.getClasses().get(c.getClassId()).getTuitionFeeString());

        }

        System.out.println("PASS for loadData() and saveData()");

        this.teachers = new HashMap<>();
        this.students = new HashMap<>();
        this.classes = new HashMap<>();
        this.rooms = new HashMap<>();
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
        this.teacherClass = new HashMap<>();
        this.studentClass = new HashMap<>();
        this.invoices = new ArrayList<>();

        initClassType();
        initStudent();
        initClass();
        initTeacher();
        initInvoice();
        initUser();
    }

    private void initClassType() {
        this.ct1 = new ClassType("Piano", ClassType.SINGLE, 1, 12000, 16000, null);
        this.ct2 = new ClassType("Organ", ClassType.DUAL, 2, 10000, 15000, null);
        this.ct3 = new ClassType("Guitar", ClassType.GROUP, 3, 15000, 20000, null);
        this.classTypes.add(ct1);
        this.classTypes.add(ct2);
        this.classTypes.add(ct3);
    }

    private void initStudent() {
        student1 = new Student("08-0808-0808", "ductu@yahoo.com", "Tu",
                "hcmc", "friend", "Duc", "", "Tu", "ductu@gmail.com",
                "0909090909", new LocalDate(), "VN", null, false);
        students.put(student1.getId(), student1);

        student2 = new Student("08-0808-0808", "taminh@yahoo.com", "Minh",
                "hcmc", "friend", "Ta", "", "Minh", "taminh@gmail.com",
                "0909090909", new LocalDate(), "VN", null, false);
        students.put(student2.getId(), student2);

        student3 = new Student("08-0808-0808", "damlinh@yahoo.com", "Linh",
                "hcmc", "friend", "Dam", "Hong", "Linh", "linhdam@gmail.com",
                "0909090909", new LocalDate(), "VN", null, true);
        students.put(student3.getId(), student3);
    }

    private void initClass() {
        LocalDate start = new LocalDate(2013, 1, 27);
        LocalDate end = new LocalDate(2013, 6, 7);
        LocalDate end2 = new LocalDate(2013, 3, 7);

        class1 = new Class("Guitar Expert", start, end, "Guitar Textbook", ct3, 120000);
        classes.put(class1.getClassId(), class1);

        sc11 = new StudentClass(student1.getId(), class1.getClassId());
        studentClass.put(sc11.getId(), sc11);

        sc21 = new StudentClass(student2.getId(), class1.getClassId());
        studentClass.put(sc21.getId(), sc21);

        class2 = new Class("Piano Expert", start, end2, "Java intro", ct1, 96000);
        classes.put(class2.getClassId(), class2);

        sc12 = new StudentClass(student1.getId(), class2.getClassId());
        studentClass.put(sc12.getId(), sc12);

        sc22 = new StudentClass(student2.getId(), class2.getClassId());
        studentClass.put(sc22.getId(), sc22);
    }

    private void initTeacher() {
        LocalDate dob = new LocalDate(1975, 6, 25);
        teacher = new Teacher(new String[][]{{"Piano", "100"}, {"Guitar", "200"}}, "Quang", "", "Tran",
                "quangtran@rmit.edu.vn", "0909040404", dob, "RMIT", null, true);

        teacher2 = new Teacher(new String[][]{{"Organ", "100"}, {"Violin", "200"}}, "Thanh", "", "Ngoc",
                "thanhngoc@rmit.edu.vn", "0904563706", dob, "HUFTLIT", null, false);

        teacher3 = new Teacher(new String[][]{{"Piano", "100"}, {"Dance", "200"}}, "George", "", "Nguyen",
                "georgenguyen@rmit.edu.vn", "0904563706", dob, "HUTECH", null, false);

        teachers.put(teacher.getId(), teacher);
        teachers.put(teacher2.getId(), teacher2);
        teachers.put(teacher3.getId(), teacher3);

        tc1 = new TeacherClass(teacher.getId(), class1.getClassId());
        teacherClass.put(tc1.getId(), tc1);

        tc2 = new TeacherClass(teacher.getId(), class2.getClassId());
        teacherClass.put(tc2.getId(), tc2);
    }

    private void initInvoice() {
        ArrayList<StudentClass> stuClasses1 = new ArrayList<>(2);
        stuClasses1.add(sc11);
        stuClasses1.add(sc12);
        LocalDate paidDate = new LocalDate();
        invoice1 = new Invoice("1304001-130404", stuClasses1, paidDate, "Cash", "Expensive", 7000, 0);
        invoice1.setPaid(true);
        invoices.add(invoice1);

        ArrayList<StudentClass> stuClasses2 = new ArrayList<>(2);
        stuClasses2.add(sc21);
        stuClasses2.add(sc22);
        invoice1 = new Invoice("1304002-130404", stuClasses2, paidDate, "Cash", "Expensive", 7000, 0);
        invoice1.setPaid(false);
        invoices.add(invoice1);
    }

    private void initUser() {
        LocalDate dob = new LocalDate(1960, 9, 6);
        Manager m1 = new Manager("admin", "Lamasia2*", "quang", "", "tran",
                "quangtran@hotmail.com", "0909941192", dob, "hcmc", null, true);
        users.put(m1.getId(), m1);

        Manager m2 = new Manager("admin2", "Lamasia2*", "quang", "", "tran",
                "quangtran@gmail.com", "0923441192", dob, "hcmc", null, false);
        users.put(m2.getId(), m2);

        Staff s1 = new Staff("mod1", "Moderator1*", "thanh", "", "ngoc", "thanh.ngoc@yahoo.com",
                "0983546671", dob, null, null, true);
        users.put(s1.getId(), s1);
    }

    //<editor-fold defaultstate="collapsed" desc="temp fold">
    /**
     * Test of checkIDPass method, of class School.
     */
    @Test
    public void testCheckIDPass() {
        System.out.println("checkIDPass");
        School instance = new School();

        users.get("admin").setIsActive(true);
        users.get("admin2").setIsActive(false);
        instance.setUsers(users);

        assertEquals(users.get("admin"), instance.checkIDPass("admin",
                users.get("admin").getPassword().toCharArray()));
        System.out.println("PASS with active user");

        assertEquals(new String(), instance.checkIDPass(users.get("admin2").getId(),
                users.get("admin2").getPassword().toCharArray()));
        System.out.println("PASS with inactive user");

        assertNull(instance.checkIDPass(users.get("admin").getId(), "Lamasia2**".toCharArray()));
        System.out.println("PASS with wrong password");

        assertNull(instance.checkIDPass("admin1", users.get("admin").getPassword().toCharArray()));
        System.out.println("PASS with wrong ID");

        System.out.println("PASS ALL");
    }

    /**
     * Test of validateUsername method, of class School.
     */
    @Test
    public void testValidateUsername() {
        System.out.println("validateUsername");
        School instance = new School();
        instance.setUsers(users);

        assertFalse(instance.validateUsername("admin"));
        assertFalse(instance.validateUsername("admin2"));
        assertFalse(instance.validateUsername("mod1"));
        System.out.println("PASS with duplicate username");

        assertTrue(instance.validateUsername("qwerty"));
        System.out.println("PASS with unique username");

        System.out.println("PASS ALL");
    }

    /**
     * Test of getClassAssociatedTeacher method, of class School.
     */
    @Test
    public void testGetClassAssociatedTeacher() {
        System.out.println("getClassAssociatedTeacher");
        School instance = new School();
        instance.setTeachers(teachers);
        instance.setTeacherClass(teacherClass);
        instance.setClasses(classes);

        assertEquals(classes, instance.getClassAssociatedTeacher(teacher));
        System.out.println("PASS with associated teacher");

        assertEquals(0, instance.getClassAssociatedTeacher(teacher2).size());
        System.out.println("PASS with unassociated teacher");

        System.out.println("PASS ALL");
    }

    /**
     * Test of getClassAssociatedStudent method, of class School.
     */
    @Test
    public void testGetClassAssociatedStudent() {
        System.out.println("getClassAssociatedStudent");
        School instance = new School();
        instance.setStudents(students);
        instance.setStudentClass(studentClass);
        instance.setClasses(classes);

        assertEquals(classes, instance.getClassAssociatedStudent(student1));
        assertEquals(classes, instance.getClassAssociatedStudent(student2));
        System.out.println("PASS with associated student");

        assertEquals(0, instance.getClassAssociatedStudent(student3).size());
        System.out.println("PASS with unassociated student");

        System.out.println("PASS ALL");
    }

    @Test
    public void testSelectAllUser() {
        Iterator<Entry<String, User>> iterUser = users.entrySet().iterator();
        School model = new School();
        model.setUsers(users);

        iterUser.next().getValue().setSelected(true);
        model.selectAllUser();// all users are selected

        iterUser = users.entrySet().iterator();
        while (iterUser.hasNext()) {
            if (!iterUser.next().getValue().isSelected()) {
                fail();
            }
        }

        model.selectAllUser();// no user is selected
        iterUser = users.entrySet().iterator();
        while (iterUser.hasNext()) {
            if (iterUser.next().getValue().isSelected()) {
                fail();
            }
        }
        assertTrue(true);
    }

    @Test
    public void testSelectAllTeacher() {
        Iterator<Entry<String, Teacher>> iter = teachers.entrySet().iterator();
        School model = new School();
        model.setTeachers(teachers);

        iter.next().getValue().setSelected(true);
        model.selectAllTeacher();// all teachers are selected

        iter = teachers.entrySet().iterator();
        while (iter.hasNext()) {
            if (!iter.next().getValue().isSelected()) {
                fail();
            }
        }

        model.selectAllTeacher();// no teacher is selected
        iter = teachers.entrySet().iterator();
        while (iter.hasNext()) {
            if (iter.next().getValue().isSelected()) {
                fail();
            }
        }
        assertTrue(true);
    }

    @Test
    public void testSelectAllStudent() {
        Iterator<Entry<String, Student>> iter = students.entrySet().iterator();
        School model = new School();
        model.setStudents(students);

        iter.next().getValue().setSelected(true);
        model.selectAllStudent();// all students are selected

        iter = students.entrySet().iterator();
        while (iter.hasNext()) {
            if (!iter.next().getValue().isSelected()) {
                fail();
            }
        }

        model.selectAllStudent();// no student is selected
        iter = students.entrySet().iterator();
        while (iter.hasNext()) {
            if (iter.next().getValue().isSelected()) {
                fail();
            }
        }
        assertTrue(true);
    }

    @Test
    public void testSelectAllClass() {
        Iterator<Entry<String, Class>> iter = classes.entrySet().iterator();
        School model = new School();
        model.setClasses(classes);

        iter.next().getValue().setSelected(true);
        model.selectAllClass();// all classes are selected

        iter = classes.entrySet().iterator();
        while (iter.hasNext()) {
            if (!iter.next().getValue().isSelected()) {
                fail();
            }
        }

        model.selectAllClass();// no class is selected
        iter = classes.entrySet().iterator();
        while (iter.hasNext()) {
            if (iter.next().getValue().isSelected()) {
                fail();
            }
        }
        assertTrue(true);
    }

    /**
     * Test of deleteClass method, of class School.
     */
    @Test
    public void testDeleteClass() {
        System.out.println("deleteClass");
        School instance = new School();
        instance.setClasses(classes);
        instance.setStudentClass(studentClass);
        instance.setStudents(students);
        instance.setTeachers(teachers);
        instance.setTeacherClass(teacherClass);
        Class c = new Class("guitar", new LocalDate(2013, 3, 1), new LocalDate(2013, 12, 1), "text", ct3, 0);

        assertFalse(instance.deleteClass(c));
        System.out.println("PASS with class which is not finished yet");

        Class c2 = new Class("piano", new LocalDate(2013, 5, 1), new LocalDate(2013, 7, 1), "text", ct1, 0);
        classes.put(c2.getClassId(), c2);
        instance.setClasses(classes);
        instance.deleteClass(c2);
        classes.remove(c2.getClassId());
        assertEquals(classes, instance.getClasses());
        System.out.println("PASS with class which has not started yet");

        studentClass.remove(sc11.getId());
        teacherClass.remove(tc1.getId());
        classes.remove(class1.getClassId());
        instance.deleteClass(class1);
        assertEquals(studentClass, instance.getStudentClass());
        assertEquals(teacherClass, instance.getTeacherClass());
        assertEquals(classes, instance.getClasses());

        System.out.println("PASS ALL");
    }

    /**
     * Test of deleteTeacher method, of class School.
     */
    @Test
    public void testDeleteTeacher() {
        System.out.println("deleteTeacher");
        School instance = new School();
        instance.setTeachers(teachers);
        instance.setTeacherClass(teacherClass);

        assertFalse(instance.deleteTeacher(teacher));
        System.out.println("PASS with a teacher currently teaching a class");

        assertTrue(instance.deleteTeacher(teacher3));
        teachers.remove(teacher3.getId());
        assertEquals(teachers, instance.getTeachers());
        System.out.println("PASS with a teacher currently not teaching any classes");

        System.out.println("PASS ALL");
    }
    //</editor-fold>

    /**
     * Test of deleteStudentClass method, of class School.
     */
    @Test
    public void testDeleteStudentClassUnpaid() {
        School instance = new School();
        instance.setStudents(students);
        instance.setStudentClass(studentClass);
        instance.setInvoices(invoices);
        instance.setClasses(classes);

        instance.deleteStudentClass(sc11);

        assertEquals(0, class1.getCurrentNumberOfStudent());
        System.out.println("Pass number of current students");

        assertEquals(studentClass.size(), 3);
        assertEquals(student1.getBalance(), 120000);
        assertEquals(invoice1.getTotalFee(), 7000);

        System.out.println("PASS ALL");
    }

    /**
     * Test of deleteStudentClass method, of class School.
     */
    @Test
    public void testDeleteStudentClassPaid() {
        School instance = new School();
        instance.setStudents(students);
        instance.setStudentClass(studentClass);
        instance.setInvoices(invoices);
        instance.setClasses(classes);

        sc11.setPaid(true);
        instance.deleteStudentClass(sc11);

        assertEquals(0, class1.getCurrentNumberOfStudent());
        System.out.println("Pass number of current students");

        assertEquals(studentClass.size(), 3);
        assertEquals(student1.getBalance(), 120000);
        assertEquals(invoice1.getTotalFee(), 7000);

        System.out.println("PASS ALL");
    }

    /**
     * Test of deleteTeacherClass method, of class School.
     */
    @Test
    public void testDeleteTeacherClass() {
        System.out.println("deleteTeacherClass");
        School instance = new School();
        instance.setTeacherClass(teacherClass);
        instance.setTeachers(teachers);
        instance.setClasses(classes);

        assertFalse(instance.deleteTeacherClass(tc1));
        System.out.println("PASS with un-finished class");

        assertTrue(instance.deleteTeacherClass(tc2));
        teacherClass.remove(tc2.getId());
        assertEquals(teacherClass, instance.getTeacherClass());
        System.out.println("PASS with finished class");

        System.out.println("PASS ALL");
    }

    /**
     * Test of resetPassword method, of class School.
     */
    @Test
    public void testResetPassword() {
        System.out.println("resetPassword");
        School instance = new School();
        LocalDate dob = new LocalDate(1960, 9, 6);
        Manager m1 = new Manager("admin", "Lamasia2*", "quang", "", "tran",
                "quangtran@hotmail.com", "0909941192", dob, "hcmc", null, true);
        instance.addUser(m1);
        instance.resetPassword(m1);

        assertEquals(Constants.DEFAULT_PASSWORD, m1.getPassword());

        System.out.println("PASS ALL");
    }

    /**
     * Test of filterStudent method, of class School.
     */
    @Test
    public void testFilterStudent() {
        System.out.println("filterStudent");
        HashMap<String, Student> result = new HashMap<>();
        School instance = new School();

        result.put(student1.getId(), student1);
        result.put(student2.getId(), student2);
        result.put(student3.getId(), student3);
        instance.setStudents(students);

        assertEquals(result, instance.filterStudent("", students));
        assertEquals(result, instance.filterStudent(" ", students));
        System.out.println("PASS with null filter");

        result.remove(student1.getId());
        result.remove(student2.getId());
        assertEquals(result, instance.filterStudent("Hong", students));
        System.out.println("PASS with name field");

        result.put(student1.getId(), student1);
        result.put(student2.getId(), student2);
        assertEquals(result, instance.filterStudent("@", students));
        assertEquals(result, instance.filterStudent("0909090909", students));
        assertEquals(result, instance.filterStudent("08-0808-0808", students));
        System.out.println("PASS with other fields");

        result.clear();
        assertEquals(result, instance.filterStudent("qwerty", students));
        System.out.println("PASS with not-found filter");

        System.out.println("PASS ALL");
    }

    /**
     * Test of generateInvoice method, of class School.
     */
    @Test
    public void testGenerateInvoice() {
        System.out.println("generateInvoice");
        School instance = new School();
        instance.setStudents(students);
        instance.setStudentClass(studentClass);
        instance.setClasses(classes);
        instance.setInvoices(invoices);

        Invoice inv1 = instance.generateInvoice(student1);
        Invoice inv2 = instance.generateInvoice(student2);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyMMdd");
        String todayString = fmt.print(new LocalDate());

        assertEquals(inv1.getId(), student1.getId() + "-" + todayString);
        assertEquals(inv2.getId(), student2.getId() + "-" + todayString);

        System.out.println("PASS ALL");
    }

    @Test
    public void testGenerateReportEmptyReport() {
        LocalDate month = new LocalDate(2012, 1, 29);
        School instance = new School();
        instance.setInvoices(invoices);
        instance.setClasses(classes);
        instance.setSessions(sessions);
        instance.setStudentClass(studentClass);
        instance.setStudents(students);

        Report result = instance.generateReport(month);
        ArrayList<String> paidStudentsInfo = result.getPaidStudentsInfo();
        ArrayList<String> unPaidStudentsInfo = result.getUnPaidStudentsInfo();
        ArrayList<Long> paidAmount = result.getPaidAmount();
        ArrayList<Long> unPaidAmount = result.getUnPaidAmount();
        ArrayList<Invoice> paidInvoice = result.getPaidInvoice();
        ArrayList<Invoice> unPaidInvoice = result.getUnPaidInvoice();

        assertTrue(paidStudentsInfo.isEmpty());
        assertTrue(unPaidStudentsInfo.isEmpty());
        assertTrue(paidAmount.isEmpty());
        assertTrue(unPaidAmount.isEmpty());
        assertTrue(paidInvoice.isEmpty());
        assertTrue(unPaidInvoice.isEmpty());
        System.out.println("PASS ALL");
    }

    @Test
    public void testGenerateReportStudentInfo() {
        LocalDate month = new LocalDate(2013, 1, 29);
        School instance = new School();
        instance.setInvoices(invoices);
        instance.setClasses(classes);
        instance.setSessions(sessions);
        instance.setStudentClass(studentClass);
        instance.setStudents(students);
        Report result = instance.generateReport(month);

        ArrayList<String> paidStudentsInfo = result.getPaidStudentsInfo();
        ArrayList<String> unPaidStudentsInfo = result.getUnPaidStudentsInfo();

        String unPaidStudentInfo2 = student2.getId();
        String paidStudentInfo1 = student1.getId();
        if (!unPaidStudentsInfo.isEmpty()) {
            assertTrue(unPaidStudentsInfo.get(0).equals(unPaidStudentInfo2));
            assertTrue(paidStudentsInfo.get(0).equals(paidStudentInfo1));
        }
    }

    @Test
    public void testGetUtilization() {
        School instance = new School();
        double testResult = 0.5;
        HashMap<Integer, Integer> times = new HashMap<>();
        times.put(5, 5);
        times.put(6, 6);
        times.put(7, 7);
        times.put(8, 8);
        times.put(9, 9);
        
        ClassSession cs1 = new ClassSession(class1.getClassId(), 1, times, true);
        ClassSession cs2 = new ClassSession(class2.getClassId(), 2, times, false);
        sessions.put(cs1.getClassId(), cs1);
        sessions.put(cs2.getClassId(), cs2);
        
        instance.setInvoices(invoices);
        instance.setClasses(classes);
        instance.setSessions(sessions);
        instance.setStudentClass(studentClass);
        instance.setStudents(students);

        assertEquals(testResult, instance.getUtilization(), 0.5);

        System.out.println("PASS getUtilization");
    }

    @Test
    public void testGenerateReportPaidAmount() {
        LocalDate month = new LocalDate(2013, 1, 29);
        School instance = new School();
        instance.setInvoices(invoices);
        instance.setClasses(classes);
        instance.setSessions(sessions);
        instance.setStudentClass(studentClass);
        instance.setStudents(students);
        Report result = instance.generateReport(month);

        String unPaidAmountString = "VND 7,000";
        String paidAmountString = "VND 7,000";
        assertFalse(unPaidAmountString.equals(result.getTotalUnpaidAmountString()));
        assertFalse(paidAmountString.equals(result.getTotalPaidAmountString()));
    }
}
