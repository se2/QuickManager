package viewcontroller.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.text.html.HTMLEditorKit;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import kulcomponent.KulLoadingFrame;
import model.Invoice;
import model.Report;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class ReportForm extends JPanel {

    //<editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private MainFrame mainFrame;
    private KulFrame parentFrame;
    private Report report;
    private JScrollPane scroll;
    private SpringLayout spring;
    private JEditorPane content = new JEditorPane();
    private Box buttons;
    private KulButton ok = new KulButton("Ok");
    private KulButton print;
    private Font f = Template.getFont().deriveFont(16f);
    private String head;
    private String hexCodeColor;
    private ArrayList<String> paidStudentsInfo;
    private ArrayList<String> unPaidStudentsInfo;
    private ArrayList<String> paidAmount;
    private ArrayList<String> unPaidAmount;
    private ArrayList<Invoice> paidInvoice;
    private ArrayList<Invoice> unPaidInvoice;
    private String totalPaid;
    private String totalUnpaid;
    private KulLoadingFrame loading;
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMMM yyyy");
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init">
    public ReportForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        spring = new SpringLayout();
        this.setLayout(spring);
        this.setBackground(Template.getBackground());
        language = mainFrame.getModel().getLanguage();
        loading = new KulLoadingFrame(getString("generatingReport"));

        //<editor-fold defaultstate="collapsed" desc="keyBinding">
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK, true), "print");
        this.getActionMap().put("print", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        //</editor-fold>

        drawButtons();
        init();
    }

    private void init() {
        loading.setVisible(true);
        loading.toFront();

        scroll = new JScrollPane(content);
        scroll.setBorder(null);

        this.add(scroll);
        content.setBackground(Template.getBackground());
        content.setEditorKit(new HTMLEditorKit());
        content.setEditable(false);

        spring.putConstraint(SpringLayout.NORTH, scroll, 10, SpringLayout.NORTH, this);
        spring.putConstraint(SpringLayout.SOUTH, scroll, 0, SpringLayout.NORTH, buttons);
        spring.putConstraint(SpringLayout.EAST, scroll, -10, SpringLayout.EAST, this);
        spring.putConstraint(SpringLayout.WEST, scroll, 10, SpringLayout.WEST, this);

        spring.putConstraint(SpringLayout.SOUTH, buttons, 0, SpringLayout.SOUTH, this);
        spring.putConstraint(SpringLayout.EAST, buttons, -10, SpringLayout.EAST, this);
        spring.putConstraint(SpringLayout.WEST, buttons, 10, SpringLayout.WEST, this);

        Color color = Template.getBorderContrast();
        hexCodeColor = Integer.toHexString(color.getRGB());
        hexCodeColor = hexCodeColor.substring(2, hexCodeColor.length());

        head = "<html><body style='margin:0px;font-family:Arial;"
                + " font-size:10px'><div style='font-size:45px;'><font color='#"
                + hexCodeColor + "'>" + getString("monthlyReport") + "</font></div><br/>"
                + "<div style='margin-left:20px;margin-right:20px;'>";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 50));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        this.add(buttons);

        print = new KulButton(getString("print"));

        ok.setPreferredSize(new Dimension(80, 30));
        ok.setMaximumSize(new Dimension(80, 30));
        ok.setFont(f);

        print.setPreferredSize(new Dimension(80, 30));
        print.setMaximumSize(new Dimension(80, 30));
        print.setFont(f);

        print.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                print();
            }
        });

        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ok();
            }
        });

        buttons.add(Box.createHorizontalGlue());
        buttons.add(print);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(20));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setReport">
    public void setReport(Report report, KulFrame parentFrame) {
        this.report = report;
        this.parentFrame = parentFrame;

        paidStudentsInfo = report.getPaidStudentsInfo();
        paidAmount = report.getPaidAmountString();
        paidInvoice = report.getPaidInvoice();
        unPaidStudentsInfo = report.getUnPaidStudentsInfo();
        unPaidAmount = report.getUnPaidAmountString();
        unPaidInvoice = report.getUnPaidInvoice();
        totalPaid = report.getTotalPaidAmountString();
        totalUnpaid = report.getTotalUnpaidAmountString();
        printReportInfo();

        drawReport();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawReport">
    private void drawReport() {
        StringBuilder s = new StringBuilder(head);
        s.append("<span style='font-size:12px'>").append(fmt.print(report.getStartDate()));
        s.append(" - ");
        s.append(fmt.print(report.getEndDate())).append("</span></div>");

        //<editor-fold defaultstate="collapsed" desc="draw paid report">
        s.append("<br/><br/>");
        s.append("<font style='font-size:20px'>").append(getString("paid")).append("</font><hr></hr>");
        s.append("<table border='0'>");
        s.append("<tr><td width='95' style='max-width:95'><b>").append(getString("student")).append(" Id</b></td>");
        s.append("<td width='200' style='max-width:200'><b>").append(getString("amount")).append("</b></td>");
        s.append("<td width='130' style='max-width:130'><b>").append(getString("paidDate0")).append("</b></td>");
        s.append("<td width='130' style='max-width:130'><b>").append(getString("paidMethod0")).append("</b></td>");
        s.append("<td width='180' style='max-width:180'><b>").append(getString("note")).append("</b></td></tr>");

        for (int i = 0; i < paidStudentsInfo.size(); i++) {
            String stu = paidStudentsInfo.get(i);
            String amount = paidAmount.get(i);
            LocalDate date = paidInvoice.get(i).getPaidDate();
            String method = paidInvoice.get(i).getPaidMethod();
            String note = paidInvoice.get(i).getPaidNote();
            s.append(drawHTMLRow(stu, amount, date, method, note));
        }
        s.append("</table>");
        s.append("<br/>");
        s.append("<hr></hr>");

        // total row
        s.append("<table border='0'>");
        s.append("<tr><td width='99' style='max-width:99'><b>").append(getString("total")).append("</b></td>");
        s.append("<td width='200' style='max-width:200; word-wrap:break-word'>")
                .append(totalPaid).append("</td>");
        s.append("<td width='130' style='max-width:130'></td>");
        s.append("<td width='130' style='max-width:130'></td>");
        s.append("<td width='180' style='max-width:180'></td></tr>");
        s.append("</table>");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="draw unPaid report">
        s.append("<br/><br/>");
        s.append("<font style='font-size:20px'>").append(getString("unpaid")).append("</font><hr></hr>");
        s.append("<table border='0'>");
        s.append("<tr><td width='95' style='max-width:95'><b>").append(getString("student")).append(" Id</b></td>");
        s.append("<td width='200' style='max-width:200'><b>").append(getString("amount")).append("</b></td>");
        s.append("<td width='130' style='max-width:130'><b>").append(getString("paidDate0")).append("</b></td>");
        s.append("<td width='130' style='max-width:130'><b>").append(getString("paidMethod0")).append("</b></td>");
        s.append("<td width='180' style='max-width:180'><b>").append(getString("note")).append("</b></td></tr>");

        for (int i = 0; i < unPaidStudentsInfo.size(); i++) {
            String stu = unPaidStudentsInfo.get(i);
            String amount = unPaidAmount.get(i);
            LocalDate date = null;
            String method = "---";
            String note = "---";
            s.append(drawHTMLRow(stu, amount, date, method, note));
            if (i > 0 && i % 10 == 0) {
                s.append(drawHTMLRow("---------", "------------------", null,
                        "------------", "------------------------"));
            }
        }
        s.append("</table>");
        s.append("<br/>");
        s.append("<hr></hr>");

        // total row
        s.append("<table border='0'>");
        s.append("<tr><td width='99' style='max-width:99'><b>").append(getString("total")).append("</b></td>");
        s.append("<td width='200' style='max-width:200; word-wrap:break-word'>")
                .append(totalUnpaid).append("</td>");
        s.append("<td width='130' style='max-width:130'></td>");
        s.append("<td width='130' style='max-width:130'></td>");
        s.append("<td width='180' style='max-width:180'></td></tr>");
        s.append("</table>");
        //</editor-fold>

        s.append("</body></html>");
        content.setText(s.toString());
        revalidate();
        repaint();
        loading.stopLoading();
    }

    private String drawHTMLRow(String stu, String amount, LocalDate date, String method, String note) {
        DateTimeFormatter fomatter = DateTimeFormat.forPattern("dd MMM yyyy");
        String dateS = "---";
        if (date != null) {
            dateS = fomatter.print(date);
        }

        String s = "<tr><td width='95' style='max-width:95; word-wrap:break-word'>" + stu + "</td>"
                + "<td width='200' style='max-width:200; word-wrap:break-word'>" + amount + "</td>"
                + "<td width='130' style='max-width:130; word-wrap:break-word'>" + dateS + "</td>"
                + "<td width='130' style='max-width:130; word-wrap:break-word'>" + method + "</td>"
                + "<td width='180' style='max-width:180; word-wrap:break-word'>" + note + "</td></tr>";
        return s;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="perform action methods">
    private void ok() {
        parentFrame.closeWindow(true);
    }

    private void print() {
        MediaPrintableArea mpa = new MediaPrintableArea(15, 15, 180, 260, MediaPrintableArea.MM);
        HashPrintRequestAttributeSet hpras = new HashPrintRequestAttributeSet(mpa);

        try {
            content.print(null, null, true, null, hpras, true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, getString("printFailContent"),
                    getString("fail"), JOptionPane.WARNING_MESSAGE);
            System.out.println("Exception when print" + ex.toString());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="debug purpose">
    private void printReportInfo() {
        System.out.println("Paid Students:");
        for (int i = 0; i < paidStudentsInfo.size(); i++) {
            System.out.print("\t" + paidStudentsInfo.get(i) + " - ");
            System.out.print(paidAmount.get(i) + " - " + paidInvoice.get(i).getPaidMethod() + paidInvoice.get(i).getPaidNote());
            System.out.println();
        }

        System.out.println("UNpaid Students:");
        for (int i = 0; i < unPaidStudentsInfo.size(); i++) {
            System.out.print("\t" + unPaidStudentsInfo.get(i) + " - ");
            System.out.print(unPaidAmount.get(i) + " - " + unPaidInvoice.get(i).getPaidMethod() + unPaidInvoice.get(i).getPaidNote());
            System.out.println();
        }
    }
    //</editor-fold>

    private String getString(String key) {
        return language.getString(key);
    }
}
