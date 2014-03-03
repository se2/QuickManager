package viewcontroller.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import kulcomponent.KulDayChooser;
import kulcomponent.KulFrame;
import kulcomponent.KulImageChooser;
import kulcomponent.KulPhoneChooser;
import kulcomponent.KulTextArea;
import kulcomponent.KulTextField;
import model.Constants;
import model.Manager;
import model.Staff;
import model.User;
import model.Validator;
import org.joda.time.LocalDate;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class AccountForm extends JPanel {

    /*
     * See ClassForm comments (it has the same structure)
     */
    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private User user;
    private JLabel title;
    private KulTextField id;
    private KulTextField name;
    private KulTextField mail;
    private KulTextArea address;
    private KulPhoneChooser phone;
    private JPasswordField oldPass;
    private JPasswordField pass;
    private JPasswordField passConfirm;
    private KulDayChooser date;
    private KulComboBox<String> isMale;
    private JTextField idTitle;
    private JTextField nameTitle;
    private JTextField phoneTitle;
    private JTextField mailTitle;
    private JTextField addressTitle;
    private JTextField dobTitle;
    private JTextField isMaleTitle;
    private JTextField passTitle;
    private JTextField oldPassTitle;
    private JTextField passConfirmTitle;
    private JLabel nameMess;
    private JLabel phoneMess;
    private JLabel idMess = new JLabel();
    private JLabel oldPassMess;
    private JLabel mailMess;
    private JLabel dobMess = new JLabel();
    private JLabel passMess;
    private JLabel passConfirmMess;
    private JLabel photo;
    private KulButton save;
    private KulButton resetPass;
    private KulButton cancel;
    private KulButton activate;
    private SpringLayout spring;
    private Box buttons;
    private JPanel left;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private boolean isManager;
    private Font f = Template.getFont().deriveFont(16f);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    public AccountForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout(10, 0));
        language = mainFrame.getModel().getLanguage();

        spring = new SpringLayout();
        left = new JPanel(spring);
        left.setBackground(Template.getBackground());
        add(new JLabel(), BorderLayout.WEST);//padding purpose
        add(left);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!address.hasFocus()) {
                    ok();
                }
            }
        });

        init();
        drawButtons();
    }

    private void init() {
        idTitle = new JTextField(getString("username"));
        nameTitle = new JTextField(getString("fullName"));
        phoneTitle = new JTextField(getString("phoneNumber"));
        mailTitle = new JTextField(getString("email"));
        addressTitle = new JTextField(getString("address"));
        dobTitle = new JTextField(getString("birthday"));
        isMaleTitle = new JTextField(getString("gender"));
        oldPassTitle = new JTextField(getString("oldPassword"));
        passTitle = new JTextField(getString("password"));
        passConfirmTitle = new JTextField(getString("passwordConfirm"));
        title = new JLabel(getString("manager"));
        phoneMess = new JLabel();
        nameMess = new JLabel();
        mailMess = new JLabel();
        passMess = new JLabel();
        oldPassMess = new JLabel();
        passConfirmMess = new JLabel();
        photo = new JLabel();

        id = new KulTextField(30);
        name = new KulTextField(30);
        phone = new KulPhoneChooser();
        mail = new KulTextField(38);
        address = new KulTextArea(3, 38);
        oldPass = new JPasswordField(38);
        pass = new JPasswordField(38);
        passConfirm = new JPasswordField(38);
        isMale = new KulComboBox<>(new String[]{getString("male"), getString("female")});
        date = new KulDayChooser(Template.getFont().deriveFont(16f), 1900, language);

        title.setFont(f.deriveFont(50f));
        id.setFont(f);
        name.setFont(f);
        phone.setFont(f);
        mail.setFont(f);
        address.setFont(f);
        isMale.setFont(f);
        oldPass.setFont(f);
        pass.setFont(f);
        passConfirm.setFont(f);
        oldPassMess.setFont(Template.getFont());
        phoneMess.setFont(Template.getFont());
        nameMess.setFont(Template.getFont());
        idMess.setFont(Template.getFont());
        mailMess.setFont(Template.getFont());
        passMess.setFont(Template.getFont());
        passConfirmMess.setFont(Template.getFont());
        dobMess.setFont(Template.getFont());

        phoneMess.setFocusable(false);
        mailMess.setFocusable(false);
        idMess.setFocusable(false);
        nameMess.setFocusable(false);
        passMess.setFocusable(false);
        dobMess.setFocusable(false);
        title.setFocusable(false);
        oldPassMess.setFocusable(false);
        passConfirmMess.setFocusable(false);

        phoneMess.setForeground(Color.red);
        mailMess.setForeground(Color.red);
        idMess.setForeground(Color.red);
        dobMess.setForeground(Color.red);
        oldPassMess.setForeground(Color.red);
        passMess.setForeground(Color.red);
        passConfirmMess.setForeground(Color.red);
        nameMess.setForeground(Color.red);

        title.setForeground(Template.getForeground());
        nameTitle.setForeground(Template.getForeground());
        idTitle.setForeground(Template.getForeground());
        phoneTitle.setForeground(Template.getForeground());
        mailTitle.setForeground(Template.getForeground());
        addressTitle.setForeground(Template.getForeground());
        dobTitle.setForeground(Template.getForeground());
        isMaleTitle.setForeground(Template.getForeground());
        oldPassTitle.setForeground(Template.getForeground());
        passTitle.setForeground(Template.getForeground());
        passConfirmTitle.setForeground(Template.getForeground());

        setComponent(idTitle, 20, 30, 16);
        setComponent(nameTitle, 20, 100, 16);
        setComponent(phoneTitle, 20, 30, 16);
        setComponent(mailTitle, 20, 30, 16);
        setComponent(addressTitle, 20, 30, 16);
        setComponent(dobTitle, 20, 30, 16);
        setComponent(oldPassTitle, 20, 30, 16);
        setComponent(isMaleTitle, 20, 30, 16);
        setComponent(passTitle, 20, 30, 16);
        setComponent(passConfirmTitle, 20, 30, 16);

        left.add(id);
        left.add(idMess);
        left.add(title);
        left.add(name);
        left.add(nameMess);
        left.add(phone);
        left.add(mail);
        left.add(address);
        left.add(isMale);
        left.add(idTitle);
        left.add(nameTitle);
        left.add(phoneTitle);
        left.add(mailTitle);
        left.add(addressTitle);
        left.add(dobTitle);
        left.add(dobMess);
        left.add(isMaleTitle);
        left.add(photo);
        left.add(date);
        left.add(phoneMess);
        left.add(mailMess);
        left.add(oldPass);
        left.add(oldPassTitle);
        left.add(oldPassMess);
        left.add(pass);
        left.add(passConfirm);
        left.add(passTitle);
        left.add(passConfirmTitle);
        left.add(passMess);
        left.add(passConfirmMess);
        setSpring();

        photo.setHorizontalTextPosition(SwingConstants.CENTER);
        photo.setVerticalTextPosition(SwingConstants.CENTER);
        photo.setFont(f);
        photo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                photo.setText(getString("change"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                photo.setText("");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                KulImageChooser imgChooser = new KulImageChooser();
                int returnValue = imgChooser.showDialog(parentFrame, getString("select"));
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String path = imgChooser.getSelectedFile().getPath();
                    Image img = new ImageIcon(path).getImage();
                    Image newImg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
                    photo.setIcon(new ImageIcon(newImg));
                }
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        save = new KulButton(getString("save"));
        resetPass = new KulButton(getString("resetPassword"));
        cancel = new KulButton(getString("cancel"));
        activate = new KulButton(getString("activate"));
        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 60));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        add(buttons, BorderLayout.SOUTH);
        setComponent(save, 120, 30, 16);
        setComponent(resetPass, 150, 30, 16);
        setComponent(cancel, 120, 30, 16);
        setComponent(activate, 120, 30, 16);

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.closeWindow(true);
            }
        });

        resetPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame.getModel().resetPassword(user)) {
                    JOptionPane.showMessageDialog(parentFrame, getString("resetPasswordContent"));
                }
            }
        });

        activate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().activate(user);
                if (user.isActive()) {
                    activate.setTextDisplay(getString("deactivate"));
                    activate.setToolTipText(getString("deactiveThisAccountHint"));
                } else {
                    activate.setTextDisplay(getString("activate"));
                    activate.setToolTipText(getString("activeThisAccountHint"));
                }
            }
        });

        save.addMouseListener(new SaveListener());

        buttons.add(Box.createHorizontalGlue());
        buttons.add(activate);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(resetPass);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(save);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(cancel);
        buttons.add(Box.createHorizontalStrut(44));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setSpring">
    private void setSpring() {
        spring.putConstraint(SpringLayout.NORTH, photo, 10, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.EAST, photo, -45, SpringLayout.EAST, left);

        spring.putConstraint(SpringLayout.NORTH, title, 10, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.WEST, title, 160, SpringLayout.WEST, left);

        spring.putConstraint(SpringLayout.NORTH, nameTitle, 70, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, name, 90, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, idTitle, 120, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, id, 140, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, dobTitle, 170, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, date, 190, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, isMaleTitle, 220, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, isMale, 240, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, phoneTitle, 270, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, phone, 290, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, mailTitle, 320, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, mail, 340, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, addressTitle, 370, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, address, 390, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, oldPassTitle, 460, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, oldPass, 480, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, passTitle, 510, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, pass, 530, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, passConfirmTitle, 560, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, passConfirm, 580, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, nameMess, 70, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, idMess, 120, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, dobMess, 170, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, phoneMess, 270, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, mailMess, 320, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, oldPassMess, 460, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, passMess, 510, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, passConfirmMess, 560, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.EAST, oldPassMess, -41, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, nameMess, -144, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, idMess, -144, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, phoneMess, -41, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, dobMess, -130, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, mailMess, -41, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, passMess, - 41, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, passConfirmMess, -41, SpringLayout.EAST, left);

        spring.putConstraint(SpringLayout.WEST, name, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, id, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, phone, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, mail, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, address, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, date, 50, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, isMale, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, oldPass, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, passConfirm, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, pass, 40, SpringLayout.WEST, left);

        spring.putConstraint(SpringLayout.WEST, nameTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, oldPassTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, idTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, phoneTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, mailTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, addressTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, dobTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, isMaleTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, passTitle, 20, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, passConfirmTitle, 20, SpringLayout.WEST, left);
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

    // <editor-fold defaultstate="collapsed" desc="setUser">
    public void setUser(User user, KulFrame parentFrame, boolean isManager) {
        this.user = user;
        this.parentFrame = parentFrame;
        this.isManager = isManager;

        name.requestFocus();

        if (user == null) {
            if (isManager) {
                title.setText(getString("manager"));
            } else {
                title.setText(getString("staff"));
            }
        } else {
            if (user instanceof Manager) {
                title.setText(getString("manager"));
            } else {
                title.setText(getString("staff"));
            }
        }

        pass.setVisible(false);
        passTitle.setVisible(false);
        passMess.setVisible(false);
        passConfirm.setVisible(false);
        passConfirmTitle.setVisible(false);
        passConfirmMess.setVisible(false);
        oldPass.setVisible(false);
        oldPassTitle.setVisible(false);
        oldPassMess.setVisible(false);
        resetPass.setVisible(true);

        mailMess.setText("");
        phoneMess.setText("");
        passMess.setText("");
        dobMess.setText("");
        passConfirmMess.setText("");

        if (user == mainFrame.getUser()) {
            resetPass.setVisible(false);
            pass.setVisible(true);
            passTitle.setVisible(true);
            passMess.setVisible(true);
            passConfirm.setVisible(true);
            passConfirmTitle.setVisible(true);
            passConfirmMess.setVisible(true);
            oldPass.setVisible(true);
            oldPassTitle.setVisible(true);
            oldPassMess.setVisible(true);
        }

        if (user != null) {
            if (user.isBeingUsed()) {
                JOptionPane.showMessageDialog(parentFrame,
                        getString("accountBeingUsedContent"), getString("caution"), JOptionPane.WARNING_MESSAGE);
                parentFrame.closeWindow(false);
                return;
            }
            if (user != mainFrame.getUser()) {
                resetPass.setVisible(true);
            }
            user.setBeingUsed(true);

            id.setText("" + user.getId());
            name.setText(user.getFullname());
            phone.setPhone(user.getPhoneNumber());
            mail.setText(user.getEmail());
            address.setText(user.getAddress());
            date.setDate(user.getDOB());
            pass.setText("");
            passConfirm.setText("");
            if (user.isIsMale()) {
                isMale.setSelectedIndex(0);
            } else {
                isMale.setSelectedIndex(1);
            }
            activate.setVisible(true);
            if (user.isActive()) {
                activate.setTextDisplay(getString("deactivate"));
                activate.setToolTipText(getString("deactiveThisAccountHint"));
            } else {
                activate.setTextDisplay(getString("activate"));
                activate.setToolTipText(getString("activeThisAccountHint"));
            }
            save.setTextDisplay("Ok");

            Image img = user.getPhoto().getImage();
            Image newImg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(newImg));
        } else {
            resetPass.setVisible(false);
            id.setText("");
            name.setText("");
            phone.setPhone("");
            mail.setText("");
            address.setText("");
            date.setDate(new LocalDate());
            save.setTextDisplay(getString("add"));
            activate.setVisible(false);

            id.setEditable(true);
            name.setEditable(true);
            mail.setEditable(true);
            address.setEditable(true);

            Image img = new ImageIcon(Template.getPhotoURLDefault()).getImage();
            Image newImg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(newImg));
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="validate and ok">
    private boolean validateFields() {
        boolean isValid = true;
        String emailString = mail.getText();
        String username = id.getText();
        char[] oldPassword = oldPass.getPassword();
        char[] password = pass.getPassword();
        char[] passwordConfirm = passConfirm.getPassword();

        //reset warning message
        oldPass.setText("");
        phoneMess.setText("");
        mailMess.setText("");
        idMess.setText("");
        dobMess.setText("");
        passMess.setText("");
        passConfirmMess.setText("");
        nameMess.setText("");
        oldPassMess.setText("");

        if (user == mainFrame.getUser()) {
            if (mainFrame.getModel().checkIDPass(mainFrame.getUser().getId(), oldPassword) == null) {
                oldPassMess.setText(getString("wrongPassword"));
                return false;
            }
        }
        if ((user == null) || (user != null && !username.equals(user.getId()))) {
            if (!Validator.usernameValidate(username)) {
                idMess.setText(getString("idMessFormat"));
                isValid = false;
            } else {
                if (!mainFrame.getModel().validateUsername(username)) {
                    idMess.setText(getString("idMessDuplicate"));
                    isValid = false;
                }
            }
        }

        if (!phone.isIsValid()) {
            phoneMess.setText(getString("phoneNumberMess"));
            isValid = false;
        }

        if (name.getText().equals("")) {
            nameMess.setText(getString("fullNameEmptyMess"));
            isValid = false;
        }

        if (name.getText().split("\\s").length < 2) {
            nameMess.setText(getString("fullName2WordMess"));
            isValid = false;
        }

        LocalDate bir = date.getDate();
        if (bir.compareTo(new LocalDate()) >= 0) {
            dobMess.setText(getString("birthdayMess"));
            isValid = false;
        }

        if (!Validator.emailValidate(emailString)) {
            mailMess.setText(getString("emailMess"));
            isValid = false;
        }

        if (user == mainFrame.getUser()) {
            if (!Validator.passwordValidate(new String(password))) {
                passMess.setText("");
                isValid = false;
            }
            if (password.length != passwordConfirm.length) { // check if passwords match
                isValid = false;
                passConfirmMess.setText(getString("passwordNotMatch"));
            } else {
                for (int i = 0; i < password.length; i++) {
                    if (password[i] != passwordConfirm[i]) {
                        passConfirmMess.setText(getString("passwordNotMatch"));
                        isValid = false;
                        break;
                    }
                }
            }
        }
        return isValid;
    }

    private void ok() {
        if (!validateFields()) {
            return;
        }
        String username = id.getText().toLowerCase();
        String phoneString = phone.getPhone();
        String emailString = mail.getText();
        char[] password = pass.getPassword();
        boolean isMale1 = isMale.getSelectedIndex() == 0;
        String addressString = address.getText();
        String nameString = name.getText();
        LocalDate birthDate = date.getDate();
        ImageIcon avatar = (ImageIcon) photo.getIcon();

        String[] fullname = nameString.split("\\s+");
        String firstname = fullname[0];
        String lastname = fullname[fullname.length - 1];
        String middlename = "";
        for (int i = 1; i < fullname.length - 1; i++) {
            middlename += fullname[i];
        }

        if (user == mainFrame.getUser()) {
            user.setPassword(new String(password));
        }

        if (user != null) {
            user.setId(username);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setMiddlename(middlename);
            user.setPhoneNumber(phoneString);
            user.setAddress(addressString);
            user.setDOB(birthDate);
            user.setEmail(emailString);
            user.setPhoto(avatar);
            if (user == mainFrame.getUser()) { // if password needs changing
                user.setPassword(new String(password));
            }
            user.setIsMale(isMale1);

            mainFrame.getModel().saveData();
            mainFrame.getMenuView().update("User");
            JOptionPane.showMessageDialog(parentFrame, getString("updateConfirmContent"), getString("success"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (isManager) {
                Manager m = new Manager(username, Constants.DEFAULT_PASSWORD, lastname,
                        middlename, firstname, emailString, phoneString,
                        birthDate, addressString, avatar, isMale1);
                mainFrame.getModel().addUser(m);
            } else {
                Staff s = new Staff(username, Constants.DEFAULT_PASSWORD, lastname,
                        middlename, firstname, emailString, phoneString,
                        birthDate, addressString, avatar, isMale1);
                mainFrame.getModel().addUser(s);
            }
            JOptionPane.showMessageDialog(parentFrame, getString("addConfirmContent"),
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

    private String getString(String key) {
        return language.getString(key);
    }
}
