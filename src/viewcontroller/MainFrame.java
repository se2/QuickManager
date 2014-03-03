package viewcontroller;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import kulcomponent.KulFrame;
import model.Class;
import model.ClassSession;
import model.ClassType;
import model.Invoice;
import model.PaySlip;
import model.Report;
import model.Room;
import model.School;
import model.Student;
import model.StudentClass;
import model.Teacher;
import model.User;
import org.joda.time.LocalDate;
import viewcontroller.form.AccountForm;
import viewcontroller.form.ClassForm;
import viewcontroller.form.ClassTypeForm;
import viewcontroller.form.EnrollForm;
import viewcontroller.form.InvoiceForm;
import viewcontroller.form.PaySlipForm;
import viewcontroller.form.ReportForm;
import viewcontroller.form.RoomForm;
import viewcontroller.form.StudentForm;
import viewcontroller.form.TeacherForm;
import viewcontroller.listview.ClassListView;
import viewcontroller.listview.PaySlipListView;
import viewcontroller.listview.StudentListView;
import viewcontroller.listview.TeacherListView;
import viewcontroller.listview.UnpaidStudentListView;

/**
 *
 * @author Dam Linh
 */
public class MainFrame extends KulFrame implements Observer {

    private ResourceBundle language;
    private JPanel mainPanel;//contains everything but RoomForm and LoginFrame
    private School model;//intentionally default modifier
    private User user;
    private MenuView menuView;
    private EnrollForm enrollForm = new EnrollForm(this);
    private RoomForm roomView;
    private TeacherListView teacherListViewForClass;
    private ClassListView classListViewForTeacher;

    //<editor-fold defaultstate="collapsed" desc="init">
    public MainFrame(School model, User currentUser) {
        super("Quick Manage");

        this.model = model;
        language = model.getLanguage();
        this.user = currentUser;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int input = JOptionPane.showConfirmDialog(MainFrame.this,
                        language.getString("quitConfirmContent"),
                        language.getString("quitConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                if (input == JOptionPane.OK_OPTION) {
                    System.exit(0);
                } else {
                    MainFrame.this.disableCloseKeyBinding();
                }
            }
        });

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(Template.getBackground());
        this.add(mainPanel);
        roomView = new RoomForm(this);
        menuView = new MenuView(this);

        mainPanel.add(menuView);

        teacherListViewForClass = new TeacherListView(this, false);
        classListViewForTeacher = new ClassListView(this, false);

//        showRoomView(model.getRooms().get(1));
    } //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters">
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MenuView getMenuView() {
        return menuView;
    }

    public School getModel() {
        return model;
    }

    public User getUser() {
        return user;
    }
    //</editor-fold>

    /*
     * these methods are to show the timetable(roomView)
     * the 1-argument method is to simply display timetable base on the room.
     * Invoked by RoomListView or ClassView
     *
     * the many-argument method is to display timetable that user can add session
     * to a class. Invoked by ClassForm
     */
    // <editor-fold defaultstate="collapsed" desc="room methods">
    public void showRoomView(Room room) {
        KulFrame frame = new KulFrame(language.getString("roomTimetable"));
        if (roomView.setRoom(room, frame)) {
            frame.setVisible(true);
            frame.add(roomView);
        }
    }

    public void showRoomView(HasReturn hasReturn, KulFrame prevFrame, ClassSession session, Class currentClass, ClassType ct) {
        KulFrame frame = new KulFrame(this, language.getString("roomTimetable"));
        if (roomView.setSession(hasReturn, session, currentClass, frame, prevFrame, ct)) {
            prevFrame.setEnabled(false);
            frame.add(roomView);
            frame.setVisible(true);
        }
    }
    // </editor-fold>

    /**
     * Show the list view of teacher, student, classes, accounts for user to
     * select These methods have the HasReturn so that they will send some
     * objects back to their caller
     */
    // <editor-fold defaultstate="collapsed" desc="showXxxListView">
    public void showClassListView(HasReturn hasReturn, KulFrame prevFrame, Teacher teacher) {
        classListViewForTeacher = new ClassListView(this, false);
        KulFrame frame = new KulFrame(language.getString("availableClasses"));
        frame.add(classListViewForTeacher);
        prevFrame.setEnabled(false);
        frame.setVisible(true);
        classListViewForTeacher.setTeacher(hasReturn, frame, prevFrame, teacher, true);
    }

    public void showClassListView(HasReturn hasReturn, KulFrame prevFrame, ArrayList<Student> students, ArrayList<Class> classes) {
        ClassListView classListView = new ClassListView(this, false);
        KulFrame frame = new KulFrame(language.getString("availableClasses"));
        frame.add(classListView);
        prevFrame.setEnabled(false);
        frame.setVisible(true);
        classListView.setStudent(hasReturn, frame, prevFrame, students, classes);
    }

    public void showTeacherListView(HasReturn hasReturn, KulFrame prevFrame, Class classes) {
        teacherListViewForClass = new TeacherListView(this, false);
        KulFrame frame = new KulFrame(language.getString("availableTeachers"));
        frame.add(teacherListViewForClass);
        prevFrame.setEnabled(false);
        frame.setVisible(true);
        teacherListViewForClass.setClass(hasReturn, frame, prevFrame, classes, true);
    }

    public void showStudentListView(HasReturn hasReturn, KulFrame prevFrame, ArrayList<Class> classes, ArrayList<Student> students) {
        StudentListView studentListView = new StudentListView(this, false);
        KulFrame frame = new KulFrame(language.getString("availableStudents"));
        frame.add(studentListView);
        prevFrame.setEnabled(false);
        frame.setVisible(true);
        studentListView.setClass(hasReturn, frame, prevFrame, classes, students);
    }
//
//    public void showStudentListView(HasReturn hasReturn, KulFrame prevFrame, Class classes) {
//        StudentListView studentListView = new StudentListView(this, false);
//        KulFrame frame = new KulFrame("Students taking " + classes.getClassId());
//        frame.add(studentListView);
//        prevFrame.setEnabled(false);
//        frame.setVisible(true);
//        studentListView.setClass(hasReturn, frame, prevFrame, classes);
//    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="accounts methods">
    public void showAccountForm(User user, boolean isManager) {
        AccountForm accountForm = new AccountForm(this);
        KulFrame frame;
        if (user != null) {
            frame = new KulFrame(user, language.getString("account") + " " + user.getFullname());
        } else {
            frame = new KulFrame(language.getString("add") + " " + language.getString("account"));
        }
        frame.setSize(600, 730);
        accountForm.setUser(user, frame, isManager);
        frame.add(accountForm);
        frame.setVisible(true);
        frame.setLocationRelativeTo(this);
    }
    // </editor-fold>

    /**
     * showXxx is to display the GUI to user to view only (not editable). it has
     * argument to specify which object to display.
     *
     * showXxxForm is to display the form GUI to user to edit or add some
     * object. if the object passed is null, it means that the form is to create
     * new object, else it means edit.
     */
    // <editor-fold defaultstate="collapsed" desc="class methods">
    public void showClassForm(Class classes, boolean isCopy, boolean isEditable) {
        if (model.getClassTypes().isEmpty()) {
            JOptionPane.showMessageDialog(this, language.getString("noClassTypeContent"),
                    language.getString("caution"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClassForm classForm = new ClassForm(this);
        KulFrame frame;
        if (classes != null) {
            frame = new KulFrame(classes, language.getString("class") + " " + classes.getClassName());
        } else {
            frame = new KulFrame(language.getString("add") + " " + language.getString("class"));
        }
        if (classForm.setClass(classes, frame, isCopy, isEditable)) {
            frame.add(classForm);
            frame.setVisible(true);
        }
    }
    // </editor-fold>

    /*
     * see class methods
     */
    //<editor-fold defaultstate="collapsed" desc="teacher methods">
    public void showTeacherForm(Teacher teacher, boolean editable) {
        if (model.getClassTypes().isEmpty()) {
            JOptionPane.showMessageDialog(this, language.getString("noClassTypeContent"),
                    language.getString("caution"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        TeacherForm teacherForm = new TeacherForm(this);
        KulFrame frame;
        if (teacher != null) {
            frame = new KulFrame(teacher, language.getString("teacher") + " " + teacher.getFullname());
        } else {
            frame = new KulFrame(language.getString("add") + " " + language.getString("teacher"));
        }
        if (teacherForm.setTeacher(teacher, frame, editable)) {
            frame.add(teacherForm);
            frame.setVisible(true);
        }
    }
    // </editor-fold>

    /*
     * see class methods
     */
    //<editor-fold defaultstate="collapsed" desc="student methods">
    public void showStudentForm(Student student, boolean editable) {
        StudentForm studentForm = new StudentForm(this);
        KulFrame frame;
        if (student != null) {
            frame = new KulFrame(student, language.getString("student") + " " + student.getFullname());
        } else {
            frame = new KulFrame(language.getString("add") + " " + language.getString("student"));
        }
        if (studentForm.setStudent(student, frame, editable)) {
            frame.add(studentForm);
            frame.setVisible(true);
        }
    }
    // </editor-fold>

    /**
     * these methods are to show the form for enrollment. 3 different arg
     * methods are for 4 different purpose. showEnrollForm(Student/Class s/c) is
     * to display the form with 1 Student/Class added to the form. The method
     * showEnrollForm(HashMap obj, String identifier) is to display the form
     * with multiple object added to the form. It cannot be seperated into 2
     * methods because they would have the same arg. Therefore, the String
     * identifier is here to identify the HashMap obj contains Student or Class
     */
    // <editor-fold defaultstate="collapsed" desc="showEnrollForm">
    public void showEnrollForm(Student s) {
        if (enrollForm.isIsBeingUsed()) {
            JOptionPane.showMessageDialog(rootPane, "Enrollment form is opening");
            return;
        }
        KulFrame frame = new KulFrame("Enrollment");
        enrollForm.setStudent(s, frame);
        frame.add(enrollForm);
        frame.setVisible(true);
    }

    public void showEnrollForm(Class c) {
        if (enrollForm.isIsBeingUsed()) {
            JOptionPane.showMessageDialog(rootPane, "Enrollment form is opening");
            return;
        }
        KulFrame frame = new KulFrame("Enrollment");
        enrollForm.setClass(c, frame);
        frame.add(enrollForm);
        frame.setVisible(true);
    }

    public void showEnrollForm(HashMap obj, String identifier) {
        if (enrollForm.isIsBeingUsed()) {
            JOptionPane.showMessageDialog(rootPane, "Enrollment form is opening");
            return;
        }
        KulFrame frame = new KulFrame("Enrollment");
        frame.setVisible(true);
        frame.add(enrollForm);

        if (identifier.equals("Student")) {
            enrollForm.setStudent(obj, frame);
        } else {
            enrollForm.setClass(obj, frame);
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="show invoice">
    public void showInvoiceForm(Invoice invoice) {
        InvoiceForm invForm = new InvoiceForm(this);
        KulFrame frame = new KulFrame(invoice, language.getString("invoice") + " " + invoice.getId());
        frame.setSize(550, 730);
        frame.setLocationRelativeTo(this);

        if (invForm.setInvoice(invoice, frame)) {
            frame.add(invForm);
            frame.setVisible(true);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generate Report">
    public void generateReport() {
        GetMonthFrame monthFrame = new GetMonthFrame(this);
        monthFrame.setVisible(true);
        this.setState(Frame.ICONIFIED);
    }

    public void showReportForm(LocalDate date) {
        final Report report = model.generateReport(date);
        final ReportForm reportForm = new ReportForm(this);

        final KulFrame frame = new KulFrame(language.getString("monthlyReport"));
        frame.setSize(550, 730);
        frame.setLocationRelativeTo(this);
        frame.add(reportForm);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                reportForm.setReport(report, frame);
                frame.setVisible(true);
            }
        };
        new Thread(runnable).start();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="show Unpaid students">
    public void showUnpaidStudentClass() {
        ArrayList<StudentClass> scList = model.getUnpaidStudentClass();
        UnpaidStudentListView unpaidList = new UnpaidStudentListView(this, scList);
        this.setEnabled(false);
        unpaidList.setVisible(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="show Class Type">
    public void showClassTypeForm(ClassType classType) {
        ClassTypeForm ctForm = new ClassTypeForm(this);

        KulFrame frame;
        if (classType != null) {
            frame = new KulFrame(classType, language.getString("classType") + " " + classType.getSkill());
        } else {
            frame = new KulFrame(language.getString("add") + " " + language.getString("classType"));
        }

        frame.setSize(550, 730);
        frame.setLocationRelativeTo(this);

        if (ctForm.setClassType(classType, frame, true)) {
            frame.add(ctForm);
            frame.setVisible(true);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="show Pay slip">
    public void showPaySlipListView(Teacher teacher, KulFrame prevFrame) {
        PaySlipListView frame = new PaySlipListView(this, model.generatePaySlip(teacher), prevFrame);
        prevFrame.setEnabled(false);
        frame.setVisible(true);
        frame.setSize(550, 730);
        frame.setLocationRelativeTo(this);
        frame.setTitle(language.getString("paySlip"));
    }

    public void showPaySlipForm(PaySlip ps, JFrame prevFrame) {
        PaySlipForm frame = new PaySlipForm(ps, this, prevFrame, language.getString("paySlip"));
        prevFrame.setEnabled(false);

        frame.setVisible(true);
        frame.setSize(1024, 730);
        frame.setLocationRelativeTo(this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    @Override
    public void update(Observable o, Object arg) {
        menuView.update(arg);
        if (arg instanceof String) {
            String s = (String) arg;
            switch (s) {
                case "Teacher":
                    if (teacherListViewForClass.isShowing()) {
                        teacherListViewForClass.refresh(true);
                    }
                    break;

                case "Class":
                    if (classListViewForTeacher.isShowing()) {
                        classListViewForTeacher.refresh(true);
                    }
                    break;
            }
        }
    }
    //</editor-fold>
}
