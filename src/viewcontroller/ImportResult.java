package viewcontroller;

import model.CSVImporter;
import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.text.html.HTMLEditorKit;
import kulcomponent.KulBorder;

/**
 *
 * @author Dam Linh
 */
public class ImportResult extends JFrame {

    private ResourceBundle language;
    private ArrayList<String>[] errorString;
    private MainFrame mainFrame;
    private JEditorPane content = new JEditorPane();
    private JScrollPane scroll;
    private JPanel container;
    private String head;
    private String hexCodeColor;

    public ImportResult(ArrayList<String>[] errorString, MainFrame mainFrame) {
        this.errorString = errorString;
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        language = mainFrame.getModel().getLanguage();

        setUndecorated(true);
        setResizable(false);
        setTitle(getString("invalidResult"));
        AWTUtilities.setWindowOpaque(this, false);

        container = new JPanel(new BorderLayout());
        container.setBorder(new KulBorder(Template.getLineBorderColor(), 10, 0.3f));
        container.setBackground(Template.getBackground());
        add(container);

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_ENTER, 0, true), "ok");
        this.getRootPane().getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        //</editor-fold>

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
                + " font-size:11px'><div style='font-size:45px;'><font color='#"
                + hexCodeColor + "'>" + getString("invalidResult") + "</font></div><br/>"
                + "<div style='margin-left:25px;margin-right:25px;'><div>";
        drawContent();
    }

    //<editor-fold defaultstate="collapsed" desc="drawContent">
    private void drawContent() {
        StringBuilder s = new StringBuilder(head);

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("account")).append("</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.USER].size(); i++) {
            s.append(errorString[CSVImporter.USER].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("classType")).append(":</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.CLASS_TYPE].size(); i++) {
            s.append(errorString[CSVImporter.CLASS_TYPE].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("teacher")).append(":</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.TEACHER].size(); i++) {
            s.append(errorString[CSVImporter.TEACHER].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("student")).append(":</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.STUDENT].size(); i++) {
            s.append(errorString[CSVImporter.STUDENT].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("class1")).append("</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.CLASS].size(); i++) {
            s.append(errorString[CSVImporter.CLASS].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("session")).append(":</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.SESSION].size(); i++) {
            s.append(errorString[CSVImporter.SESSION].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("teacherClass1")).append("</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.TEACHER_CLASS].size(); i++) {
            s.append(errorString[CSVImporter.TEACHER_CLASS].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("studentClass1")).append("</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.STUDENT_CLASS].size(); i++) {
            s.append(errorString[CSVImporter.STUDENT_CLASS].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        s.append("<br/><br/>");

        s.append("<hr></hr>");
        s.append("<span style='font-size:16px;'><b>").append(getString("table")).append(" ").append(getString("invoice")).append(":</b></span><br/>");
        for (int i = 0; i < errorString[CSVImporter.INVOICE].size(); i++) {
            s.append(errorString[CSVImporter.INVOICE].get(i)).append("<br/>");
        }
        s.append("<hr></hr>");

        content.setText(s.toString());
        revalidate();
        repaint();
    }
    //</editor-fold>

    private String getString(String key) {
        return language.getString(key);
    }
}
