package viewcontroller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import model.Class;

/**
 *
 * @author Dam Linh each object of this class is a cell of the timetable
 */
public class TimeBox extends JLabel {

    private boolean isSelected;
    private Color currentColor;
    private Color occupiedBG = Template.getTimeTableOccupied();
    private Color defaultBGColor = Template.getBackground();
    private Border defaultBorder;
    private Class thisClass;
    private MainFrame mainFrame;
    private boolean needSouthDivider = false;
    private int i;
    private int j;
    private TimeTable timeTable;
    private boolean is45;

    public TimeBox(MainFrame mainFrame, TimeTable timeTable, int i, int j) {
        super();
        this.timeTable = timeTable;
        this.mainFrame = mainFrame;
        this.i = i;
        this.j = j;
        defaultBorder = new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, Template.getButtonBGMouseClicked().darker().darker()),
                new MatteBorder(0, 0, 1, 0, Template.getTitleBar()));

        setOpaque(true);
        setFont(Template.getFont().deriveFont(14f));
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new TimeBoxMouseListener());
    }

    public Class getThisClass() {
        return thisClass;
    }

    public void setThisClass(Class thisClass) {
        this.thisClass = thisClass;

        if (thisClass != null) {
            isSelected = false;
            currentColor = occupiedBG;
            setBorder(new MatteBorder(0, 0, 0, 1, Template.getButtonBGMouseClicked().darker().darker()));
        } else {
            currentColor = defaultBGColor;
            setBorder(defaultBorder);
        }
        setBackground(currentColor);
    }

    public void setIsSelected(boolean isSelected, boolean is45) {
        this.isSelected = isSelected;

        if (isSelected) {
            this.is45 = is45;
            currentColor = Template.getTimeTableSelected();
            setBorder(new MatteBorder(0, 0, 0, 1, Template.getButtonBGMouseClicked().darker().darker()));
        } else {
            currentColor = defaultBGColor;
            setBorder(defaultBorder);
        }
        setBackground(currentColor);
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public boolean isIs45() {
        return is45;
    }

    public void setIs45(boolean is45) {
        this.is45 = is45;
    }

    public Color getOccupiedBG() {
        return occupiedBG;
    }

    public void setOccupiedBG(Color occupiedBG) {
        this.occupiedBG = occupiedBG;
    }

    public boolean isNeedSouthDivider() {
        return needSouthDivider;
    }

    public Color getDefaultBGColor() {
        return defaultBGColor;
    }

    public void setDefaultBGColor(Color defaultBGColor) {
        this.defaultBGColor = defaultBGColor;
    }

    public void setNeedSouthDivider(boolean needSouthDivider) {
        this.needSouthDivider = needSouthDivider;
        if (needSouthDivider) {
            defaultBorder = new MatteBorder(0, 0, 1, 1, Template.getButtonBGMouseClicked().darker().darker());
        }
    }

    private class TimeBoxMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && !timeTable.isIsSmall()) {
                timeTable.mouseClicked(i, j);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            timeTable.mouseEntered(i, j);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            timeTable.mouseExited(i, j);
        }
    }

    public void mouseEntered() {
        if (!isSelected && thisClass == null) {
            currentColor = Template.getTimeTableMouseOvered();
            setBackground(currentColor);
        }
    }

    public void mouseExited() {
        if (!isSelected && thisClass == null) {
            currentColor = defaultBGColor;
            setBackground(currentColor);
        }
    }

    public void mouseClicked() {
        is45 = timeTable.isIs45();
        if (thisClass == null) {
            setIsSelected(!isSelected, is45);
//            if (e.isShiftDown() && timeTable.firstClickIsClicked) {
//                timeTable.secondClick(i, j);
//            } else if (isSelected) {
//                timeTable.firstClick(i, j);
//            }
//            System.out.println("timeBox[" + i + "] [" + j + "].is45 = " + is45);
        } else {
            mainFrame.showClassForm(thisClass, false, false);
        }
    }
}
