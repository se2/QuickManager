package viewcontroller.listview;

import viewcontroller.line.RoomLine;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import model.Room;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;

/**
 *
 * @author Dam Linh
 *
 * See ClassListView
 */
public class RoomListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private HashMap<Integer, Room> rooms;
    private JScrollPane scroll;
    private Box allRoomBox;
    private JPanel centerPanel;
//    private JTextField filter;
    private MainFrame mainFrame;

    public RoomListView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        rooms = mainFrame.getModel().getRooms();
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin

//        filter = new JTextField(100);
//        filter.setToolTipText("Filter list");
//        filter.setFont(Template.getFont().deriveFont(16f));
//        filter.setPreferredSize(new Dimension(0, 30));
        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel);
//        add(filter, BorderLayout.NORTH);

        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allRoomBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allRoomBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        centerPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        centerPanel.add(new TitleBar(mainFrame, "room"), BorderLayout.NORTH);

        drawAllRoom();
    }

    private void drawAllRoom() {
        allRoomBox.removeAll();
        Iterator<Integer> iter = rooms.keySet().iterator();
        while (iter.hasNext()) {
            allRoomBox.add(new RoomLine(this, rooms.get(iter.next())));
        }
    }

    public void refresh() {
        drawAllRoom();
        allRoomBox.revalidate();
        allRoomBox.repaint();
    }

    public void showRoom(Room room) {
        mainFrame.showRoomView(room);
    }
    // </editor-fold>
}
