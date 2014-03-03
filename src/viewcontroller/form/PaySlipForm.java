package viewcontroller.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLEditorKit;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import model.Class;
import model.ClassSession;
import model.PaySlip;
import model.Teacher;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class PaySlipForm extends KulFrame {

    private ResourceBundle language;
    private PaySlip paySlip;
    private MainFrame mainFrame;
    private JFrame prevFrame;
    private Font f = Template.getFont().deriveFont(16f);
    private KulButton delete;
    private JPanel container;
    private Box buttons;
    private JScrollPane scroll;
    private JEditorPane content = new JEditorPane();
    private String hexCodeColor;
    private String head;
    private Teacher teacher;
    private final int W1 = 250;
    private final int W2 = 180;
    private final int W3 = 150;
    private final int W4 = 150;
    private final int W5 = 300;

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public PaySlipForm(PaySlip paySlip, MainFrame mainFrame, JFrame prevFrame, String title) {
        super(title);
        this.paySlip = paySlip;
        this.mainFrame = mainFrame;
        this.prevFrame = prevFrame;
        language = mainFrame.getModel().getLanguage();

        setUndecorated(true);
        setResizable(false);
        setTitle(language.getString("paySlip"));
        setLocationRelativeTo(mainFrame);

        container = new JPanel(new BorderLayout());
        container.setBorder(new CompoundBorder(
                new LineBorder(Template.getLineBorderColor(), 4),
                new LineBorder(Template.getBackground(), 5)));
        container.setBackground(Template.getBackground());
        add(container);

        //<editor-fold defaultstate="collapsed" desc="drawTitle">
//        JLabel titleLabel = new JLabel(language.getString("paySlip"));
//        titleLabel.setFont(f.deriveFont(50f));
//        titleLabel.setForeground(Template.getForeground());
//
//        Box northBox = new Box(BoxLayout.X_AXIS);
//        northBox.setPreferredSize(new Dimension(550, 100));
//        northBox.add(Box.createHorizontalGlue());
//        northBox.add(titleLabel);
//        northBox.add(Box.createHorizontalGlue());
//        container.add(northBox, BorderLayout.NORTH);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "ok");
        this.getRootPane().getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_DELETE, 0, true), "delete");
        this.getRootPane().getActionMap().put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        //</editor-fold>

        init();
        drawButtons();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    private void init() {
        scroll = new JScrollPane(content);
        scroll.setBorder(null);
        content.setBackground(Template.getBackground());
        content.setEditorKit(new HTMLEditorKit());
        content.setEditable(false);
        container.add(scroll);

        Color color = Template.getBorderContrast();
        hexCodeColor = Integer.toHexString(color.getRGB());
        hexCodeColor = hexCodeColor.substring(2, hexCodeColor.length());

        head = "<html><body style='margin:0px;font-family:Arial;"
                + " font-size:12px'><div style='font-size:45px;'><font color='#"
                + hexCodeColor + "'>" + s("paySlip") + "</font></div><br/>"
                + "<div style='margin-left:35px;margin-right:25px;'><div>";

        teacher = mainFrame.getModel().getTeachers().get(paySlip.getTeacherId());
        drawContent();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawContent">
    private void drawContent() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM yyyy");
        StringBuilder s = new StringBuilder(head);

        s.append(s("teacher1")).append(" ").append(teacher.getFullname());
        s.append("<br/>").append(s("month")).append(": ").append(fmt.print(paySlip.getMonth())).append("<br/>");

        s.append("<br/><hr></hr><br/>");

        s.append("<table border='0'>");
        s.append("<tr><td width='").append(W1).append("' style='max-width:").append(W1).append("'><b>")
                .append(s("class")).append("</b></td>");
        s.append("<td width='").append(W2).append("' style='max-width:").append(W2).append("' align='subLeft'><b>")
                .append(s("numberOfLessonPS")).append(" </b></td>");
        s.append("<td width='").append(W3).append("' style='max-width:").append(W3).append("; word-wrap:break-word'><b>")
                .append(s("teachingHour")).append("</b></td>");
        s.append("<td width='").append(W4).append("' style='max-width:").append(W4).append("'><b>")
                .append(s("payRate")).append("</b></td>");
        s.append("<td width='").append(W5).append("' style='max-width:").append(W5)
                .append("' align='subLeft'><b>").append(s("classSalary")).append(" </b></td></tr>");

        long total = 0;
        Iterator<Entry<String, Long>> iterListClass = paySlip.getListClasses().entrySet().iterator();
        while (iterListClass.hasNext()) {
            Entry<String, Long> entry = iterListClass.next();
            Class c = mainFrame.getModel().getClasses().get(entry.getKey());

            String name = c.getClassName();
            long salaryForClass = entry.getValue();
            int numberOfLesson;
            long payRate = -1;

            for (int i = 0; i < teacher.getSkills().length; i++) {
                if (teacher.getSkills()[i][0].equals(c.getClassType()[0])) {
                    payRate = Long.parseLong(teacher.getSkills()[i][1]);
                    break;
                }
            }

            double teachingHour = salaryForClass / payRate;

            Iterator<Entry<String, ClassSession>> iterSes = mainFrame.getModel().getSessions().entrySet().iterator();
            // session of class in this list won't be empty
            ClassSession ses = iterSes.next().getValue();
            if (ses.is45()) {
                numberOfLesson = (int) (teachingHour / 0.75);
            } else {
                numberOfLesson = (int) (teachingHour);
            }

            s.append(drawHTMLRow(name, numberOfLesson + "", teachingHour + "",
                    getVNDString(payRate), getVNDString(salaryForClass)));
            total += salaryForClass;
        }
        s.append("</table>");
        s.append("<br/>");
        s.append("<hr></hr>");

        //total salary row
        s.append("<table border='0'>");
        s.append("<tr><td width='").append(W1).append("' style='max-width:")
                .append(W1).append("'></td>");
        s.append("<td width='").append(W2).append("' style='max-width:")
                .append(W2).append("'></td>");
        s.append("<td width='").append(W3 + 10).append("' style='max-width:")
                .append(W3 + 10).append("'></td>");

        s.append("<td width='").append(W4).append("' style='max-width:3").append(W4)
                .append("' align='subLeft'><b>").append(s("monthlySalary")).append(" </b></td>");
        s.append("<td width='").append(W5).append("' style='max-width:").append(W5).append("; word-wrap:break-word'>")
                .append(Template.getVNDString(total)).append("</td></tr>");

        s.append("</table></div>");
        s.append("</body></html>");
        content.setText(s.toString());
        revalidate();
        repaint();
    }

    private String drawHTMLRow(String name, String number, String hour, String payRate, String total) {
        String s = "<tr><td width='" + W1 + "' style='max-width:" + W1 + "; word-wrap:break-word'>" + name + "</td>"
                + "<td width='" + W2 + "' style='max-width:" + W2 + "; word-wrap:break-word'>" + number + "</td>"
                + "<td width='" + W3 + "' style='max-width:" + W3 + "; word-wrap:break-word'>" + hour + "</td>"
                + "<td width='" + W4 + "' style='max-width:" + W4 + "; word-wrap:break-word'>" + payRate + "</td>"
                + "<td width='" + W5 + "' style='max-width:" + W5 + "; word-wrap:break-word'>" + total + "</td>"
                + "</tr>";
        return s;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 50));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        container.add(buttons, BorderLayout.SOUTH);

        delete = new KulButton(s("delete"));
        delete.setPreferredSize(new Dimension(80, 30));
        delete.setMaximumSize(new Dimension(80, 30));
        delete.setFont(f);

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                delete();
            }
        });

        buttons.add(Box.createHorizontalGlue());
        buttons.add(delete);
        buttons.add(Box.createHorizontalGlue());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ok() = closeWindow()">
    private void ok() {
        closeWindow();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="delete">
    private void delete() {
        int value = JOptionPane.showConfirmDialog(mainFrame,
                language.getString("deletePaySlipConfirmContent"), language.getString("deletePaySlipConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
        if (value == JOptionPane.OK_OPTION) {
            if (mainFrame.getModel().deletePaySlip(paySlip)) {
                JOptionPane.showMessageDialog(this, language.getString("deletePaySlipSuccessContent"));
                closeWindow();
            } else {
                JOptionPane.showMessageDialog(this, language.getString("deletePaySlipFailContent"));
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="closeWindow">
    private void closeWindow() {
        prevFrame.setEnabled(true);
        this.setVisible(false);
        this.dispose();
    }
    //</editor-fold>

    private String s(String key) {
        return language.getString(key);
    }

    private String getVNDString(long money) {
        return Template.getVNDString(money);
    }
}
