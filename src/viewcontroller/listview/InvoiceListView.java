package viewcontroller.listview;

import viewcontroller.line.InvoiceLine;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import model.Invoice;
import model.Student;
import viewcontroller.KeyBindingSetter;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;

/**
 *
 * @author Dam Linh
 */
public class InvoiceListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ArrayList<model.Invoice> invoices;
    private JScrollPane scroll;
    private Box allInvoiceBox;
    private JPanel subCenterPanel;
    private JPanel centerPanel;
//    private Box buttonBar;
//    private KulButton select;
//    private KulButton add;
    private JTextField filter;
    private MainFrame mainFrame;
//    private KulFrame parentFrame;
//    private KulFrame prevFrame;
//    private HasReturn hasReturn;
    private boolean isSmall;

    public InvoiceListView(MainFrame mainFrame, boolean isSmall) {
        this.isSmall = isSmall;
        this.mainFrame = mainFrame;
        invoices = mainFrame.getModel().getInvoices();
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Template.getBackground());
        add(centerPanel);

        KeyBindingSetter.setWestButtonKeyBinding(mainFrame, this, "Invoice");

        filter = new JTextField(100);
        filter.setToolTipText("Filter list");
        filter.setFont(Template.getFont().deriveFont(16f));
        filter.setPreferredSize(new Dimension(0, 30));
        subCenterPanel = new JPanel(new BorderLayout());
        centerPanel.add(subCenterPanel);
//        centerPanel.add(filter, BorderLayout.NORTH);

        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allInvoiceBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allInvoiceBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        subCenterPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        subCenterPanel.add(new TitleBar(mainFrame, "invoice"), BorderLayout.NORTH);

        if (isSmall) {
            drawAllInvoices();
        }
    }

    private void drawAllInvoices() {
        allInvoiceBox.removeAll();
        for (int i = invoices.size() - 1; i >= 0; i--) {
            Student student = mainFrame.getModel().getStudents().
                    get(invoices.get(i).getStudentClass().get(0).getStudentId());
            allInvoiceBox.add(new InvoiceLine(mainFrame, this, invoices.get(i), student));
        }
    }
    //</editor-fold>

    public void refresh() {
        drawAllInvoices();
        allInvoiceBox.revalidate();
        allInvoiceBox.repaint();
    }
    /*
     * invoked in the InvoiceLine to display the view of a class.
     */

    public void showInvoice(Invoice invoice) {
        mainFrame.showInvoiceForm(invoice);
    }
}
