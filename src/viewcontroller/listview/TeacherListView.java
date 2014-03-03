package viewcontroller.listview;

import viewcontroller.line.TeacherLine;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import model.Class;
import model.Teacher;
import viewcontroller.HasReturn;
import viewcontroller.KeyBindingSetter;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;

/**
 *
 * @author Dam Linh
 *
 * See ClassListView
 */
public class TeacherListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private HashMap<String, Teacher> teachers;
    private JScrollPane scroll;
    private Box allTeacherBox;
    private JPanel subCenterPanel;
    private JPanel centerPanel;
    private Box buttonBar;
    private MainFrame mainFrame;
    private KulButton select;
    private KulButton add;
    private KulFrame parentFrame;
    private KulFrame prevFrame;
    private HasReturn hasReturn;
    private Class classes;
    private boolean isSmall;

    public TeacherListView(MainFrame mainFrame, boolean isSmall) {
        this.mainFrame = mainFrame;
        this.isSmall = isSmall;
        teachers = mainFrame.getModel().getTeachers();
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin
        language = mainFrame.getModel().getLanguage();

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Template.getBackground());
        add(centerPanel);

        if (!isSmall) {
            drawButtonBar();
        } else {
            KeyBindingSetter.setWestButtonKeyBinding(mainFrame, this, "Teacher");
        }

        subCenterPanel = new JPanel(new BorderLayout());
        centerPanel.add(subCenterPanel);
        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allTeacherBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allTeacherBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        subCenterPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        subCenterPanel.add(new TitleBar(mainFrame, "teacher"), BorderLayout.NORTH);

        if (isSmall) {// if !isSmall then draw later
            drawAllTeacher();
        }
    }

    private void drawAllTeacher() {
        allTeacherBox.removeAll();
        Iterator<Entry<String, Teacher>> iter = teachers.entrySet().iterator();
        while (iter.hasNext()) {
            Teacher t = iter.next().getValue();
//            t.setSelected(false);
            allTeacherBox.add(new TeacherLine(this, mainFrame, t, isSmall));
        }
    }

    public void refresh() {
        drawAllTeacher();
        allTeacherBox.revalidate();
        allTeacherBox.repaint();
    }

    public void refresh(boolean isSmall) {// to distinguish the above refresh()
        if (prevFrame == null) {
            prevFrame = new KulFrame("QuickManage");
        }
        setClass(hasReturn, parentFrame, prevFrame, classes, false);
    }

    private void drawButtonBar() {
        buttonBar = new Box(BoxLayout.X_AXIS);
        buttonBar.setPreferredSize(new Dimension(1024, 65));
        buttonBar.setOpaque(true);
        buttonBar.setBackground(Template.getBackground());
        buttonBar.setBorder(new MatteBorder(0, 0, 15, 0, Template.getBackground()));
        add(buttonBar, BorderLayout.NORTH);

        select = new KulButton(language.getString("select"));
        select.setToolTipText(language.getString("select") + " " + language.getString("teacher"));
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
        add.setToolTipText(language.getString("add") + " " + language.getString("teacher"));
        add.setFont(Template.getFont().deriveFont(16f));
        add.setPreferredSize(new Dimension(120, 30));
        add.setMaximumSize(new Dimension(120, 30));
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showTeacherForm(null, true);
//                parentFrame.closeWindow(true);
            }
        });
        buttonBar.add(Box.createHorizontalStrut(20));
        buttonBar.add(select);
        buttonBar.add(Box.createHorizontalStrut(20));
        buttonBar.add(add);
    }
    // </editor-fold>

    public void showTeacherForm(Teacher teacher, boolean editable) {
        mainFrame.showTeacherForm(teacher, editable);
    }

    // <editor-fold defaultstate="collapsed" desc="ParentFrameListener to close">
    private class ParentFrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            if (prevFrame != null) {
                prevFrame.setEnabled(true);
            }
            if (teachers != null) {
                Iterator<Entry<String, Teacher>> iter = teachers.entrySet().iterator();
                while (iter.hasNext()) {
                    iter.next().getValue().setBeingUsed(false);
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="select">
    private void select() {
        Iterator<Entry<String, Teacher>> iter = teachers.entrySet().iterator();
        Teacher teacherToSend = null;

        int count = 0;
        while (iter.hasNext()) {
            Teacher t = iter.next().getValue();
            if (t.isSelected()) {
                count++;
                if (count > 1) {
                    JOptionPane.showMessageDialog(parentFrame, language.getString("selectOneTeacherWarningContent"),
                            language.getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    teacherToSend = t;
                }
            }
        }
        if (teacherToSend == null) {
            JOptionPane.showMessageDialog(parentFrame, language.getString("selectOneTeacherWarningContent"),
                    language.getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        parentFrame.closeWindow(true);
        hasReturn.setReturnObj(teacherToSend);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setClass">
    public void setClass(HasReturn hasReturn, KulFrame parentFrame, KulFrame prevFrame, Class classes, boolean checkBeingUsed) {
        this.parentFrame = parentFrame;
        this.prevFrame = prevFrame;
        this.classes = classes;
        this.hasReturn = hasReturn;
        if (parentFrame == null) {
            parentFrame = mainFrame;
        }
        parentFrame.addWindowListener(new ParentFrameListener());

        if (classes != null) {
            String major = mainFrame.getModel().getClassType(classes).getSkill();

            teachers = new HashMap<>();
            Iterator<Entry<String, Teacher>> iter = mainFrame.getModel().getTeachers().entrySet().iterator();

            while (iter.hasNext()) {
                Teacher t = iter.next().getValue();
                if (checkBeingUsed && t.isBeingUsed()) {
                    JOptionPane.showMessageDialog(parentFrame, language.getString("teacher") + " " + t.getId()
                            + " " + language.getString("someThingBeingUsedContent"),
                            language.getString("caution"), JOptionPane.WARNING_MESSAGE);
                    parentFrame.closeWindow(true);
                    return;
                }
                for (int i = 0; i < t.getSkills().length; i++) {
                    if (t.getSkills()[i][0].equals(major)) {
                        teachers.put(t.getId(), t);
                        t.setSelected(false);
                        if (isSmall) {
                            t.setBeingUsed(true);
                        }
                        break;
                    }
                }
            }
        } else {
            teachers = mainFrame.getModel().getTeachers();
            Iterator<Entry<String, Teacher>> iter = teachers.entrySet().iterator();
            while (iter.hasNext()) {
                Teacher t = iter.next().getValue();
                if (t.isBeingUsed()) {
                    JOptionPane.showMessageDialog(parentFrame, language.getString("teacher") + " " + t.getId()
                            + " " + language.getString("someThingBeingUsedContent"),
                            language.getString("caution"), JOptionPane.WARNING_MESSAGE);
                    parentFrame.closeWindow(true);
                    return;
                } else {
                    t.setBeingUsed(true);
                }
            }
        }
        drawAllTeacher();
        allTeacherBox.revalidate();
        allTeacherBox.repaint();
    }
    // </editor-fold>
}
