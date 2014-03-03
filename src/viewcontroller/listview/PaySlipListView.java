package viewcontroller.listview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import kulcomponent.KulButton;
import kulcomponent.KulFrame;
import kulcomponent.KulImageButton;
import kulcomponent.KulShinyText;
import model.PaySlip;
import model.Teacher;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 * @author Dam Linh
 */
public class PaySlipListView extends JFrame {

    private ResourceBundle language;
    private MainFrame mainFrame;
    private ArrayList<PaySlip> paySlips;
    private KulButton ok;
    private Font f = Template.getFont().deriveFont(16f);
    private Box psBox;
    private JPanel container;
    private KulFrame prevFrame;
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM yyyy");

    //<editor-fold defaultstate="collapsed" desc="init">
    public PaySlipListView(MainFrame mainFrame, ArrayList<PaySlip> paySlips, KulFrame prevFrame) {
        this.paySlips = paySlips;
        this.prevFrame = prevFrame;
        this.mainFrame = mainFrame;
        language = mainFrame.getModel().getLanguage();

        setUndecorated(true);

        container = new JPanel(new BorderLayout());
        container.setBorder(new CompoundBorder(
                new LineBorder(Template.getLineBorderColor(), 4),
                new LineBorder(Template.getBackground(), 5)));
        container.setBackground(Template.getBackground());
        add(container);

        //<editor-fold defaultstate="collapsed" desc="drawTitle">
//        setTitle(teacher.getFullname() + " - " + getString("listPaySlip"));

        JLabel title = new JLabel(getString("listPaySlip"));
        title.setFont(f.deriveFont(50f));
        title.setForeground(Template.getForeground());

        Box northBox = new Box(BoxLayout.X_AXIS);
        northBox.setPreferredSize(new Dimension(550, 90));
        northBox.add(Box.createHorizontalGlue());
        northBox.add(title);
        northBox.add(Box.createHorizontalGlue());
        //</editor-fold>

        psBox = new Box(BoxLayout.Y_AXIS);
        container.add(northBox, BorderLayout.NORTH);
        container.add(psBox);

        setKeyBinding();
        drawContent();
        drawButton();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButton">
    private void drawButton() {
        ok = new KulButton(language.getString("ok"));
        Box buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 40));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        container.add(buttons, BorderLayout.SOUTH);

        ok.setPreferredSize(new Dimension(100, 30));
        ok.setMaximumSize(new Dimension(100, 30));
        ok.setFont(f);

        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeWindow();
            }
        });
        buttons.add(Box.createHorizontalGlue());
        buttons.add(ok);
        buttons.add(Box.createHorizontalGlue());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="closeWindow">
    private void closeWindow() {
        prevFrame.setEnabled(true);
        PaySlipListView.this.setVisible(false);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setKeyBinding">
    private void setKeyBinding() {
        getRootPane().setFocusable(false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "exit");
        getRootPane().getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawContent">
    private void drawContent() {
        psBox.removeAll();
        if (!paySlips.isEmpty()) {
            paySlips = mainFrame.getModel().getPaySlipsOfTeacher(paySlips.get(0).getTeacherId());
        } else {
            return;
        }
        for (int i = 0; i < paySlips.size(); i++) {
            final PaySlip ps = paySlips.get(i);

            KulImageButton remove = new KulImageButton("cancel", 13, 13);
            remove.setToolTipText(language.getString("delete"));
            remove.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int value = JOptionPane.showConfirmDialog(mainFrame,
                            getString("deletePaySlipConfirmContent"), getString("deletePaySlipConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                    if (value == JOptionPane.OK_OPTION) {
                        if (mainFrame.getModel().deletePaySlip(ps)) {
                            JOptionPane.showMessageDialog(PaySlipListView.this, getString("deletePaySlipSuccessContent"));
                            drawContent();
                        } else {
                            JOptionPane.showMessageDialog(PaySlipListView.this, getString("deletePaySlipFailContent"));
                        }
                    }
                }
            });

            HashMap<String, Long> listClass = ps.getListClasses();
            long salary = 0;
            Iterator<Entry<String, Long>> iterListClass = listClass.entrySet().iterator();
            while (iterListClass.hasNext()) {
                salary += iterListClass.next().getValue();
            }

            KulShinyText paySlipLabel = new KulShinyText(getString("paySlip")
                    + " " + fmt.print(ps.getMonth()) + ": " + Template.getVNDString(salary), f.deriveFont(5f));
            paySlipLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.showPaySlipForm(ps, PaySlipListView.this);
                }
            });

            Box line = new Box(BoxLayout.X_AXIS);
            line.setPreferredSize(new Dimension(550, 25));
            line.setMaximumSize(new Dimension(550, 25));

            line.add(Box.createHorizontalStrut(30));
            line.add(remove);
            line.add(Box.createHorizontalStrut(10));
            line.add(paySlipLabel);
            line.add(Box.createHorizontalGlue());
            psBox.add(line);
        }
        psBox.revalidate();
        psBox.repaint();
    }
    //</editor-fold>

    private String getString(String key) {
        return language.getString(key);
    }
}
