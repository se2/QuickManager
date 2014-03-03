package viewcontroller.westbutton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import viewcontroller.MainFrame;

/**
 *
 * @author Dam Linh
 */
public class ClassTypeButton extends WestButtonBar {

    public ClassTypeButton(final MainFrame mainFrame) {
        super(mainFrame);

        but1.setToolTipText(s("addClassTypeHint"));
//        but2.setToolTipText("E[n]roll to selected classes");
//        but4.setToolTipText("[Delete] selected classes");
//        but5.setToolTipText(s("selectAllHint"));

//        but2.setTextDisplay("Enroll");

        sub.remove(but3);
        sub.remove(strut3);
        sub.remove(but4);
        sub.remove(strut4);
        sub.remove(but2);
        sub.remove(strut2);
        sub.remove(but5);
        sub.remove(strut5);

        but1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showClassTypeForm(null);// random last para boolean
            }
        });
    }
}
