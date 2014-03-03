package viewcontroller.form;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.text.html.HTMLEditorKit;
import kulcomponent.KulBorder;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import kulcomponent.KulLoadingFrame;
import model.Report;
import model.School;
import model.User;
import org.joda.time.LocalDate;
import quickmanage.QuickManage;
import viewcontroller.Template;

/**
 * @author Dam Linh
 */
public class Dashboard extends KulFrame {

    //<editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private JScrollPane scroll;
    private JEditorPane content = new JEditorPane();
    private JPanel container = new JPanel(new BorderLayout());
    private KulButton ok = new KulButton("Ok");
    private User user;
    private String hexCodeColor;
    private KulLoadingFrame loading;
    private School model;
    private LocalDate currentMonth = new LocalDate();
    private LocalDate lastMonth = new LocalDate().minusMonths(1);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init">
    public Dashboard(School model, User user) {
        super("Dashboard");
        language = model.getLanguage();
        this.model = model;
        loading = new KulLoadingFrame(s("loadingDashboard"));
        loading.setVisible(true);
        loading.toFront();

        setSize(712, 440);
        setLocationRelativeTo(null);
        this.user = user;
        setUndecorated(true);
        getRootPane().setBackground(Template.getBackground());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        AWTUtilities.setWindowOpaque(this, false);

        container.setBorder(new KulBorder(new Color(0, 0, 0), 15, 0.3f));
        container.setBackground(Template.getBackground());
        add(container);

        //<editor-fold defaultstate="collapsed" desc="keyBinding">
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "ok");
        this.getRootPane().getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getRootPane().getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        //</editor-fold>

        drawContent();
        drawButton();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawContent">
    private void drawContent() {
        scroll = new JScrollPane(content);
        scroll.setBorder(null);

        container.add(scroll);
        content.setBackground(Template.getBackground());
        content.setEditorKit(new HTMLEditorKit());
        content.setEditable(false);

        String u = String.format("%1.4f", model.getUtilization());

        Color color = Template.getBorderContrast();
        hexCodeColor = Integer.toHexString(color.getRGB());
        hexCodeColor = hexCodeColor.substring(2, hexCodeColor.length());

        Report lastMonthReport = model.generateReport(lastMonth);
        Report currentMonthReport = model.generateReport(currentMonth);

        String head = "<html><body style='margin:5px;font-family:Arial;"
                + " font-size:13px'><div style='font-size:50px;'><font color='#"
                + hexCodeColor + "'>" + s("dashboard") + "</font></div><br/>"
                + "<div style='margin-left:40px;margin-right:50px;'>";
        StringBuilder s = new StringBuilder(head);

        s.append("<table border='0'>");
        s.append(drawHTMLRow(s("numberOfClasses"), model.getClasses().size() + ""));
        s.append(drawHTMLRow(s("numberOfStudents"), model.getStudents().size() + ""));
        s.append(drawHTMLRow(s("numberOfTeachers"), model.getTeachers().size() + ""));
        s.append(drawHTMLRow(s("utilRatio"), u));
        s.append("</table>");

        s.append("<hr></hr><table border='0'>");
        s.append(drawHTMLRow(s("totalPaidFeeLastMonth"), lastMonthReport.getTotalPaidAmountString()));
        s.append(drawHTMLRow(s("totalUnpaidFeeLastMonth"), lastMonthReport.getTotalUnpaidAmountString()));
        s.append(drawHTMLRow(s("totalPaidFeeThisMonth"), currentMonthReport.getTotalPaidAmountString()));
        s.append(drawHTMLRow(s("totalUnpaidFeeThisMonth"), currentMonthReport.getTotalUnpaidAmountString()));

        s.append("</table>");
        s.append("</body></html>");
        content.setText(s.toString());
        loading.stopLoading();
    }

    private String drawHTMLRow(String s1, String s2) {
        String s = "<tr><td align='right' width='300' style='max-width:300; word-wrap:break-word'>" + s1 + "</td>"
                + "<td width='20px'></td"
                + "<td width='200' style='max-width:200; word-wrap:break-word'>" + s2 + "</td></tr>";
        return s;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButton and Ok()">
    private void drawButton() {
        Box tempBox = new Box(BoxLayout.X_AXIS);
        tempBox.setPreferredSize(new Dimension(1000, 50));
        tempBox.add(Box.createHorizontalGlue());
        tempBox.add(ok);
        tempBox.add(Box.createHorizontalGlue());
        tempBox.setOpaque(true);
        tempBox.setBackground(Template.getBackground());
        container.add(tempBox, BorderLayout.SOUTH);

        ok.setPreferredSize(new Dimension(80, 30));
        ok.setMaximumSize(new Dimension(80, 30));
        ok.setFont(Template.getFont().deriveFont(16f));
        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ok();
            }
        });
    }

    private void ok() {
        this.dispose();
        QuickManage.startApplication(user);
    }
    //</editor-fold>

    private String s(String key) {
        return language.getString(key);
    }
}
