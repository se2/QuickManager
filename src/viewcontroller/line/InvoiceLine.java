package viewcontroller.line;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import kulcomponent.KulImageButton;
import model.Invoice;
import model.Student;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.listview.InvoiceListView;

/**
 *
 * @author Dam Linh
 */
public class InvoiceLine extends Box {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ResourceBundle language;
    private Invoice invoice;
    private Color currentBGColor;
    private Color defaultBGColor = Template.getLineBackground();
    private Color selectedBGColor = Template.getTimeTableSelected();
    private Color borderColor = Template.getLineBorderColor();
    private Font f = Template.getFont().deriveFont(15f);
    private Border clickedBorder;
    private Border overedBorder;
    private JLabel id;
    private JLabel studentId;
    private JLabel total;
    private JLabel status;
    private KulImageButton delete;
    private InvoiceListView parent;
    private MainFrame mainFrame;

    public InvoiceLine(MainFrame mainFrame, InvoiceListView parent, Invoice invoice, Student student) {
        super(BoxLayout.X_AXIS);
        this.parent = parent;
        this.invoice = invoice;
        this.mainFrame = mainFrame;
        setOpaque(true);
        setBackground(defaultBGColor);
        setPreferredSize(new Dimension(600, 30));
        setMaximumSize(new Dimension(1024, 30));
        language = mainFrame.getModel().getLanguage();

        setCurrentBackgroundColor();
        setBackground(currentBGColor);
        setBorder(BorderFactory.createLineBorder(currentBGColor, 2));

        clickedBorder = new CompoundBorder(
                new MatteBorder(1, 0, 0, 1, currentBGColor),
                new MatteBorder(1, 2, 1, 1, borderColor));
        overedBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, borderColor),
                new MatteBorder(1, 2, 1, 0, currentBGColor));

        id = new JLabel(invoice.getId() + "");
        id.setHorizontalAlignment(SwingConstants.CENTER);
        studentId = new JLabel(student.getId() + " - " + student.getFullname());
        total = new JLabel(invoice.getTotalFeeString());
        total.setHorizontalAlignment(SwingConstants.CENTER);
        total.setFont(f);
        status = new JLabel();
        if (invoice.isPaid()) {
            status.setText("<html><font color='green'>" + language.getString("paid") + "</font></html>");
        } else {
            status.setText("<html><font color='red'>" + language.getString("unpaid") + "</font></html>");
        }

        status.setHorizontalAlignment(SwingConstants.CENTER);
        initLabel(id, 140, 30);
        initLabel(studentId, 400, 30);
        initLabel(status, 90, 30);
        status.setFont(f);

        delete = new KulImageButton("delete", 13, 13);
        delete.addMouseListener(new ButtonListener());
        delete.setToolTipText(language.getString("delete"));
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setPreferredSize(new Dimension(15, 30));
        buttonBox.setMaximumSize(new Dimension(15, 30));
        buttonBox.add(delete);
        buttonBox.add(Box.createVerticalGlue());

        add(id);
        add(Box.createHorizontalStrut(8));
        add(studentId);
        add(Box.createHorizontalStrut(8));
        add(status);
        add(Box.createHorizontalStrut(8));
        add(total);
        add(Box.createHorizontalGlue());
        add(buttonBox);

        addMouseListener(new InvoiceLineActionListener());
        addMouseListener(new InvoiceLineViewListener());
    }

    // convenient method
    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setFont(f);
    }

    // convenient method
    private void setCurrentBackgroundColor() {
        if (invoice.isSelected()) {
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
    private class InvoiceLineViewListener extends MouseAdapter {

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

    private class InvoiceLineActionListener extends MouseAdapter implements ActionListener {

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
            invoice.setSelected(!invoice.isSelected());//toggle select
            setCurrentBackgroundColor();
            setBackground(currentBGColor);
            setBorder(overedBorder);
        }

        private void doubleClick(MouseEvent e) {
            parent.showInvoice(invoice);
        }
    }

    private class ButtonListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {//accept left mouse only
                int returnValue = JOptionPane.showConfirmDialog(mainFrame,
                        language.getString("deleteInvoiceConfirmContent"), language.getString("delete"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (returnValue == JOptionPane.OK_OPTION) {
                    if (mainFrame.getModel().deleteInvoice(invoice)) {
                        JOptionPane.showMessageDialog(mainFrame,
                                language.getString("invoiceDeleteSuccessContent"), language.getString("success"),
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame,
                                language.getString("invoiceDeleteFailContent"),
                                language.getString("fail"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(overedBorder);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(BorderFactory.createLineBorder(defaultBGColor, 2));
        }
    }
    // </editor-fold>
}
