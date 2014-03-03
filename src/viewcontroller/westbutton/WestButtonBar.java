package viewcontroller.westbutton;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import kulcomponent.KulButton;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public abstract class WestButtonBar extends Box {

    protected ResourceBundle lang;
    protected Box sub;
    protected Font f = Template.getFont().deriveFont(15f);
    protected int w = 110;
    protected int h = 30;
    protected KulButton but1;
    protected KulButton but2;
    protected KulButton but3;
    protected Component strut3;
    protected Component strut2;
    protected Component strut4;
    protected Component strut5;
    protected KulButton but4;
    protected KulButton but5;

    public WestButtonBar(MainFrame mainFrame) {
        super(BoxLayout.X_AXIS);
        lang = mainFrame.getModel().getLanguage();

        setPreferredSize(new Dimension(155, 700));

        sub = new Box(BoxLayout.Y_AXIS);
        sub.setAlignmentY(Component.TOP_ALIGNMENT);
        add(Box.createHorizontalStrut(20));
        setFocusable(false);

        add(sub);
        init();
    }

    private void init() {
        but1 = new KulButton(s("add"));
        but2 = new KulButton(s("activate"));
        but3 = new KulButton(s("deactivate"));
        but4 = new KulButton(s("delete"));
        but5 = new KulButton(s("selectAll"));

        but1.setFont(f);
        but2.setFont(f);
        but3.setFont(f);
        but4.setFont(f);
        but5.setFont(f);

        but1.setFocusable(false);
        but2.setFocusable(false);
        but3.setFocusable(false);
        but4.setFocusable(false);
        but5.setFocusable(false);

        but1.setPreferredSize(new Dimension(w, h));
        but1.setMaximumSize(new Dimension(w, h));
        but1.setMinimumSize(new Dimension(w, h));
        but2.setPreferredSize(new Dimension(w, h));
        but2.setMaximumSize(new Dimension(w, h));
        but3.setPreferredSize(new Dimension(w, h));
        but3.setMaximumSize(new Dimension(w, h));
        but4.setPreferredSize(new Dimension(w, h));
        but4.setMaximumSize(new Dimension(w, h));
        but5.setPreferredSize(new Dimension(w, h));
        but5.setMaximumSize(new Dimension(w, h));

        but5.setToolTipText(s("selectAllHint"));

        sub.add(Box.createVerticalStrut(30));
        sub.add(but1);
        sub.add(Box.createVerticalStrut(25));
        sub.add(but2);
        sub.add(strut2 = Box.createVerticalStrut(25));
        sub.add(but3);
        sub.add(strut3 = Box.createVerticalStrut(25));
        sub.add(but4);
        sub.add(strut4 = Box.createVerticalStrut(25));
        sub.add(but5);
        sub.add(strut5 = Box.createVerticalStrut(25));
    }

    protected String s(String key) {
//        System.out.println("WestButton line 93 " + lang.getString(key));
        return lang.getString(key);
    }
}
