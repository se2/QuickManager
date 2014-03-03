package viewcontroller.listview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.border.MatteBorder;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import kulcomponent.KulImageButton;
import model.Class;
import model.ClassType;
import model.Student;
import model.StudentClass;
import model.Validator;
import viewcontroller.HasReturn;
import viewcontroller.KeyBindingSetter;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;
import viewcontroller.line.StudentLine;

/**
 *
 * @author Dam Linh
 *
 * See ClassListView
 */
public class StudentListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private HashMap<String, Student> students;// this students may change when user filters
    private HashMap<String, Student> studentsToFilter;// this students does not change
    private JScrollPane scroll;
    private Box allStudetsBox;
    private JPanel subCenterPanel;
    private Box buttonBar;
    private KulFrame parentFrame;
    private KulFrame prevFrame;
    private HasReturn hasReturn;
    private KulButton selectOrDeEnroll;
    private KulButton add;
    private JPanel centerPanel;
    private JTextField filter;
    private MainFrame mainFrame;
    private boolean isSmall;
    private boolean isDeEnroll;
    private Class classToDeEnroll;
    private Class classToEnroll;

    public StudentListView(MainFrame mainFrame, boolean isSmall) {
        this.mainFrame = mainFrame;
        this.isSmall = isSmall;
        students = mainFrame.getModel().getStudents();
        studentsToFilter = students;
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin
        language = mainFrame.getModel().getLanguage();

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Template.getBackground());
        add(centerPanel);

        if (!isSmall) {
            drawButtonBar();
        } else {
            KeyBindingSetter.setWestButtonKeyBinding(mainFrame, this, "Student");
        }

        subCenterPanel = new JPanel(new BorderLayout());
        centerPanel.add(subCenterPanel);

        drawFilter();
        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allStudetsBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allStudetsBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        subCenterPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        subCenterPanel.add(new TitleBar(mainFrame, "student"), BorderLayout.NORTH);

        drawAllStudents();
    }

    private void drawAllStudents() {
        allStudetsBox.removeAll();
        Iterator<Entry<String, Student>> iter = students.entrySet().iterator();
        while (iter.hasNext()) {
            Student s = iter.next().getValue();
            if (!isSmall) {
                s.setSelected(false);
            }
            allStudetsBox.add(new StudentLine(this, s, mainFrame, isSmall));
        }
    }

    private void drawFilter() {
        filter = new JTextField(100);
        filter.setToolTipText("Filter list");
        filter.setFont(Template.getFont().deriveFont(16f));
        KulImageButton search = new KulImageButton("search", 26, 26);

        SpringLayout spring = new SpringLayout();
        JPanel filterContainer = new JPanel(spring);
        filterContainer.add(search);
        filterContainer.add(filter);
        filterContainer.setOpaque(false);
        filterContainer.setPreferredSize(new Dimension(0, 30));

        spring.putConstraint(SpringLayout.WEST, filter, 0, SpringLayout.WEST, filterContainer);
        spring.putConstraint(SpringLayout.EAST, filter, -30, SpringLayout.EAST, filterContainer);
        spring.putConstraint(SpringLayout.NORTH, filter, 0, SpringLayout.NORTH, filterContainer);
        spring.putConstraint(SpringLayout.SOUTH, filter, 0, SpringLayout.SOUTH, filterContainer);

        spring.putConstraint(SpringLayout.NORTH, search, 2, SpringLayout.NORTH, filterContainer);
        spring.putConstraint(SpringLayout.EAST, search, -2, SpringLayout.EAST, filterContainer);
        spring.putConstraint(SpringLayout.SOUTH, search, -2, SpringLayout.SOUTH, filterContainer);
        spring.putConstraint(SpringLayout.WEST, search, -30, SpringLayout.EAST, filterContainer);
        centerPanel.add(filterContainer, BorderLayout.NORTH);

        filter.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "search");
        filter.getActionMap().put("search", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                search();
            }
        });
    }

    private void search() {
        students = mainFrame.getModel().filterStudent(filter.getText(), studentsToFilter);
        refresh();
    }

    public void refresh() {
        drawAllStudents();
        allStudetsBox.revalidate();
        allStudetsBox.repaint();
    }

    //<editor-fold defaultstate="collapsed" desc="drawButtonBar">
    private void drawButtonBar() {
        buttonBar = new Box(BoxLayout.X_AXIS);
        buttonBar.setPreferredSize(new Dimension(1024, 65));
        buttonBar.setOpaque(true);
        buttonBar.setBackground(Template.getBackground());
        buttonBar.setBorder(new MatteBorder(0, 0, 15, 0, Template.getBackground()));
        add(buttonBar, BorderLayout.NORTH);

        selectOrDeEnroll = new KulButton(language.getString("search"));
        selectOrDeEnroll.setToolTipText(language.getString("selectTheseStudents"));
        selectOrDeEnroll.setFont(Template.getFont().deriveFont(16f));
        selectOrDeEnroll.setPreferredSize(new Dimension(120, 30));
        selectOrDeEnroll.setMaximumSize(new Dimension(120, 30));
        selectOrDeEnroll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDeEnroll) {
                    int value = JOptionPane.showConfirmDialog(parentFrame,
                            language.getString("unenrollSelectedStudentConfirmContent"),
                            language.getString("unenrollSelectedStudentConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                    if (value == JOptionPane.OK_OPTION) {
                        deEnroll();
                    }
                } else {
                    enroll();
                }
            }
        });

        add = new KulButton(language.getString("add"));
        add.setToolTipText(language.getString("addNewStudentHint"));
        add.setFont(Template.getFont().deriveFont(16f));
        add.setPreferredSize(new Dimension(120, 30));
        add.setMaximumSize(new Dimension(120, 30));
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showStudentForm(null, true);
                parentFrame.closeWindow(true);
            }
        });
        buttonBar.add(Box.createHorizontalStrut(20));
        buttonBar.add(selectOrDeEnroll);
        buttonBar.add(Box.createHorizontalStrut(20));
        buttonBar.add(add);
    }
    //</editor-fold>
    //</editor-fold>

    public void showStudent(Student student, boolean editable) {
        mainFrame.showStudentForm(student, editable);
    }

    // <editor-fold defaultstate="collapsed" desc="ParentFrameListener to close">
    private class ParentFrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            if (prevFrame != null) {
                prevFrame.setEnabled(true);
            }
            if (students != null) {
                Iterator<Entry<String, Student>> iter = students.entrySet().iterator();
                while (iter.hasNext()) {
                    iter.next().getValue().setBeingUsed(false);// release lock
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enroll">
    private void enroll() {
        Iterator<Map.Entry<String, Student>> iter = students.entrySet().iterator();
        HashMap<String, Student> studentsToSend = new HashMap<>();

        while (iter.hasNext()) {
            Student s = iter.next().getValue();
            if (s.isSelected()) {
                studentsToSend.put(s.getId(), s);
            }
        }

        if (studentsToSend.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, language.getString("selectOneStudentWarningContent"),
                    language.getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //validate and enroll
        boolean isValid = true;
        int currentStudentNumber = studentsToSend.size();
        ClassType ctClassToEnroll = mainFrame.getModel().getClassType(classToEnroll);
        if (classToEnroll.getCurrentNumberOfStudent() + currentStudentNumber > classToEnroll.getCapacity(ctClassToEnroll)) {
            isValid = false;
            JOptionPane.showMessageDialog(parentFrame, classToEnroll.getClassId()
                    + " " + language.getString("exceedCapacity"));
        }

        Iterator<Entry<String, Student>> iterStu = studentsToSend.entrySet().iterator();
        while (iterStu.hasNext()) {
            Student s = iterStu.next().getValue();

            Iterator<Entry<String, StudentClass>> iterSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
            while (iterSC.hasNext()) {
                StudentClass sc = iterSC.next().getValue();
                if (sc.getStudentId().equals(s.getId())) {
                    Class c2 = mainFrame.getModel().getClasses().get(sc.getClassId());
                    if (!Validator.validateClassTime(classToEnroll, c2, mainFrame.getModel())) {
                        isValid = false;
                        JOptionPane.showMessageDialog(parentFrame, language.getString("student") + " "
                                + s.getId() + " " + language.getString("hasTimetableConflict"),
                                language.getString("caution"), JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }

        if (isValid) {
            iterStu = studentsToSend.entrySet().iterator();
            while (iterStu.hasNext()) {
                Student s = iterStu.next().getValue();
                StudentClass sc = new StudentClass(s.getId(), classToEnroll.getClassId());

                if (!mainFrame.getModel().addStudentClass(sc)) {
                    JOptionPane.showMessageDialog(parentFrame, language.getString("classExceedCapacity"),
                            language.getString("caution"), JOptionPane.WARNING_MESSAGE);
                }
            }
            parentFrame.closeWindow(true);
            hasReturn.setReturnObj(null);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="de-enroll">
    private void deEnroll() {
        Iterator<Map.Entry<String, Student>> iter = students.entrySet().iterator();
        if (hasReturn == null) {
            JOptionPane.showMessageDialog(parentFrame, language.getString("unenrollNotAvailableContent"),
                    language.getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (classToDeEnroll == null || hasReturn == null) {
            parentFrame.closeWindow(true);
            return;
        }

        while (iter.hasNext()) {
            Student s = iter.next().getValue();
            if (s.isSelected()) {

                Iterator<Entry<String, StudentClass>> iterSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
                while (iterSC.hasNext()) {
                    StudentClass sc = iterSC.next().getValue();
                    if (sc.getStudentId().equals(s.getId()) && classToDeEnroll.getClassId().equals(sc.getClassId())) {
                        mainFrame.getModel().deleteStudentClass(sc);
                    }
                }
            }
        }
        parentFrame.closeWindow(true);
        hasReturn.setReturnObj(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setClasses">
    public void setClass(HasReturn hasReturn, KulFrame parentFrame, KulFrame prevFrame, ArrayList<Class> classes, ArrayList<Student> stus) {
        this.parentFrame = parentFrame;
        this.prevFrame = prevFrame;
        this.hasReturn = hasReturn;
        isDeEnroll = false;
        selectOrDeEnroll.setTextDisplay(language.getString("enroll"));
        selectOrDeEnroll.setToolTipText(language.getString("enrollCheckedStudentHint"));
        parentFrame.addWindowListener(new ParentFrameListener());

        students = new HashMap<>();
        Iterator<Map.Entry<String, Student>> iter = mainFrame.getModel().getStudents().entrySet().iterator();
        while (iter.hasNext()) {
            Student s = iter.next().getValue();
            if (stus.contains(s)) {
                continue;
            }
            if (s.isBeingUsed()) {
                JOptionPane.showMessageDialog(parentFrame, language.getString("student") + " " + s.getId()
                        + " " + language.getString("someThingBeingUsedContent"),
                        language.getString("caution"), JOptionPane.WARNING_MESSAGE);
                parentFrame.closeWindow(true);
                return;
            }
            students.put(s.getId(), s);
            s.setSelected(false);
            s.setBeingUsed(true);

            for (int j = 0; j < stus.size(); j++) {
                if (stus.get(j) == s) {
                    students.remove(s.getId());
                    s.setBeingUsed(false);
                }
            }

            for (int i = 0; i < classes.size(); i++) {
                Iterator<Map.Entry<String, StudentClass>> iterSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
                while (iterSC.hasNext()) {
                    StudentClass sc = iterSC.next().getValue();
                    if (s.getId().equals(sc.getStudentId())
                            && sc.getClassId().equals(classes.get(i).getClassId())) {
                        students.remove(s.getId());
                        s.setBeingUsed(false);
                    }
                }
            }
        }
        studentsToFilter = students;
        drawAllStudents();
        classToEnroll = classes.get(0);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setClasses to de-enroll">
    public void setClass(HasReturn hasReturn, KulFrame parentFrame, KulFrame prevFrame, Class c) {
        this.parentFrame = parentFrame;
        this.prevFrame = prevFrame;
        this.hasReturn = hasReturn;
        this.classToDeEnroll = c;

        isDeEnroll = true;
        selectOrDeEnroll.setTextDisplay(language.getString("unenroll"));
        selectOrDeEnroll.setToolTipText("Unenroll selected students");
        parentFrame.addWindowListener(new ParentFrameListener());

        students = new HashMap<>();
        Iterator<Entry<String, StudentClass>> iterSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
        while (iterSC.hasNext()) {
            StudentClass sc = iterSC.next().getValue();
            if (sc.getClassId().equals(c.getClassId())) {
                Student s = mainFrame.getModel().getStudents().get(sc.getStudentId());
                if (s.isBeingUsed()) {
                    JOptionPane.showMessageDialog(parentFrame, "Some student (" + s.getId()
                            + ") is being used in other window. Please close that window!",
                            "Student is being used", JOptionPane.WARNING_MESSAGE);
                    parentFrame.closeWindow(true);
                    return;
                }
                s.setSelected(false);
                s.setBeingUsed(true);
                students.put(s.getId(), s);
            }
        }
        studentsToFilter = students;
        drawAllStudents();
    }
    // </editor-fold>
}
