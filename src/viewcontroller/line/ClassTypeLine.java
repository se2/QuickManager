package viewcontroller.line;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import model.ClassType;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 * @author Dam Linh
 */
public class ClassTypeLine extends Box {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private ClassType ct;
    private Color currentBGColor;
    private Color defaultBGColor = Template.getLineBackground();
    private Color selectedBGColor = Template.getTimeTableSelected();
    private Color borderColor = Template.getLineBorderColor();
    private Font f = Template.getFont().deriveFont(15f);
    private Border clickedBorder;
    private Border overedBorder;
    private JLabel skill;
    private JLabel type;
    private JLabel lessPerWeek;
    private JLabel note;
    private KulImageButton delete;
    private MainFrame mainFrame;

    public ClassTypeLine(MainFrame mainFrame, ClassType ct) {
        super(BoxLayout.X_AXIS);
        this.mainFrame = mainFrame;
        this.ct = ct;
        setOpaque(true);
        setPreferredSize(new Dimension(600, 30));
        setMaximumSize(new Dimension(1024, 30));
        language = mainFrame.getModel().getLanguage();

        setCurrentBackgroundColor();
        setBackground(currentBGColor);
        setBorder(BorderFactory.createLineBorder(currentBGColor, 2));

        clickedBorder = new CompoundBorder(
                new MatteBorder(1, 0, 0, 1, currentBGColor),
                new MatteBorder(1, 2, 1, 1, borderColor));
        overedBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, borderColor),
                new MatteBorder(1, 2, 1, 0, currentBGColor));

        skill = new JLabel(ct.getSkill() + "");
        skill.setHorizontalAlignment(SwingConstants.CENTER);
        type = new JLabel(ct.getTypeString(language));
        type.setHorizontalAlignment(SwingConstants.CENTER);
        lessPerWeek = new JLabel(ct.getLessonPerWeekString(language));
        lessPerWeek.setHorizontalAlignment(SwingConstants.CENTER);
        note = new JLabel(ct.getNote());
        initLabel(skill, 150, 30);
        initLabel(type, 120, 30);
        initLabel(lessPerWeek, 120, 30);
        note.setFont(f);

        delete = new KulImageButton("delete", 15, 15);
        delete.addMouseListener(new Delete());
        delete.setToolTipText(language.getString("delete"));
  
        add(skill);
        add(Box.createHorizontalStrut(8));
        add(type);
        add(Box.createHorizontalStrut(8));
        add(lessPerWeek);
        add(Box.createHorizontalStrut(8));
        add(note);
        add(Box.createHorizontalGlue());
        add(delete);

        addMouseListener(new ClassTypeLineActionListener());
        addMouseListener(new ClassTypeLineViewListener());
    }

    // convenient method
    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setFont(f);
    }

    private void setCurrentBackgroundColor() {
        if (ct.isSelected()) {
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
    private class ClassTypeLineViewListener extends MouseAdapter {

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

    //this is listener for delete button for both making view beautiful and
    //perform some action
    private class ClassTypeLineActionListener extends MouseAdapter implements ActionListener {

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
            ct.setSelected(!ct.isSelected());//toggle select

            setCurrentBackgroundColor();
            setBackground(currentBGColor);
            setBorder(overedBorder);
        }

        private void doubleClick(MouseEvent e) {
            mainFrame.showClassTypeForm(ct);
        }
    }
    // </editor-fold>

    private class Delete extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                int value = JOptionPane.showConfirmDialog(mainFrame,
                        language.getString("deleteConfirmContent"), language.getString("deleteConfirm"), JOptionPane.OK_CANCEL_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteClassType(ct)) {
                        JOptionPane.showMessageDialog(mainFrame, language.getString("deleteAccountSuccessContent"));
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(new LineBorder(currentBGColor, 2));
        }
    }
}
