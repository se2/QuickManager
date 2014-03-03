package viewcontroller.westbutton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import viewcontroller.MainFrame;

/**
 *
 * @author Dam Linh
 */
public class StudentButton extends WestButtonBar {

    public StudentButton(final MainFrame mainFrame) {
        super(mainFrame);

        but1.setToolTipText(s("addStudentHint"));
        but2.setToolTipText(s("activateSelectedStudentHint"));
        but3.setToolTipText(s("deactivateSelectedStudentHint"));
        but5.setToolTipText(s("selecteAllStudentHint"));

        sub.remove(but4);
        sub.remove(strut4);

        but1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showStudentForm(null, true);
            }
        });

        but2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().setIsActiveSelected("Student", true);
            }
        });

        but3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().setIsActiveSelected("Student", false);
            }
        });

        but5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().selectAllStudent();
            }
        });
    }
}
