package viewcontroller.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.MatteBorder;
import model.Class;
import model.Student;
import model.StudentClass;
import model.Validator;
import viewcontroller.HasReturn;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import kulcomponent.KulImageButton;
import kulcomponent.KulUnderLineListener;

/**
 *
 * @author Dam Linh
 */
public class EnrollForm extends JPanel implements HasReturn {

    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private ArrayList<Student> students;
    private ArrayList<Class> classes;
    private ArrayList<JLabel> stuLine;
    private ArrayList<JLabel> classLine;
    private Box center;
    private JScrollPane stuScroll;
    private JScrollPane classScroll;
    private Box stuBox = new Box(BoxLayout.Y_AXIS);
    private Box classBox = new Box(BoxLayout.Y_AXIS);
    private KulButton enroll = new KulButton("Enroll");
    private boolean isBeingUsed;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private final int W = 435;
    private Font f = Template.getFont().deriveFont(16f);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    public EnrollForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout(50, 0));

        center = new Box(BoxLayout.X_AXIS);
        add(center);
        add(new JLabel(), BorderLayout.WEST);//padding purpose
        add(new JLabel(), BorderLayout.EAST);//padding purpose

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "enroll");
        this.getActionMap().put("enroll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enroll();
            }
        });


        // <editor-fold defaultstate="collapsed" desc="draw North">
        Box north = new Box(BoxLayout.Y_AXIS);
        north.setPreferredSize(new Dimension(1024, 160));
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        JLabel title = new JLabel("Enrollment Form");
        title.setFont(f.deriveFont(50f));
        title.setForeground(Template.getForeground());

        enroll.setPreferredSize(new Dimension(120, 30));
        enroll.setMaximumSize(new Dimension(120, 30));
        enroll.setFont(f.deriveFont(18));
        enroll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enroll();
            }
        });

        buttonBox.add(Box.createHorizontalStrut(50));
        buttonBox.add(title);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(enroll);
        buttonBox.add(Box.createHorizontalStrut(100));

        Box titleBox = new Box(BoxLayout.X_AXIS);
        JLabel studentTitle = new JLabel("Students");
        JLabel classTitle = new JLabel("Classes");
        studentTitle.setFont(f.deriveFont(36f));
        classTitle.setFont(f.deriveFont(36f));
        studentTitle.setForeground(Template.getForeground());
        classTitle.setForeground(Template.getForeground());

        titleBox.add(Box.createHorizontalStrut(175));
        titleBox.add(studentTitle);
        titleBox.add(Box.createHorizontalGlue());
        titleBox.add(classTitle);
        titleBox.add(Box.createHorizontalStrut(200));

        north.add(buttonBox);
        north.add(titleBox);
        add(north, BorderLayout.NORTH);
        // </editor-fold>

        init();
    }

    private void init() {
        stuBox = new Box(BoxLayout.Y_AXIS);
        stuBox.setMaximumSize(new Dimension(W, 1000));
        stuBox.setPreferredSize(new Dimension(W, 20));
        stuScroll = new JScrollPane(stuBox);
        stuScroll.getViewport().setBackground(Template.getBackground());
        stuScroll.setBorder(new MatteBorder(0, 0, 0, 1, Template.getTitleBar()));
        stuScroll.getVerticalScrollBar().setUnitIncrement(20);
        center.add(stuScroll);

        classBox = new Box(BoxLayout.Y_AXIS);
        classBox.setMaximumSize(new Dimension(W, 1000));
        classBox.setPreferredSize(new Dimension(W, 20));
        classScroll = new JScrollPane(classBox);
        classScroll.getViewport().setBackground(Template.getBackground());
        classScroll.setBorder(new MatteBorder(0, 1, 0, 0, Template.getTitleBar()));
        classScroll.getVerticalScrollBar().setUnitIncrement(20);
        center.add(classScroll);

        KulButton moreStu = new KulButton("More");
        KulButton moreCl = new KulButton("More");
        moreStu.setPreferredSize(new Dimension(120, 30));
        moreStu.setMaximumSize(new Dimension(120, 30));
        moreCl.setPreferredSize(new Dimension(120, 30));
        moreCl.setMaximumSize(new Dimension(120, 30));
        moreStu.setFont(f.deriveFont(16f));
        moreCl.setFont(f.deriveFont(16f));

        moreStu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addStudent();
            }
        });

        moreCl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addClass();
            }
        });

        Box south = new Box(BoxLayout.X_AXIS);
        south.setPreferredSize(new Dimension(1024, 80));
        add(south, BorderLayout.SOUTH);

        south.add(Box.createHorizontalStrut(190));
        south.add(moreStu);
        south.add(Box.createHorizontalGlue());
        south.add(moreCl);
        south.add(Box.createHorizontalStrut(190));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setters">
    private void reset() {
        isBeingUsed = true;
        parentFrame.addWindowListener(new ParentFrameListener());
    }

    private boolean checkAvailableToUse(Student s) {
        if (s.isBeingUsed()) {
            JOptionPane.showMessageDialog(parentFrame, "This student (" + s.getId()
                    + ") is being used in other window. Please close that window!",
                    "Student is being used", JOptionPane.WARNING_MESSAGE);
            parentFrame.closeWindow(true);
            return false;
        } else {
            s.setBeingUsed(true);
            return true;
        }
    }

    private boolean checkAvailableToUse(Class c) {
        if (c.isBeingUsed()) {
            JOptionPane.showMessageDialog(parentFrame, "This class (" + c.getClassId()
                    + ") is being used in other window. Please close that window!",
                    "Class is being used", JOptionPane.WARNING_MESSAGE);
            parentFrame.closeWindow(true);
            return false;
        } else {
            c.setBeingUsed(true);
            return true;
        }
    }

    public void setStudent(Student s, KulFrame parentFrame) {
        this.parentFrame = parentFrame;
        reset();
        if (!checkAvailableToUse(s)) {
            return;
        }

        students = new ArrayList<>(5);
        stuLine = new ArrayList<>(5);
        classes = new ArrayList<>(5);
        classLine = new ArrayList<>(5);
        students.add(s);
        drawStudent();
        drawClass();
    }

    public void setClass(Class c, KulFrame parentFrame) {
        this.parentFrame = parentFrame;
        reset();
        if (!checkAvailableToUse(c)) {
            return;
        }

        classes = new ArrayList<>(5);
        classLine = new ArrayList<>(5);
        students = new ArrayList<>(5);
        stuLine = new ArrayList<>(5);
        classes.add(c);
        drawClass();
        drawStudent();
    }

    public void setStudent(HashMap<String, Student> stus, KulFrame parentFrame) {
        this.parentFrame = parentFrame;
        reset();

        students = new ArrayList<>(stus.size());
        stuLine = new ArrayList<>(stus.size());
        classes = new ArrayList<>(stus.size());
        classLine = new ArrayList<>(stus.size());
        students.addAll(stus.values());

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            if (!checkAvailableToUse(s)) {
                return;
            }
        }
        drawStudent();
        drawClass();
    }

    public void setClass(HashMap<String, Class> c, KulFrame parentFrame) {
        this.parentFrame = parentFrame;
        reset();

        classes = new ArrayList<>(c.size());
        classLine = new ArrayList<>(c.size());
        students = new ArrayList<>(c.size());
        stuLine = new ArrayList<>(c.size());
        classes.addAll(c.values());

        for (int i = 0; i < classes.size(); i++) {
            Class cl = classes.get(i);
            if (!checkAvailableToUse(cl)) {
                return;
            }
        }
        drawClass();
        drawStudent();
    }
    // </editor-fold>

    /*
     * this method is to (re)draw all the students and classes to be enrolled.
     * Invoked from the setter methods and setReturnObj() method.
     */
    // <editor-fold defaultstate="collapsed" desc="draw student/class">
    private void drawStudent() {
        stuBox.removeAll();
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);

            JLabel label = new JLabel("Student id: " + s.getId() + ", " + s.getFullname());
            label.setFont(f);
            label.setForeground(Template.getForeground());
            label.addMouseListener(new KulUnderLineListener());
            label.addMouseListener(new StudentViewtListener(s));
            stuLine.add(label);

            KulImageButton remove = new KulImageButton("cancel", 16, 16);
            remove.setToolTipText("Remove from this list");
            remove.addMouseListener(new StudentRemoveListener(s));

            Box line = new Box(BoxLayout.X_AXIS);
            label.setMaximumSize(new Dimension(W - 20, 30));
            line.add(remove);
            line.add(Box.createRigidArea(new Dimension(10, 30)));
            line.add(label);
            line.add(Box.createRigidArea(new Dimension(10, 30)));
            stuBox.add(line);
        }
    }

    private void drawClass() {
        classBox.removeAll();
        for (int i = 0; i < classes.size(); i++) {
            Class c = classes.get(i);

//            JLabel label = new JLabel(c.getClassId() + ", " + c.getMajor());
//            label.setFont(f);
//            label.setForeground(Template.getForeground());
//            label.addMouseListener(new KulUnderLineListener());
//            label.addMouseListener(new ClassViewListener(c));
//            classLine.add(label);


            KulImageButton remove = new KulImageButton("cancel", 16, 16);
            remove.setToolTipText("Remove from this list");
            remove.addMouseListener(new ClassRemoveListener(c));

            Box line = new Box(BoxLayout.X_AXIS);
//            label.setMaximumSize(new Dimension(W - 20, 30));
            line.add(Box.createRigidArea(new Dimension(30, 30)));
            line.add(remove);
            line.add(Box.createRigidArea(new Dimension(10, 30)));
//            line.add(label);
            line.add(Box.createRigidArea(new Dimension(10, 30)));
            classBox.add(line);
        }
    }
    // </editor-fold>

    /*
     * these methods are to call methods in mainFrame to display the GUI to select
     * students and classes to add to this form. Invoked by the listener below
     */
    // <editor-fold defaultstate="collapsed" desc="add student/class">
    private void addStudent() {
        classes.trimToSize();
        students.trimToSize();
        mainFrame.showStudentListView(this, parentFrame, classes, students);
    }

    private void addClass() {
        classes.trimToSize();
        students.trimToSize();
        mainFrame.showClassListView(this, parentFrame, students, classes);
    }
    // </editor-fold>

    /*
     * this method is called by the previous frames(studentListView or classListView)
     * to return some objects(students or classes) to this class.
     * this method then calls drawStudents() and drawClasses() methods
     */
    // <editor-fold defaultstate="collapsed" desc="setReturnObj">
    @Override
    public void setReturnObj(Object o) {
        boolean isClassUpdate = false;

        if (o instanceof HashMap) {
            HashMap hm = (HashMap) o;
            Iterator<Entry> iter = hm.entrySet().iterator();
            while (iter.hasNext()) {
                Object obj = iter.next().getValue();
                if (obj instanceof Class) {
                    Class c = (Class) obj;
                    classes.add(c);
                    isClassUpdate = true;
                } else {
                    Student s = (Student) obj;
                    students.add(s);
                    isClassUpdate = false;
                }
            }

            if (isClassUpdate) {
                drawClass();
                classBox.revalidate();
                classBox.repaint();
            } else {
                drawStudent();
                stuBox.revalidate();
                stuBox.repaint();
            }
        }
        this.requestFocus();
    }
    // </editor-fold>

    /*
     * this method is to validate the class time and the classes of the student's
     * time. Then it will enroll and close the frame. It is invoked when user
     * presses Enroll button
     */
    // <editor-fold defaultstate="collapsed" desc="enroll">
    private void enroll() {
        boolean isValid = true;
        if (classes.isEmpty() || students.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Please select some class and student to enroll",
                    "Enroll fails", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < classes.size(); i++) {
            Class c1 = classes.get(i);
            int currentStudentNumber = students.size();
            classLine.get(i).setForeground(Color.BLACK);//reset color

//            if (c1.getCurrentNumberOfStudent() + currentStudentNumber > c1.getMaximumStudent()) {
//                classLine.get(i).setForeground(Color.red);
//                isValid = false;
//                JOptionPane.showMessageDialog(parentFrame, c1.getClassId() + " exceed its capacity");
//            }

            for (int j = 0; j < currentStudentNumber; j++) {
                stuLine.get(j).setForeground(Color.BLACK);//reset color
                Student s = students.get(j);

                Iterator<Entry<String, StudentClass>> iterSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
                while (iterSC.hasNext()) {
                    StudentClass sc = iterSC.next().getValue();
                    if (sc.getStudentId().equals(s.getId())) {
                        Class c2 = mainFrame.getModel().getClasses().get(sc.getClassId());
                        if (!Validator.validateClassTime(c1, c2, mainFrame.getModel())) {
                            isValid = false;
                            JOptionPane.showMessageDialog(parentFrame, "Student "
                                    + s.getId() + " has timetable conflict!",
                                    "Timetable conflicts", JOptionPane.WARNING_MESSAGE);
                            stuLine.get(j).setForeground(Color.red);
                            classLine.get(i).setForeground(Color.red);
                        }
                    }
                }
            }
        }

        if (isValid) {
            for (int i = 0; i < classes.size(); i++) {
                for (int j = 0; j < students.size(); j++) {
                    StudentClass sc = new StudentClass(students.get(j).getId(), classes.get(i).getClassId());
                    mainFrame.getModel().addStudentClass(sc);
                }
            }
            parentFrame.closeWindow(true);
        }
    }
    // </editor-fold>

    /*
     * when this class(GUI) is visible, all classes and students that this class
     * display will be locked (isBeingUser = true). Then if the frame that contains
     * this class closed, this class will release all the locks
     */
    // <editor-fold defaultstate="collapsed" desc="ParentFrameListener to close">
    private class ParentFrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            isBeingUsed = false;

            if (students != null) {
                for (int i = 0; i < students.size(); i++) {
                    students.get(i).setBeingUsed(false);
                }
            }
            if (classes != null) {
                for (int i = 0; i < classes.size(); i++) {
                    classes.get(i).setBeingUsed(false);
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="student/class listener">
    private class StudentViewtListener extends MouseAdapter {

        private Student s;

        public StudentViewtListener(Student s) {
            this.s = s;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.showStudentForm(s, false);
        }
    }

    private class StudentRemoveListener extends MouseAdapter {

        private Student s;

        public StudentRemoveListener(Student s) {
            this.s = s;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            students.remove(s);
            s.setBeingUsed(false);
            drawStudent();
            stuBox.revalidate();
            stuBox.repaint();
        }
    }

    private class ClassViewListener extends MouseAdapter {

        private Class c;

        public ClassViewListener(Class c) {
            this.c = c;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.showClassForm(c, false, false);
        }
    }

    private class ClassRemoveListener extends MouseAdapter {

        private Class c;

        public ClassRemoveListener(Class c) {
            this.c = c;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            classes.remove(c);
            c.setBeingUsed(false);
            drawClass();
            classBox.revalidate();
            classBox.repaint();
        }
    }
    // </editor-fold>

    public boolean isIsBeingUsed() {
        return isBeingUsed;
    }
}
