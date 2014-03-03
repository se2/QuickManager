package viewcontroller.listview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import model.User;
import viewcontroller.KeyBindingSetter;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;
import viewcontroller.line.AccountLine;

/**
 *
 * @author Dam Linh
 *
 * See ClassListView.
 */
public class AccountListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private HashMap<String, User> users;
    private JScrollPane scroll;
    private Box allUserBox;
    private JPanel centerPanel;
    private JTextField filter;
    private MainFrame mainFrame;
    private boolean isSmall;

    public AccountListView(MainFrame mainFrame, boolean isSmall) {
        this.mainFrame = mainFrame;
        this.isSmall = isSmall;
        users = mainFrame.getModel().getUsers();
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin

//        filter = new JTextField(100);
//        filter.setToolTipText("Filter list");
//        filter.setFont(Template.getFont().deriveFont(16f));
//        filter.setPreferredSize(new Dimension(0, 30));
        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel);
//        add(filter, BorderLayout.NORTH);

        if (isSmall) {
            KeyBindingSetter.setWestButtonKeyBinding(mainFrame, this, "User");
        }

        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allUserBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allUserBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        centerPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        centerPanel.add(new TitleBar(mainFrame, "account"), BorderLayout.NORTH);

        drawAllUser();
    }

    private void drawAllUser() {
        allUserBox.removeAll();
        Set keySet = users.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            User user = users.get(iter.next());
            allUserBox.add(new AccountLine(mainFrame, user));
        }
    }

    public void refresh() {
        drawAllUser();
        allUserBox.revalidate();
        allUserBox.repaint();
    }
    // </editor-fold>
}
