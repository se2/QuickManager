package viewcontroller.line;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import model.Room;
import viewcontroller.Template;
import viewcontroller.listview.RoomListView;

/**
 *
 * @author Dam Linh
 *
 * See ClassLine, it has the same Structure
 */
public class RoomLine extends Box {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private Room room;
    private Color currentBGColor;
    private Color defaultBGColor = Template.getLineBackground();
    private Color selectedBGColor = Template.getTimeTableSelected();
    private Color borderColor = Template.getLineBorderColor();
    private Font f = Template.getFont().deriveFont(15f);
    private Border clickedBorder;
    private Border overedBorder;
    private JLabel id;
    private JLabel name;
    private JLabel capacity;
    private RoomListView parent;

    public RoomLine(RoomListView parent, Room room) {
        super(BoxLayout.X_AXIS);
        this.parent = parent;
        this.room = room;
        setOpaque(true);
        setBackground(defaultBGColor);
        setPreferredSize(new Dimension(600, 30));
        setMaximumSize(new Dimension(1024, 30));

        setCurrentBackgroundColor();
        setBackground(currentBGColor);
        setBorder(BorderFactory.createLineBorder(currentBGColor, 2));

        clickedBorder = new CompoundBorder(
                new MatteBorder(1, 0, 0, 1, currentBGColor),
                new MatteBorder(1, 2, 1, 1, borderColor));
        overedBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, borderColor),
                new MatteBorder(1, 2, 1, 0, currentBGColor));

        id = new JLabel(room.getRoomNumber() + "");
        id.setHorizontalAlignment(SwingConstants.CENTER);
        name = new JLabel(room.getRoomName());
        capacity = new JLabel("" + room.getCapacity());
        capacity.setHorizontalAlignment(SwingConstants.CENTER);
        initLabel(id, 100, 30);
        initLabel(capacity, 100, 30);
        name.setFont(f);
        name.setMaximumSize(new Dimension(1000, 30));

        add(id);
        add(Box.createHorizontalStrut(8));
        add(capacity);
        add(Box.createHorizontalStrut(8));
        add(name);

        addMouseListener(new RoomLineActionListener());
        addMouseListener(new RoomLineViewListener());
    }

    // convenient method
    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setFont(f);
    }

    // convenient method
    private void setCurrentBackgroundColor() {
        if (room.isSelected()) {
            currentBGColor = selectedBGColor;
        } else {
            currentBGColor = defaultBGColor;
        }
        clickedBorder = new CompoundBorder(
                new MatteBorder(1, 0, 0, 1, currentBGColor),
                new MatteBorder(1, 2, 1, 1, borderColor));
        overedBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, borderColor),
                new MatteBorder(1, 2, 1, 0, currentBGColor));
    }

    //this listener is just making the LineTask view more beautiful
    private class RoomLineViewListener extends MouseAdapter {

        private boolean isMouseOvered;

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
            isMouseOvered = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(new LineBorder(currentBGColor, 2));
            isMouseOvered = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                setBorder(clickedBorder);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isMouseOvered) {
                setBorder(overedBorder);
            }
        }
    }

    private class RoomLineActionListener extends MouseAdapter implements ActionListener {

        private int interval = (Integer) Toolkit.getDefaultToolkit().
                getDesktopProperty("awt.multiClickInterval");
        private Timer timer = new Timer(interval, this);
        private MouseEvent lastEvent;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 2 || SwingUtilities.isRightMouseButton(e)) {
                return;
            }
            lastEvent = e;

            if (timer.isRunning()) {
                timer.stop();
                doubleClick(lastEvent);
            } else {
                timer.restart();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            singleClick(lastEvent);
        }

        private void singleClick(MouseEvent e) {
            room.setSelected(!room.isSelected());//toggle select

            setCurrentBackgroundColor();
            setBackground(currentBGColor);
            setBorder(overedBorder);
        }

        private void doubleClick(MouseEvent e) {
            parent.showRoom(room);
        }
    }
    // </editor-fold>
}
