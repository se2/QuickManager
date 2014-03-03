package model;

import exeception.InvalidCSVFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Dam Linh
 */
public class CSVImporter {

    public static final int USER = 0;
    public static final int CLASS_TYPE = 1;
    public static final int TEACHER = 2;
    public static final int STUDENT = 3;
    public static final int CLASS = 4;
    public static final int SESSION = 5;
    public static final int TEACHER_CLASS = 6;
    public static final int STUDENT_CLASS = 7;
    public static final int INVOICE = 8;
    public static final int PAY_SLIP = 9;
    public static final String ACCOUNT_STRING = "Accounts";
    public static final String CLASS_TYPE_STRING = "Class Types";
    public static final String TEACHER_STRING = "Teachers";
    public static final String STUDENT_STRING = "Students";
    public static final String CLASS_STRING = "Classes";
    public static final String SESSION_STRING = "Lessons";
    public static final String TEACHER_CLASS_STRING = "Teacher-Class";
    public static final String STUDENT_CLASS_STRING = "Student-Class";
    public static final String INVOICE_STRING = "Invoice";
    public static final String PAY_SLIP_STRING = "Pay Slips";
    private File file;
    private ResourceBundle language;
    private School school;
    private HashMap<String, User> users = new HashMap<>();
    private ArrayList<ClassType> classTypes = new ArrayList<>();
    private HashMap<String, Teacher> teachers = new HashMap<>();
    private HashMap<String, Student> students = new HashMap<>();
    private HashMap<String, Class> classes = new HashMap<>();
    private HashMap<String, ClassSession> sessions = new HashMap<>();
    private HashMap<String, TeacherClass> teacherClasses = new HashMap<>();
    private HashMap<String, StudentClass> studentClasses = new HashMap<>();
    private ArrayList<Invoice> invoices = new ArrayList<>();
    private ArrayList<PaySlip> paySlips = new ArrayList<>();
    private ArrayList<String> zipcode;
    private DateTimeFormatter fm = DateTimeFormat.forPattern("yyyy-MM-dd");
    private ArrayList<String>[] errorString;
    private LocalDate today = new LocalDate();

    public CSVImporter(File file, School school) {
        this.file = file;
        errorString = new ArrayList[10];
        this.school = school;
        language = school.getLanguage();
        String[] prefix = new String[]{"08", "0120", "0121", "0122", "0123", "0124",
            "0125", "0126", "0127", "0128", "0129", "0163", "0164", "0165", "0166",
            "0167", "0168", "0169", "0188", "0199", "090", "091", "092", "093",
            "094", "095", "096", "097", "098", "099"};
        zipcode = new ArrayList<>(Arrays.asList(prefix));

        for (int i = 0; i < errorString.length; i++) {
            errorString[i] = new ArrayList<>(5);
        }
    }

    public ArrayList<String>[] startImport() throws InvalidCSVFormatException {
        System.out.println("---------------------------\nStart Importing\n");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            String s = "";

            try {
                while ((line = br.readLine()) != null) {
                    //<editor-fold defaultstate="collapsed" desc="is Line Empty">
                    if (line.isEmpty()) {
                        line = br.readLine();
                        switch (line) {
                            case CLASS_TYPE_STRING:
                                importUsers(s);
                                break;
                            case TEACHER_STRING:
                                importClassType(s);
                                break;
                            case STUDENT_STRING:
                                importTeacher(s);
                                break;
                            case CLASS_STRING:
                                importStudent(s);
                                break;
                            case SESSION_STRING:
                                importClass(s);
                                break;
                            case TEACHER_CLASS_STRING:
                                importSession(s);
                                break;
                            case STUDENT_CLASS_STRING:
                                importTeacherClass(s);
                                break;
                            case INVOICE_STRING:
                                importStudentClass(s);
                                break;
                            case PAY_SLIP_STRING:
                                importInvoice(s);
                                break;
                            case "":
                                importPaySlip(s);
                                break;
                            default:
                                throw new InvalidCSVFormatException();
                        }
                        s = "";
                    } else {
                        s += line + "\n";
                    }
                    //</editor-fold>
                }
            } catch (Exception e) {
                throw new InvalidCSVFormatException();
            }

        } catch (IOException e) {
            System.err.println("Catched Exception when import: " + e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.err.println("Cannot close file. Catched Exception: " + e.toString());
            }
        }
        validateRelationship();

        school.getClasses().putAll(classes);
        school.getTeachers().putAll(teachers);
        school.getStudents().putAll(students);
        school.getTeacherClass().putAll(teacherClasses);
        school.getStudentClass().putAll(studentClasses);
        school.getUsers().putAll(users);
        school.getSessions().putAll(sessions);
        school.getInvoices().addAll(invoices);
        school.getClassTypes().addAll(classTypes);
        school.getPaySlips().addAll(paySlips);

        school.saveData();
        school.notifyAllGUI();
        return errorString;
    }

    //<editor-fold defaultstate="collapsed" desc="import all users">
    public void importUsers(String s) throws Exception {
        s = s.substring(ACCOUNT_STRING.length() + 1, s.length());// remove first line (ACCOUNT_STRING)
//        System.out.println(s);

        String[] allUserLines = s.split("\n");
        users = new HashMap<>(allUserLines.length - 1);

        for (int i = 1; i < allUserLines.length; i++) { // start from 1 because first line is title line
            try {
                String[] userLine = allUserLines[i].split("\t,");

                String id = userLine[0];
                String pass = userLine[1];
                boolean isManager = userLine[2].equals("Manager") ? true : false;
                String[] names = getNameFromFullName(userLine[3]);
                boolean isMale = Boolean.parseBoolean(userLine[4]);
                LocalDate birth = fm.parseLocalDate(userLine[5]);
                String email = userLine[6];
                String phone = userLine[7];
                String address = "";
                if (userLine.length >= 9) {
                    address = userLine[8];
                }

                boolean isValid = true;
                if (!Validator.usernameValidate(id)) {
                    isValid = false;
                    System.out.println("username inValid");
                } else if (!school.validateUsername(id) || users.containsKey(id)) {
                    isValid = false;
                    System.out.println("username  " + id + " duplicates");
                }

                if (!Validator.passwordValidate(pass)) {
                    isValid = false;
                    System.out.println("Password inValid");
                }

                if (names.length < 2) {
                    isValid = false;
                    System.out.println("Name inValid");
                }

                if (!Validator.emailValidate(email)) {
                    isValid = false;
                    System.out.println("Email inValid");
                }

                if (!validatePhone(phone)) {
                    isValid = false;
                }
                System.out.println("asdasdasdasdasdasdads");
                if (!isValid) {
                    throw new Exception("Invalid record");
                } else {
                    User user;
                    if (isManager) {
                        user = new Manager(id, pass, names[2], names[1], names[0], email, phone, birth, address, null, isMale);
                    } else {
                        user = new Staff(id, pass, names[2], names[1], names[0], email, phone, birth, address, null, isMale);
                    }
                    System.out.println("new user is added: " + user.getId());
                    users.put(user.getId(), user);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                errorString[USER].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Account.");
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="importClassType">
    public void importClassType(String s) throws Exception {
        s = s.substring(CLASS_TYPE_STRING.length() + 1, s.length());

        String[] allCTLines = s.split("\n");
        classTypes = new ArrayList<>(allCTLines.length - 1);

        for (int i = 1; i < allCTLines.length; i++) { // start from 1 because first line is title line
            try {
                String[] line = allCTLines[i].split("\t,");

                String skill = line[0];
                int type = Integer.parseInt(line[1]);
                int lessonPerWeek = Integer.parseInt(line[2]);
                long fee45Min = Long.parseLong(line[3]);
                long fee60Min = Long.parseLong(line[4]);
                String note = line[5];

                boolean isValid = true;
                if (type < 0 || type > 2) {
                    System.out.println("ClassType's type is out of range");
                    isValid = false;
                }

                if (lessonPerWeek < 0 || lessonPerWeek > 2) {
                    System.out.println("ClassType's lessonPerWeek is out of range");
                    isValid = false;
                }

                ClassType ct = new ClassType(skill, type, lessonPerWeek, fee45Min, fee60Min, note);

                //<editor-fold defaultstate="collapsed" desc="check duplicate">
                for (int j = 0; j < school.getClassTypes().size(); j++) {// check in database
                    ClassType ctTemp = school.getClassTypes().get(j);
                    if (ctTemp.getType() == (ct.getType())
                            && ctTemp.getSkill().equalsIgnoreCase(ct.getSkill())) {
                        System.out.println("Class Type duplicates");
                        isValid = false;
                        break;
                    }
                }
                for (int j = 0; j < classTypes.size(); j++) {// check in the CSV file
                    ClassType ctTemp = classTypes.get(j);
                    if (ctTemp.getType() == (ct.getType())
                            && ctTemp.getSkill().equalsIgnoreCase(ct.getSkill())) {
                        System.out.println("Class Type duplicates");
                        isValid = false;
                        break;
                    }
                }
                //</editor-fold>

                if (!isValid) {
                    throw new Exception("Invalid record");
                } else {
                    System.out.println("new Class Type is added: " + ct.getSkill());
                    classTypes.add(ct);
                }
            } catch (Exception e) {
                errorString[CLASS_TYPE].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Class Type.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="importTeacher">
    public void importTeacher(String s) throws Exception {
        s = s.substring(TEACHER_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] teacherLines = s.split("\n");
        teachers = new HashMap<>(teacherLines.length - 1);

        for (int i = 1; i < teacherLines.length; i++) {
            try {
                String[] line = teacherLines[i].split("\t,");

                String id = line[0];
                String[] names = getNameFromFullName(line[1]);
                boolean isMale = Boolean.parseBoolean(line[2]);
                LocalDate birth = fm.parseLocalDate(line[3]);
                String skillString = line[4];
                String email = line[5];
                String phone = line[6];
                String address = "";
                if (line.length < 8) {
                    address = line[7];
                }


                if (!validatePersonId(id)) {
                    System.out.println("Invalid Person Id");
                    throw new Exception("Invalid record");
                }

                if (names.length < 2) {
                    System.out.println("Name inValid");
                    throw new Exception("Invalid record");
                }

                if (!Validator.emailValidate(email)) {
                    System.out.println("Email inValid");
                    throw new Exception("Invalid record");
                }

                if (!validatePhone(phone)) {
                    throw new Exception("Invalid record");
                }

                String[] skillsTemp = skillString.split("\\s");
                String[][] skills = new String[skillsTemp.length][2];
                for (int j = 0; j < skillsTemp.length; j++) {
                    String[] temp = skillsTemp[j].split("-");
                    skills[j][0] = temp[0];
                    skills[j][1] = temp[1];
                }

                Teacher teacher = new Teacher(id, skills, names[2], names[1], names[0], email, phone, birth, address, null, isMale);
                System.out.println("new Teacher is added: " + teacher.getId());
                teachers.put(id, teacher);
            } catch (Exception e) {
                errorString[TEACHER].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Account.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="importStudent">
    private void importStudent(String s) throws Exception {
        s = s.substring(STUDENT_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] studentLines = s.split("\n");
        students = new HashMap<>(studentLines.length - 1);

        for (int i = 1; i < studentLines.length; i++) {
            try {
                String[] line = studentLines[i].split("\t,");

                String id = line[0];
                String[] names = getNameFromFullName(line[1]);
                boolean isMale = Boolean.parseBoolean(line[2]);
                LocalDate birth = fm.parseLocalDate(line[3]);
                String email = line[4];
                String phone = line[5];
                String address = line[6];
                String contactName = line[7];
                String contactRelate = line[8];
                String contactEmail = line[9];
                String contactPhone = line[10];
                String contactAddress = line[11];

                if (!validatePersonId(id)) {
                    System.out.println("Invalid Person Id");
                    throw new Exception("Invalid record");
                }

                if (names.length < 2) {
                    System.out.println("Name inValid");
                    throw new Exception("Invalid record");
                }

                if (contactName.length() < 2) {
                    System.out.println("Contact Name invalid");
                    throw new Exception("Invalid record");
                }

                if (!Validator.emailValidate(email)) {
                    System.out.println("Email inValid");
                    throw new Exception("Invalid record");
                }

                if (!Validator.emailValidate(contactEmail)) {
                    System.out.println("Contact Email inValid");
                    throw new Exception("Invalid record");
                }

                if (!validatePhone(phone)) {
                    throw new Exception("Invalid record");
                }

                if (!validatePhone(contactPhone)) {
                    throw new Exception("Invalid record");
                }

                Student student = new Student(id, contactPhone, contactEmail, contactName,
                        contactAddress, contactRelate, names[2], names[1], names[0],
                        email, phone, birth, address, null, isMale);
                System.out.println("new Student is added: " + student.getId());
                students.put(id, student);
            } catch (Exception e) {
                errorString[STUDENT].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Account.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="import Class">
    public void importClass(String s) throws Exception {
        s = s.substring(CLASS_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] classLines = s.split("\n");
        classes = new HashMap<>(classLines.length - 1);

        for (int i = 1; i < classLines.length; i++) {
            try {
                String[] line = classLines[i].split("\t,");

                String id = line[0];
                String name = line[1];
                LocalDate startDate = fm.parseLocalDate(line[2]);
                LocalDate endDate = fm.parseLocalDate(line[3]);
                int currentStudent = Integer.parseInt(line[4]);
                long fee = Long.parseLong(line[5]);
                String textBook = line[6];
                String[] ctString = line[7].split("-");

                if (!id.matches("^(Class)[0-9]{1,10}$")) {
                    System.out.println("Invalid Class Id");
                    throw new Exception("Invalid record");
                }

                if (startDate.compareTo(endDate) >= 0) {
                    System.out.println("Invalid start and end date");
                    throw new Exception("Invalid record");
                }

                if (ctString.length != 2) {
                    System.out.println("Invalid format of class type of this class");
                    throw new Exception("Invalid record");
                }

                ClassType classTypeOfNewClass = null;
                //<editor-fold defaultstate="collapsed" desc="find classType">
                for (int j = 0; j < school.getClassTypes().size(); j++) {
                    ClassType ct = school.getClassTypes().get(j);
                    if (ct.getSkill().equals(ctString[0]) && (ct.getType() + "").equals(ctString[1])) {
                        classTypeOfNewClass = ct;
                        break;
                    }
                }
                if (classTypeOfNewClass == null) {
                    for (int j = 0; j < classTypes.size(); j++) {
                        ClassType ct = classTypes.get(j);
                        if (ct.getSkill().equals(ctString[0]) && (ct.getType() + "").equals(ctString[1])) {
                            classTypeOfNewClass = ct;
                            break;
                        }
                    }
                }
                //</editor-fold>
                if (classTypeOfNewClass == null) {
                    System.out.println("No class type found");
                    throw new Exception("Invalid record");
                }

                Class c = new Class(id, name, startDate, endDate, textBook, classTypeOfNewClass, fee);
                c.setCurrentNumberOfStudent(currentStudent);
                System.out.println("new Class is added: " + c.getClassId());
                classes.put(id, c);
            } catch (Exception e) {
//                e.printStackTrace();
                errorString[CLASS].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Class.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="import session">
    public void importSession(String s) throws Exception {
        s = s.substring(SESSION_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] sessionLines = s.split("\n");
        sessions = new HashMap<>(sessionLines.length - 1);

        for (int i = 1; i < sessionLines.length; i++) {
            try {
                String[] line = sessionLines[i].split("\t,");

                String classId = line[0];
                int roomId = Integer.parseInt(line[1]);
                String sesId = line[2];
                boolean is45 = Boolean.parseBoolean(line[3]);
                int firstTime = Integer.parseInt(line[4]);
                HashMap<Integer, Integer> times;

                if (!classes.containsKey(classId) && !school.getClasses().containsKey(classId)) {
                    System.out.println("Cannot find class for this session");
                    throw new Exception("Invalid record");
                }

                if (!school.getRooms().containsKey(roomId)) {
                    System.out.println("Cannot find room for this session");
                    throw new Exception("Invalid record");
                }

                if (!sesId.matches("^(Session)[0-9]{1,10}$")) {
                    System.out.println("Invalid Seesion Id");
                    throw new Exception("Invalid record");
                }

                if (is45) {
                    times = new HashMap<>(3);
                    times.put(firstTime, firstTime);
                    times.put(firstTime + 1, firstTime + 1);
                    times.put(firstTime + 2, firstTime + 2);
                } else {
                    times = new HashMap<>(4);
                    times.put(firstTime, firstTime);
                    times.put(firstTime + 1, firstTime + 1);
                    times.put(firstTime + 2, firstTime + 2);
                    times.put(firstTime + 3, firstTime + 3);
                }
                ClassSession ses1 = new ClassSession(sesId, classId, roomId, times, is45);

                Class c1 = school.getClasses().get(classId);
                if (classes.containsKey(classId)) {
                    c1 = classes.get(classId);
                }
                LocalDate start1 = c1.getStartDate();
                LocalDate end1 = c1.getEndDate();

                //<editor-fold defaultstate="collapsed" desc="validate sesion time">
                //<editor-fold defaultstate="collapsed" desc="check with classes">
                Iterator<Entry<String, Class>> iClass = classes.entrySet().iterator();
                while (iClass.hasNext()) {
                    Class c2 = iClass.next().getValue();
                    if (!c2.getClassId().equals(c1.getClassId())) {
                        LocalDate start2 = c2.getStartDate();
                        LocalDate end2 = c2.getEndDate();
                        if (start2.compareTo(start1) >= 0 && start2.compareTo(end1) <= 0
                                || end2.compareTo(start1) >= 0 && end2.compareTo(end1) <= 0
                                || start2.compareTo(start1) <= 0 && end2.compareTo(end1) >= 0) {

                            Iterator<Entry<String, ClassSession>> iSes = sessions.entrySet().iterator();
                            while (iSes.hasNext()) {
                                ClassSession ses2 = iSes.next().getValue();
                                if (ses2.getClassId().equals(c2.getClassId())) {
                                    if (!Validator.validateSessionTime(ses1, ses2)) {
                                        System.out.println("Session overlaps other session: " + ses2.getClassSessionId());
                                        throw new InvalidCSVFormatException();
                                    }
                                }
                            }
                            iSes = school.getSessions().entrySet().iterator();
                            while (iSes.hasNext()) {
                                ClassSession ses2 = iSes.next().getValue();
                                if (ses2.getClassId().equals(c2.getClassId())) {
                                    if (!Validator.validateSessionTime(ses1, ses2)) {
                                        System.out.println("Session overlaps other session: " + ses2.getClassSessionId());
                                        throw new InvalidCSVFormatException();
                                    }
                                }
                            }
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="check with school.getClasses">
                iClass = school.getClasses().entrySet().iterator();
                while (iClass.hasNext()) {
                    Class c2 = iClass.next().getValue();
                    if (!c2.getClassId().equals(c1.getClassId())) {
                        LocalDate start2 = c2.getStartDate();
                        LocalDate end2 = c2.getEndDate();
                        if (start2.compareTo(start1) >= 0 && start2.compareTo(end1) <= 0
                                || end2.compareTo(start1) >= 0 && end2.compareTo(end1) <= 0
                                || start2.compareTo(start1) <= 0 && end2.compareTo(end1) >= 0) {

                            Iterator<Entry<String, ClassSession>> iSes = sessions.entrySet().iterator();
                            while (iSes.hasNext()) {
                                ClassSession ses2 = iSes.next().getValue();
                                if (ses2.getClassId().equals(c2.getClassId())) {
                                    if (!Validator.validateSessionTime(ses1, ses2)) {
                                        System.out.println("Session overlaps other session: " + ses2.getClassSessionId());
                                        throw new InvalidCSVFormatException();
                                    }
                                }
                            }
                            iSes = school.getSessions().entrySet().iterator();
                            while (iSes.hasNext()) {
                                ClassSession ses2 = iSes.next().getValue();
                                if (ses2.getClassId().equals(c2.getClassId())) {
                                    if (!Validator.validateSessionTime(ses1, ses2)) {
                                        System.out.println("Session overlaps other session: " + ses2.getClassSessionId());
                                        throw new InvalidCSVFormatException();
                                    }
                                }
                            }
                        }
                    }
                }
                //</editor-fold>
                //</editor-fold>

                System.out.println("new Session is added: " + ses1.getClassSessionId());
                sessions.put(sesId, ses1);
            } catch (Exception e) {
//                e.printStackTrace();
                errorString[SESSION].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Session.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="import TeacherClass">
    public void importTeacherClass(String s) throws Exception {
        s = s.substring(TEACHER_CLASS_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] tcLines = s.split("\n");
        teacherClasses = new HashMap<>(tcLines.length - 1);

        for (int i = 1; i < tcLines.length; i++) {
            try {
                String[] line = tcLines[i].split("\t,");

                String id = line[0];
                String teacherId = line[1];
                String classId = line[2];

                if (!id.matches("^(TC)[0-9]{1,10}$")) {
                    System.out.println("Invalid TeacherClass Id");
                    throw new Exception("Invalid record");
                }

                if (!classes.containsKey(classId) && !school.getClasses().containsKey(classId)) {
                    System.out.println("Cannot find class for this teacherClass");
                    throw new Exception("Invalid record");
                }

                if (!teachers.containsKey(teacherId) && !school.getTeachers().containsKey(teacherId)) {
                    System.out.println("Cannot find Teacher for this teacherClass");
                    throw new Exception("Invalid record");
                }

                TeacherClass tc = new TeacherClass(id, teacherId, classId);
                System.out.println("new TeacherClass is added: " + tc.getId());
                teacherClasses.put(id, tc);
            } catch (Exception e) {
//                e.printStackTrace();
                errorString[TEACHER_CLASS].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table TeacherClass.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="import studentClass">
    public void importStudentClass(String s) throws Exception {
        s = s.substring(STUDENT_CLASS_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] scLines = s.split("\n");
        studentClasses = new HashMap<>(scLines.length - 1);

        for (int i = 1; i < scLines.length; i++) {
            try {
                String[] line = scLines[i].split("\t,");

                String id = line[0];
                String classId = line[1];
                String studentId = line[2];
                boolean isPaid = Boolean.parseBoolean(line[3]);
                boolean invoiced = Boolean.parseBoolean(line[4]);

                if (!id.matches("^(Enrollment)[0-9]{1,10}$")) {
                    System.out.println("Invalid StudentClass Id");
                    throw new Exception("Invalid record");
                }

                if (!classes.containsKey(classId) && !school.getClasses().containsKey(classId)) {
                    System.out.println("Cannot find class for this studentClass");
                    throw new Exception("Invalid record");
                }

                if (!students.containsKey(studentId) && !school.getTeacherClass().containsKey(studentId)) {
                    System.out.println("Cannot find Student for this studentClass");
                    throw new Exception("Invalid record");
                }

                StudentClass sc = new StudentClass(id, studentId, classId, isPaid, invoiced);
                System.out.println("new StudentClass is added: " + sc.getId());
                studentClasses.put(id, sc);
            } catch (Exception e) {
//                e.printStackTrace();
                errorString[STUDENT_CLASS].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table StudentClass.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="importInvoice">
    public void importInvoice(String s) throws Exception {
        s = s.substring(INVOICE_STRING.length() + 1, s.length());
//        System.out.println(s);

        String[] invoiceLine = s.split("\n");
        invoices = new ArrayList<>(invoiceLine.length - 1);

        for (int i = 1; i < invoiceLine.length; i++) {
            try {
                String[] line = invoiceLine[i].split("\t,");

                String id = line[0];
                String[] scString = line[1].split("-");
                LocalDate paidDate;
                if (!line[2].equals("")) {
                    paidDate = fm.parseLocalDate(line[2]);
                } else {
                    paidDate = null;
                }
                String paidMethod = line[3];
                String paidNote = line[4];
                long totalFee = Long.parseLong(line[5]);
                long balance = Long.parseLong(line[6]);

                // validate invoice id
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyMMdd");
                String studentId = id.substring(0, 7);
                String dateId = id.substring(8, 14);
                LocalDate dateTemp = fmt.parseLocalDate(dateId);
                if (id.length() > 15 || id.length() < 14) {
                    System.out.println("Invalid Invoice id");
                    throw new Exception("Invalid record");
                }
                if (!validatePersonId(studentId)) {
                    System.out.println("Invalid Student Id invoice");
                    throw new Exception("Invalid record");
                }
                if (dateTemp.compareTo(today) > 0) {
                    System.out.println("Invoice Id invalid. date of invoice > today");
                    throw new Exception("Invalid record");
                }
                // done validate invoice id

                if (totalFee < 0 || balance < 0) {
                    System.out.println("Total fee is invalid or Balance is invalid");
                    throw new Exception("Invalid record");
                }

                ArrayList<StudentClass> sc = new ArrayList<>(scString.length);
                for (int j = 0; j < scString.length; j++) {
                    String scId = scString[j];
                    if (!studentClasses.containsKey(scId) && !school.getStudentClass().containsKey(scId)) {
                        System.out.println("Cannot find student class for this invoice");
                        throw new Exception("Invalid record");
                    }
                    StudentClass scTemp = studentClasses.get(scId);
                    if (scTemp == null) {
                        scTemp = school.getStudentClass().get(scId);
                    }
                    sc.add(scTemp);
                }

                //find existed invoice
                int existIndex = -1;
                for (int j = 0; j < invoices.size(); j++) {
                    if (invoices.get(j).getId().equals(id)) {
                        existIndex = j;
                    }
                }

                for (int j = 0; j < school.getInvoices().size(); j++) {
                    if (school.getInvoices().get(j).getId().equals(id)) {
                        existIndex = j;
                    }
                }

                Invoice invoice = new Invoice(id, sc, paidDate, paidMethod, paidNote, totalFee, balance);
                System.out.println("new Invoice is added: " + invoice.getId());
                if (existIndex != -1) {
                    System.out.println("Invoice duplicates");
                    invoices.set(existIndex, invoice);
                } else {
                    invoices.add(invoice);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                errorString[INVOICE].add(s("lineNumber") + " " + i + " " + s("invalidCSV"));
                System.err.println("Invalid record number " + i + " in table Invoice.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="importPaySlip">
    public void importPaySlip(String s) throws Exception {
//        System.out.println("importing pay slip");
//        System.out.println(s + "\n");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getNameFromFullName">
    private String[] getNameFromFullName(String fullName) throws Exception {
        String[] fullname = fullName.split("\\s+");
        String firstname = fullname[0];
        String lastname = fullname[fullname.length - 1];
        String middlename = "";
        for (int i = 1; i < fullname.length - 1; i++) {
            middlename += fullname[i];
        }

        String[] names = new String[3];
        names[0] = firstname;
        names[1] = middlename;
        names[2] = lastname;
        return names;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="phone validator">
    private boolean validatePhone(String phone) {
        if (!phone.matches("^[0-9 \\- ]{15,17}$")) {
            System.out.println("Phone contains invalid characters or wrong length");
            return false;
        }

        String[] temp = phone.split("\\s-\\s");

        if (temp.length != 3) {
            System.out.println("Invalid phone format");
            return false;
        }

        if (!zipcode.contains(temp[0])) {
            System.out.println("Wrong zip code");
            return false;
        }

        if (temp[0].equals("08")) {
            if (temp[1].length() != 4 || temp[2].length() != 4) {
                return false;
            }
        } else {
            if (temp[1].length() != 3 || temp[2].length() != 4) {
                return false;
            }
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="validatePersonId">
    private boolean validatePersonId(String id) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyMMdd");
        if (id.length() != 7) {
            return false;
        }
        try {
            String dateString = id.substring(0, 4) + "01";
            LocalDate date = dtf.parseLocalDate(dateString);

            if (date.getYearOfCentury() > today.getYearOfCentury()) {
                System.out.println("Person id is generated in future");
                return false;
            } else if (date.getYearOfCentury() == today.getYearOfCentury()) {
                int subId = Integer.parseInt(id.substring(4, 7));
                if (subId <= 0) {
                    return false;
                }
                if (subId >= Person.getCurrentId()) {
                    System.out.println("this.person.id >= Person.currentId");
                    Person.setCurrentId(subId + 1);
                }
            } else {
                // not supported yet.
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void increasePersonId(String id) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="validateRelationship">
    private void validateRelationship() {
        Iterator<Entry<String, Class>> iterClass = classes.entrySet().iterator();
        while (iterClass.hasNext()) {
            Class c = iterClass.next().getValue();
            boolean isClassHasTeacher = false;

            Iterator<Entry<String, TeacherClass>> iterTC = teacherClasses.entrySet().iterator();
            while (iterTC.hasNext()) {
                TeacherClass tc = iterTC.next().getValue();
                if (tc.getClassId().equals(c.getClassId())) {
                    isClassHasTeacher = true;
                }
            }

            iterTC = school.getTeacherClass().entrySet().iterator();
            while (iterTC.hasNext()) {
                TeacherClass tc = iterTC.next().getValue();
                if (tc.getClassId().equals(c.getClassId())) {
                    isClassHasTeacher = true;
                }
            }

            if (!isClassHasTeacher) {// if Class does not have teacher -> remove class
                System.out.println("Class " + c.getClassId() + " does not have teacher. So it is remove");
                iterClass.remove();
            }
        }

        //remove all sessions in School that have same classId with imported session
        Iterator<Entry<String, ClassSession>> iterSes1 = sessions.entrySet().iterator();
        while (iterSes1.hasNext()) {
            ClassSession ses1 = iterSes1.next().getValue();
            Iterator<Entry<String, ClassSession>> iterSes2 = school.getSessions().entrySet().iterator();
            while (iterSes2.hasNext()) {
                ClassSession ses2 = iterSes2.next().getValue();
                if (ses1.getClassId().equals(ses2.getClassId())) {
                    iterSes2.remove();
                }
            }
        }

        //remove all teacherClass that have same teacher and class
        Iterator<Entry<String, TeacherClass>> iterTC1 = teacherClasses.entrySet().iterator();
        while (iterTC1.hasNext()) {
            TeacherClass tc1 = iterTC1.next().getValue();
            Iterator<Entry<String, TeacherClass>> iterTC2 = school.getTeacherClass().entrySet().iterator();
            while (iterTC2.hasNext()) {
                TeacherClass tc2 = iterTC2.next().getValue();
                if (tc2.getClassId().equals(tc1.getClassId()) && tc2.getTeacherId().equals(tc1.getTeacherId())) {
                    iterTC2.remove();
                }
            }
        }

        //remove all studentClass that have same student and class
        Iterator<Entry<String, StudentClass>> iterSC1 = studentClasses.entrySet().iterator();
        while (iterSC1.hasNext()) {
            StudentClass sc1 = iterSC1.next().getValue();
            Iterator<Entry<String, StudentClass>> iterSC2 = school.getStudentClass().entrySet().iterator();
            while (iterSC2.hasNext()) {
                StudentClass sc2 = iterSC2.next().getValue();
                if (sc2.getClassId().equals(sc1.getClassId()) && sc2.getStudentId().equals(sc1.getStudentId())) {
                    iterSC2.remove();
                }
            }
        }
    }
    //</editor-fold>

    private String s(String key) {
        return language.getString(key);
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public ArrayList<ClassType> getClassTypes() {
        return classTypes;
    }

    public HashMap<String, Teacher> getTeachers() {
        return teachers;
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public HashMap<String, Class> getClasses() {
        return classes;
    }

    public HashMap<String, ClassSession> getSessions() {
        return sessions;
    }

    public HashMap<String, TeacherClass> getTeacherClasses() {
        return teacherClasses;
    }

    public HashMap<String, StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public ArrayList<PaySlip> getPaySlips() {
        return paySlips;
    }
}
