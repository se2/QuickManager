package viewcontroller.westbutton;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import kulcomponent.KulButton;
import viewcontroller.MainFrame;

/**
 *
 * @author Dam Linh See ClassButton class, it has the same structure
 */
public class AccountButton extends WestButtonBar {

    private KulButton but6;

    public AccountButton(final MainFrame mainFrame) {
        super(mainFrame);

        but6 = new KulButton(lang.getString("selectAll"));
        but6.setPreferredSize(new Dimension(super.w, super.h));
        but6.setMaximumSize(new Dimension(super.w, super.h));
        but6.setFont(super.f);
        super.sub.add(but6);

        but1.setTextDisplay(lang.getString("addManager"));
        but2.setTextDisplay(lang.getString("addStaff"));
        but3.setTextDisplay(lang.getString("activate"));
        but4.setTextDisplay(lang.getString("deactivate"));
        but5.setTextDisplay(lang.getString("delete"));

        but1.setToolTipText(lang.getString("addManagerHint"));
        but2.setToolTipText(lang.getString("addStaffHint"));
        but3.setToolTipText(lang.getString("activeAccountHint"));
        but4.setToolTipText(lang.getString("deactivateAccountHint"));
        but5.setToolTipText(lang.getString("deleteAccountHint"));
        but6.setToolTipText(lang.getString("selectAllHint"));

        sub.remove(but5);
        sub.remove(strut5);

        but1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showAccountForm(null, true);
            }
        });

        but2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showAccountForm(null, false);
            }
        });

        but6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().selectAllUser();
            }
        });

        but5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().deleteSeletedUser();
            }
        });

        but3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().setIsActiveSelected("User", true);
            }
        });

        but4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.getModel().setIsActiveSelected("User", false);
            }
        });
    }
}
