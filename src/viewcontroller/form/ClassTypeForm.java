package viewcontroller.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import kulcomponent.KulFrame;
import kulcomponent.KulTextArea;
import kulcomponent.KulTextField;
import model.ClassType;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 * @author Dam Linh
 */
public class ClassTypeForm extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private final Font f = Template.getFont().deriveFont(16f);
    private ClassType classType;
    private JLabel title;
    private JLabel skillTitle;
    private JLabel typeTitle;
    private JLabel lessonPerWeekTitle;
    private JLabel fee45Title;
    private JLabel fee60Title;
    private JLabel noteTitle;
    private KulTextField skill = new KulTextField(31);
    private KulComboBox<String> type;
    private KulComboBox<String> lessonPerWeek;
    private KulTextField fee45 = new KulTextField(31);
    private KulTextField fee60 = new KulTextField(31);
    private KulTextArea note = new KulTextArea(4, 31);
    private JLabel fee45Mess = new JLabel();
    private JLabel fee60Mess = new JLabel();
    private KulButton save;
    private KulButton cancel;
    private KulButton delete;
    private Box buttons;
    private JPanel left;
    private SpringLayout spring;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private boolean editable;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constructor">
    public ClassTypeForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout());
        language = mainFrame.getModel().getLanguage();

        spring = new SpringLayout();
        left = new JPanel(spring);
        left.setBackground(Template.getBackground());
        add(new JLabel(), BorderLayout.WEST);//padding purpose
        add(left);

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        //</editor-fold>

        init();
        drawButtons();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    private void init() {
        title = new JLabel(s("classType"));
        skillTitle = new JLabel(s("class1"));
        typeTitle = new JLabel(s("type"));
        lessonPerWeekTitle = new JLabel(s("lessonPerWeek"));
        fee45Title = new JLabel(s("fee45"));
        fee60Title = new JLabel(s("fee60"));
        noteTitle = new JLabel(s("remark"));
        lessonPerWeek = new KulComboBox<>(new String[]{"1 " + s("or") + " 2", "1", "2"});
        type = new KulComboBox<>(new String[]{s("individual"), s("dual"), s("group")});

        title.setFont(f.deriveFont(50f));
        skillTitle.setFont(f);
        typeTitle.setFont(f);
        lessonPerWeekTitle.setFont(f);
        fee45Title.setFont(f);
        fee60Title.setFont(f);
        noteTitle.setFont(f);

        skill.setFont(f);
        type.setFont(f);
        lessonPerWeek.setFont(f);
        fee45.setFont(f);
        fee60.setFont(f);
        note.setFont(f);

        fee45Mess.setFont(Template.getFont());
        fee60Mess.setFont(Template.getFont());

        fee45Mess.setFocusable(false);
        fee60Mess.setFocusable(false);
        title.setFocusable(false);
        skillTitle.setFocusable(false);
        typeTitle.setFocusable(false);
        lessonPerWeekTitle.setFocusable(false);
        fee45Title.setFocusable(false);
        fee60Title.setFocusable(false);
        noteTitle.setFocusable(false);

        fee45Mess.setForeground(Color.red);
        fee60Mess.setForeground(Color.red);
        title.setForeground(Template.getForeground());
        skillTitle.setForeground(Template.getForeground());
        typeTitle.setForeground(Template.getForeground());
        lessonPerWeekTitle.setForeground(Template.getForeground());
        fee45Title.setForeground(Template.getForeground());
        fee60Title.setForeground(Template.getForeground());
        noteTitle.setForeground(Template.getForeground());

        left.add(skill);
        left.add(type);
        left.add(lessonPerWeek);
        left.add(fee45);
        left.add(fee60);
        left.add(note);
        left.add(fee45Mess);
        left.add(fee60Mess);
        left.add(title);
        left.add(skillTitle);
        left.add(typeTitle);
        left.add(lessonPerWeekTitle);
        left.add(fee45Title);
        left.add(fee60Title);
        left.add(noteTitle);
        setSpring();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setSpring">
    private void setSpring() {
        spring.putConstraint(SpringLayout.NORTH, title, 40, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.WEST, title, 140, SpringLayout.WEST, left);

        spring.putConstraint(SpringLayout.NORTH, skillTitle, 120, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, skill, 145, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, typeTitle, 190, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, type, 215, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, lessonPerWeekTitle, 260, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, lessonPerWeek, 285, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, fee45Title, 330, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, fee45, 355, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, fee60Title, 400, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, fee60, 425, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, noteTitle, 470, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, note, 495, SpringLayout.NORTH, left);

        spring.putConstraint(SpringLayout.NORTH, fee45Mess, 330, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.NORTH, fee60Mess, 400, SpringLayout.NORTH, left);
        spring.putConstraint(SpringLayout.EAST, fee45Mess, -90, SpringLayout.EAST, left);
        spring.putConstraint(SpringLayout.EAST, fee60Mess, -90, SpringLayout.EAST, left);

        spring.putConstraint(SpringLayout.WEST, skill, 70, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, type, 70, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, lessonPerWeek, 70, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, fee45, 70, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, fee60, 70, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, note, 70, SpringLayout.WEST, left);

        spring.putConstraint(SpringLayout.WEST, skillTitle, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, typeTitle, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, lessonPerWeekTitle, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, fee45Title, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, fee60Title, 40, SpringLayout.WEST, left);
        spring.putConstraint(SpringLayout.WEST, noteTitle, 40, SpringLayout.WEST, left);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        save = new KulButton("Ok");
        cancel = new KulButton(s("cancel"));
        delete = new KulButton(s("delete"));

        save.setPreferredSize(new Dimension(120, 30));
        save.setMaximumSize(new Dimension(120, 30));
        cancel.setPreferredSize(new Dimension(120, 30));
        cancel.setMaximumSize(new Dimension(120, 30));
        delete.setPreferredSize(new Dimension(120, 30));
        delete.setMaximumSize(new Dimension(120, 30));
        save.setFont(f);
        delete.setFont(f);
        cancel.setFont(f);

        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 60));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        add(buttons, BorderLayout.SOUTH);

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.closeWindow(true);
            }
        });

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int value = JOptionPane.showConfirmDialog(parentFrame,
                        s("deleteClassTypeConfirmContent"), s("deleteConfirm"),
                        JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteClassType(classType)) {
                        JOptionPane.showMessageDialog(parentFrame,
                                s("deleteSuccessContent"), s("success"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame,
                                s("deleteFailContent"),
                                s("fail"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ok();
            }
        });

        buttons.add(Box.createHorizontalGlue());
        buttons.add(delete);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(save);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(cancel);
        buttons.add(Box.createHorizontalStrut(20));
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setClassType">
    public boolean setClassType(ClassType ct, KulFrame parentFrame, boolean editable) {
        this.classType = ct;
        this.editable = editable;
        this.parentFrame = parentFrame;

        fee45Mess.setText("");
        fee60Mess.setText("");

        if (ct != null) {
            if (ct.isBeingUsed()) {
                JOptionPane.showMessageDialog(parentFrame,
                        s("classTypeBeingUsedContent"), s("caution"), JOptionPane.INFORMATION_MESSAGE);
                parentFrame.closeWindow(false);
                return false;
            }
            ct.setBeingUsed(true);
            delete.setVisible(true);

            save.setTextDisplay(s("ok"));
            skill.setText(ct.getSkill());
            type.setSelectedIndex(ct.getType());
            lessonPerWeek.setSelectedIndex(ct.getLessonPerWeek());
            fee45.setText(ct.getFeeFor45Min() + "");
            fee60.setText(ct.getFeeFor60Min() + "");
            note.setText(ct.getNote());
        } else {
            delete.setVisible(false);

            save.setTextDisplay(s("add"));
            skill.setText("");
            type.setSelectedIndex(0);
            lessonPerWeek.setSelectedIndex(0);
            fee45.setText("");
            fee60.setText("");
            note.setText("");
        }

        if (!editable) {
            save.setVisible(false);
        }
        return true;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="validate">
    private boolean validateField() {
        boolean isValid = true;
        fee45Mess.setText("");
        fee60Mess.setText("");

        if (!fee45.getText().matches("^[0-9]{1,9}$")) {
            fee45Mess.setText(s("fee45Mess"));
            isValid = false;
        }
        if (!fee60.getText().matches("^[0-9]{1,9}$")) {
            fee60Mess.setText(s("fee60Mess"));
            isValid = false;
        }
        return isValid;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ok">
    private void ok() {
        if (!editable) {
            return;
        }
        if (!validateField()) {
            return;
        }

        if (classType != null) {
            classType.setFeeFor45Min(Long.parseLong(fee45.getText()));
            classType.setFeeFor60Min(Long.parseLong(fee60.getText()));
            classType.setLessonPerWeek(lessonPerWeek.getSelectedIndex());
            classType.setNote(note.getText());
            classType.setSkill(skill.getText());
            classType.setType(type.getSelectedIndex());

            mainFrame.getModel().updateClassType(classType);
//            mainFrame.getModel().saveData();
//            mainFrame.getMenuView().update("Class Type");
            JOptionPane.showMessageDialog(parentFrame, s("updateConfirmContent"),
                    s("success"), JOptionPane.INFORMATION_MESSAGE);
            parentFrame.closeWindow(true);
        } else {
            ClassType ct = new ClassType(skill.getText(), type.getSelectedIndex(),
                    lessonPerWeek.getSelectedIndex(), Long.parseLong(fee45.getText()),
                    Long.parseLong(fee60.getText()), note.getText());
            if (mainFrame.getModel().addClassType(ct)) {
                JOptionPane.showMessageDialog(parentFrame, s("addConfirmContent"),
                        s("success"), JOptionPane.INFORMATION_MESSAGE);
                parentFrame.closeWindow(true);
            } else {
                JOptionPane.showMessageDialog(parentFrame, s("addClassTypeFailContent"),
                        s("fail"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    //</editor-fold>

    private String s(String key) {
        return language.getString(key);
    }
}
