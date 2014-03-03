package viewcontroller.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.Class;
import model.ClassSession;
import model.Room;
import viewcontroller.HasReturn;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TimeBox;
import viewcontroller.TimeTable;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import kulcomponent.KulFrame;
import model.ClassType;

/**
 *
 * @author Dam Linh this class is to display Timetable of a room. Or to let user
 * select time to add session to a class
 */
public class RoomForm extends JPanel {

    //<editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private MainFrame mainFrame;
    private Box roomBar;
    private TimeTable timeTable;
    private JLabel roomLabel;
    private KulComboBox<String> rooms;
    private HasReturn hasReturn;
    private KulFrame parentFrame;
    private Class currentClass;
    private KulButton addSession;
    private KulButton clear;
    private KulButton changeMinButton;
    private boolean is45;
    private KulFrame prevFrame;
    private boolean isUsing = false;
    private ClassType classType;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constructor">
    public RoomForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Template.getBackground());
        language = mainFrame.getModel().getLanguage();

        timeTable = new TimeTable(this.mainFrame, false);
        add(timeTable, BorderLayout.CENTER);

        drawButtonBar();
        addButtonListener();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawButtonBar">
    private void drawButtonBar() {
        roomBar = new Box(BoxLayout.X_AXIS);
        roomBar.setPreferredSize(new Dimension(2000, 45));
        add(roomBar, BorderLayout.NORTH);

        addSession = new KulButton(getString("addSession"));
        addSession.setFont(Template.getFont().deriveFont(16f));
        setFixedSize(addSession, 100, 30);

        changeMinButton = new KulButton(getString("45mSession"));
        changeMinButton.setFont(Template.getFont().deriveFont(16));
        setFixedSize(changeMinButton, 100, 30);

        clear = new KulButton(getString("clearSession"));
        clear.setFont(Template.getFont().deriveFont(16));
        setFixedSize(clear, 100, 30);

        // <editor-fold defaultstate="collapsed" desc="room">
        roomLabel = new JLabel(getString("room"));
        roomLabel.setFont(Template.getFont().deriveFont(1, 16f));
        rooms = new KulComboBox<>(new String[]{getString("piano1"), getString("piano2"),
                    getString("piano3"), getString("guitarViolin"), getString("painting"),
                    getString("vocal"), getString("organ"), getString("dance")});
        setFixedSize(rooms, 200, 30);
        rooms.setFont(Template.getFont().deriveFont(16f));
        rooms.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateTimeTable();
                }
            }
        });
        // </editor-fold>

        roomBar.add(Box.createHorizontalStrut(20));
        roomBar.add(addSession);
        roomBar.add(Box.createHorizontalGlue());
        roomBar.add(roomLabel);
        roomBar.add(Box.createHorizontalStrut(10));
        roomBar.add(rooms);
        roomBar.add(Box.createHorizontalGlue());
        roomBar.add(changeMinButton);
        roomBar.add(Box.createHorizontalStrut(20));
        roomBar.add(clear);
        roomBar.add(Box.createHorizontalStrut(20));
        addSession.setVisible(false);
        changeMinButton.setVisible(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="convenient method">
    private void setFixedSize(JComponent c, int width, int height) {
        c.setPreferredSize(new Dimension(width, height));
        c.setMaximumSize(new Dimension(width, height));
        c.setMinimumSize(new Dimension(width, height));
    }
    // </editor-fold>

    /*
     * display warning message if this view is being open then do nothing to
     * avoid conflit. Only one timetable can be displayed at anytime
     */
    // <editor-fold defaultstate="collapsed" desc="checkIsAvalable">
//    private boolean checkIsAvailable(KulFrame parentFrame) {
//        if (isUsing) {
//            System.out.println("showing dialog this window isbeing used RoomForm");
////            JOptionPane.showMessageDialog(parentFrame,
////                    "The window is being opeasdsdssning", "Caution", JOptionPane.INFORMATION_MESSAGE);
////            parentFrame.closeWindow(true);
//            return false;
//        } else {
//            return true;
//        }
//    }
    // </editor-fold>

    /*
     * The frame that invokes this timetable frame will be disable to avoid conflict.
     * Therefore, when this frame closes, the prevFrame will be enable again
     */
    // <editor-fold defaultstate="collapsed" desc="ParentFrameListener to close">
    private class ParentFrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            isUsing = false;
            if (prevFrame != null) {
                prevFrame.setEnabled(true);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="add button listeners">
    private void addButtonListener() {
        addSession.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    addSession();
                }
            }
        });

        changeMinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (is45) {
                    changeMinButton.setTextDisplay(getString("45mSession"));
                    is45 = false;
                } else {
                    changeMinButton.setTextDisplay(getString("60mSession"));
                    is45 = true;
                }
                timeTable.setIs45(is45);
            }
        });

        clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timeTable.clearSession();
            }
        });
    }
    // </editor-fold>

    /*
     * invoked when user wants to view timetable only(cannot add/edit session of a class)
     * this method repaints all the cell to fit the new room(week)
     */
    //<editor-fold defaultstate="collapsed" desc="setRoom invoked when user view room only">
    public boolean setRoom(Room room, KulFrame parentFrame) {
        if (isUsing) {
            JOptionPane.showMessageDialog(parentFrame,
                    getString("timetableBeingUsed"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        isUsing = true;
        prevFrame = null;
        parentFrame.addWindowListener(new ParentFrameListener());

        this.parentFrame = parentFrame;
        currentClass = null;
        hasReturn = null;
        addSession.setVisible(false);
        changeMinButton.setVisible(false);

        if (rooms.getSelectedIndex() == room.getRoomNumber() - 1) {
            rooms.setSelectedIndex(room.getRoomNumber() - 2);//force re select
        }
        this.rooms.setSelectedIndex(room.getRoomNumber() - 1);
        updateTimeTable();
        return true;
    }
    //</editor-fold>

    /*
     * invoked when user wants to edit or add sessions of a class
     * this method repaints all the cell to fit the new room(week)
     * this method will find all sessions associated with this class AND room,
     * then set the cell to selected
     */
    //<editor-fold defaultstate="collapsed" desc="setSession, invoked when user wants to edit sessions of a class">
    public boolean setSession(HasReturn hasReturn, ClassSession session, Class currentClass,
            KulFrame parentFrame, KulFrame prevFrame, ClassType ct) {
        this.prevFrame = prevFrame;
        this.classType = ct;
        if (isUsing) {
            JOptionPane.showMessageDialog(parentFrame,
                    getString("timetableBeingUsed"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (ct.isBeingUsed()) {
            JOptionPane.showMessageDialog(parentFrame,
                    getString("classTypeBeingUsedContent"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        isUsing = true;
        parentFrame.addWindowListener(new ParentFrameListener());

        this.hasReturn = hasReturn;
        this.parentFrame = parentFrame;
        this.currentClass = currentClass;
        addSession.setVisible(true);
        changeMinButton.setVisible(true);
        switch (ct.getLessonPerWeek()) {
            case 0:
                timeTable.setMaxSession(2);
                break;
            case 1:
                timeTable.setMaxSession(1);
                break;
            case 2:
                timeTable.setMaxSession(2);
                break;
        }

        if (currentClass != null) {
//                && session == null) {
            timeTable.setCurrentSession(timeTable.getMaxSession());
            switch (ct.getType()) {
                case ClassType.SINGLE:
                    rooms.setSelectedIndex(1);//force the rooms ComboBox to re select
                    rooms.setSelectedIndex(0);
                    break;
                case ClassType.DUAL:
                    rooms.setSelectedIndex(0);//force the rooms ComboBox to re select
                    rooms.setSelectedIndex(3);
                    break;
                default:
                    rooms.setSelectedIndex(6);//force the rooms ComboBox to re select
                    rooms.setSelectedIndex(5);
            }
        }
        updateTimeTable();
        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateTimeTable">
    private void updateTimeTable() {
        timeTable.setRoomId(rooms.getSelectedIndex() + 1);
        timeTable.setClass(currentClass);
    }
    //</editor-fold>

    /*
     * this method is invoked in order transfer sessions to the one that calls
     * this timetable(classForm)
     */
    // <editor-fold defaultstate="collapsed" desc="add session, invoked to transfer sessions to HasReturn">
    private void addSession() {
        ClassType ct = mainFrame.getModel().getClassType(currentClass);
//        String major = mainFrame.getModel().getClassType(currentClass).getSkill();

        switch (ct.getType()) {
            case ClassType.SINGLE:
                if (rooms.getSelectedIndex() + 1 > 4) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("individualAndDualRoomOnlyContent"), "Caution", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                break;
            case ClassType.DUAL:
                if (rooms.getSelectedIndex() + 1 != 4) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("dualRoomOnlyContent"), "Caution", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                break;
            default:
                if (rooms.getSelectedIndex() + 1 <= 4) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("groupRoomOnlyContent"), "Caution", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
        }

        TimeBox[][] timeBoxes = timeTable.getTimeBoxes();
        HashMap<String, ClassSession> sessionsToSend = new HashMap<>();
        HashMap<Integer, Integer> times = new HashMap<>();
        for (int i = 0; i < timeBoxes.length; i++) {
            for (int j = 0; j < timeBoxes[i].length; j++) {
                if (timeBoxes[i][j].isIsSelected()) {
                    int time = 44 * i + j;
                    times.put(time, time);
                    if (j == timeBoxes[i].length - 1) {
                        //packTimesToSession
                        ClassSession session = new ClassSession(currentClass.getClassId(),
                                rooms.getSelectedIndex() + 1, times, timeBoxes[i][j].isIs45());
                        sessionsToSend.put(session.getClassSessionId(), session);

                        times = new HashMap<>();
                    }
                } else {
                    if (times.isEmpty()) {
                        continue;
                    }
                    //packTimesToSession
                    ClassSession session = new ClassSession(currentClass.getClassId(),
                            rooms.getSelectedIndex() + 1, times, timeBoxes[i][j - 1].isIs45());
                    sessionsToSend.put(session.getClassSessionId(), session);
//                    System.out.println("timeBox[" + i + "][" + (j - 1) + "].is45 = " + timeBoxes[i][j - 1].isIs45());

                    times = new HashMap<>();
                }
            }
        }
        if (sessionsToSend.isEmpty()) {
            sessionsToSend.put(rooms.getSelectedIndex() + 1 + "", null);
        } else {
            Iterator<Entry<String, ClassSession>> iterSesToSend = sessionsToSend.entrySet().iterator();
            boolean is45 = iterSesToSend.next().getValue().is45();
//            System.out.println("RoomForn line 372: is45 out of loop: " + is45);
            while (iterSesToSend.hasNext()) {
                ClassSession ses = iterSesToSend.next().getValue();
//                System.out.println("RoomForm line 375 ses.is45() = " + ses.is45());
                if (ses.is45() != is45) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("sessionShouldBeSame"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
        switch (classType.getLessonPerWeek()) {
            case 0:
                if (sessionsToSend.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("sessionIsNotEnoughContent"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                break;
            case 1:
                if (sessionsToSend.size() < 1) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("sessionIsNotEnoughContent"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                break;
            case 2:
                if (sessionsToSend.size() < 2) {
                    JOptionPane.showMessageDialog(parentFrame,
                            getString("sessionIsNotEnoughContent"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                break;
        }

        hasReturn.setReturnObj(sessionsToSend);
        parentFrame.closeWindow(false);
    }
    // </editor-fold>

    private String getString(String key) {
        return language.getString(key);
    }
}
