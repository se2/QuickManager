package viewcontroller.westbutton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import model.Class;
import viewcontroller.MainFrame;

/**
 *
 * @author Dam Linh
 *
 * This class and other class named with XxxButton is to draw all buttons and
 * their listeners in the menu view that associated with the list view.
 */
public class ClassButton extends WestButtonBar {

    public ClassButton(final MainFrame mainFrame) {
        super(mainFrame);

        but1.setToolTipText(s("addClassHint"));
//        but2.setToolTipText("E[n]roll to selected classes");
//        but4.setToolTipText("[Delete] selected classes");
        but5.setToolTipText(s("selectAllHint"));

//        but2.setTextDisplay("Enroll");

        sub.remove(but3);
        sub.remove(strut3);
        sub.remove(but4);
        sub.remove(strut4);
        sub.remove(but2);
        sub.remove(strut2);

        but1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showClassForm(null, true, true);// random last para boolean
            }
        });

        but2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HashMap<String, Class> classes = new HashMap<>();
                Iterator<Entry<String, Class>> iter = mainFrame.getModel().getClasses().entrySet().iterator();
                while (iter.hasNext()) {
                    Class c = iter.next().getValue();
                    if (c.isSelected()) {
                        classes.put(c.getClassId(), c);
                    }
                }
                mainFrame.showEnrollForm(classes, "Class");
            }
        });

        but5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().selectAllClass();
            }
        });
    }
}
