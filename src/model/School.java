package model;

import exeception.InvalidCSVFormatException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JOptionPane;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class School extends Observable {

    private HashMap<String, Teacher> teachers;
    private HashMap<String, Student> students;
    private HashMap<String, Class> classes;
    private HashMap<Integer, Room> rooms;
    private HashMap<String, User> users;
    private HashMap<String, ClassSession> sessions;
    private HashMap<String, TeacherClass> teacherClass;
    private HashMap<String, StudentClass> studentClass;
    private ArrayList<Invoice> invoices;
    private ArrayList<ClassType> classTypes;
    private ArrayList<PaySlip> paySlips;
    private boolean isEnglish = true;
    private ResourceBundle en = ResourceBundle.getBundle("en");
    private ResourceBundle vi = ResourceBundle.getBundle("vi");
    private final URL DEFAULT_FILE_LOCATION = School.class.getProtectionDomain().getCodeSource().getLocation();
    private File file;

    public School() {
//        System.out.println("Source code location: " + DEFAULT_FILE_LOCATION);
        try {
            String dbLocation = new File(DEFAULT_FILE_LOCATION.toURI()).getParentFile().getPath() + "/database.elephant";
//            System.out.println("Database file location: " + dbLocation);
            file = new File(dbLocation);
        } catch (Exception e) {
            System.out.println("Exception in contructor School: " + e.toString());
        }
        loadData();
    }

    // <editor-fold defaultstate="collapsed" desc="check ID Password">
    public Object checkIDPass(String username, char[] password) {
        username = username.toLowerCase();
        String pass = new String(password);
        if (users.containsKey(username) && users.get(username).getPassword().equals(pass)) {
            if (users.get(username).isActive()) {
                return users.get(username);
            } else {
                return new String();
            }
        } else {
            return null;
        }
    }

    public boolean validateUsername(String username) {
        username = username.toLowerCase();
        if (users.containsKey(username)) {
            return false;
        } else {
            return true;
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter and setter methods">
    public ArrayList<Invoice> getInvoices() {
        invoices.trimToSize();
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices1) {
        this.invoices = invoices1;
    }

    public HashMap<String, TeacherClass> getTeacherClass() {
        return teacherClass;
    }

    public void setTeacherClass(HashMap<String, TeacherClass> teacherClass) {
        this.teacherClass = teacherClass;
    }

    public HashMap<String, ClassSession> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, ClassSession> sessions) {
        this.sessions = sessions;
    }

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public void setStudents(HashMap<String, Student> students) {
        this.students = students;
    }

    public HashMap<String, Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(HashMap<String, Teacher> teachers) {
        this.teachers = teachers;
    }

    public HashMap<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public HashMap<String, StudentClass> getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(HashMap<String, StudentClass> studentClass) {
        this.studentClass = studentClass;
    }

    public ArrayList<ClassType> getClassTypes() {
        return classTypes;
    }

    public ClassType getClassType(Class c) {
        String skill = c.getClassType()[0];
        int type = Integer.parseInt(c.getClassType()[1]);

        for (int i = 0; i < classTypes.size(); i++) {
            ClassType ct = classTypes.get(i);
            if (ct.getType() == type
                    && ct.getSkill().equalsIgnoreCase(skill)) {
                return ct;
            }
        }
        return null;
    }

    public void setClassTypes(ArrayList<ClassType> classTypes) {
        this.classTypes = classTypes;
    }

    public ArrayList<PaySlip> getPaySlips() {
        return paySlips;
    }

    public ArrayList<PaySlip> getPaySlipsOfTeacher(String teacherId) {
        ArrayList<PaySlip> psToSend = new ArrayList<>(paySlips.size());
        for (int i = 0; i < paySlips.size(); i++) {
            if (paySlips.get(i).getTeacherId().equals(teacherId)) {
                psToSend.add(paySlips.get(i));
            }
        }
        psToSend.trimToSize();
        return psToSend;
    }

    public void setPaySlips(ArrayList<PaySlip> paySlips) {
        this.paySlips = paySlips;
    }

    public ResourceBundle getLanguage() {
        if (isEnglish) {
            return en;
        } else {
            return vi;
        }
    }

    public void setLanguage(boolean isEnglish) {
        this.isEnglish = isEnglish;
    }

    public boolean isEnglish() {
        return isEnglish;
    }

    public HashMap<String, Class> getClassAssociatedTeacher(Teacher t) {
        HashMap<String, Class> classesToReturn = new HashMap<>();
        Iterator<Entry<String, TeacherClass>> iter = teacherClass.entrySet().iterator();

        while (iter.hasNext()) {
            TeacherClass tc = iter.next().getValue();
            if (tc.getTeacherId().equals(t.getId())) {
                Class c = classes.get(tc.getClassId());
                classesToReturn.put(c.getClassId(), c);
            }
        }
        return classesToReturn;
    }

    public HashMap<String, Class> getClassAssociatedStudent(Student s) {
        HashMap<String, Class> classesToReturn = new HashMap<>();
        Iterator<Entry<String, StudentClass>> iter = studentClass.entrySet().iterator();

        while (iter.hasNext()) {
            StudentClass sc = iter.next().getValue();
            if (sc.getStudentId().equals(s.getId())) {
                Class c = classes.get(sc.getClassId());
                classesToReturn.put(c.getClassId(), c);
            }
        }
        return classesToReturn;
    }

    public ArrayList<StudentClass> getUnpaidStudentClass() {
        ArrayList<StudentClass> unpaidSC = new ArrayList<>(studentClass.size());

        Iterator<Entry<String, StudentClass>> iter = studentClass.entrySet().iterator();
        while (iter.hasNext()) {
            StudentClass sc = iter.next().getValue();
            Class c = classes.get(sc.getClassId());
            if (!sc.isPaid() && (c.getStartDate().compareTo(new LocalDate()) <= 0)) {
                unpaidSC.add(sc);
            }
        }

        unpaidSC.trimToSize();
        return unpaidSC;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="add new object methods">
    public void addTeacherClass(TeacherClass teacherC) {
        teacherClass.put(teacherC.getId(), teacherC);
        saveData();
    }

    public void addTeacherClass(HashMap<String, TeacherClass> tc) {
        teacherClass.putAll(tc);
        saveData();
    }

    public boolean addStudentClass(StudentClass sc) {
        Class c = classes.get(sc.getClassId());
        ClassType ct = getClassType(c);

        //check capacity of class
        switch (ct.getType()) {
            case 0:
                if (c.getCurrentNumberOfStudent() >= 1) {
                    return false;
                }
                break;
            case 1:
                if (c.getCurrentNumberOfStudent() >= 2) {
                    return false;
                }
                break;
            default:
                if (c.getCurrentNumberOfStudent() >= 20) {
                    return false;
                }
                break;
        }

        studentClass.put(sc.getId(), sc);
        c.setCurrentNumberOfStudent(c.getCurrentNumberOfStudent() + 1);
        saveData();
        return true;
    }

    public void addSession(ClassSession session) {
        sessions.put(session.getClassSessionId(), session);
        setChanged();
        notifyObservers("Room");
        saveData();
    }

    public void addSession(HashMap<String, ClassSession> sessionsToAdd) {
        sessions.putAll(sessionsToAdd);
        setChanged();
        notifyObservers("Room");
        saveData();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        setChanged();
        notifyObservers("User");
        saveData();
    }

    public void addStudent(Student st) {
        students.put(st.getId(), st);
        setChanged();
        notifyObservers("Student");
        saveData();
    }

    public void addClass(Class c) {
        classes.put(c.getClassId(), c);
        setChanged();
        notifyObservers("Class");
        saveData();
    }

    public void addTeacer(Teacher t) {
        teachers.put(t.getId(), t);
        setChanged();
        notifyObservers("Teacher");
        saveData();
    }

    public boolean addClassType(ClassType ct) {
        //check duplicate ClassType Skill and Type
        for (int i = 0; i < classTypes.size(); i++) {
            ClassType ctTemp = classTypes.get(i);

            if (ctTemp.getType() == (ct.getType())
                    && ctTemp.getSkill().equalsIgnoreCase(ct.getSkill())) {
                return false;
            }
        }

        classTypes.add(ct);
        setChanged();
        notifyObservers("Class Type");
        saveData();
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="save load data">
    private void loadData() {
        ObjectInputStream input = null;
        try {
            FileInputStream fileInput = new FileInputStream(file);
            input = new ObjectInputStream(fileInput);
            teachers = (HashMap<String, Teacher>) input.readObject();
            students = (HashMap<String, Student>) input.readObject();
            classes = (HashMap<String, Class>) input.readObject();
            rooms = (HashMap<Integer, Room>) input.readObject();
            users = (HashMap<String, User>) input.readObject();
            sessions = (HashMap<String, ClassSession>) input.readObject();
            teacherClass = (HashMap<String, TeacherClass>) input.readObject();
            studentClass = (HashMap<String, StudentClass>) input.readObject();
            invoices = (ArrayList<Invoice>) input.readObject();
            classTypes = (ArrayList<ClassType>) input.readObject();
            paySlips = (ArrayList<PaySlip>) input.readObject();
            Person.setCurrentId(input.readInt());
            Class.setCurrentId(input.readInt());
            ClassSession.setCurrentId(input.readInt());
            TeacherClass.setCurrentId(input.readInt());
            StudentClass.setCurrentId(input.readInt());
            PaySlip.setCurrentId(input.readInt());
            isEnglish = input.readBoolean();
            Template.changeTheme(input.readUTF());
        } catch (Exception e) {
            System.out.println(e.toString());
            teachers = new HashMap<>();
            students = new HashMap<>();
            classes = new HashMap<>();
            rooms = new HashMap<>();
            users = new HashMap<>();
            sessions = new HashMap<>();
            teacherClass = new HashMap<>();
            studentClass = new HashMap<>();
            invoices = new ArrayList<>();
            classTypes = new ArrayList<>();
            paySlips = new ArrayList<>();
            Person.setCurrentId(1);
            Class.setCurrentId(1);
            ClassSession.setCurrentId(1);
            TeacherClass.setCurrentId(1);
            StudentClass.setCurrentId(1);
            PaySlip.setCurrentId(1);
            isEnglish = true;
            Template.changeTheme("Summer");
            Manager m = new Manager("manager", Constants.DEFAULT_PASSWORD,
                    "Default", "", "Manager", "defaultmanager@themusicschool.edu.vn",
                    "08 - 1111 - 1111", new LocalDate(2012, 12, 21), "", null, true);
            users.put(m.getId(), m);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void saveData() {
        ObjectOutputStream output = null;
        try {
            if (file == null) {
                JOptionPane.showMessageDialog(null, "Fatal ERROR: file database."
                        + "elephant not found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(teachers);
            output.writeObject(students);
            output.writeObject(classes);
            output.writeObject(rooms);
            output.writeObject(users);
            output.writeObject(sessions);
            output.writeObject(teacherClass);
            output.writeObject(studentClass);
            output.writeObject(invoices);
            output.writeObject(classTypes);
            output.writeObject(paySlips);
            output.writeInt(Person.getCurrentId());
            output.writeInt(Class.getCurrentId());
            output.writeInt(ClassSession.getCurrentId());
            output.writeInt(TeacherClass.getCurrentId());
            output.writeInt(StudentClass.getCurrentId());
            output.writeInt(PaySlip.getCurrentId());
            output.writeBoolean(isEnglish);
            output.writeUTF(Template.getCurrentTheme());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="selectAll methods">
    public void selectAllUser() {
        Set keySet = users.keySet();
        Iterator<String> iter = keySet.iterator();
        boolean allSelected = true;
        while (iter.hasNext()) {
            if (!users.get(iter.next()).isSelected()) {
                allSelected = false;
                break;
            }
        }

        iter = keySet.iterator();
        while (iter.hasNext()) {
            users.get(iter.next()).setSelected(!allSelected);
        }
        setChanged();
        notifyObservers("User");
    }

    public void selectAllTeacher() {
        Set keySet = teachers.keySet();
        Iterator<String> iter = keySet.iterator();
        boolean allSelected = true;
        while (iter.hasNext()) {
            if (!teachers.get(iter.next()).isSelected()) {
                allSelected = false;
                break;
            }
        }

        iter = keySet.iterator();
        while (iter.hasNext()) {
            teachers.get(iter.next()).setSelected(!allSelected);
        }
        setChanged();
        notifyObservers("Teacher");
    }

    public void selectAllClass() {
        Set keySet = classes.keySet();
        Iterator<String> iter = keySet.iterator();
        boolean allSelected = true;
        while (iter.hasNext()) {
            if (!classes.get(iter.next()).isSelected()) {
                allSelected = false;
                break;
            }
        }

        iter = keySet.iterator();
        while (iter.hasNext()) {
            classes.get(iter.next()).setSelected(!allSelected);
        }
        setChanged();
        notifyObservers("Class");
    }

    public void selectAllStudent() {
        Set keySet = students.keySet();
        Iterator<String> iter = keySet.iterator();
        boolean allSelected = true;
        while (iter.hasNext()) {
            if (!students.get(iter.next()).isSelected()) {
                allSelected = false;
                break;
            }
        }

        iter = keySet.iterator();
        while (iter.hasNext()) {
            students.get(iter.next()).setSelected(!allSelected);
        }
        setChanged();
        notifyObservers("Student");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="delete selected methods">
    public void deleteSeletedUser() {
        Set keySet = users.keySet();
        Iterator<String> iter = keySet.iterator();
        //ask user for confirmation
        String[] options = {"Sure", "No"};
        int choice = JOptionPane.showOptionDialog(null,
                "Are you sure you want to delete selected accounts?", "Delete Selected Accounts",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[1]);
        if (choice == JOptionPane.OK_OPTION) {

            while (iter.hasNext()) {
                User temp = users.get(iter.next());
                if (temp.isSelected()) {
                    users.remove(temp.getId());
                }
            }
            setChanged();
            notifyObservers("User");
        }
        saveData();
    }

//    public void deleteSeletedClass() {
//        Set keySet = classes.keySet();
//        Iterator<String> iter = keySet.iterator();
//        //ask user for confirmation
//        String[] options = {"Sure", "No"};
//        int choice = JOptionPane.showOptionDialog(null,
//                "Are you sure you want to delete selected classes?", "Delete Selected Classes",
//                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
//                null, options, options[1]);
//        if (choice == JOptionPane.OK_OPTION) {
//
//            while (iter.hasNext()) {
//                Class temp = classes.get(iter.next());
//                if (temp.isSelected()) {
//                    classes.remove(temp.getClassId());
//                }
//            }
//            setChanged();
//            notifyObservers("Class");
//        }
//        saveData();
//    }
    public boolean deleteSeletedTeacher() {
        Iterator<Entry<String, Teacher>> iter = teachers.entrySet().iterator();
        while (iter.hasNext()) {
            Teacher temp = iter.next().getValue();
            if (!deleteTeacher(temp)) {
                return false;
            }
        }
        setChanged();
        notifyObservers("Teacher");
        saveData();
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="activate/deactivate">
    public void activate(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            user.setIsActive(!user.isActive());
            setChanged();
            notifyObservers("User");
        } else if (o instanceof Teacher) {
            Teacher teacher = (Teacher) o;
            teacher.setIsActive(!teacher.isActive());
            setChanged();
            notifyObservers("Teacher");
        } else if (o instanceof Student) {
            Student student = (Student) o;
            student.setIsActive(!student.isActive());
            setChanged();
            notifyObservers("Student");
        }
        saveData();
    }

    public void setIsActiveSelected(String identifier, boolean isActive) {
        Set keySet;
        switch (identifier) {
            case "User":
                keySet = users.keySet();
                Iterator<String> iterUser = keySet.iterator();
                while (iterUser.hasNext()) {
                    User temp = users.get(iterUser.next());
                    if (temp.isSelected()) {
                        temp.setIsActive(isActive);
                    }
                }
                setChanged();
                notifyObservers(identifier);
                break;
            case "Teacher":
                keySet = teachers.keySet();
                Iterator<String> iterTeacher = keySet.iterator();
                while (iterTeacher.hasNext()) {
                    Teacher temp = teachers.get(iterTeacher.next());
                    if (temp.isSelected()) {
                        temp.setIsActive(isActive);
                    }
                }
                setChanged();
                notifyObservers(identifier);
                break;
            case "Student":
                keySet = students.keySet();
                Iterator<String> iterStudent = keySet.iterator();
                while (iterStudent.hasNext()) {
                    Student temp = students.get(iterStudent.next());
                    if (temp.isSelected()) {
                        temp.setIsActive(isActive);
                    }
                }
                setChanged();
                notifyObservers(identifier);
                break;
        }
        saveData();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="delete object">
    public boolean deleteUser(String userId) {
        users.remove(userId);
        setChanged();
        notifyObservers("User");
        saveData();
        return true;
    }

    public boolean deleteClass(Class c) {
        LocalDate today = new LocalDate();
        if (c.getEndDate().compareTo(today) >= 0 && c.getStartDate().compareTo(today) < 0) {
            return false;
        }

        //remove studentClass
        Iterator<String> iter = studentClass.keySet().iterator();
        // construct a new collection of StudentClass,
        // in order to delete to make use of the below method.
        // initialSize = avg number of StudentClass per class
        ArrayList<StudentClass> scToDelete = new ArrayList<>(studentClass.size() / classes.size());
        while (iter.hasNext()) {
            StudentClass sc = studentClass.get(iter.next());
            if (sc.getClassId().equals(c.getClassId())) {
                scToDelete.add(sc);
            }
        }
        scToDelete.trimToSize();
        for (int i = 0; i < scToDelete.size(); i++) {
            deleteStudentClass(scToDelete.get(i));
        }
        //done remove studentClass

        // remove studentClass has to be finished before remove class
        classes.remove(c.getClassId());

        //remove teacher_class
        iter = teacherClass.keySet().iterator();
        while (iter.hasNext()) {
            TeacherClass tc = teacherClass.get(iter.next());
            if (tc.getClassId().equals(c.getClassId())) {
                iter.remove();
                break;
            }
        }

        //remove session
        iter = sessions.keySet().iterator();
        while (iter.hasNext()) {
            ClassSession ses = sessions.get(iter.next());
            if (ses.getClassId().equals(c.getClassId())) {
                iter.remove();
            }
        }

        setChanged();
        notifyObservers("Class");
        saveData();
        return true;
    }

    public boolean deleteTeacher(Teacher t) {
        Iterator<Entry<String, TeacherClass>> iter = teacherClass.entrySet().iterator();
        while (iter.hasNext()) {
            TeacherClass tc = iter.next().getValue();
            if (tc.getTeacherId().equals(t.getId())) {
                return false;
            }
        }
        teachers.remove(t.getId());
        setChanged();
        notifyObservers("Teacher");
        saveData();
        return true;
    }

    public boolean deleteSessionRelatedToClass(String classId) {
        Iterator<Entry<String, ClassSession>> iter = sessions.entrySet().iterator();
        while (iter.hasNext()) {
            if (iter.next().getValue().getClassId().equals(classId)) {
                iter.remove();
            }
        }
        saveData();
        return true;
    }

    public boolean deleteStudentClass(StudentClass sc) {
        studentClass.remove(sc.getId());
        Class c = classes.get(sc.getClassId());
        Student s = students.get(sc.getStudentId());

        int newCurrentStudent = c.getCurrentNumberOfStudent() - 1;
        if (newCurrentStudent < 0) {
            newCurrentStudent = 0;
        }
        c.setCurrentNumberOfStudent(newCurrentStudent);
        // flag to check if this student has paid the class but un-enroll
        // if this student unPaid and un-enroll, no balance is added
        boolean allowAddBalance = false;

        for (int i = 0; i < invoices.size(); i++) {
            Invoice inv = invoices.get(i);
            System.out.println("Begin of the loop: Invocie " + inv.getId() + " isPaid = " + inv.isPaid());

            ArrayList<StudentClass> thisSC = inv.getStudentClass();
            //checks if s.id == student'is of this invoice
            if (s.getId().equals(thisSC.get(0).getStudentId())) {
                allowAddBalance = inv.isPaid();
                if (thisSC.remove(sc)) {// if this sc is the needed one
                    if (inv.getStudentClass().isEmpty()) {
                        deleteInvoice(inv);
                    } else {
                        inv.reduceFee(c, getClassType(c));
                    }
                    System.out.println("Invocie " + inv.getId() + " isPaid = " + inv.isPaid());
                    System.out.println("Delete StudentClass in Model:"
                            + " Delete class in invoice done: Class " + sc.getClassId());
                    break;
                }
                allowAddBalance = false;
            }
        }

        if (c.getStartDate().compareTo(new LocalDate()) <= 0 && allowAddBalance) {
            System.out.println("Student receive money back");
            s.setBalance(s.getBalance() + c.getFee());
        }

        setChanged();
        notifyObservers("Invoice");
        saveData();
        return true;
    }

    public boolean deleteTeacherClass(TeacherClass tc) {
        Class c = classes.get(tc.getClassId());
        if (c.getEndDate().compareTo(new LocalDate()) >= 0) {
            return false;
        }
        teacherClass.remove(tc.getId());
        saveData();
        return true;
    }

    public boolean deleteInvoice(Invoice inv) {
        ArrayList<StudentClass> scList = inv.getStudentClass();
        if (!scList.isEmpty()) {
            for (int i = 0; i < scList.size(); i++) {
                scList.get(i).setInvoiced(false);
            }
        }

        invoices.remove(inv);
        saveData();
        setChanged();
        notifyObservers("Invoice");
        return true;
    }

    public boolean deleteClassType(ClassType ct) {
        Iterator<Entry<String, Class>> iter = classes.entrySet().iterator();
        while (iter.hasNext()) {
            Class c = iter.next().getValue();
            //check if any class uses this class type
            if (c.getClassType()[0].equalsIgnoreCase(ct.getSkill())
                    && Integer.parseInt(c.getClassType()[1]) == ct.getType()) {
                return false;
            }
        }

        Iterator<Entry<String, Teacher>> iterTeacher = teachers.entrySet().iterator();
        while (iterTeacher.hasNext()) {
            Teacher t = iterTeacher.next().getValue();
            for (int i = 0; i < t.getSkills().length; i++) {
                if (t.getSkills()[i][0].equals(ct.getSkill())) {
                    return false;
                }
            }
        }

        classTypes.remove(ct);
        saveData();
        setChanged();
        notifyObservers("Class Type");
        return true;
    }

    public boolean deletePaySlip(PaySlip ps) {
        paySlips.remove(ps);
        saveData();
        setChanged();
        notifyObservers("Pay Slip");
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="reset password">
    public boolean resetPassword(User user) {
        user.setPassword(Constants.DEFAULT_PASSWORD);
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="changeTheme">
    public void changeTheme(int theme) {
        if (theme == 1) {
            Template.changeTheme("Wood");
        } else {
            Template.changeTheme("Ocean");
        }
        saveData();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="filterStudent">
    public HashMap<String, Student> filterStudent(String filter, HashMap<String, Student> studentToFilter) {
        HashMap<String, Student> result = new HashMap<>();

        Iterator<Entry<String, Student>> iter = studentToFilter.entrySet().iterator();
        while (iter.hasNext()) {
            Student student = iter.next().getValue();
            if (student.toString().toLowerCase().contains(filter.toLowerCase())) {
                result.put(student.getId(), student);
            }
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generate invoice">
    public Invoice generateInvoice(Student student) {
        Invoice invoice;

        // <editor-fold defaultstate="collapsed" desc="get id">
        String id = student.getId();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyMMdd");
        id += "-" + fmt.print(new LocalDate());
        //</editor-fold>

        //find duplicated invoice id
        int maxDuplicate = 0;
        boolean isDuplicated = false;
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i) != null) {
                String oldInvoiceId = invoices.get(i).getId();
                String subInvoiceId = oldInvoiceId.substring(0, 14);
                System.out.println("School line 773: subInvoiceId = " + subInvoiceId);

                if (subInvoiceId.equals(id)) {
                    int duplicate;
                    if (oldInvoiceId.length() > 14) {
                        duplicate = Integer.parseInt(invoices.get(i).getId().substring(14, 15));
                    } else {
                        duplicate = 0;
                    }

                    if (duplicate > maxDuplicate) {
                        maxDuplicate = duplicate;
                    }
                    isDuplicated = true;
                }
            }
        }
        maxDuplicate++;
        if (isDuplicated) {
            id += maxDuplicate;
        }

        // find studentClass to add to invoice
        //<editor-fold defaultstate="collapsed" desc="find studentClass">
        ArrayList<StudentClass> scToInvoice = new ArrayList<>(studentClass.size());
        Iterator<Entry<String, StudentClass>> iterSC = studentClass.entrySet().iterator();
        while (iterSC.hasNext()) {
            StudentClass sc = iterSC.next().getValue();

            // only unpaid class and not invoiced yet of this student:
            if (sc.getStudentId().equals(student.getId())
                    && !sc.isInvoiced()) {
                scToInvoice.add(sc);
                sc.setInvoiced(true);
            }
        }
        // if there is not class un-paid and invoiced, invoice should not be generated
        if (scToInvoice.isEmpty()) {
            System.out.println("There is not class unpaid so invoice cannot be generated");
            return null;
        }
        scToInvoice.trimToSize();
        //</editor-fold>

        long fee = 0;
        long balance = student.getBalance();
        //<editor-fold defaultstate="collapsed" desc="get fee">
        for (int i = 0; i < scToInvoice.size(); i++) {
            Class c = classes.get(scToInvoice.get(i).getClassId());
            fee += c.getFee();
        }
        fee = fee - balance;
        if (fee < 0) {
            fee = 0;
        }
        //</editor-fold>

        invoice = new Invoice(id, scToInvoice, null, "", "", fee, balance);
        invoices.add(invoice);

        saveData();
        setChanged();
        notifyObservers("Invoice");
        return invoice;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generate paySlip">
    public ArrayList<PaySlip> generatePaySlip(Teacher t) {
        System.out.println("\n\nStart generating Pay Slip");
        LocalDate firstDate = new LocalDate();

        //<editor-fold defaultstate="collapsed" desc="get first day of the teacher employing time">
        Iterator<Entry<String, TeacherClass>> iterTC = teacherClass.entrySet().iterator();
        while (iterTC.hasNext()) {
            TeacherClass tc = iterTC.next().getValue();
            if (tc.getTeacherId().equals(t.getId())) {
                Class c = classes.get(tc.getClassId());
                if (c.getStartDate().compareTo(firstDate) < 0) {
                    firstDate = c.getStartDate();
                }
            }
        }
        //</editor-fold>

        firstDate = firstDate.dayOfMonth().withMinimumValue();
        LocalDate today = new LocalDate().dayOfMonth().withMinimumValue();
        ArrayList<PaySlip> paySlipTemp = new ArrayList<>(paySlips.size());

        outer:
        while (firstDate.compareTo(today) < 0) {
            System.out.println("Generating for month: " + firstDate);
            //<editor-fold defaultstate="collapsed" desc="skip if paySlip in this month is already generated">
            for (int i = 0; i < paySlips.size(); i++) {
                if (paySlips.get(i).getTeacherId().equals(t.getId())
                        && paySlips.get(i).getMonth().compareTo(firstDate) == 0) {
                    System.out.println("\tgenerate payslip skip a month");
                    firstDate = firstDate.plusMonths(1);
                    continue outer;
                }
            }
            //</editor-fold>

            iterTC = teacherClass.entrySet().iterator();
            ArrayList<String> listClassId = new ArrayList<>(classes.size());
            ArrayList<Long> listSalary = new ArrayList<>(classes.size());
            String[][] skillsAndSalary = new String[t.getSkills().length][2];

            while (iterTC.hasNext()) {
                TeacherClass tc = iterTC.next().getValue();
                System.out.println("Loop iterTC. ClassId = " + tc.getClassId());
                if (tc.getTeacherId().equals(t.getId())) {
                    Class c = classes.get(tc.getClassId());
                    if (c.getStartDate().compareTo(firstDate.dayOfMonth().withMaximumValue()) <= 0
                            && c.getEndDate().compareTo(firstDate) >= 0) {

                        ArrayList<ClassSession> seses = new ArrayList<>(2);

                        //<editor-fold defaultstate="collapsed" desc="getSesson">
                        Iterator<Entry<String, ClassSession>> iterSes = sessions.entrySet().iterator();
                        while (iterSes.hasNext()) {
                            ClassSession ses = iterSes.next().getValue();
                            if (ses.getClassId().equals(c.getClassId())) {
                                seses.add(ses);
                            }
                        }
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="get day1, get day2">
                        LocalDate day1;
                        if (c.getStartDate().compareTo(firstDate) >= 0) {
                            day1 = c.getStartDate();
                        } else {
                            day1 = firstDate;
                        }

                        LocalDate day2;
                        if (c.getEndDate().compareTo(firstDate.dayOfMonth().withMaximumValue()) > 0) {
                            day2 = firstDate.dayOfMonth().withMaximumValue();
                        } else {
                            day2 = c.getEndDate().dayOfMonth().withMaximumValue();
                        }
                        //</editor-fold>
                        System.out.println("\tFirstDate = " + day1 + ". LastDate = " + day2);

                        int numberOfLesson = 0;
                        //<editor-fold defaultstate="collapsed" desc="get numberOfLesson">
                        seses.trimToSize();
                        if (seses.size() == 2) {
                            numberOfLesson = countSessionAWeek(seses.get(0), seses.get(1), day1, day2);
                        } else if (seses.size() == 1) {
                            numberOfLesson = countSessionAWeek(seses.get(0), null, day1, day2);
                        } else if (seses.isEmpty()) {
                            continue;// skip this class if there is no session
                        } else {
                            System.err.print("ERROR when generating pay slip");
                        }
                        //</editor-fold>

                        ClassType ct = getClassType(c);
                        long salaryOfClass = 0;
                        for (int i = 0; i < t.getSkills().length; i++) {
                            if (t.getSkills()[i][0].equals(ct.getSkill())) {
                                long payRate = Long.parseLong(t.getSkills()[i][1]);

                                double teachingHours;
                                if (seses.get(0).is45()) {
                                    teachingHours = numberOfLesson * 0.75;
                                } else {
                                    teachingHours = numberOfLesson;
                                }

                                long skillSalary;
                                if (skillsAndSalary[i][1] == null) {
                                    skillSalary = 0;
                                } else {
                                    skillSalary = Long.parseLong(skillsAndSalary[i][1]);
                                }

                                salaryOfClass = (long) (payRate * teachingHours);
                                skillSalary += salaryOfClass;

                                skillsAndSalary[i][1] = skillSalary + "";
                                break;
                            }
                        }

                        listClassId.add(c.getClassId());
                        listSalary.add(salaryOfClass);
                    }
                }
            }
            listClassId.trimToSize();
            listSalary.trimToSize();
            HashMap<String, Long> listClass = new HashMap<>(listClassId.size());
            for (int i = 0; i < listClassId.size(); i++) {
                listClass.put(listClassId.get(i), listSalary.get(i));
            }
            PaySlip ps = new PaySlip(t.getId(), firstDate, listClass);
            paySlipTemp.add(ps);
            System.out.println("\tPaySlip for teacher " + t.getFullname() + " in " + firstDate + " is generated. Salary is: ");
            firstDate = firstDate.plusMonths(1);
        }
        paySlips.addAll(paySlipTemp);
        saveData();
        return getPaySlipRelatedToTeacher(t);
    }

    //<editor-fold defaultstate="collapsed" desc="countSessionAWeek">
    private int countSessionAWeek(ClassSession ses1, ClassSession ses2, LocalDate start, LocalDate end) {
        int count = 0;
        while (start.compareTo(end) <= 0) {
            Iterator<Integer> iter;

            if (ses1 != null) {
                iter = ses1.getTimes().keySet().iterator();
                int time1 = iter.next();
                int dateOfSes1 = time1 / 44 + 1; //MONDAY = 1, SUNDAY = 7
                if (start.getDayOfWeek() == dateOfSes1) {
                    count++;
                }
            }
            if (ses2 != null) {
                iter = ses2.getTimes().keySet().iterator();
                int time2 = iter.next();
                int dateOfSes2 = time2 / 44 + 1;
                if (start.getDayOfWeek() == dateOfSes2) {
                    count++;
                }
            }
            start = start.plusDays(1);
        }
        System.out.println("\t\tCount sesion of Class [" + ses1.getClassId() + "] from [" + start + "] to [" + end + "] = [" + count + "]");
        return count;
    }
    //</editor-fold>

//    private long getWeeklySalary(long total, long payRate, long fee45, long fee60) {
//        long salary;
//        double duration = 0;
//
//        if (fee45 == total) {
//            duration = .75;
//        } else if (fee60 == total) {
//            duration = 1;
//        } else if (fee45 + fee60 == total) {
//            duration = 1.75;
//        } else if (fee45 * 2 == total) {
//            duration = 1.5;
//        } else if (fee60 * 2 == total) {
//            duration = 2;
//        } else {
//            System.err.print("line 1032 in School in getWeeklySalary");
//        }
//
//        salary = (long) (payRate * duration);
//        System.out.println("weekly salary of teacher OF A CLASS = " + salary);
//        return salary;
//    }
    private ArrayList<PaySlip> getPaySlipRelatedToTeacher(Teacher t) {
        ArrayList<PaySlip> paySlip = new ArrayList<>(paySlips.size());
        for (int i = 0; i < paySlips.size(); i++) {
            if (paySlips.get(i).getTeacherId().equals(t.getId())) {
                paySlip.add(paySlips.get(i));
            }
        }
        return paySlip;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generate report">
    public Report generateReport(LocalDate month) {
        System.out.println("\nStart generating report: ");
        LocalDate lastDayOfMonth = month.dayOfMonth().withMaximumValue();
        LocalDate firstDayOfMonth = month.dayOfMonth().withMinimumValue();
        System.out.println("First date: " + firstDayOfMonth.toString());
        System.out.println("Last date: " + lastDayOfMonth.toString());

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");

        if (lastDayOfMonth.compareTo(new LocalDate()) > 0) {
            lastDayOfMonth = new LocalDate();
        }

        ArrayList<String> paidStudentsInfo = new ArrayList<>(students.size());
        ArrayList<String> unPaidStudentsInfo = new ArrayList<>(students.size());
        ArrayList<Long> paidAmount = new ArrayList<>(students.size());
        ArrayList<Long> unPaidAmount = new ArrayList<>(students.size());
        ArrayList<Invoice> paidInvoice = new ArrayList<>(students.size());
        ArrayList<Invoice> unPaidInvoice = new ArrayList<>(students.size());

        int countPaid = -1;
        int countUnpaid = -1;

        Iterator<Entry<String, Student>> iter = students.entrySet().iterator();
        System.out.println("Start looping student: ");
        while (iter.hasNext()) {
            Student s = iter.next().getValue();
            System.out.println("\tStudent: " + s.getId() + " - " + s.getFullname());
            System.out.println("\tStart looping invoices:");
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);
                System.out.println("\t\tInvoices: " + invoice.getId());
                if (invoice.getStudentClass() != null && invoice.getStudentClass().get(0).getStudentId().equals(s.getId())) {
                    String invoiceDateString = "20" + invoice.getId().substring(8, 14);
                    LocalDate invoiceDate = dateTimeFormatter.parseLocalDate(invoiceDateString);
                    if (invoice.isPaid()) {
                        System.out.println("\t\t\tInvoice is paid: ");
                        //<editor-fold defaultstate="collapsed" desc="paid section">
                        boolean isThisRowCreated = false;
                        System.out.println("\t\t\tStart looping StudentClass: ");
                        for (int j = 0; j < invoice.getStudentClass().size(); j++) {
                            StudentClass sc = invoice.getStudentClass().get(j);
                            Class c = classes.get(sc.getClassId());
                            System.out.println("\t\t\t\tStudentClass: " + sc.getClassId() + " - "
                                    + c.getClassName() + ". Date of invoice: " + invoiceDate.toString());

                            if (invoiceDate.compareTo(firstDayOfMonth) >= 0 && invoiceDate.compareTo(lastDayOfMonth) <= 0) {
                                long thisClassFee = c.getFee();
                                if (!isThisRowCreated) {
                                    System.out.println("\t\t\t\t\tThis row is first created");
                                    paidStudentsInfo.add(s.getId());
                                    if (invoice.getPaidDate().compareTo(firstDayOfMonth) > 0 && invoice.getPaidDate().compareTo(lastDayOfMonth) < 0) {
                                        thisClassFee = thisClassFee - invoice.getBalance();
                                    }
                                    paidAmount.add(thisClassFee);
                                    paidInvoice.add(invoice);
                                    isThisRowCreated = true;
                                    countPaid++;
                                } else {
                                    System.out.println("\t\t\t\t\tThis row is ALREADY created");
                                    long amount = paidAmount.get(countPaid);
                                    amount += thisClassFee;
                                    paidAmount.set(countPaid, amount);
                                }
                            }
                        }
                        //</editor-fold>
                    } else {
                        System.out.println("\t\t\tInvoice is UNpaid: ");
                        //<editor-fold defaultstate="collapsed" desc="UNpaid section">
                        boolean isThisRowCreated = false;
                        System.out.println("\t\t\tStart looping StudentClass: ");
                        for (int j = 0; j < invoice.getStudentClass().size(); j++) {
                            StudentClass sc = invoice.getStudentClass().get(j);
                            Class c = classes.get(sc.getClassId());
                            System.out.println("\t\t\t\tStudentClass: " + sc.getClassId() + " - "
                                    + c.getClassName() + ". Date of invoice: " + invoiceDate.toString());

                            if (invoiceDate.compareTo(firstDayOfMonth) >= 0 && invoiceDate.compareTo(lastDayOfMonth) <= 0) {
                                long thisClassFee = c.getFee();
                                if (!isThisRowCreated) {
                                    System.out.println("\t\t\t\t\tThis row is first created");
                                    unPaidStudentsInfo.add(s.getId());
                                    unPaidAmount.add(thisClassFee - s.getBalance());
                                    unPaidInvoice.add(invoice);
                                    isThisRowCreated = true;
                                    countUnpaid++;
                                } else {
                                    System.out.println("\t\t\t\t\tThis row is ALREADY created");
                                    long amount = unPaidAmount.get(countUnpaid);
                                    amount += thisClassFee;
                                    unPaidAmount.set(countUnpaid, amount);
                                }
                            }
                        }
                        //</editor-fold>
                    }
                }
            }
            System.out.println();
        }
        paidStudentsInfo.trimToSize();
        unPaidStudentsInfo.trimToSize();
        paidAmount.trimToSize();
        unPaidAmount.trimToSize();
        paidInvoice.trimToSize();
        unPaidInvoice.trimToSize();

        System.out.println("Done generate report\n------------------------------------------------------------------\n");
        Report report = new Report(paidStudentsInfo, unPaidStudentsInfo,
                paidAmount, unPaidAmount, paidInvoice, unPaidInvoice,
                firstDayOfMonth, lastDayOfMonth);
        return report;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="get Utilization">
    public double getUtilization() {
        LocalDate today = new LocalDate();
        LocalDate lastDayOfMonth = today.dayOfMonth().withMaximumValue();
        LocalDate firstDayOfMonth = today.dayOfMonth().withMinimumValue();
        System.out.println("Start get ultilization: \nfirstDate = " + firstDayOfMonth.toString());
        System.out.println("lastDate = " + lastDayOfMonth.toString() + "\n");

        int total = (80 + 88) * 4 * 8;

        int count = 0;
        Iterator<Entry<String, ClassSession>> iter = sessions.entrySet().iterator();
        while (iter.hasNext()) {
            ClassSession ses = iter.next().getValue();
            Class c = classes.get(ses.getClassId());
            if (c.getStartDate().compareTo(firstDayOfMonth) >= 0 && c.getEndDate().compareTo(lastDayOfMonth) <= 0) {
                count = count + ses.getTimes().size();
            }
        }
        System.out.println("Session count = " + count);
        System.out.println("Total timetable  cell = " + total);
        return (double) (count) / total;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateClassType">
    public boolean updateClassType(ClassType ct) {
        ArrayList<Teacher> affectedTeachers = new ArrayList<>(teachers.size());

        //<editor-fold defaultstate="collapsed" desc="updates class and prepare to updates teacher">
        Iterator<Entry<String, Class>> iterClass = classes.entrySet().iterator();
        while (iterClass.hasNext()) {
            Class c = iterClass.next().getValue();
            if (c.getClassType()[0].equals(ct.getSkill()) && c.getClassType()[1].equals(ct.getType() + "")) {
                long newFee = 0;

                Iterator<Entry<String, ClassSession>> iterSes = sessions.entrySet().iterator();
                while (iterSes.hasNext()) {
                    ClassSession cs = iterSes.next().getValue();
                    if (cs.getClassId().equals(c.getClassId())) {
                        if (cs.is45()) {
                            newFee += ct.getFeeFor45Min();
                        } else {
                            newFee += ct.getFeeFor60Min();
                        }
                    }
                }
                c.setFee(calculateTotalFeeFromWeeklyFee(newFee, c));
//                c.setWeeklyFee(newFee);

                Iterator<Entry<String, TeacherClass>> iterTC = teacherClass.entrySet().iterator();
                while (iterTC.hasNext()) {
                    TeacherClass tc = iterTC.next().getValue();
                    if (tc.getClassId().equals(c.getClassId())) {
                        Teacher t = teachers.get(tc.getTeacherId());// this teacher is affected by the class type
                        affectedTeachers.add(t);
                        paySlips.removeAll(getPaySlipRelatedToTeacher(t));
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="update teacher">
        for (int i = 0; i < affectedTeachers.size(); i++) {
            generatePaySlip(affectedTeachers.get(i));
        }
        //</editor-fold>

        saveData();
        setChanged();
        notifyObservers("Class Type");
        return true;
    }

    private long calculateTotalFeeFromWeeklyFee(long fee, Class c) {
        LocalDate start = c.getStartDate();
        LocalDate end = c.getEndDate();
        int dYear = end.getYear() - start.getYear();
        int dWeek = end.weekOfWeekyear().get() - start.weekOfWeekyear().get() + 1;
        int d = dYear * start.weekOfWeekyear().getMaximumValue() + dWeek;

        return d * fee;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="resetPaySlipOfTeacher()">
    public void resetPaySlipOfTeacher(Teacher t) {
        paySlips.removeAll(getPaySlipRelatedToTeacher(t));
        generatePaySlip(t);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update teacherClass">
    public boolean updateTeacherClass(TeacherClass tc) {
        Teacher t = teachers.get(tc.getTeacherId());
        resetPaySlipOfTeacher(t);
        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CSV">
    public boolean exportToCSV(File file) {
        BufferedWriter out = null;
        try {
            FileWriter writer = new FileWriter(file.getAbsoluteFile());
            out = new BufferedWriter(writer);

            //Write accounts
            Iterator<Entry<String, User>> iterUser = users.entrySet().iterator();
            out.write(CSVImporter.ACCOUNT_STRING + "\n");
            out.write("ID, Password, Type, Fullname, Gender, Birth, Email, Phone, Address\n");
            while (iterUser.hasNext()) {
                User user = iterUser.next().getValue();
                out.write(user.printDetail());
            }
            out.write("\n");

            //Write Class Type
            out.write(CSVImporter.CLASS_TYPE_STRING + "\n");
            out.write("Skill, Type, Lesson/Week, Fee/45mins, Fee/60mins, Note\n");
            for (int i = 0; i < classTypes.size(); i++) {
                out.write(classTypes.get(i).printDetail());
            }
            out.write("\n");

            //Write teachers
            Iterator<Entry<String, Teacher>> iterTeacher = teachers.entrySet().iterator();
            out.write(CSVImporter.TEACHER_STRING + "\n");
            out.write("ID, Fullname, Gender, Birth, Skills, Email, Phone, Address\n");
            while (iterTeacher.hasNext()) {
                Teacher t = iterTeacher.next().getValue();
                out.write(t.printDetail());
            }
            out.write("\n");

            //Write students
            Iterator<Entry<String, Student>> iterStu = students.entrySet().iterator();
            out.write(CSVImporter.STUDENT_STRING + "\n");
            out.write("ID, Fullname, Gender, Birth, Email, Phone, Address, Contact Name,"
                    + "Contact Relationship, Contact Email, Contact Phone, Contact Address\n");
            while (iterStu.hasNext()) {
                Student s = iterStu.next().getValue();
                out.write(s.printDetail());
            }
            out.write("\n");

            //Write classes
            Iterator<Entry<String, Class>> iterClasses = classes.entrySet().iterator();
            out.write(CSVImporter.CLASS_STRING + "\n");
            out.write("ID, Class Name, Start Date, End Date, "
                    + "Current Number Students, Tuition Fee, Textbook, Class Type\n");
            while (iterClasses.hasNext()) {
                Class c = iterClasses.next().getValue();
                out.write(c.printDetail());
            }
            out.write("\n");

            //Write Class Session
            Iterator<Entry<String, ClassSession>> iterCS = sessions.entrySet().iterator();
            out.write(CSVImporter.SESSION_STRING + "\n");
            out.write("Class ID, Room ID, Session ID, 45Min-Session,  Session\n");
            while (iterCS.hasNext()) {
                ClassSession ses = iterCS.next().getValue();
                out.write(ses.printDetail());
            }
            out.write("\n");

            //Write Teacher Class
            Iterator<Entry<String, TeacherClass>> iterTC = teacherClass.entrySet().iterator();
            out.write(CSVImporter.TEACHER_CLASS_STRING + "\n");
            out.write("Teacher Class ID, Teacher ID, Class ID\n");
            while (iterTC.hasNext()) {
                TeacherClass tc = iterTC.next().getValue();
                out.write(tc.printDetail());
            }
            out.write("\n");

            //Write Student Class
            Iterator<Entry<String, StudentClass>> iterSC = studentClass.entrySet().iterator();
            out.write(CSVImporter.STUDENT_CLASS_STRING + "\n");
            out.write("ID, Class ID, Student ID, Is Paid, Invoice\n");
            while (iterSC.hasNext()) {
                StudentClass sc = iterSC.next().getValue();
                out.write(sc.printDetail());
            }
            out.write("\n");

            //Write invoice
            out.write(CSVImporter.INVOICE_STRING + "\n");
            out.write("ID, Student-Class, Paid Date, Paid Method, Paid Note, Total Free, Balance\n");
            for (int i = 0; i < invoices.size(); i++) {
                out.write(invoices.get(i).printDetail());
            }
            out.write("\n");

            //Write Pay Slip
            out.write(CSVImporter.PAY_SLIP_STRING + "\n");
            out.write("ID, Teacher ID, Date, Classes\n");
            for (int i = 0; i < paySlips.size(); i++) {
                out.write(paySlips.get(i).printDetail());
            }
            out.write("\n");
            out.write("\n");

            out.flush();
            out.close();
        } catch (IOException ex) {
            System.err.println("Catched Exception " + ex.toString());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                System.err.println("Catched Exception: cannot close file" + e.toString());
            }
        }
        return true;
    }

    public ArrayList<String>[] importFromCSV(File file) throws InvalidCSVFormatException {
        CSVImporter importer = new CSVImporter(file, this);
//        try {
//            Thread.sleep(10000);
//        } catch (Exception e) {
//        }
        return importer.startImport();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="notifyAllGUI">
    public void notifyAllGUI() {
        setChanged();
        notifyObservers("Class Type");

        setChanged();
        notifyObservers("Class");

        setChanged();
        notifyObservers("Teacher");

        setChanged();
        notifyObservers("Student");

        setChanged();
        notifyObservers("Room");

        setChanged();
        notifyObservers("User");

        setChanged();
        notifyObservers("Invoice");
    }
    //</editor-fold>
}
