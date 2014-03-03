package viewcontroller.westbutton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import viewcontroller.MainFrame;

/**
 *
 * @author Dam Linh
 */
public class TeacherButton extends WestButtonBar {

    public TeacherButton(final MainFrame mainFrame) {
        super(mainFrame);
        but1.setToolTipText(s("addTeacherHint"));
        but2.setToolTipText(s("activateSelectedTeacherHint"));
        but3.setToolTipText(s("deactivateSelectedTeacherHint"));
        but4.setToolTipText(s("deleteSelectedTeacherHint"));
        but5.setToolTipText(s("selecteAllTeacherHint"));

        but1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showTeacherForm(null, true);
            }
        });

        but2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().setIsActiveSelected("Teacher", true);
            }
        });

        but5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().selectAllTeacher();
            }
        });

        but3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().setIsActiveSelected("Teacher", false);
            }
        });

        but4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] options = {"Sure", "No"};
                int choice = JOptionPane.showOptionDialog(null,
                        s("deleteTeacherConfirmContent"), s("deleteTeacherConfirmTitle"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if (choice == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteSeletedTeacher()) {
                    }
                }
            }
        });
    }
}
