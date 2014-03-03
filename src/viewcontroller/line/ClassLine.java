package viewcontroller.line;

import viewcontroller.listview.ClassListView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import model.Class;
import model.TeacherClass;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.listview.ClassListView;
import kulcomponent.KulImageButton;

/**
 *
 * @author Dam Linh
 *
 * All the classes with the format name: XxxLine are the same with this class.
 * This class is to display each line of the list view(record). When user click
 * on this line, it invoke method in the mainFrame: showXxx() to display
 * information about that object. If the object has some functions such as
 * delete, deactivate or copy, this line also displays some button for those
 * functions
 */
public class ClassLine extends Box {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private Class classes;
    private Color currentBGColor;
    private Color defaultBGColor = Template.getLineBackground();
    private Color selectedBGColor = Template.getTimeTableSelected();
    private Color borderColor = Template.getLineBorderColor();
    private Font f = Template.getFont().deriveFont(15f);
    private Border clickedBorder;
    private Border overedBorder;
    private JLabel id;
    private JLabel teacherName;
    private JLabel startDate;
    private JLabel endDate;
    private KulImageButton delete;
    private KulImageButton copy;
    private ClassListView parent;
    private boolean isSmall;
    private MainFrame mainFrame;

    public ClassLine(ClassListView parent, Class classes, MainFrame mainFrame, boolean isSmall) {
        super(BoxLayout.X_AXIS);
        this.parent = parent;
        this.classes = classes;
        this.mainFrame = mainFrame;
        this.isSmall = isSmall;
        language = mainFrame.getModel().getLanguage();

        setOpaque(true);
        setBackground(defaultBGColor);
        setPreferredSize(new Dimension(600, 30));
        setMaximumSize(new Dimension(1024, 30));

        setCurrentBackgroundColor();
        setBackground(currentBGColor);
        setBorder(BorderFactory.createLineBorder(currentBGColor, 2));

        clickedBorder = new CompoundBorder(
                new MatteBorder(1, 0, 0, 1, currentBGColor),
                new MatteBorder(1, 2, 1, 1, borderColor));
        overedBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, borderColor),
                new MatteBorder(1, 2, 1, 0, currentBGColor));

        id = new JLabel(classes.getClassId() + "");
        id.setHorizontalAlignment(SwingConstants.CENTER);

        Iterator<Entry<String, TeacherClass>> iter = mainFrame.getModel().getTeacherClass().entrySet().iterator();
        while (iter.hasNext()) {
            TeacherClass tc = iter.next().getValue();
            if (tc.getClassId().equals(classes.getClassId())) {
                teacherName = new JLabel(mainFrame.getModel().getTeachers().get(tc.getTeacherId()).getFullname());
                break;
            }
        }

        startDate = new JLabel(classes.getStartDate().toString());
        endDate = new JLabel(classes.getEndDate().toString());
        initLabel(id, 80, 30);
        initLabel(startDate, 180, 30);
        initLabel(endDate, 180, 30);
        teacherName.setFont(f);

        delete = new KulImageButton("delete", 11, 11);
        delete.addMouseListener(new Delete());
        delete.setToolTipText(language.getString("deleteThisClassHint"));


        copy = new KulImageButton("copy", 11, 11);
        copy.addMouseListener(new Copy());
        copy.setToolTipText(language.getString("copyThisClassHint"));

        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setPreferredSize(new Dimension(15, 30));
        buttonBox.setMaximumSize(new Dimension(15, 30));
        buttonBox.add(delete);
        buttonBox.add(Box.createVerticalStrut(4));
        buttonBox.add(copy);

        add(id);
        add(Box.createHorizontalStrut(8));
        add(startDate);
        add(Box.createHorizontalStrut(8));
        add(endDate);
        add(Box.createHorizontalStrut(8));
        add(teacherName);
        add(Box.createHorizontalGlue());
        add(buttonBox);

        addMouseListener(new ClassLineActionListener());
        addMouseListener(new ClassLineViewListener());
    }

    // convenient method
    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(f);
    }

    // convenient method
    private void setCurrentBackgroundColor() {
        if (classes.isSelected()) {
            currentBGColor = selectedBGColor;
        } else {
            currentBGColor = defaultBGColor;
        }
        clickedBorder = new CompoundBorder(
                new MatteBorder(1, 0, 0, 1, currentBGColor),
                new MatteBorder(1, 2, 1, 1, borderColor));
        overedBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, borderColor),
                new MatteBorder(1, 2, 1, 0, currentBGColor));
    }

    //this listener is just making the LineTask view more beautiful
    private class ClassLineViewListener extends MouseAdapter {

        private boolean isMouseOvered;

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
            isMouseOvered = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(new LineBorder(currentBGColor, 2));
            isMouseOvered = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                setBorder(clickedBorder);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isMouseOvered) {
                setBorder(overedBorder);
            }
        }
    }

    private class ClassLineActionListener extends MouseAdapter implements ActionListener {

        private int interval = (Integer) Toolkit.getDefaultToolkit().
                getDesktopProperty("awt.multiClickInterval");
        private Timer timer = new Timer(interval, this);
        private MouseEvent lastEvent;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 2 || SwingUtilities.isRightMouseButton(e)) {
                return;
            }
            lastEvent = e;

            if (timer.isRunning()) {
                timer.stop();
                doubleClick(lastEvent);
            } else {
                timer.restart();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            singleClick(lastEvent);
        }

        private void singleClick(MouseEvent e) {
            classes.setSelected(!classes.isSelected());//toggle select

            setCurrentBackgroundColor();
            setBackground(currentBGColor);
            setBorder(overedBorder);
        }

        private void doubleClick(MouseEvent e) {
            parent.showClass(classes, isSmall);
        }
    }
    // </editor-fold>

    //this is listener for delete button for both making view beautiful and
    //perform some action
    private class Delete extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {//accept left mouse only
                int value = JOptionPane.showConfirmDialog(mainFrame,
                        language.getString("deleteClassConfirmContent"),
                        language.getString("deleteConfirm"), JOptionPane.OK_CANCEL_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    if (!mainFrame.getModel().deleteClass(classes)) {
                        JOptionPane.showMessageDialog(mainFrame, language.getString("deleteFailContent"),
                                language.getString("fail"), JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }

        // <editor-fold defaultstate="collapsed" desc="GUI also">
        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(BorderFactory.createLineBorder(defaultBGColor, 2));
        }
        // </editor-fold>
    }

    private class Copy extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {//accept left mouse only
                mainFrame.showClassForm(classes, true, true);
            }
        }

        // <editor-fold defaultstate="collapsed" desc="GUI also">
        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(BorderFactory.createLineBorder(defaultBGColor, 2));
        }
        // </editor-fold>
    }
}
