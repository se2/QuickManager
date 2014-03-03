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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
import kulcomponent.KulSkillChooser;
import kulcomponent.KulTextArea;
import kulcomponent.KulTextField;
import model.Class;
import model.Teacher;
import model.TeacherClass;
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
public class TeacherForm extends JPanel implements HasReturn {

    /*
     * See ClassForm comments (it has the same structure)
     */
    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private Teacher teacher;
    private JLabel id;
    private KulTextField name;
    private KulPhoneChooser phone;
    private KulTextField mail;
    private KulTextArea address;
    private KulDayChooser date;
    private KulComboBox<String> isMale;
    private KulSkillChooser skills;
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
    private JLabel dobMess = new JLabel();
    private JLabel phoneMess = new JLabel();
    private JLabel skillMess = new JLabel();
    private JLabel photo;
    private KulButton save;
    private KulButton delete;
    private KulButton activate;
    private KulButton addClass;
    private KulButton switchView;
    private KulButton getPaySlip;
    private TimeTable timeTable;
    private boolean isTimeTableShowing = false;
    private SpringLayout spring;
    private Box buttons;
    private Box classBox;
    private JLabel classTitle;
    private HashMap<String, Class> classes;
    private HashMap<String, TeacherClass> teacherClasses;
    private JPanel classListPanel = new JPanel(new BorderLayout());
    private JPanel subLeft;
    private JPanel subRight = new JPanel(null);
    private JPanel left;
    private JPanel right;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private boolean editable = true;
    private final int RIGHT_PANEL_W = 520;
    private final int SUB_RIGHT_PANEL_H = 650;
    private Font f = Template.getFont().deriveFont(16f);
    private ResourceBundle language;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    public TeacherForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout(0, 0));
        language = mainFrame.getModel().getLanguage();

        // <editor-fold defaultstate="collapsed" desc="drawLeftPanel">
        left = new JPanel(new BorderLayout());
        left.setBorder(new MatteBorder(0, 0, 0, 1, Template.getBorderContrast()));

        spring = new SpringLayout();
        subLeft = new JPanel(spring);
        subLeft.setBackground(Template.getBackground());
        left.add(subLeft);
        add(left);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="drawRightPanel">
        right = new JPanel(new BorderLayout());
        right.setPreferredSize(new Dimension(RIGHT_PANEL_W, 770));
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
        classTitle.setPreferredSize(new Dimension(510, 80));
        classListPanel.add(classBox);

        classListPanel.setBounds(0, 0, RIGHT_PANEL_W, SUB_RIGHT_PANEL_H);
        // </editor-fold>

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!address.hasFocus()) {
                    ok();
                }
            }
        });
        //</editor-fold>

        drawButtons();
        init();
    }

    private void init() {
        title = new JLabel(getString("teacher"));
        idTitle = new JLabel(getString("id"));
        nameTitle = new JTextField(getString("fullName"));
        phoneTitle = new JTextField(getString("phoneNumber"));
        mailTitle = new JTextField(getString("email1"));
        addressTitle = new JTextField(getString("address"));
        dobTitle = new JTextField(getString("birthday"));
        isMaleTitle = new JTextField(getString("gender"));
        classTitle.setToolTipText(getString("classTitleHint"));

        nameMess = new JLabel();
        mailMess = new JLabel();
        photo = new JLabel();
        id = new JLabel();

        name = new KulTextField(29);
        phone = new KulPhoneChooser();
        mail = new KulTextField(29);
        address = new KulTextArea(3, 30);
        isMale = new KulComboBox<>(new String[]{getString("male"), getString("female")});

        skills = new KulSkillChooser(mainFrame);
        skills.setToolTipText(getString("skillHint"));

        date = new KulDayChooser(f, 1900, language);

        name.setFont(f);
        skills.setFont(f);
        phone.setFont(f);
        mail.setFont(f);
        address.setFont(f);
        isMale.setFont(f);
        id.setFont(f);
        idTitle.setFont(f);
        classTitle.setFont(f.deriveFont(50f));
        title.setFont(f.deriveFont(50f));
        nameMess.setFont(Template.getFont());
        mailMess.setFont(Template.getFont());
        dobMess.setFont(Template.getFont());
        phoneMess.setFont(Template.getFont());
        skillMess.setFont(Template.getFont());

        mailMess.setFocusable(false);
        phoneMess.setFocusable(false);
        dobTitle.setFocusable(false);
        nameMess.setFocusable(false);
        dobMess.setFocusable(false);
        skillMess.setFocusable(false);
        title.setFocusable(false);
        idTitle.setFocusable(false);
        id.setFocusable(false);
        classTitle.setFocusable(false);

        setKeyBinding(name);
        setKeyBinding(phone);
        setKeyBinding(mail);
        setKeyBinding(date);
        setKeyBinding(isMale);

        mailMess.setForeground(Color.red);
        dobMess.setForeground(Color.red);
        skillMess.setForeground(Color.red);
        nameMess.setForeground(Color.red);
        phoneMess.setForeground(Color.red);
        nameTitle.setForeground(Template.getForeground());
        phoneTitle.setForeground(Template.getForeground());
        mailTitle.setForeground(Template.getForeground());
        addressTitle.setForeground(Template.getForeground());
        dobTitle.setForeground(Template.getForeground());
        classTitle.setForeground(Template.getForeground());
        isMaleTitle.setForeground(Template.getForeground());
        title.setForeground(Template.getForeground());
        idTitle.setForeground(Template.getForeground());
        id.setForeground(Template.getForeground());

        setComponent(nameTitle, 20, 100, 16);
        setComponent(phoneTitle, 20, 30, 16);
        setComponent(mailTitle, 20, 30, 16);
        setComponent(addressTitle, 20, 30, 16);
        setComponent(dobTitle, 20, 30, 16);
        setComponent(isMaleTitle, 20, 30, 16);

        subLeft.add(name);
        subLeft.add(nameMess);
        subLeft.add(phone);
        subLeft.add(mail);
        subLeft.add(address);
        subLeft.add(isMale);
        subLeft.add(skills);
        subLeft.add(skillMess);
        subLeft.add(phoneMess);
        subLeft.add(nameTitle);
        subLeft.add(phoneTitle);
        subLeft.add(mailTitle);
        subLeft.add(addressTitle);
        subLeft.add(dobTitle);
        subLeft.add(dobMess);
        subLeft.add(isMaleTitle);
        subLeft.add(photo);
        subLeft.add(date);
        subLeft.add(mailMess);
        subLeft.add(title);
        subLeft.add(id);
        subLeft.add(idTitle);
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
                    Image newImg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
                    photo.setIcon(new ImageIcon(newImg));
                }
            }
        });
        //</editor-fold>
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        addClass = new KulButton(getString("addClass"));
        switchView = new KulButton(getString("timetable"));
        delete = new KulButton(getString("delete"));
        save = new KulButton(getString("save"));
        activate = new KulButton(getString("activate"));
        getPaySlip = new KulButton(getString("getPaySlip"));

        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 60));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        buttons.setOpaque(true);
        buttons.setBackground(Template.getBackground());
        left.add(buttons, BorderLayout.SOUTH);
        setComponent(delete, 100, 30, 16);
        setComponent(save, 100, 30, 16);
        setComponent(activate, 100, 30, 16);
        setComponent(addClass, 100, 25, 14);
        setComponent(switchView, 100, 25, 14);
        setComponent(getPaySlip, 100, 25, 14);

        // <editor-fold defaultstate="collapsed" desc="left buttons">
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int value = JOptionPane.showConfirmDialog(mainFrame,
                        getString("deleteTeacherConfirmContent"), getString("deleteTeacherConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteTeacher(teacher)) {
                        JOptionPane.showMessageDialog(parentFrame, getString("deleteTeacherSuccess"));
                        parentFrame.closeWindow(true);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, getString("deleteTeacherFail"));
                    }
                }
            }
        });

        activate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().activate(teacher);
                if (teacher.isActive()) {
                    activate.setTextDisplay(getString("deactivate"));
                } else {
                    activate.setTextDisplay(getString("activate"));
                }
            }
        });
        save.addMouseListener(new SaveListener());

        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(activate);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(delete);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(save);
        buttons.add(Box.createHorizontalGlue());
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="right buttons">
        Box temp = new Box(BoxLayout.X_AXIS);
        temp.setPreferredSize(new Dimension(RIGHT_PANEL_W, 35));
        temp.add(Box.createHorizontalStrut(30));
        temp.add(addClass);
        temp.add(Box.createHorizontalStrut(20));
        temp.add(getPaySlip);
        temp.add(Box.createHorizontalGlue());
        temp.add(switchView);
        temp.add(Box.createHorizontalStrut(30));
        right.add(temp, BorderLayout.NORTH);

        addClass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showClassListView(TeacherForm.this, parentFrame, teacher);
            }
        });

        switchView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayRightPanel();
            }
        });

        getPaySlip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showPaySlipListView(teacher, parentFrame);
            }
        });
        // </editor-fold>
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawClass">
    private void drawClass() {
        classBox.removeAll();
        if (classes == null) {
            return;
        }

        Iterator<Map.Entry<String, Class>> iter = classes.entrySet().iterator();
        while (iter.hasNext()) {
            Class c = iter.next().getValue();

            KulShinyText classLabel = new KulShinyText(c.getClassId() + ", Name: " + c.getClassName(), f.deriveFont(5f));
            classLabel.addMouseListener(new Listener(c));

//            KulImageButton remove = new KulImageButton("cancel", 16, 16);
//            remove.setToolTipText(getString("removeClassFromTeacher"));
//            Iterator<Entry<String, TeacherClass>> i = mainFrame.getModel().getTeacherClass().entrySet().iterator();
//            while (i.hasNext()) {
//                TeacherClass tc = i.next().getValue();
//                if (tc.getClassId().equals(c.getClassId())) {
//                    if (tc.getTeacherId().equals(teacher.getId())) {
//                        teacherClasses.put(tc.getId(), tc);
//                        remove.addMouseListener(new DeleteTeacherClassListener(tc, false));
//                    } else {
//                        remove.addMouseListener(new DeleteTeacherClassListener(tc, true));
//                    }
//                    break;
//                }
//            }

            JLabel padding = new JLabel(" ");
            padding.setPreferredSize(new Dimension(50, 10));

            Box line = new Box(BoxLayout.X_AXIS);
            line.setPreferredSize(new Dimension(RIGHT_PANEL_W, 30));
            line.setMaximumSize(new Dimension(RIGHT_PANEL_W, 30));

            line.add(Box.createHorizontalStrut(50));
//            line.add(remove);
//            line.add(Box.createHorizontalStrut(10));
            line.add(classLabel);
            line.add(Box.createHorizontalGlue());
            classBox.add(line);
        }
        classBox.add(Box.createVerticalGlue());

        // update timeTable
        timeTable.setTeacher(teacher);
        timeTable.setRoomId(-1);

        revalidate();
        repaint();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setSpring">
    private void setSpring() {
        spring.putConstraint(SpringLayout.NORTH, photo, 7, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.EAST, photo, -7, SpringLayout.EAST, subLeft);

        spring.putConstraint(SpringLayout.NORTH, title, 55, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.WEST, title, 145, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.NORTH, idTitle, 120, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, id, 120, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, nameTitle, 150, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, name, 175, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, dobTitle, 210, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, date, 235, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, isMaleTitle, 270, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, isMale, 295, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, phoneTitle, 330, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, phone, 355, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, mailTitle, 390, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, mail, 415, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, addressTitle, 450, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, address, 475, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, skills, 550, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.NORTH, nameMess, 150, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, mailMess, 390, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, dobMess, 210, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, skillMess, 550, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.NORTH, phoneMess, 330, SpringLayout.NORTH, subLeft);

        spring.putConstraint(SpringLayout.EAST, nameMess, -90, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, dobMess, -90, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, skillMess, -90, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, phoneMess, -90, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.EAST, mailMess, -90, SpringLayout.EAST, subLeft);

        spring.putConstraint(SpringLayout.WEST, id, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, name, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, phone, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, mail, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, address, 63, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, date, 60, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, isMale, 65, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, addClass, 65, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.WEST, idTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, nameTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, phoneTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, mailTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, addressTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, dobTitle, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.WEST, isMaleTitle, 40, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.WEST, skills, 40, SpringLayout.WEST, subLeft);
        spring.putConstraint(SpringLayout.EAST, skills, -40, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.SOUTH, skills, -5, SpringLayout.SOUTH, subLeft);
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

    // <editor-fold defaultstate="collapsed" desc="setTeacher">
    public boolean setTeacher(Teacher teacher, KulFrame parentFrame, boolean editable) {
        this.teacher = teacher;
        this.parentFrame = parentFrame;
        this.editable = editable;

        mailMess.setText("");
        dobMess.setText("");
        skillMess.setText("");
        mailMess.setText("");
        phoneMess.setText("");

        if (teacher != null) {
            if (teacher.isBeingUsed() && editable) {
                JOptionPane.showMessageDialog(parentFrame,
                        getString("teacherBeingUsedContent"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                parentFrame.closeWindow(false);
                return false;
            }
            teacher.setBeingUsed(true);
            address.requestFocus();//avoid name having focus
            right.setVisible(true);

            delete.setVisible(true);
            id.setVisible(true);
            idTitle.setVisible(true);
            id.setText(teacher.getId());
            name.setText(teacher.getFullname());
            phone.setPhone(teacher.getPhoneNumber());
            mail.setText(teacher.getEmail());
            address.setText(teacher.getAddress());
            date.setDate(teacher.getDOB());
            if (teacher.isIsMale()) {
                isMale.setSelectedIndex(0);
            } else {
                isMale.setSelectedIndex(1);
            }
            activate.setVisible(true);
            if (teacher.isActive()) {
                activate.setTextDisplay(getString("deactivate"));
            } else {
                activate.setTextDisplay(getString("activate"));
            }
            save.setTextDisplay("Ok");
            // <editor-fold defaultstate="collapsed" desc="set selected skills">
            skills.setSkill(teacher.getSkills());
            // </editor-fold>
            Image img = teacher.getPhoto().getImage();
            Image newImg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(newImg));

            classes = new HashMap<>();
            teacherClasses = new HashMap<>();
            Iterator<Map.Entry<String, TeacherClass>> iter = mainFrame.getModel().getTeacherClass().entrySet().iterator();
            while (iter.hasNext()) {
                TeacherClass tc = iter.next().getValue();
                if (tc.getTeacherId().equals(teacher.getId())) {
                    Class c = mainFrame.getModel().getClasses().get(tc.getClassId());
                    classes.put(c.getClassId(), c);
                }
            }
            drawClass();
        } else {
            addClass.setVisible(false);
            classTitle.setVisible(false);
            activate.setVisible(false);
            id.setVisible(false);
            idTitle.setVisible(false);
            delete.setVisible(false);
            right.removeAll();

            name.setText("");
            phone.setPhone("");
            mail.setText("");
            address.setText("");
            date.setDate(new LocalDate());
            save.setTextDisplay(getString("add"));

            name.setEditable(true);
            mail.setEditable(true);
            address.setEditable(true);

            Image img = new ImageIcon(Template.getPhotoURLDefault()).getImage();
            Image newImg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(newImg));
        }
        if (!editable) {
            save.setVisible(false);
            delete.setVisible(false);
            activate.setVisible(false);
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setReturnObj">
    @Override
    public void setReturnObj(Object o) {
        if (o instanceof HashMap) {
            HashMap<String, Class> classedAdded = (HashMap<String, Class>) o;
            Iterator<Entry<String, TeacherClass>> iterTC = mainFrame.getModel().getTeacherClass().entrySet().iterator();
            while (iterTC.hasNext()) {
                TeacherClass tc = iterTC.next().getValue();
                if (classedAdded.containsKey(tc.getClassId())) {
                    teacherClasses.put(tc.getId(), tc);
                }
            }
            classes.putAll(classedAdded);
            drawClass();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="validate and ok">
    private boolean validateFields() {
        boolean isValid = true;
        String emailString = mail.getText();

        //reset warning message
        mailMess.setText("");
        nameMess.setText("");
        dobMess.setText("");
        skillMess.setText("");
        phoneMess.setText("");

        if (name.getText() == null) {
            nameMess.setText(getString("fullNameEmptyMess"));
            isValid = false;
        }
        if (name.getText().split("\\s").length < 2) {
            nameMess.setText(getString("fullName2WordMess"));
            isValid = false;
        }

        if (!Validator.emailValidate(emailString)) {
            mailMess.setText(getString("emailMess"));
            isValid = false;
        }

        if (!phone.isIsValid()) {
            phoneMess.setText(getString("phoneNumberMess"));
            isValid = false;
        }

        LocalDate bir = date.getDate();
        if (bir.compareTo(new LocalDate()) >= 0) {
            dobMess.setText(getString("birthdayMess"));
            isValid = false;
        }

        if (!skills.isSkillValid()) {
            isValid = false;
        }

        // <editor-fold defaultstate="collapsed" desc="validate teacher and class major">
        if (classes != null && teacher != null) {
            Iterator<Entry<String, Class>> iter = classes.entrySet().iterator();
            while (iter.hasNext()) {
                Class c = iter.next().getValue();
                if (!Validator.teacherClassValidate(skills.getSkills(), mainFrame.getModel().getClassType(c))) {
                    isValid = false;
                    JOptionPane.showMessageDialog(parentFrame, getString("teacherSkillClass") + " " + c.getClassId());
                }
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="validate class time conflit">
        if (classes != null) {
            Iterator<Entry<String, Class>> iter1 = classes.entrySet().iterator();
            while (iter1.hasNext()) {
                Class c1 = iter1.next().getValue();
                Iterator<Entry<String, Class>> iter2 = classes.entrySet().iterator();
                while (iter2.hasNext()) {
                    Class c2 = iter2.next().getValue();
                    if (c2 != c1) {
                        if (!Validator.validateClassTime(c1, c2, mainFrame.getModel())) {
                            JOptionPane.showMessageDialog(parentFrame, c1.getClassId()
                                    + " and " + c2.getClassId() + " have session(s) that conflict.",
                                    "Time conflict", JOptionPane.WARNING_MESSAGE);
                            isValid = false;
                        }
                    }
                }
            }
        }
        // </editor-fold>

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
        }

        if (teacher != null) {
            teacher.setFirstname(firstname);
            teacher.setLastname(lastname);
            teacher.setMiddlename(middlename);
            teacher.setPhoneNumber(phoneString);
            teacher.setAddress(addressString);
            teacher.setDOB(birthDate);
            teacher.setEmail(emailString);
            teacher.setPhoto(avatar);
            if (isPayRateChange()) {
                teacher.setSkills(skills.getSkills());
                mainFrame.getModel().resetPaySlipOfTeacher(teacher);
            }
            teacher.setIsMale(isMale1);

            // change the teacherId of TeacherClass to this teaecherId
            Iterator<Entry<String, TeacherClass>> tcs = teacherClasses.entrySet().iterator();
            while (tcs.hasNext()) {
                tcs.next().getValue().setTeacherId(teacher.getId());
            }

            JOptionPane.showMessageDialog(parentFrame, getString("teacherUpdateSuccess"), getString("success"), JOptionPane.INFORMATION_MESSAGE);
            mainFrame.getModel().saveData();
            mainFrame.getMenuView().update("Teacher");
        } else {
            Teacher t = new Teacher(skills.getSkills(), lastname,
                    middlename, firstname, emailString, phoneString,
                    birthDate, addressString, avatar, isMale1);
            mainFrame.getModel().addTeacer(t);
            JOptionPane.showMessageDialog(parentFrame, getString("teacherAddSucess"), getString("success"), JOptionPane.INFORMATION_MESSAGE);
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

    // <editor-fold defaultstate="collapsed" desc="set key binding">
    private void setKeyBinding(JComponent c) {
        c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        c.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="listerer for class">
    private class Listener extends MouseAdapter {

        private model.Class classes;

        public Listener(model.Class classes) {
            this.classes = classes;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.showClassForm(classes, true, false);
        }
    }

    private class DeleteTeacherClassListener extends MouseAdapter {

        private TeacherClass tc;
        private boolean instantRemove;

        public DeleteTeacherClassListener(TeacherClass tc, boolean instantRemove) {
            this.tc = tc;
            this.instantRemove = instantRemove;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (instantRemove || mainFrame.getModel().deleteTeacherClass(tc)) {
                JOptionPane.showMessageDialog(parentFrame, getString("removeSuccess"), getString("success"), JOptionPane.INFORMATION_MESSAGE);
                classes.remove(tc.getClassId());
                teacherClasses.remove(tc.getId());
                drawClass();
                classBox.revalidate();
                classBox.repaint();
            } else {
                JOptionPane.showMessageDialog(parentFrame, getString("teacherRemoveClassFailContent"), getString("fail"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    // </editor-fold >

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

        if (!isTimeTableShowing) {
            new KulAnimator(subRight, timeTable, x1, x2, y1, y2, w, h).slideHorizontal();
            new KulAnimator(subRight, classListPanel, x2, x1, y1, y2, w, h).slideHorizontal();

            isTimeTableShowing = true;
            switchView.setTextDisplay(getString("allClass"));
        } else {
            new KulAnimator(subRight, classListPanel, x1, x2, y1, y2, w, h).slideHorizontal();
            new KulAnimator(subRight, timeTable, x2, x1, y1, y2, w, h).slideHorizontal();

            isTimeTableShowing = false;
            switchView.setTextDisplay(getString("timetableTeacher"));
        }
    }
    // </editor-fold>

    private boolean isPayRateChange() {
        String[][] skills1 = teacher.getSkills();
        String[][] skills2 = skills.getSkills();

        if (skills1.length != skills2.length) {
            return true;
        }

        for (int i = 0; i < skills1.length; i++) {
            if (!skills1[i][1].equals(skills2[i][0])) {
                return true;
            }
        }
        return false;
    }

    private String getString(String key) {
        return language.getString(key);
    }
}
