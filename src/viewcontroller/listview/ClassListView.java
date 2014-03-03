package viewcontroller.listview;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import model.Class;
import model.Student;
import model.StudentClass;
import model.Teacher;
import model.TeacherClass;
import viewcontroller.HasReturn;
import viewcontroller.KeyBindingSetter;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;
import viewcontroller.line.ClassLine;

/**
 *
 * @author Dam Linh
 *
 * All classes with format name: XxxListView are same with this class. This
 * class is to display all the classes of the system in the list format. It has
 * 2 uses. 1 is to display all the classes in the MenuView (isSmall = true) The
 * other purpose is to display all the classes available for some purposes such
 * as enrollment, select class for student, for teacher (isSmall = false). The
 * flag isSmall is to separate 2 purposes.
 *
 */
public class ClassListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private HashMap<String, Class> classes;
    private JScrollPane scroll;
    private Box allClassBox;
    private JPanel subCenterPanel;
    private JPanel centerPanel;
    private Box buttonBar;
    private KulButton select;
    private KulButton add;
    private JTextField filter;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private KulFrame prevFrame;
    private HasReturn hasReturn;
    private Teacher teacher;
    private boolean isSmall;

    public ClassListView(MainFrame mainFrame, boolean isSmall) {
        this.isSmall = isSmall;
        this.mainFrame = mainFrame;
        classes = mainFrame.getModel().getClasses();
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin
        language = mainFrame.getModel().getLanguage();

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Template.getBackground());
        add(centerPanel);

        if (!isSmall) {
            drawButtonBar();
        } else {
            KeyBindingSetter.setWestButtonKeyBinding(mainFrame, this, "Class");
        }

        filter = new JTextField(100);
        filter.setToolTipText("Filter list");
        filter.setFont(Template.getFont().deriveFont(16f));
        filter.setPreferredSize(new Dimension(0, 30));
        subCenterPanel = new JPanel(new BorderLayout());
        centerPanel.add(subCenterPanel);
//        centerPanel.add(filter, BorderLayout.NORTH);

        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allClassBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allClassBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        subCenterPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        subCenterPanel.add(new TitleBar(mainFrame, "class"), BorderLayout.NORTH);

        if (isSmall) {
            drawAllClass();
        }
    }

    private void drawAllClass() {
        allClassBox.removeAll();
        Iterator<String> iter = classes.keySet().iterator();
        while (iter.hasNext()) {
            allClassBox.add(new ClassLine(this, classes.get(iter.next()), mainFrame, isSmall));
        }
    }

    public void refresh() {
        drawAllClass();
        allClassBox.revalidate();
        allClassBox.repaint();
    }

    public void refresh(boolean isSmall) {//distinguish from the above refresh
        if (prevFrame == null) {
            prevFrame = new KulFrame("QuickManage");
        }
        setTeacher(hasReturn, parentFrame, prevFrame, teacher, false);
    }

    private void drawButtonBar() {
        buttonBar = new Box(BoxLayout.X_AXIS);
        buttonBar.setPreferredSize(new Dimension(1024, 65));
        buttonBar.setOpaque(true);
        buttonBar.setBackground(Template.getBackground());
        buttonBar.setBorder(new MatteBorder(0, 0, 15, 0, Template.getBackground()));
        add(buttonBar, BorderLayout.NORTH);

        select = new KulButton(language.getString("select"));
        select.setToolTipText(language.getString("selectTheseClassHint"));
        select.setFont(Template.getFont().deriveFont(16f));
        select.setPreferredSize(new Dimension(120, 30));
        select.setMaximumSize(new Dimension(120, 30));
        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select();
            }
        });

        add = new KulButton(language.getString("add"));
        add.setToolTipText(language.getString("addNewClassHint"));
        add.setFont(Template.getFont().deriveFont(16f));
        add.setPreferredSize(new Dimension(120, 30));
        add.setMaximumSize(new Dimension(120, 30));
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showClassForm(null, false, true);
//                parentFrame.closeWindow(true);
            }
        });
        buttonBar.add(Box.createHorizontalStrut(20));
        buttonBar.add(select);
        buttonBar.add(Box.createHorizontalStrut(20));
        buttonBar.add(add);
    }
    // </editor-fold>

    /*
     * invoked in the ClassLine to display the view of a class.
     */
    public void showClass(Class classes, boolean isSmall) {
        mainFrame.showClassForm(classes, false, isSmall);
    }

    /**
     * When the list view is opening and isSmall = false, all the classes
     * displayed in the list will be locked (isBeingUsed = true) and the
     * previous frame will be disabled. Therefore, when the frame is closed, it
     * will release the locks and enable the previous frame.
     */
    // <editor-fold defaultstate="collapsed" desc="ParentFrameListener to close">
    private class ParentFrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            if (prevFrame != null) {
                prevFrame.setEnabled(true);
            }
            if (classes != null) {
                Iterator<Entry<String, Class>> iter = classes.entrySet().iterator();
                while (iter.hasNext()) {
                    iter.next().getValue().setBeingUsed(false);// release lock
                }
            }
        }
    }
    // </editor-fold>.

    /**
     * This method will be invoked when user clicked on Select button. This
     * method will get all the classes object and validate then send them to the
     * previous frame that calls this list view class. Then it will close the
     * frame
     */
    // <editor-fold defaultstate="collapsed" desc="select">
    private void select() {
        Iterator<Map.Entry<String, Class>> iter = classes.entrySet().iterator();
        HashMap<String, Class> classesToSend = new HashMap<>();

        while (iter.hasNext()) {
            Class c = iter.next().getValue();
            if (c.isSelected()) {
                classesToSend.put(c.getClassId(), c);
            }
        }
        if (classesToSend.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, language.getString("selectOneClassWarningContent"),
                    language.getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        parentFrame.closeWindow(true);
        hasReturn.setReturnObj(classesToSend);
    }
    // </editor-fold>

    /*
     * This method is invoked when the previous frame is TeacherForm so that this class
     * will display only classes that are availble for that teacher
     */
    // <editor-fold defaultstate="collapsed" desc="setTeacher">
    public void setTeacher(HasReturn hasReturn, KulFrame parentFrame, KulFrame prevFrame, Teacher teacher, boolean checkBeingUsed) {
        this.parentFrame = parentFrame;
        this.prevFrame = prevFrame;
        this.teacher = teacher;
        this.hasReturn = hasReturn;
        if (parentFrame == null) {
            parentFrame = mainFrame;
        }
        parentFrame.addWindowListener(new ParentFrameListener());

        if (teacher != null) {
            String[][] skills = teacher.getSkills();
            classes = new HashMap<>();

            Iterator<Entry<String, Class>> iter = mainFrame.getModel().getClasses().entrySet().iterator();
            while (iter.hasNext()) {
                Class c = iter.next().getValue();
                String major = mainFrame.getModel().getClassType(c).getSkill();

                if (checkBeingUsed && checkClassIsBeingUsed(c)) {
                    return;
                }

                Iterator<Entry<String, TeacherClass>> iterTC = mainFrame.getModel().getTeacherClass().entrySet().iterator();
                while (iterTC.hasNext()) {
                    TeacherClass tc = iterTC.next().getValue();
                    if (!c.getClassId().equals(tc.getClassId())
                            && tc.getTeacherId().equals(teacher.getId())) {
                        for (int i = 0; i < skills.length; i++) {
                            if (major.equals(skills[i][0])) {
                                classes.put(c.getClassId(), c);
                                c.setSelected(false);
                                if (isSmall) {
                                    c.setBeingUsed(true);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            classes = mainFrame.getModel().getClasses();
            Iterator<Entry<String, Class>> iter = classes.entrySet().iterator();
            while (iter.hasNext()) {
                Class c = iter.next().getValue();
                if (checkClassIsBeingUsed(c)) {
                    return;
                }
                c.setBeingUsed(true);
            }
        }
        drawAllClass();
    }    // </editor-fold>

    /*
     * Same with setTeacher
     */
    // <editor-fold defaultstate="collapsed" desc="setStudent">
    public void setStudent(HasReturn hasReturn, KulFrame parentFrame, KulFrame prevFrame, ArrayList<Student> students, ArrayList<Class> classesToCheck) {
        this.parentFrame = parentFrame;
        this.prevFrame = prevFrame;
        this.hasReturn = hasReturn;
        parentFrame.addWindowListener(new ParentFrameListener());

        classes = new HashMap<>();

        Iterator<Entry<String, Class>> iter = mainFrame.getModel().getClasses().entrySet().iterator();
        outer:
        while (iter.hasNext()) {
            Class c = iter.next().getValue();

            for (int j = 0; j < classesToCheck.size(); j++) {
                if (classesToCheck.get(j).getClassId().equals(c.getClassId())) {
                    continue outer;
                }
            }

            if (checkClassIsBeingUsed(c)) {
                return;
            }
            classes.put(c.getClassId(), c);
            c.setSelected(false);
            c.setBeingUsed(true);

            for (int i = 0; i < students.size(); i++) {

                Iterator<Entry<String, StudentClass>> iterSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
                while (iterSC.hasNext()) {
                    StudentClass sc = iterSC.next().getValue();
                    if (c.getClassId().equals(sc.getClassId())
                            && sc.getStudentId().equals(students.get(i).getId())) {
                        classes.remove(c.getClassId());
                        c.setBeingUsed(false);
                    }
                }
            }
        }
        drawAllClass();
    }
    // </editor-fold>

    /**
     * If the class is being used(locked), this class will warn user, then close
     * the frame to avoid conflict
     */
    // <editor-fold defaultstate="collapsed" desc="check class is being used">
    private boolean checkClassIsBeingUsed(Class c) {
        if (c.isBeingUsed()) {
            JOptionPane.showMessageDialog(parentFrame, language.getString("class") + " " + c.getClassId()
                    + " " + language.getString("someThingBeingUsedContent"),
                    language.getString("caution"), JOptionPane.WARNING_MESSAGE);
            parentFrame.closeWindow(true);
            return true;
        } else {
            return false;
        }
    }
    // </editor-fold>
}
