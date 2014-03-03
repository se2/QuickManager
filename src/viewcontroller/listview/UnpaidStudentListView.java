package viewcontroller.listview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import kulcomponent.KulButton;
import kulcomponent.KulImageButton;
import kulcomponent.KulShinyText;
import model.Student;
import model.StudentClass;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class UnpaidStudentListView extends JFrame {

    private ResourceBundle language;
    private MainFrame mainFrame;
    private ArrayList<StudentClass> unPaidStudentClass;
    private Font f = Template.getFont().deriveFont(16f);
    private Box scBox;
    private KulButton deEnrollAll;
    private JPanel container;

    //<editor-fold defaultstate="collapsed" desc="init">
    public UnpaidStudentListView(MainFrame mainFrame, ArrayList<StudentClass> unpaidSC) {
        this.mainFrame = mainFrame;
        this.unPaidStudentClass = unpaidSC;
        language = mainFrame.getModel().getLanguage();

        setSize(new Dimension(550, 730));
        setUndecorated(true);
        setResizable(false);
        setTitle(language.getString("overDueStudents"));
        setLocationRelativeTo(mainFrame);

        container = new JPanel(new BorderLayout());
        container.setBorder(new CompoundBorder(
                new LineBorder(Template.getLineBorderColor(), 4),
                new LineBorder(Template.getBackground(), 5)));
        container.setBackground(Template.getBackground());
        add(container);

        //<editor-fold defaultstate="collapsed" desc="drawTitle">
        JLabel title = new JLabel(language.getString("overDueStudents"));
        title.setFont(f.deriveFont(50f));
        title.setForeground(Template.getForeground());

        Box northBox = new Box(BoxLayout.X_AXIS);
        northBox.setPreferredSize(new Dimension(550, 100));
        northBox.add(Box.createHorizontalGlue());
        northBox.add(title);
        northBox.add(Box.createHorizontalGlue());
        //</editor-fold>

        scBox = new Box(BoxLayout.Y_AXIS);
        container.add(northBox, BorderLayout.NORTH);
        setKeyBinding();
        drawContent();
        drawButton();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButton">
    private void drawButton() {
        deEnrollAll = new KulButton(language.getString("unenrollAll"));
        Box buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 40));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        container.add(buttons, BorderLayout.SOUTH);

        deEnrollAll.setPreferredSize(new Dimension(100, 30));
        deEnrollAll.setMaximumSize(new Dimension(100, 30));
        deEnrollAll.setFont(f);

        deEnrollAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int value = JOptionPane.showConfirmDialog(UnpaidStudentListView.this,
                        language.getString("unenrollAllStudentConfirmContent"),
                        language.getString("unenrollConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                if (value == JOptionPane.OK_OPTION) {
                    boolean deleteAllSuccess = true;
                    for (int i = 0; i < unPaidStudentClass.size(); i++) {
                        if (!mainFrame.getModel().deleteStudentClass(unPaidStudentClass.get(i))) {
                            deleteAllSuccess = false;
                            JOptionPane.showMessageDialog(UnpaidStudentListView.this,
                                    language.getString("unenrollFailContent"),
                                    language.getString("fail"), JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    if (deleteAllSuccess) {
                        JOptionPane.showMessageDialog(UnpaidStudentListView.this,
                                language.getString("unenrollSuccessContent"),
                                language.getString("success"), JOptionPane.INFORMATION_MESSAGE);
                        closeWindow();
                    }
                }
            }
        });
        buttons.add(Box.createHorizontalGlue());
        buttons.add(deEnrollAll);
        buttons.add(Box.createHorizontalGlue());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawContent">
    private void drawContent() {
        scBox.removeAll();
        container.add(scBox);

        for (int i = 0; i < unPaidStudentClass.size(); i++) {
            final StudentClass sc = unPaidStudentClass.get(i);
            final Student s = mainFrame.getModel().getStudents().get(sc.getStudentId());
            final model.Class c = mainFrame.getModel().getClasses().get(sc.getClassId());

            KulImageButton remove = new KulImageButton("cancel", 13, 13);
            remove.setToolTipText(language.getString("unenroll"));
            remove.addMouseListener(new DeleteStudentClassListener(sc));

            KulShinyText studentLabel = new KulShinyText(
                    s.getId() + " - " + s.getFullname(), f.deriveFont(4f));
//            studentLabel.setPreferredSize(new Dimension(0, 25));
            studentLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mainFrame.showStudentForm(s, false);
                }
            });

            KulShinyText classLabel = new KulShinyText(
                    c.getClassId() + " - " + c.getClassName(), f.deriveFont(4f));
//            classLabel.setPreferredSize(new Dimension(0, 25));
            classLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mainFrame.showClassForm(c, false, false);
                }
            });

            Box line = new Box(BoxLayout.X_AXIS);
            line.setPreferredSize(new Dimension(550, 25));
            line.setMaximumSize(new Dimension(550, 25));

            line.add(Box.createHorizontalStrut(30));
            line.add(remove);
            line.add(Box.createHorizontalStrut(10));
            line.add(studentLabel);
//            line.add(Box.createHorizontalStrut(10));
            line.add(classLabel);
            line.add(Box.createHorizontalGlue());
            scBox.add(line);
        }
        scBox.add(Box.createVerticalGlue());
        scBox.revalidate();
        scBox.repaint();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setKeyBinding">
    private void setKeyBinding() {
        getRootPane().setFocusable(false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "exit");
        getRootPane().getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });
    }
    //</editor-fold>

    private void closeWindow() {
        this.setVisible(false);
        mainFrame.setEnabled(true);
        mainFrame.setState(Frame.NORMAL);
        mainFrame.toFront();
    }

    private class DeleteStudentClassListener extends MouseAdapter {

        private StudentClass sc;

        public DeleteStudentClassListener(StudentClass sc) {
            this.sc = sc;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (mainFrame.getModel().deleteStudentClass(sc)) {
                JOptionPane.showMessageDialog(UnpaidStudentListView.this,
                        language.getString("unenrollSuccessContent"),
                        language.getString("success"), JOptionPane.INFORMATION_MESSAGE);
                unPaidStudentClass.remove(sc);
                drawContent();
            } else {
                JOptionPane.showMessageDialog(UnpaidStudentListView.this,
                        language.getString("unenrollFailContent"),
                        language.getString("fail"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
