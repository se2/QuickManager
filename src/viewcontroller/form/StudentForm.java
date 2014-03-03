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
import java.util.Iterator;
import java.util.Map;
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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import kulcomponent.KulAnimator;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import kulcomponent.KulDayChooser;
import kulcomponent.KulFrame;
import kulcomponent.KulImageChooser;
import kulcomponent.KulPhoneChooser;
import kulcomponent.KulShinyText;
import kulcomponent.KulTextArea;
import kulcomponent.KulTextField;
import model.ClassType;
import model.Invoice;
import model.Student;
import model.StudentClass;
import model.Validator;
import org.joda.time.LocalDate;
import viewcontroller.HasReturn;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TimeTable;

/**
 *
 * @author Dam Linh
 */
public class StudentForm extends JPanel implements HasReturn {

    /*
     * See ClassForm comments (it has the same structure)
     */
    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private Student student;
    private JLabel id;
    private KulTextField name;
    private KulPhoneChooser phone;
    private KulTextField mail;
    private KulTextArea address;
    private KulDayChooser date;
    private KulComboBox<String> isMale;
    private JLabel title;
    private JLabel idTitle;
    private JTextField nameTitle;
    private JTextField phoneTitle;
    private JTextField mailTitle;
    private JTextField addressTitle;
    private JTextField dobTitle;
    private JTextField isMaleTitle;
    private JLabel nameMess;
    private JLabel mailMess;
    private JLabel guardNameMess;
    private JLabel guardMailMess;
    private JLabel phoneMess = new JLabel();
    private JLabel guardPhoneMess = new JLabel();
    private JLabel dobMess = new JLabel();
    private JLabel photo;
    private KulButton save;
    private KulButton generate;
    private KulButton activate;
    private SpringLayout spring;
    private Box buttons;
    private boolean editable = true;
    private JPanel left;
    private JPanel subLeft;
    private JPanel right;
    private JPanel subRight = new JPanel(null);
    private JPanel classListPanel = new JPanel(new BorderLayout());
    private JLabel classTitle;
    private boolean isTimeTableShowing = false;
    private TimeTable timeTable;
    private JLabel guardTitle;
    private JTextField guardPhoneTitle;
    private KulPhoneChooser guardPhone;
    private KulTextField guardName;
    private JTextField guardNameTitle;
    private JTextField guardAddressTitle;
    private KulTextArea guardAddress;
    private JTextField guardMailTitle;
    private KulTextField guardMail;
    private JTextField guardRelationTitle;
    private KulTextField guardRelation;
    private KulButton switchView;
    private final int RIGHT_PANEL_W = 520;
    private final int SUB_RIGHT_PANEL_H = 650;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private Box classBox;
    private Font f = Template.getFont().deriveFont(14f);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    public StudentForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout());
        language = mainFrame.getModel().getLanguage();

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

        timeTable = new TimeTable(mainFrame, true);
        classBox = new Box(BoxLayout.Y_AXIS);
        right.add(subRight);
        subRight.add(timeTable);
        subRight.add(classListPanel);
        add(right, BorderLayout.EAST);

        classTitle = new JLabel(getString("classes1"));
        Box temp = new Box(BoxLayout.X_AXIS);
        temp.add(Box.createHorizontalStrut(160));
        temp.add(classTitle);

        classListPanel.setBackground(Template.getBackground());
        classListPanel.add(temp, BorderLayout.NORTH);
        classListPanel.add(classBox);

        classListPanel.setBounds(0, 0, RIGHT_PANEL_W, SUB_RIGHT_PANEL_H);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!address.hasFocus() && !guardAddress.hasFocus()) {
                    ok();
                }
            }
        });
        //</editor-fold>

        init();
        drawButtons();
    }

    private void init() {
        title = new JLabel(getString("student"));
        idTitle = new JLabel(getString("id"));
        guardTitle = new JLabel(getString("guardian"));
        guardPhoneTitle = new JTextField(getString("guardianPhone"));
        guardNameTitle = new JTextField(getString("guardianName"));
        guardAddressTitle = new JTextField(getString("guardianAddress"));
        guardMailTitle = new JTextField(getString("guardianEmail"));
        guardRelationTitle = new JTextField(getString("relationship"));
        nameTitle = new JTextField(getString("fullName"));
        phoneTitle = new JTextField(getString("phoneNumber"));
        mailTitle = new JTextField(getString("email"));
        addressTitle = new JTextField(getString("address"));
        dobTitle = new JTextField(getString("birthday"));
        isMaleTitle = new JTextField(getString("gender"));
        classTitle.setToolTipText("Current enrolled clasess of this student");

        nameMess = new JLabel();
        mailMess = new JLabel();
        guardNameMess = new JLabel();
        guardMailMess = new JLabel();
        photo = new JLabel();
        id = new JLabel();

        name = new KulTextField(33);
        phone = new KulPhoneChooser(f);
        mail = new KulTextField(33);
        address = new KulTextArea(3, 33);
        isMale = new KulComboBox<>(new String[]{getString("male"), getString("female")});
        guardMail = new KulTextField(33);
        guardName = new KulTextField(33);
        guardPhone = new KulPhoneChooser(f);
        guardAddress = new KulTextArea(3, 33);
        guardRelation = new KulTextField(33);
        date = new KulDayChooser(f, 1900, language);

        id.setFont(f);
        idTitle.setFont(f);
        name.setFont(f);
        nameTitle.setFont(f);
        phone.setFont(f);
        mail.setFont(f);
        address.setFont(f);
        isMale.setFont(f);

        guardMail.setFont(f);
        guardName.setFont(f);
        guardRelation.setFont(f);
        guardRelationTitle.setFont(f);
        guardAddress.setFont(f);

        title.setFont(f.deriveFont(50f));
        guardTitle.setFont(f.deriveFont(26f));
        classTitle.setFont(f.deriveFont(50f));
        nameMess.setFont(Template.getFont());
        mailMess.setFont(Template.getFont());
        guardNameMess.setFont(Template.getFont());
        guardMailMess.setFont(Template.getFont());
        phoneMess.setFont(Template.getFont());
        guardPhoneMess.setFont(Template.getFont());
        dobMess.setFont(Template.getFont());

        guardPhoneMess.setFocusable(false);
        mailMess.setFocusable(false);
        nameMess.setFocusable(false);
        dobMess.setFocusable(false);
        guardMailMess.setFocusable(false);
        guardNameMess.setFocusable(false);
        dobTitle.setFocusable(false);
        title.setFocusable(false);
        guardTitle.setFocusable(false);
        guardRelationTitle.setFocusable(false);
        idTitle.setFocusable(false);
        id.setFocusable(false);
        phoneMess.setFocusable(false);
        dobMess.setFocusable(false);
        classTitle.setFocusable(false);

        mailMess.setForeground(Color.red);
        phoneMess.setForeground(Color.red);
        guardPhoneMess.setForeground(Color.red);
        nameMess.setForeground(Color.red);
        guardMailMess.setForeground(Color.red);
        guardNameMess.setForeground(Color.red);
        phoneMess.setForeground(Color.red);
        dobMess.setForeground(Color.red);
        title.setForeground(Template.getForeground());
        nameTitle.setForeground(Template.getForeground());
        id.setForeground(Template.getForeground());
        idTitle.setForeground(Template.getForeground());
        phoneTitle.setForeground(Template.getForeground());
        mailTitle.setForeground(Template.getForeground());
        addressTitle.setForeground(Template.getForeground());
        dobTitle.setForeground(Template.getForeground());
        isMaleTitle.setForeground(Template.getForeground());
        guardTitle.setForeground(Template.getForeground());
        guardNameTitle.setForeground(Template.getForeground());
        guardRelationTitle.setForeground(Template.getForeground());
        guardMailTitle.setForeground(Template.getForeground());
        guardPhoneTitle.setForeground(Template.getForeground());
        guardAddressTitle.setForeground(Template.getForeground());
        classTitle.setForeground(Template.getForeground());

        setComponent(nameTitle, 20, 30, 14);
        setComponent(guardRelationTitle, 20, 30, 14);
        setComponent(phoneTitle, 20, 30, 14);
        setComponent(mailTitle, 20, 30, 14);
        setComponent(addressTitle, 20, 30, 14);
        setComponent(guardNameTitle, 20, 30, 14);
        setComponent(guardPhoneTitle, 20, 30, 14);
        setComponent(guardMailTitle, 20, 30, 14);
        setComponent(guardAddressTitle, 20, 30, 14);
        setComponent(dobTitle, 20, 30, 14);
        setComponent(isMaleTitle, 20, 30, 14);

        subLeft.add(title);
        subLeft.add(id);
        subLeft.add(name);
        subLeft.add(nameMess);
        subLeft.add(phone);
        subLeft.add(mail);
        subLeft.add(address);
        subLeft.add(isMale);
        subLeft.add(idTitle);
        subLeft.add(nameTitle);
        subLeft.add(phoneTitle);
        subLeft.add(mailTitle);
        subLeft.add(addressTitle);
        subLeft.add(dobTitle);
        subLeft.add(dobMess);
        subLeft.add(isMaleTitle);
        subLeft.add(photo);
        subLeft.add(phoneMess);
        subLeft.add(date);
        subLeft.add(mailMess);

        subLeft.add(guardNameTitle);
        subLeft.add(guardName);
        subLeft.add(guardNameMess);
        subLeft.add(guardPhone);
        subLeft.add(guardPhoneTitle);
        subLeft.add(guardMail);
        subLeft.add(guardMailMess);
        subLeft.add(guardMailTitle);
        subLeft.add(guardPhoneMess);
        subLeft.add(guardAddress);
        subLeft.add(guardAddressTitle);
        subLeft.add(guardRelation);
        subLeft.add(guardRelationTitle);
        subLeft.add(guardTitle);

        setSpring();

        // <editor-fold defaultstate="collapsed" desc="draw photo">
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
                    Image newImg = img.getScaledInstance(75, 100, Image.SCALE_SMOOTH);
                    photo.setIcon(new ImageIcon(newImg, path));
                }
            }
        });
        //</editor-fold>
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        switchView = new KulButton(getString("timetable"));
        save = new KulButton(getString("save"));
        generate = new KulButton(getString("getInvoice"));
        activate = new KulButton(getString("activate"));
        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 40));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        left.add(buttons, BorderLayout.SOUTH);

        setComponent(save, 120, 30, 16);
        setComponent(generate, 120, 30, 16);
        setComponent(activate, 120, 30, 16);
        setComponent(switchView, 100, 30, 16);

        //<editor-fold defaultstate="collapsed" desc="left buttons">
        generate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Invoice invoice = mainFrame.getModel().generateInvoice(student);
                if (invoice != null) {
                    int result = JOptionPane.showConfirmDialog(parentFrame,
                            getString("getInvoiceSuccessContent"), getString("success"),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        parentFrame.closeWindow(true);
                        mainFrame.showInvoiceForm(invoice);
                    }
                } else {
                    JOptionPane.showMessageDialog(parentFrame, getString("getInvoiceFailContent"),
                            getString("fail"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        activate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().activate(student);
                if (student.isActive()) {
                    activate.setTextDisplay(getString("deactivate"));
                } else {
                    activate.setTextDisplay(getString("activate"));
                }
            }
        });
        save.addMouseListener(new SaveListener());

        buttons.add(Box.createHorizontalGlue());
        buttons.add(activate);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(save);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(generate);
        buttons.add(Box.createHorizontalStrut(20));
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="right buttons">
        Box temp = new Box(BoxLayout.X_AXIS);
        temp.setPreferredSize(new Dimension(510, 42));
        temp.add(Box.createHorizontalGlue());
        temp.add(switchView);
        temp.add(Box.createHorizontalStrut(30));
        right.add(temp, BorderLayout.NORTH);

        switchView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayRightPanel();
            }
        });
        // </editor-fold>
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setSpring">
    private void setSpring() {
        spring.putConstraint(SpringLayout.NORTH, photo, 5, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.EAST, photo, -5, SpringLayout.EAST, subLeft);

        spring.putConstraint(SpringLayout.NORTH, title, 20, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.WEST, title, 145, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.NORTH, idTitle, 80, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, id, 80, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, nameTitle, 105, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, name, 122, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, dobTitle, 150, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, date, 168, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, isMaleTitle, 202, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, isMale, 200, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, phoneTitle, 224, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, phone, 239, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, mailTitle, 265, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, mail, 282, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, addressTitle, 310, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, address, 328, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, nameMess, 105, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, mailMess, 265, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, dobMess, 150, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, phoneMess, 224, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardTitle, 380, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardTitle, 185, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardNameTitle, 410, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardName, 428, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardRelationTitle, 455, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardRelation, 472, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardPhoneTitle, 500, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardPhone, 517, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardMailTitle, 544, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardMail, 561, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardAddressTitle, 587, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardAddress, 604, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, guardNameMess, 410, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardMailMess, 544, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, guardPhoneMess, 500, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.EAST, nameMess, -100, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, phoneMess, -100, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, dobMess, -100, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, mailMess, -100, SpringLayout.EAST, subLeft);

        spring.putConstraint(SpringLayout.EAST, guardPhoneMess, -100, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, guardNameMess, -100, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, guardMailMess, -100, SpringLayout.EAST, subLeft);

        spring.putConstraint(SpringLayout.WEST, id, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, name, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, phone, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, mail, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, address, 68, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, date, 60, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, isMale, 110, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.WEST, guardName, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardPhone, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardMail, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardAddress, 68, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardRelation, 63, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.WEST, guardNameTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardPhoneTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardMailTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardAddressTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, guardRelationTitle, 40, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.WEST, idTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, nameTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, phoneTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, mailTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, addressTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, dobTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, isMaleTitle, 40, SpringLayout.WEST, subLeft);
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

    // <editor-fold defaultstate="collapsed" desc="drawClass">
    private void drawClass() {
        classBox.removeAll();
        if (student == null) {
            return;
        }

        Iterator<Map.Entry<String, StudentClass>> iter = mainFrame.getModel().getStudentClass().entrySet().iterator();
        while (iter.hasNext()) {
            final StudentClass sc = iter.next().getValue();
            if (sc.getStudentId().equals(student.getId())) {
                model.Class c = mainFrame.getModel().getClasses().get(sc.getClassId());
                ClassType ct = mainFrame.getModel().getClassType(c);
                KulShinyText classLabel = new KulShinyText(c.getClassId() + ": " + c.getClassName() + " - " + ct.getSkill(), f.deriveFont(5f));
                classLabel.addMouseListener(new ClassListener(c));
                classLabel.setMaximumSize(new Dimension(340, 30));
                classLabel.setPreferredSize(new Dimension(340, 30));
                classLabel.setMinimumSize(new Dimension(340, 30));

                KulButton remove = new KulButton(getString("unenroll"));
                remove.setFont(f.deriveFont(11f));
                remove.setToolTipText(getString("unenroll"));
                remove.addMouseListener(new DeleteStudentClassListener(sc));
                remove.setPreferredSize(new Dimension(60, 20));
                remove.setMaximumSize(new Dimension(60, 20));

                JLabel statusLabel = new JLabel();
                statusLabel.setFont(f);
                if (sc.isPaid()) {
                    statusLabel.setText("Paid");
                    statusLabel.setForeground(Template.getPaidColor());
                } else {
                    statusLabel.setText("Unpaid");
                    statusLabel.setForeground(Template.getUnPaidColor());
                }

                Box line = new Box(BoxLayout.X_AXIS);
                line.setPreferredSize(new Dimension(RIGHT_PANEL_W, 30));
                line.setMaximumSize(new Dimension(RIGHT_PANEL_W, 30));

                line.add(Box.createHorizontalStrut(20));
                line.add(remove);
                line.add(Box.createHorizontalStrut(10));
                line.add(classLabel);
                line.add(Box.createHorizontalGlue());
                line.add(statusLabel);
                line.add(Box.createHorizontalStrut(20));
                classBox.add(line);
            }
        }
        classBox.add(Box.createVerticalGlue());

        // update timeTable
        timeTable.setStudent(student);
        timeTable.setRoomId(-1);

        revalidate();
        repaint();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setStudent">
    public boolean setStudent(Student student, KulFrame parentFrame, boolean editable) {
        this.student = student;
        this.parentFrame = parentFrame;
        this.editable = editable;

        mailMess.setText("");
        dobMess.setText("");
        phoneMess.setText("");
        guardPhoneMess.setText("");
        mailMess.setText("");
        guardMailMess.setText("");
        guardNameMess.setText("");

        if (student != null) {
            if (student.isBeingUsed()) {
                JOptionPane.showMessageDialog(parentFrame,
                        getString("studentBeingUsedContent"), getString("caution"),
                        JOptionPane.INFORMATION_MESSAGE);
                parentFrame.closeWindow(false);
                return false;
            }
            student.setBeingUsed(true);

            activate.setVisible(true);
            id.setVisible(true);
            idTitle.setVisible(true);
            generate.setVisible(true);

            id.setText("" + student.getId());
            name.setText(student.getFullname());
            phone.setPhone(student.getPhoneNumber());
            mail.setText(student.getEmail());
            address.setText(student.getAddress());
            date.setDate(student.getDOB());

            if (student.isIsMale()) {
                isMale.setSelectedIndex(0);
            } else {
                isMale.setSelectedIndex(1);
            }
            guardAddress.setText(student.getContactAddress());
            guardMail.setText(student.getContactEmail());
            guardName.setText(student.getContactName());
            guardPhone.setPhone(student.getContactPhone());
            guardRelation.setText(student.getContactRelationship());

            if (student.isActive()) {
                activate.setTextDisplay(getString("deactivate"));
            } else {
                activate.setTextDisplay(getString("activate"));
            }

            save.setTextDisplay("Ok");

            Image img = student.getPhoto().getImage();
            Image newImg = img.getScaledInstance(75, 100, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(newImg));
            drawClass();
        } else {
            id.setVisible(false);
            idTitle.setVisible(false);
            activate.setVisible(false);
            generate.setVisible(false);
            right.removeAll();

            name.setText("");
            phone.setPhone("");
            mail.setText("");
            address.setText("");
            date.setDate(new LocalDate());
            save.setTextDisplay(getString("add"));
            guardPhone.setPhone("");

            name.setEditable(true);
            mail.setEditable(true);
            address.setEditable(true);
            guardAddress.setEditable(true);
            guardName.setEditable(true);
            guardRelation.setEditable(true);
            guardMail.setEditable(true);

            Image img = new ImageIcon(Template.getPhotoURLDefault()).getImage();
            Image newImg = img.getScaledInstance(75, 100, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(newImg));
            drawClass();
        }
        if (!editable) {
            save.setVisible(false);
            activate.setVisible(false);

            if (student != null) {
                generate.setVisible(true);
            }
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setReturnObj">
    @Override
    public void setReturnObj(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="validate and ok">
    private boolean validateFields() {
        boolean isValid = true;
        String emailString = mail.getText();

        //reset warning message
        nameMess.setText("");
        mailMess.setText("");
        dobMess.setText("");
        mailMess.setText("");
        phoneMess.setText("");
        guardPhoneMess.setText("");
        guardMailMess.setText("");
        guardNameMess.setText("");

        if (name.getText() == null) {
            nameMess.setText(getString("fullNameEmptyMess"));
            isValid = false;
        }
        if (name.getText().split("\\s").length < 2) {
            nameMess.setText(getString("fullName2WordMess"));
            isValid = false;
        }
        if (!phone.isIsValid()) {
            phoneMess.setText(getString("phoneNumberMess"));
            isValid = false;
        }
        if (!guardPhone.isIsValid()) {
            guardPhoneMess.setText(getString("phoneNumberMess"));
            isValid = false;
        }
        if (guardName.getText() == null) {
            guardNameMess.setText(getString("fullNameEmptyMess"));
            isValid = false;
        }
        if (guardName.getText().split("\\s").length < 2) {
            guardNameMess.setText(getString("fullName2WordMess"));
            isValid = false;
        }

        LocalDate today = new LocalDate();
        LocalDate bir = date.getDate();
        if (bir.compareTo(today) >= 0) {
            dobMess.setText(getString("birthdayMess"));
            isValid = false;
        }

        if (!Validator.emailValidate(emailString)) {
            mailMess.setText(getString("emailMess"));
            isValid = false;
        }
        if (!Validator.emailValidate(guardMail.getText())) {
            guardMailMess.setText(getString("emailMess"));
            isValid = false;
        }
        return isValid;
    }

    private void ok() {
        if (!editable) {
            return;
        }
        if (!validateFields()) {
            return;
        }
        String phoneString = phone.getPhone();
        String emailString = mail.getText();
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
            if (i < fullname.length - 1) {
                middlename += " ";
            }
        }

        String guardPhoneString = guardPhone.getPhone();
        String guardEmailString = guardMail.getText();
        String guardAddressString = guardAddress.getText();
        String guardNameString = guardName.getText();
        String guardRelationship = guardRelation.getText();

        if (student != null) {
            student.setFirstname(firstname);
            student.setLastname(lastname);
            student.setMiddlename(middlename);
            student.setPhoneNumber(phoneString);
            student.setAddress(addressString);
            student.setDOB(birthDate);
            student.setEmail(emailString);
            student.setPhoto(avatar);
            student.setContactAddress(guardAddressString);
            student.setContactEmail(guardEmailString);
            student.setContactName(guardNameString);
            student.setContactPhone(guardPhoneString);
            student.setContactRelationship(guardRelationship);
            student.setIsMale(isMale1);

            mainFrame.getModel().saveData();
            mainFrame.getMenuView().update("Student");
            JOptionPane.showMessageDialog(parentFrame, getString("updateSuccessfulContent"),
                    getString("success"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            Student s = new Student(guardPhoneString, guardEmailString,
                    guardNameString, guardAddressString, guardRelationship,
                    lastname, middlename, firstname, emailString, phoneString,
                    birthDate, addressString, avatar, isMale1);
            mainFrame.getModel().addStudent(s);
            JOptionPane.showMessageDialog(parentFrame, getString("addSuccessfulContent"),
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

    // <editor-fold defaultstate="collapsed" desc="listerer for class">
    private class ClassListener extends MouseAdapter {

        private model.Class classes;

        public ClassListener(model.Class classes) {
            this.classes = classes;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.showClassForm(classes, false, false);
        }
    }

    private class DeleteStudentClassListener extends MouseAdapter {

        private StudentClass sc;

        public DeleteStudentClassListener(StudentClass sc) {
            this.sc = sc;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int value = JOptionPane.showConfirmDialog(parentFrame,
                    getString("unenrollConfirmContent"), getString("unenrollConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
            if (value == JOptionPane.OK_OPTION) {
                if (mainFrame.getModel().deleteStudentClass(sc)) {
                    JOptionPane.showMessageDialog(parentFrame, getString("unenrollSuccessContent"),
                            getString("success"), JOptionPane.INFORMATION_MESSAGE);
                    drawClass();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, getString("unenrollFailContent"),
                            getString("fail"), JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    // </editor-fold >

    // <editor-fold defaultstate="collapsed" desc="display right panel">
    private void displayRightPanel() {
        int x1 = RIGHT_PANEL_W;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int w = RIGHT_PANEL_W;
        int h = SUB_RIGHT_PANEL_H;

        if (!isTimeTableShowing) {
            new KulAnimator(subRight, timeTable, x1, x2, y1, y2, w, h).slideHorizontal();
            new KulAnimator(subRight, classListPanel, x2, x1, y1, y2, w, h).slideHorizontal();

            isTimeTableShowing = true;
            switchView.setTextDisplay(getString("allClass"));
        } else {
            new KulAnimator(subRight, classListPanel, x1, x2, y1, y2, w, h).slideHorizontal();
            new KulAnimator(subRight, timeTable, x2, x1, y1, y2, w, h).slideHorizontal();

            isTimeTableShowing = false;
            switchView.setTextDisplay(getString("timetableStudent"));
        }
    }
    // </editor-fold>

    private String getString(String key) {
        return language.getString(key);
    }
}
