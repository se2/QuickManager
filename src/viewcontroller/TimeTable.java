package viewcontroller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import kulcomponent.KulAnimator;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import model.ClassSession;
import model.Student;
import model.StudentClass;
import model.Teacher;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Dam Linh
 */
public class TimeTable extends JPanel {

    //<editor-fold defaultstate="collapsed" desc="variable declaration">
    private MainFrame mainFrame;
    private Color currentTimeBoxBG = Template.getTimeTableOccupied();
    private byte currentTimeBoxBGIndex = -1;
    private final Color paidColor = Template.getPaidColor();
    private final Color unPaidColor = Template.getUnPaidColor();
    private Box dayBarWeekDay;
    private Box weekDayBox;
    private Box navBar;
    private Box westBoxWeekDay;
    private Box westTimeBoxWeekDay;
    private JPanel weekDayPanel;
    private KulButton thisWeekBut;
    private KulButton next;
    private KulButton prev;
    private KulComboBox<Integer> year;
    private JLabel mo;
    private JLabel tu;
    private JLabel we;
    private JLabel th;
    private JLabel fr;
    private JLabel sa;
    private JLabel su;
    private Font f = Template.getFont().deriveFont(14f);
    private static final int WEST_WIDTH = 80;
    private static final int TITLE_DAY_H = 20;
    private static final int TOGGLE_BUTTON_WIDTH = 27;
    private static final int WEEK_DAY_WIDTH_CELL_SMALL = 83;
    private static final int WEEK_DAY_WIDTH_CELL_BIG = 182;
    private static final int WEEK_END_CELL_WIDTH = 208;
    private static int WEEK_END_WIDTH;
    private static final int TIMETABLE_HEIGHT = 629;
    private static final int WEEK_DAY_WIDTH_SMALL = WEEK_DAY_WIDTH_CELL_SMALL * 5 + WEST_WIDTH;
    private static final int WEEK_DAY_WIDTH_BIG = WEEK_DAY_WIDTH_CELL_BIG * 5 + WEST_WIDTH;
    private final int WEEK_DAY_CELL_HEIGHT = (TIMETABLE_HEIGHT - TITLE_DAY_H) / 16;
    private final int WEEK_END_CELL_HEIGHT = (TIMETABLE_HEIGHT - TITLE_DAY_H) / 44 + 1;
    private int weekDayCellWidth;
    private int weekDayWidth;
    private TimeBox[][] timeBoxes;
    private Box[] colDays;
    private int roomId = -1;
    private model.Class currentClass;
    private LocalDate now = new LocalDate();
    private LocalDate currentDay = now.withDayOfWeek(DateTimeConstants.MONDAY);
    private KulButton toggle = new KulButton("<");
    private boolean isUserFireEvent;
    boolean firstClickIsClicked;
    private int firstClickI;
    private int firstClickJ;
    private int secondClickI;
    private int secondClickJ;
    private Student currentStudent;
    private Teacher currentTeacher;
    private boolean isSmall = false;
    private Box weekEndBox;
    private Box westBoxWeekEnd;
    private Box westTimeBoxWeekEnd;
    private JPanel weekEndPanelSuper;
    private JPanel weekEndPanel;
    private JPanel centerPanel = new JPanel(null);
    private Box dayBarWeekEnd;
    private boolean isWeekEndShowing = false;
    private Timer timer;
    private boolean is45;
    private int d = 20; //used for animation
    private int maxSession = 0;
    private int currentSession = 0;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constructor">
    public TimeTable(MainFrame mainFrame, boolean isSmall) {
        this.isSmall = isSmall;
        if (isSmall) {
            this.weekDayCellWidth = WEEK_DAY_WIDTH_CELL_SMALL;
            this.weekDayWidth = WEEK_DAY_WIDTH_SMALL;
        } else {
            this.weekDayCellWidth = WEEK_DAY_WIDTH_CELL_BIG;
            this.weekDayWidth = WEEK_DAY_WIDTH_BIG;
        }
        WEEK_END_WIDTH = WEEK_END_CELL_WIDTH * 2 + WEST_WIDTH + TOGGLE_BUTTON_WIDTH;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        add(centerPanel);
        centerPanel.setBackground(Template.getBackground());

        weekDayPanel = new JPanel(new BorderLayout());
        weekDayPanel.setBackground(Template.getBackground());
        weekDayPanel.setBounds(0, 0, weekDayWidth, TIMETABLE_HEIGHT);
        centerPanel.add(weekDayPanel);

        weekEndPanelSuper = new JPanel(new BorderLayout());
        weekEndPanelSuper.setBackground(Template.getBackground());
        weekEndPanelSuper.setBounds(weekDayWidth, 0, WEEK_END_WIDTH, TIMETABLE_HEIGHT);
        centerPanel.add(weekEndPanelSuper);

        weekEndPanel = new JPanel(new BorderLayout());
        weekEndPanel.setBackground(Template.getBackground());
        weekEndPanelSuper.add(weekEndPanel, BorderLayout.CENTER);

        drawNavBar();
        drawDayBar();
        drawWestBoxWeekDay();
        drawMain();
        drawToggleButton();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toggle weekend">
    private void drawToggleButton() {
        setFixedSize(toggle, TOGGLE_BUTTON_WIDTH, TIMETABLE_HEIGHT);
        weekEndPanelSuper.add(toggle, BorderLayout.WEST);
        toggle.setFont(f.deriveFont(20f));
        toggle.setForeground(Template.getBorderContrast());
        toggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleWeekend();
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleWeekend();
            }
        });
    }

    private void toggleWeekend() {
        int h = TIMETABLE_HEIGHT;

        if (!isWeekEndShowing) {
            if (!isSmall) {
                scaleHorizontal(WEEK_DAY_WIDTH_SMALL);
            } else {
                scaleHorizontal(0);
            }
            new KulAnimator(centerPanel, weekEndPanelSuper, weekDayWidth,
                    weekDayWidth - WEEK_END_WIDTH + 1 + TOGGLE_BUTTON_WIDTH, 0, 0,
                    WEEK_END_WIDTH, h).slideHorizontal();

            isWeekEndShowing = true;
            toggle.setTextDisplay(">");
        } else {
            if (!isSmall) {
                scaleHorizontal(WEEK_DAY_WIDTH_BIG);
                new KulAnimator(centerPanel, weekEndPanelSuper, WEEK_DAY_WIDTH_SMALL,
                        WEEK_DAY_WIDTH_BIG, 0, 0, WEEK_END_WIDTH, h).slideHorizontal();
            } else {
                scaleHorizontal(WEEK_DAY_WIDTH_SMALL);
                new KulAnimator(centerPanel, weekEndPanelSuper, 0,
                        WEEK_DAY_WIDTH_SMALL, 0, 0, WEEK_END_WIDTH, h).slideHorizontal();
            }
            isWeekEndShowing = false;
            toggle.setTextDisplay("<");
        }
    }

    private void scaleHorizontal(final int width2) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (weekDayWidth < width2) {
            d = 30;
        } else {
            d = -30;
        }
        timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Math.abs(weekDayWidth - width2) < Math.abs(d)) {// when animation almost finishes
                    //then do the last animation
                    weekDayPanel.setBounds(0, 0, width2, TIMETABLE_HEIGHT);
                    timer.stop();
                    if (isWeekEndShowing) {
                        if (isSmall) {
                            weekDayCellWidth = 0;
                        } else {
                            weekDayCellWidth = WEEK_DAY_WIDTH_CELL_SMALL;
                        }
                    } else {
                        if (isSmall) {
                            weekDayCellWidth = WEEK_DAY_WIDTH_CELL_SMALL;
                        } else {
                            weekDayCellWidth = WEEK_DAY_WIDTH_CELL_BIG;
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 16; j++) {
                            setFixedSize(timeBoxes[i][j], weekDayCellWidth, WEEK_DAY_CELL_HEIGHT);
                            timeBoxes[i][j].repaint();
                        }
                        colDays[i].revalidate();
                        colDays[i].repaint();
                    }
                    updateDayBarSize();
                    dayBarWeekDay.revalidate();
                    dayBarWeekDay.repaint();

                } else {
                    weekDayWidth += d;
                    weekDayPanel.setBounds(0, 0, weekDayWidth, TIMETABLE_HEIGHT);

                    weekDayCellWidth = Math.round(weekDayWidth / 5f);
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 16; j++) {
                            setFixedSize(timeBoxes[i][j], weekDayCellWidth, WEEK_DAY_CELL_HEIGHT);
                            timeBoxes[i][j].repaint();
                        }
                        colDays[i].revalidate();
                        colDays[i].repaint();
                    }
                }
                centerPanel.revalidate();
                centerPanel.repaint();

                updateDayBarSize();
                dayBarWeekDay.revalidate();
                dayBarWeekDay.repaint();
            }
        });
        timer.start();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawDayBar">
    private void drawDayBar() {
        dayBarWeekEnd = new Box(BoxLayout.X_AXIS);
        dayBarWeekEnd.setOpaque(true);
        dayBarWeekEnd.setBackground(Template.getTitleBar());
        weekEndPanel.add(dayBarWeekEnd, BorderLayout.NORTH);
        Font font = f.deriveFont(12f);

        dayBarWeekDay = new Box(BoxLayout.X_AXIS);
        dayBarWeekDay.setOpaque(true);
        dayBarWeekDay.setBackground(Template.getTitleBar());
        weekDayPanel.add(dayBarWeekDay, BorderLayout.NORTH);

        mo = new JLabel();
        mo.setFont(font);
        mo.setHorizontalAlignment(SwingConstants.CENTER);

        tu = new JLabel();
        tu.setFont(font);
        tu.setHorizontalAlignment(SwingConstants.CENTER);

        we = new JLabel();
        we.setFont(font);
        we.setHorizontalAlignment(SwingConstants.CENTER);

        th = new JLabel();
        th.setFont(font);
        th.setHorizontalAlignment(SwingConstants.CENTER);

        fr = new JLabel();
        fr.setFont(font);
        fr.setHorizontalAlignment(SwingConstants.CENTER);

        sa = new JLabel();
        sa.setFont(font);
        sa.setHorizontalAlignment(SwingConstants.CENTER);

        su = new JLabel();
        su.setFont(font);
        su.setHorizontalAlignment(SwingConstants.CENTER);

        updateDayBar();
        updateDayBarSize();

        JLabel emptyLabel = new JLabel(" ");
        emptyLabel.setOpaque(true);
        emptyLabel.setBackground(Template.getBackground());
        setFixedSize(emptyLabel, WEST_WIDTH, TITLE_DAY_H);

        dayBarWeekDay.add(emptyLabel);
        dayBarWeekDay.add(mo);
        dayBarWeekDay.add(tu);
        dayBarWeekDay.add(we);
        dayBarWeekDay.add(th);
        dayBarWeekDay.add(fr);

        JLabel emptyLabel1 = new JLabel(" ");
        emptyLabel1.setOpaque(true);
        emptyLabel1.setBackground(Template.getBackground());
        setFixedSize(emptyLabel1, WEST_WIDTH, TITLE_DAY_H);

        dayBarWeekEnd.add(sa);
        dayBarWeekEnd.add(su);
        dayBarWeekEnd.add(emptyLabel1);
    }

    private void updateDayBarSize() {
        setFixedSize(mo, weekDayCellWidth, TITLE_DAY_H);
        setFixedSize(tu, weekDayCellWidth, TITLE_DAY_H);
        setFixedSize(we, weekDayCellWidth, TITLE_DAY_H);
        setFixedSize(th, weekDayCellWidth, TITLE_DAY_H);
        setFixedSize(fr, weekDayCellWidth, TITLE_DAY_H);
        setFixedSize(sa, WEEK_END_CELL_WIDTH, TITLE_DAY_H);
        setFixedSize(su, WEEK_END_CELL_WIDTH, TITLE_DAY_H);
    }

    private void updateDayBar() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM");

        String moString = currentDay.withDayOfWeek(DateTimeConstants.MONDAY).toString(fmt);
        String tuString = currentDay.withDayOfWeek(DateTimeConstants.TUESDAY).toString(fmt);
        String weString = currentDay.withDayOfWeek(DateTimeConstants.WEDNESDAY).toString(fmt);
        String thString = currentDay.withDayOfWeek(DateTimeConstants.THURSDAY).toString(fmt);
        String frString = currentDay.withDayOfWeek(DateTimeConstants.FRIDAY).toString(fmt);
        String saString = currentDay.withDayOfWeek(DateTimeConstants.SATURDAY).toString(fmt);
        String suString = currentDay.withDayOfWeek(DateTimeConstants.SUNDAY).toString(fmt);

        mo.setText("<html><body style='text-align:center'>Mon-" + moString + "</body></html>");
        tu.setText("<html><body style='text-align:center'>Tue-" + tuString + "</body></html>");
        we.setText("<html><body style='text-align:center'>Wed-" + weString + "</body></html>");
        th.setText("<html><body style='text-align:center'>Thu-" + thString + "</body></html>");
        fr.setText("<html><body style='text-align:center'>Fri-" + frString + "</body></html>");
        sa.setText("<html><body style='text-align:center'>Sat-" + saString + "</body></html>");
        su.setText("<html><body style='text-align:center'>Sun-" + suString + "</body></html>");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="draw West">
    private void drawWestBoxWeekDay() {
        westBoxWeekDay = new Box(BoxLayout.Y_AXIS);
        westBoxWeekDay.setOpaque(true);
        westBoxWeekDay.setBackground(Template.getBackground());
        weekDayPanel.add(westBoxWeekDay, BorderLayout.WEST);

        westBoxWeekEnd = new Box(BoxLayout.Y_AXIS);
        westBoxWeekEnd.setOpaque(true);
        westBoxWeekEnd.setBackground(Template.getBackground());
        weekEndPanel.add(westBoxWeekEnd, BorderLayout.EAST);

        drawWestTimePanelWeekDay();
    }

    private void drawWestTimePanelWeekDay() {
        westTimeBoxWeekDay = new Box(BoxLayout.Y_AXIS);
        westTimeBoxWeekDay.setBackground(Template.getBackground());
        westBoxWeekDay.add(westTimeBoxWeekDay);

        westTimeBoxWeekEnd = new Box(BoxLayout.Y_AXIS);
        westTimeBoxWeekEnd.setBackground(Template.getBackground());
        westBoxWeekEnd.add(westTimeBoxWeekEnd);

        //<editor-fold defaultstate="collapsed" desc="add weekDay to WestTimeBox">
        for (int i = 0; i < 16; i++) {
            int hour1 = i / 4 + 16;
            String min1;
            int hour2;
            String min2;

            if (i % 4 == 0) {
                min1 = "00";
                hour2 = hour1;
                min2 = "15";
            } else if (i % 4 == 1) {
                min1 = "15";
                hour2 = hour1;
                min2 = "30";
            } else if (i % 4 == 2) {
                min1 = "30";
                hour2 = hour1;
                min2 = "45";
            } else {
                min1 = "45";
                hour2 = hour1 + 1;
                min2 = "00";
            }
            String time = String.format("%02d:%s - %02d:%s", hour1, min1, hour2, min2);

            JLabel timeLabel = new JLabel(time);
            if (i % 4 == 3) {
                timeLabel.setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 1, 0, Template.getButtonBGMouseClicked().darker().darker()),
                        new MatteBorder(0, 0, 0, 1, Template.getTitleBar())));
            } else {
                timeLabel.setBorder(new MatteBorder(0, 0, 1, 1, Template.getTitleBar()));
            }
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setFont(f.deriveFont(12f));
            setFixedSize(timeLabel, WEST_WIDTH, WEEK_DAY_CELL_HEIGHT);
            westTimeBoxWeekDay.add(timeLabel);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="add weekEnd to WestTimeBox">
        for (int i = 0; i < 11; i++) {
            int hour1 = i + 9;
            String min1 = "00";
            int hour2 = hour1 + 1;
            String time = String.format("%02d:%s - %02d:%s", hour1, min1, hour2, min1);

            JLabel timeLabel = new JLabel(time);
            timeLabel.setBorder(new MatteBorder(0, 0, 1, 0, Template.getButtonBGMouseClicked().darker().darker()));
            timeLabel.setOpaque(true);
            timeLabel.setBackground(Template.getButtonDefaultColor().brighter());
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setFont(f.deriveFont(12f));
            setFixedSize(timeLabel, WEST_WIDTH, WEEK_END_CELL_HEIGHT * 4 - 2);
            westTimeBoxWeekEnd.add(timeLabel);
        }
        //</editor-fold>
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawMain">
    private void drawMain() {
        weekDayBox = new Box(BoxLayout.X_AXIS);
        weekEndBox = new Box(BoxLayout.X_AXIS);

        //initialize colDays and times
        colDays = new Box[7];
        timeBoxes = new TimeBox[7][44];
        for (int i = 0; i < 7; i++) {
            if (i < 5) {
                colDays[i] = new Box(BoxLayout.Y_AXIS);
                weekDayBox.add(colDays[i]);
            } else {
                colDays[i] = new Box(BoxLayout.Y_AXIS);
                weekEndBox.add(colDays[i]);
            }
            for (int j = 0; j < timeBoxes[i].length; j++) {
                timeBoxes[i][j] = new TimeBox(mainFrame, this, i, j);

                if (i < 5 && j < 16) {
                    setFixedSize(timeBoxes[i][j], weekDayCellWidth, WEEK_DAY_CELL_HEIGHT);
                    colDays[i].add(timeBoxes[i][j]);
                } else if (i >= 5) {
                    if (j % 2 == 0) {
                        setFixedSize(timeBoxes[i][j], WEEK_END_CELL_WIDTH, WEEK_END_CELL_HEIGHT);
                    } else {
                        setFixedSize(timeBoxes[i][j], WEEK_END_CELL_WIDTH, WEEK_END_CELL_HEIGHT - 1);
                    }
                    colDays[i].add(timeBoxes[i][j]);
                    timeBoxes[i][j].setDefaultBGColor(Template.getButtonDefaultColor().brighter().brighter());
                }
                if (j % 4 == 3) {
                    timeBoxes[i][j].setNeedSouthDivider(true);
                }
            }
            if (i >= 5) {
                colDays[i].add(Box.createVerticalGlue());
            }
        }

        weekDayPanel.add(weekDayBox);
        weekEndPanel.add(weekEndBox);

        drawTimeBoxes();
        centerPanel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    nextWeek();
                } else {
                    prevWeek();
                }
            }
        });
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawNavBar">
    private void drawNavBar() {
        navBar = new Box(BoxLayout.X_AXIS);
        navBar.setOpaque(true);
        navBar.setBackground(Template.getBackground());
        add(navBar, BorderLayout.SOUTH);
        navBar.setPreferredSize(new Dimension(2000, 35));

        thisWeekBut = new KulButton("This week");
        thisWeekBut.setFont(Template.getFontAlter().deriveFont(16f));
        setFixedSize(thisWeekBut, 90, 26);
        thisWeekBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentDay = now.withDayOfWeek(DateTimeConstants.MONDAY);
                drawTimeBoxes();
                updateDayBar();
                repaint();
            }
        });

        // <editor-fold defaultstate="collapsed" desc="year">
        year = new KulComboBox<>();
        for (int i = 2000; i < 2200; i++) {
            year.addItem(i);
        }
        setFixedSize(year, 75, 25);
        year.setFont(f.deriveFont(0, 16));
        year.setSelectedIndex(13);
        year.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (isUserFireEvent) {
                        currentDay = currentDay.withYear((Integer) year.getSelectedItem());
                        updateDayBar();
                        drawTimeBoxes();
                        weekDayBox.repaint();
                    }
                    isUserFireEvent = true;
                }
            }
        });
        // </editor-fold>

        next = new KulButton(">");
        prev = new KulButton("<");
        next.setForeground(Template.getBorderContrast());
        prev.setForeground(Template.getBorderContrast());
        setFixedSize(next, 70, 26);
        next.setFont(f.deriveFont(0, 30f));
        setFixedSize(prev, 70, 26);
        prev.setFont(f.deriveFont(0, 30f));

        next.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nextWeek();
            }
        });

        prev.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                prevWeek();
            }
        });

        navBar.add(Box.createHorizontalStrut(WEST_WIDTH + 10));
        navBar.add(prev);
        navBar.add(Box.createHorizontalGlue());
        navBar.add(thisWeekBut);
        navBar.add(Box.createHorizontalStrut(20));
        navBar.add(year);
        navBar.add(Box.createHorizontalGlue());
        navBar.add(next);
        navBar.add(Box.createHorizontalStrut(10));
    }

    private void nextWeek() {
        //show message if user goes beyond month: December, year: 2200
        if (currentDay.getWeekOfWeekyear() == currentDay.weekOfWeekyear().getMaximumValue() && currentDay.getYear() == 2200) {
            JOptionPane.showMessageDialog(this,
                    "Are you gonna live until this year?");
            return;
        }
        currentDay = currentDay.plusWeeks(1);
        drawTimeBoxes();
        updateDayBar();
        weekDayBox.repaint();
    }

    private void prevWeek() {
        //show message if user goes below month: January, year: 2000
        if (currentDay.getWeekOfWeekyear() == 1 && currentDay.getYear() == 2000) {
            JOptionPane.showMessageDialog(this,
                    "Don't be obsessed with the past too much!");
            return;
        }
        currentDay = currentDay.minusWeeks(1);
        drawTimeBoxes();
        updateDayBar();
        weekDayBox.repaint();
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
     * The frame that invokes this timetable frame will be disable to avoid conflict.
     * Therefore, when this frame closes, the prevFrame will be enable again
     */

    /*
     * re-draw all cell in the timetable with correct information
     * when the room or the week is changed
     */
    // <editor-fold defaultstate="collapsed" desc="drawTimeBoxes">
    private void drawTimeBoxes() {
        isUserFireEvent = false;
        year.setSelectedItem(currentDay.getYear());
        isUserFireEvent = true;
        HashMap<String, ClassSession> thisWeekSession = new HashMap<>();
        HashMap<String, model.Class> classesToSearch = new HashMap<>();
        if (currentTeacher != null) {
            classesToSearch = mainFrame.getModel().getClassAssociatedTeacher(currentTeacher);
        } else if (currentStudent != null) {
            classesToSearch = mainFrame.getModel().getClassAssociatedStudent(currentStudent);
        }
        resetTimeBoxes();

        Iterator<Entry<String, ClassSession>> iter;

        // <editor-fold defaultstate="collapsed" desc="add session to timetable">
        // add session to thisWeekSession
        iter = mainFrame.getModel().getSessions().entrySet().iterator();
        while (iter.hasNext()) {
            ClassSession ses = iter.next().getValue();
            if (ses.getRoomId() == roomId //room view
                    || (currentTeacher != null && classesToSearch.containsKey(ses.getClassId())) // timetable for teacher
                    || (currentStudent != null && classesToSearch.containsKey(ses.getClassId()))) {//time table for student

                model.Class cWithSes = mainFrame.getModel().getClasses().get(ses.getClassId());
                LocalDate startDate = cWithSes.getStartDate().withDayOfWeek(DateTimeConstants.MONDAY);
                LocalDate endDate = cWithSes.getEndDate().withDayOfWeek(DateTimeConstants.SUNDAY);
                if (startDate.compareTo(currentDay) <= 0
                        && endDate.compareTo(currentDay.plusDays(6)) >= 0) {
                    thisWeekSession.put(ses.getClassSessionId(), ses);
                }
            }
        }

        HashMap<model.Class, Color> colorAssociatedWithClass = new HashMap<>();
        ArrayList<StudentClass> scList = new ArrayList<>(10);
        if (currentStudent != null) {
            Iterator<Entry<String, StudentClass>> iterAllSC = mainFrame.getModel().getStudentClass().entrySet().iterator();
            while (iterAllSC.hasNext()) {
                StudentClass sc = iterAllSC.next().getValue();
                if (sc.getStudentId().equals(currentStudent.getId())) {
                    scList.add(sc);
                }
            }
        }
        scList.trimToSize();

        iter = thisWeekSession.entrySet().iterator();
        while (iter.hasNext()) {
            ClassSession ses = iter.next().getValue();
            model.Class c = mainFrame.getModel().getClasses().get(ses.getClassId());
            if (!colorAssociatedWithClass.containsKey(c)) {
                if (currentStudent != null) {
                    for (int i = 0; i < scList.size(); i++) {
                        if (c.getClassId().equals(scList.get(i).getClassId())) {
                            if (scList.get(i).isPaid()) {
                                colorAssociatedWithClass.put(c, paidColor);
                            } else {
                                colorAssociatedWithClass.put(c, unPaidColor);
                            }
                        }
                    }
                } else {
                    colorAssociatedWithClass.put(c, currentTimeBoxBG);
                }
                generateCurrentTimeBoxGBColor();
            }
            Iterator<Integer> iterTime = ses.getTimes().keySet().iterator();
            while (iterTime.hasNext()) {
                int t = iterTime.next();
                Color temp = colorAssociatedWithClass.get(c);
                timeBoxes[t / 44][t % 44].setOccupiedBG(temp);//do not change the order of
                timeBoxes[t / 44][t % 44].setThisClass(c);// these 2 lines
            }
        }

        //<editor-fold defaultstate="collapsed" desc="draw class name on time boxes">
        int count = 0;
        for (int i = 0; i < timeBoxes.length; i++) {
            for (int j = 0; j < timeBoxes[i].length; j++) {
                int boxNo = i * 44 + j;
                boxNo = boxNo - count / 2;
                TimeBox box = timeBoxes[boxNo / 44][boxNo % 44];
                if (j + 1 < timeBoxes[i].length) {// if not at last row
                    if (timeBoxes[i][j].getThisClass() != null) {
                        if (timeBoxes[i][j + 1].getThisClass() != timeBoxes[i][j].getThisClass()) {
                            box.setText(box.getThisClass().getClassName());
                            timeBoxes[i][j].setNeedSouthDivider(true);
                            count = 0;//reset count
                        } else {
                            count++;
                        }
                    }
                } else {//if last row
                    if (timeBoxes[i][j].getThisClass() != null) {
                        box.setText(box.getThisClass().getClassName());
                        count = 0;//reset count
                    }
                }
            }
        }
        //</editor-fold>

        int countCellOfCurrentClass = 0;
        for (int i = 0; i < timeBoxes.length; i++) {
            for (int j = 0; j < timeBoxes[i].length; j++) {
                if (timeBoxes[i][j].getThisClass() == currentClass && currentClass != null) {
                    timeBoxes[i][j].setThisClass(null);
                    timeBoxes[i][j].setText("");
                    timeBoxes[i][j].setIsSelected(true, is45);
                    countCellOfCurrentClass++;
                }
            }
        }

        currentSession = Math.round(countCellOfCurrentClass / 3f);
        revalidate();
        repaint();
        //</editor-fold>
    }
    // </editor-fold>

    /*
     * invoked everytime when a room or week is changed,in order to repaint every
     * cell in the preparation for painting the cells to fit the new room(week)
     */
    // <editor-fold defaultstate="collapsed" desc="resetTimeBoxes">
    private void resetTimeBoxes() {
        for (int i = 0; i < timeBoxes.length; i++) {
            for (int j = 0; j < timeBoxes[i].length; j++) {
                timeBoxes[i][j].setThisClass(null);
                timeBoxes[i][j].setIsSelected(false, is45);
                timeBoxes[i][j].setText("");
                timeBoxes[i][j].setNeedSouthDivider(false);
            }
        }
        currentSession = 0;
//        System.out.println("TimeTable currentSession = " + currentSession);
//        System.out.println("TimeTable maxSession = " + maxSession);
        currentTimeBoxBGIndex = -1;
        generateCurrentTimeBoxGBColor();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public void setRoomId(int roomId) {
        this.roomId = roomId;
        drawTimeBoxes();
    }

    public void setClass(model.Class c) {
        this.currentClass = c;
    }

    public void setStudent(Student s) {
        currentStudent = s;
    }

    public void setTeacher(Teacher t) {
        currentTeacher = t;
    }

    public TimeBox[][] getTimeBoxes() {
        return timeBoxes;
    }

    public boolean isIs45() {
        return is45;
    }

    public void setIs45(boolean is45) {
        this.is45 = is45;
    }

    public boolean isIsSmall() {
        return isSmall;
    }

    public void setIsSmall(boolean isSmall) {
        this.isSmall = isSmall;
    }

    public int getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(int currentSession) {
        this.currentSession = currentSession;
    }
    // </editor-fold>

    /*
     * enhanced usability for user when selecting multiple cells with SHIFT then
     * clicks
     */
    // <editor-fold defaultstate="collapsed" desc="select multiple cells when SHIFT click" >
    void firstClick(int i, int j) {
//        firstClickI = i;
//        firstClickJ = j;
//        firstClickIsClicked = true;
    }

    void secondClick(int i, int j) {
//        secondClickI = i;
//        secondClickJ = j;
//
//        int fromI;
//        int toI;
//        int fromJ;
//        int toJ;
//
//        if (secondClickI < firstClickI) {
//            fromI = secondClickI;
//            toI = firstClickI;
//        } else {
//            fromI = firstClickI;
//            toI = secondClickI;
//        }
//
//        if (secondClickJ < firstClickJ) {
//            fromJ = secondClickJ;
//            toJ = firstClickJ;
//        } else {
//            fromJ = firstClickJ;
//            toJ = secondClickJ;
//        }
//
//        for (int k = fromI; k <= toI; k++) {
//            for (int l = fromJ; l <= toJ; l++) {
//                if (timeBoxes[k][l].getThisClass() == null) {
//                    timeBoxes[k][l].setIsSelected(true, is45);
//                }
//            }
//        }
//        firstClickIsClicked = false;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="generateCurrentTimeBoxColor">
    private void generateCurrentTimeBoxGBColor() {
        if (currentTimeBoxBGIndex < 5) {
            currentTimeBoxBGIndex++;
        } else {
            currentTimeBoxBGIndex = 0;
        }

        switch (currentTimeBoxBGIndex) {
            case 0:
                currentTimeBoxBG = Template.getButtonBG1();
                break;
            case 1:
                currentTimeBoxBG = Template.getButtonBG2();
                break;
            case 2:
                currentTimeBoxBG = Template.getButtonBG3();
                break;
            case 3:
                currentTimeBoxBG = Template.getButtonBG4();
                break;
            case 4:
                currentTimeBoxBG = Template.getButtonBG5();
                break;
            default:
                currentTimeBoxBG = Template.getButtonBG6();
        }
    }
    //</editor-fold>

    public void clearSession() {
        for (int i = 0; i < timeBoxes.length; i++) {
            for (int j = 0; j < timeBoxes[i].length; j++) {
                if (timeBoxes[i][j].isIsSelected()) {
                    timeBoxes[i][j].setIsSelected(false, is45);
                }
            }
        }
        currentSession = 0;
    }

    public void mouseClicked(int i, int j) {
        if (currentSession >= maxSession) {
            return;
        }
        int maxJ;
        if (i < 5) {
            maxJ = 16;
        } else {
            maxJ = 44;
        }
        if (is45) {
//            if (j < 1 && j > maxJ - 2
//                    && timeBoxes[i][j + 1].isIsSelected()
//                    && timeBoxes[i][j - 1].isIsSelected()
//                    && timeBoxes[i][j].isIsSelected()) {
//                timeBoxes[i][j - 1].mouseClicked();
//                timeBoxes[i][j].mouseClicked();
//                timeBoxes[i][j + 1].mouseClicked();
//            } else
            if (j < 1 || j > maxJ - 2
                    || timeBoxes[i][j - 1].getThisClass() != null
                    || timeBoxes[i][j + 1].getThisClass() != null
                    || timeBoxes[i][j + 1].isIsSelected()
                    || timeBoxes[i][j - 1].isIsSelected()) {
            } else {
                currentSession++;
                timeBoxes[i][j - 1].mouseClicked();
                timeBoxes[i][j].mouseClicked();
                timeBoxes[i][j + 1].mouseClicked();
            }
        } else {
            if (j < 1 || j > maxJ - 3
                    || timeBoxes[i][j - 1].getThisClass() != null
                    || timeBoxes[i][j + 2].getThisClass() != null
                    || timeBoxes[i][j + 1].getThisClass() != null
                    || timeBoxes[i][j + 1].isIsSelected()
                    || timeBoxes[i][j - 1].isIsSelected()
                    || timeBoxes[i][j + 2].isIsSelected()) {
            } else {
                currentSession++;
                timeBoxes[i][j - 1].mouseClicked();
                timeBoxes[i][j].mouseClicked();
                timeBoxes[i][j + 1].mouseClicked();
                timeBoxes[i][j + 2].mouseClicked();
            }
        }
    }

    public void mouseEntered(int i, int j) {
        if (currentSession >= maxSession) {
//            System.out.println("TimeTable mousEntered currentSes >= maxSes");
            return;
        }
        int maxJ;
        if (i < 5) {
            maxJ = 16;
        } else {
            maxJ = 44;
        }

        if (is45) {
            if (j < 1 || j > maxJ - 2
                    || timeBoxes[i][j - 1].getThisClass() != null
                    || timeBoxes[i][j + 1].getThisClass() != null) {
            } else {
                timeBoxes[i][j - 1].mouseEntered();
                timeBoxes[i][j].mouseEntered();
                timeBoxes[i][j + 1].mouseEntered();
            }
        } else {
            if (j < 1 || j > maxJ - 3
                    || timeBoxes[i][j - 1].getThisClass() != null
                    || timeBoxes[i][j + 2].getThisClass() != null
                    || timeBoxes[i][j + 1].getThisClass() != null) {
            } else {
                timeBoxes[i][j - 1].mouseEntered();
                timeBoxes[i][j].mouseEntered();
                timeBoxes[i][j + 1].mouseEntered();
                timeBoxes[i][j + 2].mouseEntered();
            }
        }
    }

    public void mouseExited(int i, int j) {
        int maxJ;
        if (i < 5) {
            maxJ = 16;
        } else {
            maxJ = 44;
        }
        if (is45) {
            if (j < 1 || j > maxJ - 2
                    || timeBoxes[i][j - 1].getThisClass() != null
                    || timeBoxes[i][j + 1].getThisClass() != null) {
            } else {
                timeBoxes[i][j - 1].mouseExited();
                timeBoxes[i][j].mouseExited();
                timeBoxes[i][j + 1].mouseExited();
            }
        } else {
            if (j < 1 || j > maxJ - 3
                    || timeBoxes[i][j - 1].getThisClass() != null
                    || timeBoxes[i][j + 2].getThisClass() != null
                    || timeBoxes[i][j + 1].getThisClass() != null) {
            } else {
                timeBoxes[i][j - 1].mouseExited();
                timeBoxes[i][j].mouseExited();
                timeBoxes[i][j + 1].mouseExited();
                timeBoxes[i][j + 2].mouseExited();
            }
        }
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }
}
