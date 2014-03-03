package viewcontroller.westbutton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Staff;
import viewcontroller.MainFrame;

/**
 *
 * @author Dam Linh
 */
public class InvoiceButton extends WestButtonBar {

    public InvoiceButton(final MainFrame mainFrame) {
        super(mainFrame);

//        but2.setToolTipText("Mark as Paid");
//        but3.setToolTipText("Mark as Unpaid");
//        but2.setTextDisplay("Mard Unpaid");
//        but3.setTextDisplay("Mark Paid");
        but1.setTextDisplay(s("overdueButton"));
        but1.setFont(f.deriveFont(13f));
        but1.setToolTipText(s("overdueButtonHint"));

        if (mainFrame.getUser() instanceof Staff) {
            sub.remove(but3);
            sub.remove(strut3);
        }
        sub.remove(but4);
        sub.remove(strut4);
        sub.remove(but2);
        sub.remove(strut2);
        sub.remove(but3);
        sub.remove(strut3);
        sub.remove(but5);
        sub.remove(strut5);

        but1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showUnpaidStudentClass();
            }
        });
    }
}
