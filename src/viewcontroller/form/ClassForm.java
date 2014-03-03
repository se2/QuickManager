package viewcontroller.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.border.MatteBorder;
import kulcomponent.KulAnimator;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import kulcomponent.KulDayChooser;
import kulcomponent.KulFrame;
import kulcomponent.KulShinyText;
import kulcomponent.KulTextField;
import kulcomponent.KulUnderLineListener;
import model.Class;
import model.ClassSession;
import model.ClassType;
import model.Student;
import model.StudentClass;
import model.Teacher;
import model.TeacherClass;
import model.Validator;
import org.joda.time.LocalDate;
import viewcontroller.HasReturn;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class ClassForm extends JPanel implements HasReturn {

    /*
     * Any class with the name with this format XxxForm has the same code structure
     * with this class. The methods: init(), setSpring(), drawButtons(), convenient methods
     * are mostly to defint the GUI.
     */
    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private final Font f = Template.getFont().deriveFont(16f);
    private Class classes;
    private ClassType classType;
    private JLabel id = new JLabel();
    private JLabel title;
    private KulTextField name = new KulTextField(29);
    private KulShinyText teacher;
    private KulDayChooser startDate;
    private KulDayChooser endDate;
    private JLabel fee = new JLabel();
    private KulTextField book = new KulTextField(29);
    private JTextField nameTitle;
    private JTextField idTitle = new JTextField("ID:");
    private KulComboBox<String> classTypes;
    private JTextField teacherTitle;
    private JTextField startDateTitle;
    private JTextField endDateTitle;
    private JTextField feeTitle;
    private JTextField bookTitle;
    private JLabel sessionTitle;
    private JLabel currentStudentTitle;
    private JLabel classTypesTitle;
    private JLabel currentStudent = new JLabel("0");
//    private JLabel feeMess = new JLabel();
    private JLabel nameMess = new JLabel();
    private JLabel teacherMess = new JLabel();
    private JLabel dateMess = new JLabel();
    private JPanel subLeft;
    private JPanel sessionListPanel = new JPanel(new BorderLayout());
    private JPanel studentListPanel = new JPanel(new BorderLayout());
    private JPanel subRight = new JPanel(null);
    private JPanel left;
    private JPanel right;
    private boolean isStudentListShowing = false;
    private KulButton save;
    private KulButton cancel;
    private KulButton delete;
    private KulButton addSession;
    private KulButton enroll;
    private KulButton switchView;
    private Box buttons;
    private SpringLayout spring;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private HashMap<String, ClassSession> sessions;
    private Box sessionBox;
    private Box studentBox;
    private boolean isCopy;
    private boolean editable;
    private Teacher teacherOb;
    private final int RIGHT_PANEL_W = 510;
    private final int SUB_RIGHT_PANEL_H = 650;
    private TeacherClass tc;
//    private long weeklyFee;
    private long tuitionFee;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    public ClassForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout());
        language = mainFrame.getModel().getLanguage();
        currentStudentTitle = new JLabel(getString("currentStudent") + ": ");
        sessionTitle = new JLabel(getString("sessions"));
        startDate = new KulDayChooser(f, language);
        endDate = new KulDayChooser(f, language);

        // <editor-fold defaultstate="collapsed" desc="drawLeftPanel">
        left = new JPanel(new BorderLayout());
        left.setBorder(new MatteBorder(0, 0, 0, 1, Template.getBorderContrast()));
        left.setBackground(Template.getBackground());

        spring = new SpringLayout();
        subLeft = new JPanel(spring);
        subLeft.setBackground(Template.getBackground());
        left.add(subLeft);
        add(left);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="drawRightPanel">
        right = new JPanel(new BorderLayout());
        right.setPreferredSize(new Dimension(RIGHT_PANEL_W, 700));
        right.setBackground(Template.getBackground());
        subRight.setBackground(Template.getBackground());

        right.add(subRight);
        subRight.add(studentListPanel);
        subRight.add(sessionListPanel);
        add(right, BorderLayout.EAST);

        Box temp = new Box(BoxLayout.X_AXIS);
        temp.add(Box.createHorizontalStrut(45));
        temp.add(currentStudentTitle);
        temp.add(currentStudent);

        Box temp1 = new Box(BoxLayout.X_AXIS);
        temp1.add(Box.createHorizontalStrut(160));
        temp1.add(sessionTitle);

        studentBox = new Box(BoxLayout.Y_AXIS);
        studentListPanel.setBackground(Template.getBackground());
        studentListPanel.add(temp, BorderLayout.NORTH);
        studentListPanel.add(studentBox);

        sessionBox = new Box(BoxLayout.Y_AXIS);
        sessionListPanel.setBackground(Template.getBackground());
        sessionListPanel.add(temp1, BorderLayout.NORTH);
        sessionListPanel.add(sessionBox);

        sessionListPanel.setBounds(0, 0, RIGHT_PANEL_W, SUB_RIGHT_PANEL_H);
        // </editor-fold>

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        //</editor-fold>

        init();
        drawButtons();
    }

    private void init() {
        title = new JLabel(getString("class"));
        teacher = new KulShinyText(getString("pleaseAssignANewTeacher"), f.deriveFont(5f));
        nameTitle = new JTextField(getString("name1"));
        teacherTitle = new JTextField(getString("teacher1"));
        startDateTitle = new JTextField(getString("startDate1"));
        endDateTitle = new JTextField(getString("endDate1"));
        feeTitle = new JTextField(getString("tuitionFee"));
        bookTitle = new JTextField(getString("book"));
        classTypesTitle = new JLabel(getString("classType"));

        ArrayList<ClassType> allCT = mainFrame.getModel().getClassTypes();
        String[] ctString = new String[allCT.size()];
        for (int i = 0; i < allCT.size(); i++) {
            ctString[i] = allCT.get(i).getSkill() + " - " + allCT.get(i).getTypeString(language);
        }
        classTypes = new KulComboBox<>(ctString);

        classTypes.setFont(f);
        id.setFont(f);
        name.setFont(f);
        fee.setFont(f);
        book.setFont(f);
        classTypesTitle.setFont(f);
        title.setFont(f.deriveFont(50f));
        sessionTitle.setFont(f.deriveFont(46f));
        currentStudentTitle.setFont(f.deriveFont(46f));
        currentStudent.setFont(f.deriveFont(46f));
        sessionTitle.setToolTipText(getString("sessionHint"));
        currentStudentTitle.setToolTipText(getString("currentStudentHint"));

//        feeMess.setFont(Template.getFont());
        nameMess.setFont(Template.getFont());
        teacherMess.setFont(Template.getFont());
        dateMess.setFont(Template.getFont());

//        feeMess.setFocusable(false);
        nameMess.setFocusable(false);
        dateMess.setFocusable(false);
        teacherMess.setFocusable(false);
        teacher.setFocusable(false);
        title.setFocusable(false);
        sessionTitle.setFocusable(false);
        currentStudentTitle.setFocusable(false);
        currentStudent.setFocusable(false);
        classTypesTitle.setFocusable(false);

        teacherMess.setForeground(Color.red);
//        feeMess.setForeground(Color.red);
        nameMess.setForeground(Color.red);
        dateMess.setForeground(Color.red);
        id.setForeground(Template.getForeground());
        title.setForeground(Template.getForeground());
        nameTitle.setForeground(Template.getForeground());
        idTitle.setForeground(Template.getForeground());
        teacherTitle.setForeground(Template.getForeground());
        startDateTitle.setForeground(Template.getForeground());
        endDateTitle.setForeground(Template.getForeground());
        feeTitle.setForeground(Template.getForeground());
        bookTitle.setForeground(Template.getForeground());
        sessionTitle.setForeground(Template.getForeground());
        currentStudentTitle.setForeground(Template.getForeground());
        currentStudent.setForeground(Template.getForeground());
        classTypesTitle.setForeground(Template.getForeground());
        fee.setForeground(Template.getForeground());

        setComponent(nameTitle, 20, 30, 16);
        setComponent(idTitle, 20, 30, 16);
        setComponent(teacherTitle, 20, 30, 16);
        setComponent(startDateTitle, 20, 30, 16);
        setComponent(endDateTitle, 20, 30, 16);
        setComponent(feeTitle, 20, 30, 16);
        setComponent(bookTitle, 20, 30, 16);

        subLeft.add(classTypes);
        subLeft.add(classTypesTitle);
        subLeft.add(id);
        subLeft.add(nameTitle);
        subLeft.add(name);
        subLeft.add(teacher);
        subLeft.add(startDate);
        subLeft.add(endDate);
        subLeft.add(fee);
        subLeft.add(book);
//        subLeft.add(feeMess);
        subLeft.add(nameMess);
        subLeft.add(dateMess);
        subLeft.add(teacherMess);
        subLeft.add(idTitle);
        subLeft.add(title);
        subLeft.add(teacherTitle);
        subLeft.add(startDateTitle);
        subLeft.add(endDateTitle);
        subLeft.add(feeTitle);
        subLeft.add(bookTitle);
        setSpring();

        teacher.addMouseListener(new KulUnderLineListener());
        teacher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showTeacherListView(ClassForm.this, parentFrame, classes);
            }
        });

        classTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                sessions = new HashMap<>();
                validateClassType();
                classType = mainFrame.getModel().getClassTypes().get(classTypes.getSelectedIndex());
                classes.setClassType(new String[]{classType.getSkill(), classType.getType() + ""});
                fee.setText(classes.getTuitionFeeString());
                drawSession();
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setSpring">
    private void setSpring() {
        spring.putConstraint(SpringLayout.NORTH, title, 40, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.WEST, title, 185, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.NORTH, idTitle, 120, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, id, 120, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, nameTitle, 150, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, name, 175, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, teacherTitle, 210, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, teacher, 235, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, startDateTitle, 270, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, startDate, 295, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, endDateTitle, 330, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, endDate, 355, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, bookTitle, 390, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, book, 415, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, classTypesTitle, 450, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, classTypes, 475, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, feeTitle, 505, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, fee, 530, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, nameMess, 150, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, dateMess, 330, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, teacherMess, 210, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.EAST, nameMess, -90, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, dateMess, -90, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, teacherMess, -90, SpringLayout.EAST, subLeft);

        spring.putConstraint(SpringLayout.WEST, name, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, id, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, teacher, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, startDate, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, endDate, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, fee, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, book, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, classTypes, 65, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.WEST, nameTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, teacherTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, idTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, startDateTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, endDateTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, feeTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, bookTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, classTypesTitle, 40, SpringLayout.WEST, subLeft);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        save = new KulButton("Ok");
        cancel = new KulButton(getString("cancel"));
        delete = new KulButton(getString("delete"));
        addSession = new KulButton(getString("addSession"));
        enroll = new KulButton(getString("enroll"));
        switchView = new KulButton(getString("students"));

        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 60));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        left.add(buttons, BorderLayout.SOUTH);
        setComponent(save, 120, 30, 16);
        setComponent(cancel, 120, 30, 16);
        setComponent(delete, 120, 30, 16);
        setComponent(enroll, 120, 30, 16);
        setComponent(addSession, 120, 30, 16);
        setComponent(switchView, 120, 30, 16);

        //<editor-fold defaultstate="collapsed" desc="left buttons">
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.closeWindow(true);
            }
        });

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int value = JOptionPane.showConfirmDialog(parentFrame,
                        getString("deleteClassConfirmContent"), getString("deleteConfirm"),
                        JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteClass(classes)) {
                        JOptionPane.showMessageDialog(parentFrame,
                                getString("deleteSuccessContent"), getString("success"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame,
                                getString("deleteFailContent"),
                                getString("fail"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        save.addMouseListener(new SaveListener());

        buttons.add(Box.createHorizontalGlue());
        buttons.add(delete);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(save);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(cancel);
        buttons.add(Box.createHorizontalStrut(20));
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="right buttons">
        Box temp = new Box(BoxLayout.X_AXIS);
        temp.setPreferredSize(new Dimension(510, 42));
        temp.add(Box.createHorizontalStrut(20));
        temp.add(enroll);
        temp.add(addSession);
        temp.add(Box.createHorizontalGlue());
        temp.add(switchView);
        temp.add(Box.createHorizontalStrut(20));
        right.add(temp, BorderLayout.NORTH);
        enroll.setVisible(false);

        enroll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ArrayList<Class> classesTemp = new ArrayList<>(1);
                ArrayList<Student> students = new ArrayList<>(mainFrame.getModel().getStudents().size());
                classesTemp.add(classes);

                Iterator<Entry<String, StudentClass>> iter = mainFrame.getModel().getStudentClass().entrySet().iterator();
                while (iter.hasNext()) {
                    StudentClass sc = iter.next().getValue();
                    if (sc.getClassId().equalsIgnoreCase(classes.getClassId())) {
                        Student s = mainFrame.getModel().getStudents().get(sc.getStudentId());
                        students.add(s);
                    }
                }
                mainFrame.showStudentListView(ClassForm.this, parentFrame, classesTemp, students);
            }
        });

        addSession.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showRoomView(ClassForm.this, parentFrame, null, classes, classType);
            }
        });

        switchView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayRightPanel();
            }
        });
        // </editor-fold>
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="convenient method">
    private void setComponent(JComponent c, int w, int h, float size) {
        c.setFont(Template.getFont().deriveFont(size));
        c.setFocusable(false);
        if (c instanceof JTextField) {
            JTextField tf = (JTextField) c;
            tf.setEditable(false);
            tf.setBackground(Template.getBackground());
            tf.setBorder(null);
        } else {
            c.setPreferredSize(new Dimension(w, h));
            c.setMaximumSize(new Dimension(w, h));
        }
    }
    // </editor-fold>

    /*
     * This method is to draw all the session related to this class. Each session
     * is in each line. Each line has a listener for session
     */
    // <editor-fold defaultstate="collapsed" desc="drawSession">
    private void drawSession() {
        sessionBox.removeAll();
        if (sessions == null) {
            repaint();
            return;
        }

        Iterator<Entry<String, ClassSession>> iter = sessions.entrySet().iterator();

        int i = 1;
        while (iter.hasNext()) {
            ClassSession ses = iter.next().getValue();
            KulShinyText sessionLabel = new KulShinyText(getString("session") + " " + i + ": "
                    + getString("room") + " " + ses.getRoomId() + " " + ses.getTimeString(), f.deriveFont(5f));
            sessionLabel.addMouseListener(new SessionListener(ses));

            Box line = new Box(BoxLayout.X_AXIS);
            line.setPreferredSize(new Dimension(RIGHT_PANEL_W, 30));
            line.setMaximumSize(new Dimension(RIGHT_PANEL_W, 30));
            line.add(Box.createHorizontalStrut(50));
            line.add(sessionLabel);

            sessionBox.add(line);
            i++;
        }
        sessionBox.add(Box.createVerticalGlue());
        sessionBox.revalidate();
        repaint();
    }
    // </editor-fold>

    /**
     * draws all students that are currently enrolled in this class. Each
     * student is in each line
     */
    //<editor-fold defaultstate="collapsed" desc="drawStudents">
    private void drawStudents() {
        studentBox.removeAll();
        currentStudent.setText(classes.getCurrentNumberOfStudent() + "");

        Iterator<Entry<String, StudentClass>> iter = mainFrame.getModel().getStudentClass().entrySet().iterator();
        while (iter.hasNext()) {
            final StudentClass sc = iter.next().getValue();
            if (sc.getClassId().equalsIgnoreCase(classes.getClassId())) {
                final Student s = mainFrame.getModel().getStudents().get(sc.getStudentId());
                KulShinyText stuLabel = new KulShinyText(getString("student") + " " + s.getId()
                        + ", " + getString("name") + ": " + s.getFullname(), f.deriveFont(4f));
                stuLabel.setPreferredSize(new Dimension(340, 25));
                stuLabel.setMinimumSize(new Dimension(340, 25));
                stuLabel.setMaximumSize(new Dimension(340, 25));
                stuLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mainFrame.showStudentForm(s, false);
                    }
                });

                KulButton remove = new KulButton(getString("unenroll"));
                remove.setFont(f.deriveFont(11f));
                remove.setToolTipText(getString("unenrollHint"));
                remove.setPreferredSize(new Dimension(60, 20));
                remove.setMaximumSize(new Dimension(60, 20));
                remove.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int value = JOptionPane.showConfirmDialog(parentFrame,
                                getString("unenrollConfirmaContent"),
                                getString("unenrollConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                        if (value == JOptionPane.OK_OPTION) {
                            if (mainFrame.getModel().deleteStudentClass(sc)) {
                                JOptionPane.showMessageDialog(parentFrame, getString("unenrollSuccessContent"),
                                        getString("success"), JOptionPane.INFORMATION_MESSAGE);
                                drawStudents();
                            } else {
                                JOptionPane.showMessageDialog(parentFrame, getString("unenrollingFailContent"),
                                        getString("fail"), JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                });

                JLabel statusLabel = new JLabel();
                statusLabel.setFont(f.deriveFont(12f));
                if (sc.isPaid()) {
                    statusLabel.setText(getString("paid"));
                    statusLabel.setForeground(Template.getPaidColor());
                } else {
                    statusLabel.setText(getString("unpaid"));
                    statusLabel.setForeground(Template.getUnPaidColor());
                }

                Box line = new Box(BoxLayout.X_AXIS);
                line.setPreferredSize(new Dimension(RIGHT_PANEL_W, 25));
                line.setMaximumSize(new Dimension(RIGHT_PANEL_W, 25));
                line.add(Box.createHorizontalStrut(30));
                line.add(remove);
                line.add(Box.createHorizontalStrut(10));
                line.add(stuLabel);
                line.add(Box.createHorizontalGlue());
                line.add(statusLabel);
                line.add(Box.createHorizontalStrut(20));
                studentBox.add(line);
            }
        }
        studentBox.add(Box.createHorizontalGlue());
        studentBox.revalidate();
        repaint();
    }
    //</editor-fold>

    /*
     * This method is to draw the teacher that is associated with this class. If
     * user clicks on this line, it will call method in mainFrame: showTeacherListView
     * to display all available teacher for this class for user to choose.
     */
    // <editor-fold defaultstate="collapsed" desc="drawTeacher">
    private void drawTeacher() {
        if (classes == null) {
            teacherOb = null;
            teacher.setTextDisplay(getString("pleaseAssignANewTeacher"));
        } else {
            Iterator<Entry<String, TeacherClass>> iter = mainFrame.getModel().getTeacherClass().entrySet().iterator();
            while (iter.hasNext()) {
                TeacherClass tcTemp = iter.next().getValue();
                if (tcTemp.getClassId().equals(classes.getClassId())) {
                    tc = tcTemp;
                    teacherOb = mainFrame.getModel().getTeachers().get(tcTemp.getTeacherId());
                    teacher.setTextDisplay(teacherOb.getFullname());
                    break;
                }
            }
        }
    }
    // </editor-fold>

    /*
     * this method is to set the class to the view in order to display the information
     * of the setted class. If the class arg is null, it means that this GUI will display
     * empty field for user to ADD new class. When adding new class, this method creates
     * temp class. This temp class is only added to database when user click OK, else
     * the temp class is for fun
     */
    // <editor-fold defaultstate="collapsed" desc="setClass">
    public boolean setClass(Class classes, KulFrame parentFrame, boolean isCopy, boolean editable) {
        this.classes = classes;
        this.editable = editable;
        this.parentFrame = parentFrame;
        this.isCopy = isCopy;

        nameMess.setText("");
        dateMess.setText("");
        teacherMess.setText("");

        sessions = new HashMap<>();

        if (classes != null) {
            this.classType = mainFrame.getModel().getClassType(classes);

            if (classes.isBeingUsed()) {
                JOptionPane.showMessageDialog(parentFrame,
                        getString("classBeingUsedContent"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                parentFrame.closeWindow(false);
                return false;
            }
            classes.setBeingUsed(true);
            if (isCopy) {
                save.setTextDisplay(getString("add"));
                id.setVisible(false);
                idTitle.setVisible(false);
                delete.setVisible(false);
                this.classes = new Class(name.getText(), startDate.getDate(),
                        endDate.getDate(), book.getText(), classType, 0);
                teacher.setTextDisplay(getString("pleaseAssignANewTeacher"));

                enroll.setVisible(false);
                switchView.setVisible(false);
            } else {
                save.setTextDisplay(getString("ok"));
                id.setVisible(true);
                idTitle.setVisible(true);
            }

            name.setText(classes.getClassName());
            id.setText(classes.getClassId());
            teacher.setToolTipText("");
            startDate.setDate(classes.getStartDate());
            endDate.setDate(classes.getEndDate());
            fee.setText(classes.getTuitionFeeString());
            book.setText(classes.getTextBook());
            currentStudent.setText(classes.getCurrentNumberOfStudent() + "");
            tuitionFee = classes.getFee();
//            weeklyFee = classes.getWeeklyFee();

            if (!isCopy) {
                Iterator<Entry<String, ClassSession>> iter = mainFrame.getModel().getSessions().entrySet().iterator();
                while (iter.hasNext()) {
                    ClassSession ses = iter.next().getValue();
                    if (ses.getClassId().equals(classes.getClassId())) {
                        sessions.put(ses.getClassSessionId(), ses);
                    }
                }
            }
            drawSession();
            drawStudents();
            drawTeacher();
        } else {
            classType = mainFrame.getModel().getClassTypes().get(0);
            id.setVisible(false);
            delete.setVisible(false);
            idTitle.setVisible(false);
            enroll.setVisible(false);
            switchView.setVisible(false);
            save.setTextDisplay(getString("add"));
            name.setText("");
            id.setText("");
            teacher.setTextDisplay(getString("pleaseAssignANewTeacher"));
            teacher.setToolTipText(getString("assignTeacherHint"));
            startDate.setDate(new LocalDate());
            endDate.setDate(new LocalDate());
            book.setText("");

            // create a temp class. if user cancels while filling form,
            // this temp class cannot be save in the database
            this.classes = new Class(name.getText(), startDate.getDate(),
                    endDate.getDate(), book.getText(), classType, 0);
            fee.setText(this.classes.getTuitionFeeString());
            tuitionFee = this.classes.getFee();
//            weeklyFee = this.classes.getWeeklyFee();

            drawSession();
            drawTeacher();
        }
        if (!editable) {
            save.setVisible(false);
        }
        return true;
    }
    // </editor-fold>

    /*
     * when user clicks on the session line, this method will call the method in
     * mainFrame: showRoomView(), in order to display the timetable with the room
     * is the room of this session, each timetable's cells is selected if that cell
     * is the time of the session
     */
    // <editor-fold defaultstate="collapsed" desc="listerer for session">
    private class SessionListener extends MouseAdapter {

        private ClassSession classSession;

        public SessionListener(ClassSession classSession) {
            this.classSession = classSession;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.showRoomView(ClassForm.this, parentFrame, classSession, classes, classType);
        }
    }
    // </editor-fold >

    /*
     * this method is called from other view. The arg is the object that the other
     * view returns back to this class. So that, this method will repaint teacher or
     * session based on the returned object
     */
    // <editor-fold defaultstate="collapsed" desc="setReturnObj">
    @Override
    public void setReturnObj(Object o) {
        if (o == null) {// de-enroll
            currentStudent.setText(classes.getCurrentNumberOfStudent() + "");
            drawStudents();
        } else if (o instanceof HashMap) {
            HashMap<String, ClassSession> addedSessions = (HashMap<String, ClassSession>) o;
            Iterator<Entry<String, ClassSession>> addedSesIter = addedSessions.entrySet().iterator();
            Entry<String, ClassSession> addedSes = addedSesIter.next();

//            int roomIdToRemoveSession;
//            if (addedSes.getValue() == null) { // if roomView return 'empty' sessionsToAdd
//                roomIdToRemoveSession = Integer.parseInt(addedSes.getKey());
//            } else {
//                roomIdToRemoveSession = addedSes.getValue().getRoomId();
//            }

            sessions = new HashMap<>();
            Iterator<Entry<String, ClassSession>> iter = sessions.entrySet().iterator();
            //remove previous session with same room
//            while (iter.hasNext()) {
//                ClassSession cs = iter.next().getValue();
//                if (cs.getRoomId() == roomIdToRemoveSession) {
//                iter.remove();
//                }
//            }

            if (addedSes.getValue() != null) { // if roomView return 'empty' sessionsToAdd
                long feeTemp = 0;
                iter = addedSessions.entrySet().iterator();
                while (iter.hasNext()) {
                    ClassSession ses = iter.next().getValue();
                    sessions.put(ses.getClassSessionId(), ses);

                    if (ses.is45()) {
                        feeTemp += classType.getFeeFor45Min();
                    } else {
                        feeTemp += classType.getFeeFor60Min();
                    }
                }
//                weeklyFee = feeTemp;
                tuitionFee = calculateTotalFeeFromWeeklyFee(feeTemp);
            }
            fee.setText(tuitionFee + " VND");
            drawSession();
        } else if (o instanceof Teacher) {
            teacherOb = (Teacher) o;
            teacher.setTextDisplay(teacherOb.getFullname());
        }
        this.requestFocus();
    }
    // </editor-fold>

    /*
     * This method validate all the field when adding or editing class
     * it return true if all fields are passed, else returns false
     */
    // <editor-fold defaultstate="collapsed" desc="validate">
    private boolean validateFields() {
        boolean isValid;
        nameMess.setText("");
        dateMess.setText("");
        teacherMess.setText("");

        if (teacherOb == null) {
            teacherMess.setText(getString("teacherMess"));
            isValid = false;
        } else {
            isValid = false;
            String majorString = mainFrame.getModel().getClassType(classes).getSkill();
            for (int i = 0; i < teacherOb.getSkills().length; i++) {
                if (teacherOb.getSkills()[i][0].equals(majorString)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                teacherMess.setText(getString("teacherSkillClass"));
            }
        }

        if (startDate.getDate().compareTo(endDate.getDate()) >= 0) {
            dateMess.setText(getString("dateMess"));
            isValid = false;
        }

        if (name.getText() == null) {
            nameMess.setText(getString("fullNameEmptyMess"));
            isValid = false;
        }

        if (classes == null) {
            return isValid;
        }

        LocalDate end = endDate.getDate();
        Iterator<Entry<String, ClassSession>> sesIter1 = sessions.entrySet().iterator();

        // <editor-fold defaultstate="collapsed" desc="check its sessions with other's">
        while (sesIter1.hasNext()) {
            ClassSession ses1 = sesIter1.next().getValue();
            Iterator<Entry<String, ClassSession>> sesIter2 = mainFrame.getModel().getSessions().entrySet().iterator();
            while (sesIter2.hasNext()) {
                ClassSession ses2 = sesIter2.next().getValue();
                if (!ses2.getClassId().equals(ses1.getClassId())
                        && ses2.getRoomId() == ses1.getRoomId()) {
                    Class c = mainFrame.getModel().getClasses().get(ses2.getClassId());
                    if (end.compareTo(c.getStartDate()) >= 0) {
                        // <editor-fold defaultstate="collapsed" desc="smallest and biggest 1">
                        Iterator<Integer> subSubi1 = ses1.getTimes().keySet().iterator();
                        int s1 = subSubi1.next();
                        int b1 = s1;
                        while (subSubi1.hasNext()) {
                            int i = subSubi1.next();
                            if (s1 > i) {
                                s1 = i;
                            }
                            if (b1 < i) {
                                b1 = i;
                            }
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="smallest and biggest 2">
                        Iterator<Integer> subSubi2 = ses2.getTimes().keySet().iterator();
                        int s2 = subSubi2.next();
                        int b2 = s2;
                        while (subSubi2.hasNext()) {
                            int i = subSubi2.next();
                            if (s2 > i) {
                                s2 = i;
                            }
                            if (b2 < i) {
                                b2 = i;
                            }
                        }
                        // </editor-fold>
                        if ((s1 >= s2 && s1 < b2)
                                || (b1 <= b2 && b1 > s2)) {
                            JOptionPane.showMessageDialog(parentFrame,
                                    getString("session") + " " + ses1.getTimeString() + ", " + getString("room")
                                    + " " + ses1.getRoomId() + " " + getString("conflitWithSession") + " "
                                    + ses2.getTimeString() + ", " + getString("class") + " "
                                    + ses2.getClassId());
                            isValid = false;
                        }
                    }
                }
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="check its sessions with itself but other room">
        sesIter1 = sessions.entrySet().iterator();//reset iter
        while (sesIter1.hasNext()) {
            Iterator<Entry<String, ClassSession>> sesIter2 = sessions.entrySet().iterator();
            ClassSession ses1 = sesIter1.next().getValue();
            while (sesIter2.hasNext()) {
                ClassSession ses2 = sesIter2.next().getValue();
                if (ses1.getRoomId() != ses2.getRoomId()) {
                    // smallest and biggest 1
                    Iterator<Integer> subSubi1 = ses1.getTimes().keySet().iterator();
                    int s1 = subSubi1.next();
                    int b1 = s1;
                    while (subSubi1.hasNext()) {
                        int i = subSubi1.next();
                        if (s1 > i) {
                            s1 = i;
                        }
                        if (b1 < i) {
                            b1 = i;
                        }
                    }

                    // smallest and biggest 2
                    Iterator<Integer> subSubi2 = ses2.getTimes().keySet().iterator();
                    int s2 = subSubi2.next();
                    int b2 = s2;
                    while (subSubi2.hasNext()) {
                        int i = subSubi2.next();
                        if (s2 > i) {
                            s2 = i;
                        }
                        if (b2 < i) {
                            b2 = i;
                        }
                    }

                    if ((s1 >= s2 && s1 <= b2)
                            || (b1 <= b2 && b1 >= s2)) {
                        JOptionPane.showMessageDialog(parentFrame,
                                getString("session") + " " + ses1.getTimeString() + ", " + getString("room")
                                + " " + ses1.getRoomId() + " " + getString("conflitWithSession") + " "
                                + ses2.getTimeString() + ", " + getString("room") + " " + ses2.getRoomId());
                        isValid = false;
                    }
                }
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="validate teacher time">
        sesIter1 = sessions.entrySet().iterator();
        while (sesIter1.hasNext()) {
            if (!Validator.validateTeacherAndClassTime(mainFrame.getModel(), teacherOb,
                    sesIter1.next().getValue(), startDate.getDate(), endDate.getDate())) {
                teacherMess.setText(getString("teacherTimetableConflictClass"));
                isValid = false;
                break;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="validate student time">
        Iterator<Entry<String, StudentClass>> iterSC1 = mainFrame.getModel().getStudentClass().entrySet().iterator();
        while (iterSC1.hasNext()) {
            StudentClass sc1 = iterSC1.next().getValue();
            if (sc1.getClassId().equals(classes.getClassId())) {
                Student s = mainFrame.getModel().getStudents().get(sc1.getStudentId());

                Iterator<Entry<String, StudentClass>> iterSC2 = mainFrame.getModel().getStudentClass().entrySet().iterator();
                while (iterSC2.hasNext()) {
                    StudentClass sc2 = iterSC2.next().getValue();
                    if (sc2.getStudentId().equals(s.getId())) {
                        Class c2 = mainFrame.getModel().getClasses().get(sc2.getClassId());
                        if (!c2.getClassId().equals(classes.getClassId())) {

                            Iterator<Entry<String, ClassSession>> iterSes1 = sessions.entrySet().iterator();
                            while (iterSes1.hasNext()) {
                                ClassSession ses1 = iterSes1.next().getValue();

                                Iterator<Entry<String, ClassSession>> iterSes2 = mainFrame.getModel().getSessions().entrySet().iterator();
                                while (iterSes2.hasNext()) {
                                    ClassSession ses2 = iterSes2.next().getValue();
                                    if (ses2.getClassId().equals(c2.getClassId())) {
                                        if (!Validator.validateSessionTime(ses1, ses2)) {
                                            JOptionPane.showMessageDialog(parentFrame, getString("student") + " " + s.getId()
                                                    + getString("classTimeConflictClass"),
                                                    getString("caution"), JOptionPane.WARNING_MESSAGE);
                                            isValid = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // </editor-fold>

        return isValid;
    }
    // </editor-fold>

    /*
     * this method firstly invoke validate(). then it set all the variable of the
     * class based on all the fields. It deletes all session related to this class
     * before adding new sessions to database to avoid conflict
     */
    // <editor-fold defaultstate="collapsed" desc="ok">
    private void ok() {
        if (!editable) {
            return;
        }
        if (!validateFields()) {
            return;
        }

        boolean needResetPaySlip = false;
        if (endDate.getDate().compareTo(classes.getEndDate()) != 0
                || startDate.getDate().compareTo(classes.getEndDate()) != 0) {
            needResetPaySlip = true;
        }
        classes.setClassName(name.getText());
        classes.setEndDate(endDate.getDate());
        classes.setStartDate(startDate.getDate());
        classes.setTextBook(book.getText());
        classes.setClassType(new String[]{classType.getSkill(), classType.getType() + ""});
        classes.setFee(tuitionFee);
//        classes.setWeeklyFee(weeklyFee);

        if (save.getTextDisplay().equals("Ok")) {//edit a class
            //add new session to model
            mainFrame.getModel().saveData();
            mainFrame.getMenuView().update("Class");
            mainFrame.getModel().deleteSessionRelatedToClass(classes.getClassId());
            mainFrame.getModel().addSession(sessions);

            if (!tc.getTeacherId().equals(teacherOb.getId())) {
                mainFrame.getModel().updateTeacherClass(tc);//before
                tc.setTeacherId(teacherOb.getId());
                mainFrame.getModel().updateTeacherClass(tc);//after
                needResetPaySlip = false;
            }
            if (needResetPaySlip) {
                mainFrame.getModel().updateTeacherClass(tc);
            }

            JOptionPane.showMessageDialog(parentFrame, getString("updateClassConfirmContent"),
                    getString("success"), JOptionPane.INFORMATION_MESSAGE);
        } else {// add new class
            tc = new TeacherClass(teacherOb.getId(), classes.getClassId());
            mainFrame.getModel().addTeacherClass(tc);// do not change this order
            mainFrame.getModel().addClass(classes);// do not change this order
            mainFrame.getModel().addSession(sessions);

            JOptionPane.showMessageDialog(parentFrame, getString("addClassConfirmContent"),
                    getString("success"), JOptionPane.INFORMATION_MESSAGE);
        }
        parentFrame.closeWindow(true);
    }

    private class SaveListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            ok();
        }
    }
    // </editor-fold>

    /*
     * This method is to let user switch the right side view of thie form with
     * animation
     */
    // <editor-fold defaultstate="collapsed" desc="display right panel">
    private void displayRightPanel() {
        int x1 = RIGHT_PANEL_W;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int w = RIGHT_PANEL_W;
        int h = SUB_RIGHT_PANEL_H;

        if (!isStudentListShowing) {
            new KulAnimator(subRight, studentListPanel, x1, x2, y1, y2, w, h).slideHorizontal();
            new KulAnimator(subRight, sessionListPanel, x2, x1, y1, y2, w, h).slideHorizontal();

            isStudentListShowing = true;
            enroll.setVisible(true);
            addSession.setVisible(false);
            switchView.setTextDisplay(getString("sessions"));
        } else {
            new KulAnimator(subRight, sessionListPanel, x1, x2, y1, y2, w, h).slideHorizontal();
            new KulAnimator(subRight, studentListPanel, x2, x1, y1, y2, w, h).slideHorizontal();

            enroll.setVisible(false);
            addSession.setVisible(true);
            isStudentListShowing = false;
            switchView.setTextDisplay(getString("students"));
        }
    }
    // </editor-fold>

    private void validateClassType() {
        if (classTypes.getItemCount() != mainFrame.getModel().getClassTypes().size()) {
            JOptionPane.showMessageDialog(parentFrame, getString("classTypesChangeContent")
                    + " " + getString("add") + " " + getString("class") + " " + getString("classTypesChangeContent1"),
                    getString("caution"), JOptionPane.INFORMATION_MESSAGE);

            parentFrame.closeWindow(true);
        }
    }

    private String getString(String key) {
        return language.getString(key);
    }

    private long calculateTotalFeeFromWeeklyFee(long fee) {
        LocalDate start = startDate.getDate();
        LocalDate end = endDate.getDate();
        int dYear = end.getYear() - start.getYear();
        int dWeek = end.weekOfWeekyear().get() - start.weekOfWeekyear().get() + 1;
        int d = dYear * start.weekOfWeekyear().getMaximumValue() + dWeek;

        System.out.print("\nStart of class = " + start.toString());
        System.out.println(" -- End of class = " + end.toString());
        System.out.println("start - end (week) = " + d);

        return d * fee;
    }
}
