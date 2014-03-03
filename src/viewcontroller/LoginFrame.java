package viewcontroller;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import kulcomponent.KulBorder;
import kulcomponent.KulButton;
import model.Constants;
import model.School;
import model.User;
import quickmanage.QuickManage;

/**
 *
 * @author Dam Linh
 */
public class LoginFrame extends JFrame {

    private School model;
    private Box container;
    private Box userPanel;
    private Box passPanel;
    private Box loginPanel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JLabel message;
    private JTextField username;
    private JPasswordField password;
    private KulButton login;
    private KulButton changeLanguage;
    private Font f;
    private ResourceBundle language;

    public LoginFrame(School model) {
        this.model = model;
        this.setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);
        f = new Font("Arial", 0, 16);
        language = model.getLanguage();

        container = new Box(BoxLayout.Y_AXIS);
        container.setOpaque(true);
        container.setBorder(new KulBorder(Template.getLineBorderColor(), 10, 0.3f));
        container.setBackground(Template.getBackground());
        add(container);

        container.add(Box.createVerticalStrut(15));
        drawUsername();
        drawPassword();
        drawButton();
        container.add(Box.createVerticalStrut(5));

        getRootPane().setFocusable(false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "exit");
        getRootPane().getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        addKeyBinding();
    }

    private void drawUsername() {
        userPanel = new Box(BoxLayout.X_AXIS);
        container.add(userPanel);

        userLabel = new JLabel(language.getString("username"));
        userLabel.setFont(f);
        username = new JTextField();
        username.setPreferredSize(new Dimension(190, 25));
        username.setMaximumSize(new Dimension(190, 25));
        username.setFont(f);
        username.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                username.selectAll();
            }
        });

        userPanel.add(Box.createHorizontalStrut(25));
        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalGlue());
        userPanel.add(username);
        userPanel.add(Box.createHorizontalStrut(25));
    }

    private void drawPassword() {
        passPanel = new Box(BoxLayout.X_AXIS);
        container.add(passPanel);

        passLabel = new JLabel(language.getString("password"));
        passLabel.setFont(f);
        password = new JPasswordField();
        password.setPreferredSize(new Dimension(190, 25));
        password.setMaximumSize(new Dimension(190, 25));
        password.setFont(f);
        password.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                password.selectAll();
            }
        });

        passPanel.add(Box.createHorizontalStrut(25));
        passPanel.add(passLabel);

        passPanel.add(Box.createHorizontalGlue());
        passPanel.add(password);

        passPanel.add(Box.createHorizontalStrut(25));
    }

    private void drawButton() {
        loginPanel = new Box(BoxLayout.X_AXIS);
        container.add(loginPanel);

        message = new JLabel();
        message.setForeground(Color.red);
        message.setFont(f.deriveFont(13f));

        f = (Template.getFont());
        login = new KulButton(language.getString("login"));
        login.setFont(f);
        login.setPreferredSize(new Dimension(85, 27));
        login.setMaximumSize(new Dimension(85, 27));
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                validateUsernamePassword();
            }
        });

        if (model.isEnglish()) {
            changeLanguage = new KulButton("VI");
        } else {
            changeLanguage = new KulButton("EN");
        }
        changeLanguage.setFont(f);
        changeLanguage.setPreferredSize(new Dimension(27, 27));
        changeLanguage.setMaximumSize(new Dimension(27, 27));
        changeLanguage.setDefaultBorder(null);
        changeLanguage.setMouseOveredBorder(null);
        changeLanguage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeLanguage();
            }
        });

        loginPanel.add(Box.createHorizontalStrut(25));
        loginPanel.add(message);
        loginPanel.add(Box.createHorizontalGlue());
        loginPanel.add(changeLanguage);
        loginPanel.add(Box.createHorizontalStrut(5));
        loginPanel.add(login);
        loginPanel.add(Box.createHorizontalStrut(25));
    }

    private void addKeyBinding() {
        password.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
        password.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateUsernamePassword();
            }
        });

        username.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
        username.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password.requestFocus();
            }
        });

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "help");
        this.getRootPane().getActionMap().put("help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserGuide();
            }
        });
    }

    private void showUserGuide() {
        if (Desktop.isDesktopSupported()) {
            try {
                URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
                String userguideLocation = new File(url.toURI()).getParent() + Constants.USERGUIDE_ENGLISH_FILE_LOCATION;
                Desktop.getDesktop().open(new File(userguideLocation));
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }

    private void validateUsernamePassword() {
        Object returnObj = model.checkIDPass(username.getText(), password.getPassword());
        if (returnObj instanceof User) {
            User user = (User) returnObj;
            QuickManage.showDashboard(user);
        } else if (returnObj == null) {
            message.setText(language.getString("wrongPassMess"));
            password.setText("");
        } else if (returnObj instanceof String) {
            message.setText(language.getString("deactivatedAccount"));
            password.setText("");
        }
    }

    /**
     * This method should be called in the method logout() of Main class only.
     * This method is called when user logs-out. This method is to reset itself
     * so that it will look as if it was just created.
     */
    public void reset() {
        password.setText("");
        message.setText("");
        username.setText("");
        username.requestFocus();
    }

    private void changeLanguage() {
        model.setLanguage(!model.isEnglish());
        if (model.isEnglish()) {
            changeLanguage.setTextDisplay("VI");
        } else {
            changeLanguage.setTextDisplay("EN");
        }
        language = model.getLanguage();

        userLabel.setText(language.getString("username"));
        passLabel.setText(language.getString("password"));
        login.setTextDisplay(language.getString("login"));
        login.repaint();
    }
}
