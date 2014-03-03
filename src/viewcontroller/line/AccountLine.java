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
import model.Manager;
import model.User;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 *
 * See ClassLine, it has the same structure
 */
public class AccountLine extends Box {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private User user;
    private Color currentBGColor;
    private Color defaultBGColor = Template.getLineBackground();
    private Color selectedBGColor = Template.getTimeTableSelected();
    private Color borderColor = Template.getLineBorderColor();
    private Font f = Template.getFont().deriveFont(15f);
    private Border clickedBorder;
    private Border overedBorder;
    private JLabel id;
    private JLabel name;
    private JLabel accType;
    private JLabel email;
    private KulImageButton delete;
    private KulImageButton deactive;
    private MainFrame mainFrame;

    public AccountLine(MainFrame mainFrame, User user) {
        super(BoxLayout.X_AXIS);
        this.mainFrame = mainFrame;
        this.user = user;
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

        id = new JLabel(user.getId() + "");
        id.setHorizontalAlignment(SwingConstants.CENTER);
        name = new JLabel(user.getFullname());
        if (user instanceof Manager) {
            accType = new JLabel(language.getString("manager"));
        } else {
            accType = new JLabel(language.getString("staff"));
        }
        email = new JLabel(user.getEmail());
        initLabel(id, 80, 30);
        initLabel(name, 190, 30);
        initLabel(accType, 120, 30);
        email.setFont(f);

        delete = new KulImageButton("delete", 11, 11);
        delete.addMouseListener(new Delete());
        delete.setToolTipText(language.getString("delete"));

        deactive = new KulImageButton("cancel", 11, 11);
        deactive.addMouseListener(new Deactivate());
        if (user.isActive()) {
            deactive.setToolTipText(language.getString("deactiveThisAccountHint"));
        } else {
            deactive.setToolTipText(language.getString("activateThisAccountHint"));
        }

        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setPreferredSize(new Dimension(15, 30));
        buttonBox.setMaximumSize(new Dimension(15, 30));
        if (mainFrame.getUser() != user) {
            buttonBox.add(delete);
            buttonBox.add(Box.createVerticalStrut(4));
        }
        buttonBox.add(deactive);

        add(id);
        add(Box.createHorizontalStrut(8));
        add(name);
        add(Box.createHorizontalStrut(8));
        add(accType);
        add(Box.createHorizontalStrut(8));
        add(email);
        add(Box.createHorizontalGlue());
        add(buttonBox);

        addMouseListener(new UserLineActionListener());
        addMouseListener(new UserLineViewListener());
    }

    // convenient method
    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setFont(f);
    }

    private void setCurrentBackgroundColor() {
        if (user.isSelected()) {
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
    private class UserLineViewListener extends MouseAdapter {

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
    private class UserLineActionListener extends MouseAdapter implements ActionListener {

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
            user.setSelected(!user.isSelected());//toggle select

            setCurrentBackgroundColor();
            setBackground(currentBGColor);
            setBorder(overedBorder);
        }

        private void doubleClick(MouseEvent e) {
            mainFrame.showAccountForm(user, false);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if (!user.isActive()) {
            int y = getHeight() / 2;
            g.drawLine(0, y, getWidth(), y);
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
                    if (mainFrame.getModel().deleteUser(user.getId())) {
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

    private class Deactivate extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(new LineBorder(currentBGColor, 2));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                mainFrame.getModel().activate(user);
            }
        }
    }
}
