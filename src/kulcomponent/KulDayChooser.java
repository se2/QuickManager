package kulcomponent;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.joda.time.LocalDate;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulDayChooser extends Box {

    private KulComboBox<Integer> day;
    private KulComboBox<Integer> month;
    private KulComboBox<Integer> year;
    private int startYear;
    private int endYear;
    private Font f;
    private LocalDate date = new LocalDate();
    private boolean isLeap;
    private int strut;

    public KulDayChooser(Font f, int startYear, ResourceBundle language) {
        this(f, startYear, 40, language);
    }

    public KulDayChooser(Font f, int startYear, int endYear, int strut,
            boolean monthYearOnly, ResourceBundle language) {
        super(BoxLayout.X_AXIS);
        this.f = f;
        this.isLeap = date.year().isLeap();
        this.startYear = startYear;
        this.strut = strut;

        JLabel labelDay = new JLabel(language.getString("day"));
        labelDay.setFont(f);
        day = new KulComboBox<>();
        for (int i = 1; i <= 31; i++) {
            day.addItem(i);
        }
        day.setSelectedIndex(date.getDayOfMonth() - 1);
        day.setFont(f);
        day.setBackground(Template.getBackground());
        day.setDisable(true);
        setFixedSize(day, 50, 25);

        JLabel labelMonth = new JLabel(language.getString("month"));
        labelMonth.setFont(f);
        month = new KulComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        month.setSelectedIndex(date.getMonthOfYear() - 1);
        month.setFont(f);
        month.setBackground(Template.getBackground());
        month.setDisable(true);
        setFixedSize(month, 50, 25);

        JLabel labelYear = new JLabel(language.getString("year"));
        labelYear.setFont(f);
        year = new KulComboBox<>();
        for (int i = startYear; i < endYear + 1; i++) {
            year.addItem(i);
        }
        year.setSelectedItem(date.getYear());
        year.setFont(f);
        year.setBackground(Template.getBackground());
        year.setDisable(true);
        setFixedSize(year, 85, 25);

        setMaximumSize(new Dimension(2000, 30));
        if (!monthYearOnly) {
            add(labelDay);
            add(Box.createHorizontalStrut(12));
            add(day);
            add(Box.createHorizontalStrut(strut));
        }
        add(labelMonth);
        add(Box.createHorizontalStrut(12));
        add(month);
        add(Box.createHorizontalStrut(strut));
        add(labelYear);
        add(Box.createHorizontalStrut(12));
        add(year);

        addListener();
    }

    public KulDayChooser(Font f, int startYear, int strut, ResourceBundle language) {
        this(f, startYear, 2200, strut, false, language);
    }

    public KulDayChooser(Font f, ResourceBundle language) {
        this(f, 2000, language);
    }

    private void addListener() {

        // <editor-fold defaultstate="collapsed" desc="wheel listener">
        day.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    if (day.getSelectedIndex() <= 0) {
                        day.setSelectedIndex(day.getItemCount() - 1);
                    } else {
                        day.setSelectedIndex(day.getSelectedIndex() - 1);
                    }
                } else {
                    if (day.getSelectedIndex() >= day.getItemCount() - 1) {
                        day.setSelectedIndex(0);
                    } else {
                        day.setSelectedIndex(day.getSelectedIndex() + 1);
                    }
                }
            }
        });

        month.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    if (month.getSelectedIndex() <= 0) {
                        month.setSelectedIndex(11);
                    } else {
                        month.setSelectedIndex(month.getSelectedIndex() - 1);
                    }
                } else {
                    if (month.getSelectedIndex() >= 11) {
                        month.setSelectedIndex(0);
                    } else {
                        month.setSelectedIndex(month.getSelectedIndex() + 1);
                    }
                }
            }
        });

        year.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    if (year.getSelectedIndex() <= 0) {
                        year.setSelectedIndex(year.getItemCount() - 1);
                    } else {
                        year.setSelectedIndex(year.getSelectedIndex() - 1);
                    }
                } else {
                    if (year.getSelectedIndex() >= year.getItemCount() - 1) {
                        year.setSelectedIndex(0);
                    } else {
                        year.setSelectedIndex(year.getSelectedIndex() + 1);
                    }
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="item listener">
        month.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {//deselecting stage
                    //make number of day = 30
                    if (day.getItemCount() == 31) {
                        day.removeItemAt(30);//remove day-31 at index 30
                    }
                    if (day.getItemCount() == 28) {
                        day.addItem(29);
                    }
                    if (day.getItemCount() == 29) {
                        day.addItem(30);
                    }
                } else {//selecting stage
                    int monthNo = month.getSelectedIndex() + 1;
                    switch (monthNo) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            day.addItem(31);//re-add day-31 at last index 30
                            break;
                    }
                    if (monthNo == 2) {
                        day.removeItemAt(29);//remove day-30 at index 29
                        if (!isLeap) {
                            day.removeItemAt(28);//remove day-29
                        }
                    }
                }
            }
        });

        year.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (year.getSelectedIndex() % 4 == 0 && year.getSelectedIndex() % 100 != 0) {
                    isLeap = true;
                } else {
                    isLeap = false;
                }
                month.setSelectedIndex(month.getSelectedIndex() - 1);//re-select switchView to
                month.setSelectedIndex(month.getSelectedIndex() + 1);//update number of day
            }
        });
        // </editor-fold>
    }

    // <editor-fold defaultstate="collapsed" desc="convenient method">
    private void setFixedSize(Component comp, int x, int y) {
        Dimension dim = new Dimension(x, y);
        comp.setPreferredSize(dim);
        comp.setMaximumSize(dim);
        comp.setMinimumSize(dim);
    }
    // </editor-fold>

    public JComboBox<Integer> getDay() {
        return day;
    }

    public JComboBox<Integer> getMonth() {
        return month;
    }

    public JComboBox<Integer> getYear() {
        return year;
    }

    public LocalDate getDate() {
        int dayNo = day.getSelectedIndex() + 1;
        int monthNo = month.getSelectedIndex() + 1;
        int yearNo = year.getSelectedIndex() + startYear;
        LocalDate date = new LocalDate(yearNo, monthNo, dayNo);
        this.date = date;
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        day.setSelectedIndex(date.getDayOfMonth() - 1);
        month.setSelectedIndex(date.getMonthOfYear() - 2);// force re-select item
        month.setSelectedIndex(date.getMonthOfYear() - 1);
        year.setSelectedItem(date.getYear());
    }
}
