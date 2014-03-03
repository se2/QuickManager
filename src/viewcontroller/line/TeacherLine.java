package viewcontroller.line;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import kulcomponent.KulImageButton;
import model.Teacher;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.listview.TeacherListView;

/**
 *
 * @author Dam Linh
 *
 * See ClassLine, it has the same Structure
 */
public class TeacherLine extends Box {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private Teacher teacher;
    private Color currentBGColor;
    private Color defaultBGColor = Template.getLineBackground();
    private Color selectedBGColor = Template.getTimeTableSelected();
    private Color borderColor = Template.getLineBorderColor();
    private Font f = Template.getFont().deriveFont(15f);
    private Border clickedBorder;
    private Border overedBorder;
    private JLabel id;
    private JLabel name;
    private JLabel skill;
    private JLabel email;
    private KulImageButton delete;
    private KulImageButton deactive;
    private TeacherListView parent;
    private boolean isSmall;
    private MainFrame mainFrame;

    public TeacherLine(TeacherListView parent, MainFrame mainFrame, Teacher teacher, boolean isSmall) {
        super(BoxLayout.X_AXIS);
        this.parent = parent;
        this.mainFrame = mainFrame;
        this.teacher = teacher;
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

        id = new JLabel(teacher.getId() + "");
        id.setHorizontalAlignment(SwingConstants.CENTER);
        name = new JLabel(teacher.getFullname());
        String skillString = "";
        for (int i = 0; i < teacher.getSkills().length; i++) {
            if (i == 2) {
                skillString += "...";
                break;
            }
            skillString += teacher.getSkills()[i][0];
            if (i + 1 < teacher.getSkills().length && teacher.getSkills()[i + 1] != null) {
                skillString += ", ";
            }
        }
        skill = new JLabel(skillString);
        email = new JLabel(teacher.getEmail());
        initLabel(id, 80, 30);
        initLabel(name, 190, 30);
        initLabel(email, 240, 30);
        skill.setFont(f);

        delete = new KulImageButton("delete", 11, 11);
        delete.addMouseListener(new Delete());
        delete.setToolTipText(language.getString("deleteTeacherHint"));

        deactive = new KulImageButton("cancel", 11, 11);
        deactive.addMouseListener(new Activate());
        if (teacher.isActive()) {
            deactive.setToolTipText(language.getString("deactivateTeacherHint"));
        } else {
            deactive.setToolTipText(language.getString("activateTeacherHint"));
        }

        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setPreferredSize(new Dimension(15, 30));
        buttonBox.setMaximumSize(new Dimension(15, 30));
        buttonBox.add(delete);
        buttonBox.add(Box.createVerticalStrut(4));
        buttonBox.add(deactive);

        add(id);
        add(Box.createHorizontalStrut(8));
        add(name);
        add(Box.createHorizontalStrut(8));
        add(email);
        add(Box.createHorizontalStrut(8));
        add(skill);
        add(Box.createHorizontalGlue());
        add(buttonBox);

        addMouseListener(new TeacherLineActionListener());
        addMouseListener(new TeacherLineViewListener());
    }

    // convenient method
    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setFont(f);
    }

    // convenient method
    private void setCurrentBackgroundColor() {
        if (teacher.isSelected()) {
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
    private class TeacherLineViewListener extends MouseAdapter {

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

    private class TeacherLineActionListener extends MouseAdapter implements ActionListener {

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
            teacher.setSelected(!teacher.isSelected());//toggle select

            setCurrentBackgroundColor();
            setBackground(currentBGColor);
            setBorder(overedBorder);
        }

        private void doubleClick(MouseEvent e) {
            parent.showTeacherForm(teacher, isSmall);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if (!teacher.isActive()) {
            int y = getHeight() / 2;
            g.drawLine(0, y, getWidth(), y);
        }
    }
//</editor-fold>

    //this is listener for delete button for both making view beautiful and
    //perform some action
    private class Delete extends MouseAdapter {

        // <editor-fold defaultstate="collapsed" desc="GUI also">
        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(BorderFactory.createLineBorder(defaultBGColor, 2));
        }
        //</editor-fold>

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {//accept left mouse only
                int value = JOptionPane.showConfirmDialog(mainFrame,
                        language.getString("deleteTeacherConfirmContent"), language.getString("deleteConfirm"), JOptionPane.OK_CANCEL_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteTeacher(teacher)) {
                        JOptionPane.showMessageDialog(mainFrame, language.getString("deleteTeacherSuccess"));
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, language.getString("deleteTeacherFail"),
                                language.getString("deleteTeacherFail"), JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }

    private class Activate extends MouseAdapter {

        // <editor-fold defaultstate="collapsed" desc="GUI also">
        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(BorderFactory.createLineBorder(defaultBGColor, 2));
        }
        //</editor-fold>

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {//accept left mouse only
                mainFrame.getModel().activate(teacher);
            }
        }
    }
}
