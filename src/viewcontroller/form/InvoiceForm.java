package viewcontroller.form;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLEditorKit;
import kulcomponent.KulButton;
import kulcomponent.KulComboBox;
import kulcomponent.KulDayChooser;
import kulcomponent.KulFrame;
import kulcomponent.KulTextArea;
import model.ClassType;
import model.Invoice;
import model.Manager;
import model.Student;
import model.StudentClass;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class InvoiceForm extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="variable declaration">
    private ResourceBundle language;
    private Invoice invoice;
    private JLabel noteTitle;
    private KulTextArea note;
    private JPanel left;
    private JPanel subLeft;
    private JEditorPane content = new JEditorPane();
    private MainFrame mainFrame;
    private Student studentObj;
    private KulFrame parentFrame;
    private JScrollPane scroll;
    private KulButton ok = new KulButton("Ok");
    private KulButton changePaid;
    private KulButton print;
    private KulButton delete;
    private SpringLayout spring;
    private Box buttons;
    private String head;
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMMM yyyy");
    private Font f = Template.getFont().deriveFont(16f);
    private String hexCodeColor;
    private long totalFeeBeforeMinusbalance = 0;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    public InvoiceForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Template.getBackground());
        setLayout(new BorderLayout());
        language = mainFrame.getModel().getLanguage();

        left = new JPanel(new BorderLayout());
        left.setBackground(Template.getBackground());
        spring = new SpringLayout();
        subLeft = new JPanel(spring);
        subLeft.setBackground(Template.getBackground());
        left.add(subLeft);
        add(left);

        noteTitle = new JLabel(getString("paidNote"));
        changePaid = new KulButton(getString("markAsPaid"));
        print = new KulButton(getString("print"));
        delete = new KulButton(getString("delete"));

        //<editor-fold defaultstate="collapsed" desc="keyBiding to OK">
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

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_DELETE, 0, true), "delete");
        this.getActionMap().put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete invoice in key binding");
                delete();
            }
        });
        //</editor-fold>

        init();
        drawButtons();
    }

    private void init() {
        scroll = new JScrollPane(content);
        scroll.setBorder(null);
        content.setBackground(Template.getBackground());
        content.setEditorKit(new HTMLEditorKit());
        content.setEditable(false);
        subLeft.add(scroll);

        note = new KulTextArea(3, 28);
        note.setFont(f.deriveFont(12f));
        note.setEditable(true);
        subLeft.add(note);

        noteTitle.setFont(f);
        noteTitle.setFocusable(false);
        noteTitle.setForeground(Template.getForeground());
        subLeft.add(noteTitle);

        setSpring();

        Color color = Template.getBorderContrast();
        hexCodeColor = Integer.toHexString(color.getRGB());
        hexCodeColor = hexCodeColor.substring(2, hexCodeColor.length());

        head = "<html><body style='margin:0px;font-family:Arial;"
                + " font-size:11px'><div style='font-size:45px;'><font color='#"
                + hexCodeColor + "'>" + getString("invoice") + "</font></div><br/>"
                + "<div style='margin-left:35px;margin-right:25px;'><div>" + getString("name") + " ";
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setSpring">
    private void setSpring() {
        spring.putConstraint(SpringLayout.NORTH, scroll, 5, SpringLayout.NORTH, subLeft);
        spring.putConstraint(SpringLayout.EAST, scroll, -8, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.SOUTH, scroll, -5, SpringLayout.NORTH, noteTitle);
        spring.putConstraint(SpringLayout.WEST, scroll, 15, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.SOUTH, noteTitle, -3, SpringLayout.NORTH, note);
        spring.putConstraint(SpringLayout.WEST, noteTitle, 10, SpringLayout.WEST, subLeft);

        spring.putConstraint(SpringLayout.EAST, note, -10, SpringLayout.EAST, subLeft);
        spring.putConstraint(SpringLayout.SOUTH, note, 0, SpringLayout.SOUTH, subLeft);
        spring.putConstraint(SpringLayout.WEST, note, 10, SpringLayout.WEST, subLeft);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawButtons">
    private void drawButtons() {
        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setPreferredSize(new Dimension(0, 50));
        buttons.setAlignmentX(BOTTOM_ALIGNMENT);
        left.add(buttons, BorderLayout.SOUTH);

        delete.setPreferredSize(new Dimension(80, 30));
        delete.setMaximumSize(new Dimension(80, 30));
        delete.setFont(f);

        ok.setPreferredSize(new Dimension(80, 30));
        ok.setMaximumSize(new Dimension(80, 30));
        ok.setFont(f);

        print.setPreferredSize(new Dimension(80, 30));
        print.setMaximumSize(new Dimension(80, 30));
        print.setFont(f);

        changePaid.setPreferredSize(new Dimension(110, 30));
        changePaid.setMaximumSize(new Dimension(110, 30));
        changePaid.setFont(f.deriveFont(15f));

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

        changePaid.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int returnValue = JOptionPane.showConfirmDialog(parentFrame,
                        getString("changeInvoiceConfirmContent"),
                        getString("caution"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (returnValue == JOptionPane.OK_OPTION) {
                    changePaid();
                }
            }
        });

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                delete();
            }
        });

        buttons.add(Box.createHorizontalGlue());
        buttons.add(delete);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(changePaid);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(print);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(20));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setInvoice">
    public boolean setInvoice(Invoice invoice, KulFrame parentFrame) {
        this.invoice = invoice;
        this.parentFrame = parentFrame;

        if (invoice.isBeingUsed()) {
            JOptionPane.showMessageDialog(parentFrame,
                    getString("invoiceBeingUsed"), getString("caution"), JOptionPane.INFORMATION_MESSAGE);
            parentFrame.closeWindow(false);
            return false;
        }
        invoice.setBeingUsed(true);

        studentObj = mainFrame.getModel().getStudents().get(invoice.getStudentClass().get(0).getStudentId());

        if (invoice.isPaid()) {
            note.setText(invoice.getPaidNote());
            note.setVisible(true);
            noteTitle.setVisible(true);
        } else {
            note.setVisible(false);
            noteTitle.setVisible(false);
        }

        if (mainFrame.getUser() instanceof Manager) {
            delete.setVisible(true);
        } else {
            delete.setVisible(false);
        }

        drawChangePaidButton();
        drawContent();
        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawContent">
    private void drawContent() {
        StringBuilder s = new StringBuilder(head);

        s.append(studentObj.getFullname());
        s.append("<br/>").append(getString("student")).append(" ID: ").append(studentObj.getId());
        s.append("<br/>").append(getString("email")).append(" ").append(studentObj.getEmail());

        s.append("<br/></div><br/><div>").append(getString("invoice")).append(" ").append("ID: ");
        s.append(invoice.getId());

        if (invoice.isPaid()) {
            s.append("<br/>").append(getString("status")).append(" ").append(getString("paid")).append("</font>");
            LocalDate paidDate = invoice.getPaidDate();
            s.append("<br/>").append(getString("paidDate")).append(" ").append(fmt.print(paidDate));
            s.append("<br/>").append("paidMethod").append(" ").append(invoice.getPaidMethod());
        } else {
            s.append("<br/>").append(getString("status")).append(" ").append(getString("unpaid")).append("</font>");
        }

        s.append("<br/></div></div><br/><hr></hr>");
        s.append("<div><table border='0'>");

        s.append("<tr><td width='90' style='max-width:90'><b>").append(getString("classCode")).append("</b></td>");
        s.append("<td width='330' style='max-width:330'><b>").append(getString("className")).append("</b></td>");
        s.append("<td width='130' style='max-width:130'><b>").append(getString("tuitionFee0")).append("</b></td></tr>");

        ArrayList<StudentClass> stuClasses = invoice.getStudentClass();
        for (int i = 0; i < stuClasses.size(); i++) {
            model.Class c = mainFrame.getModel().getClasses().get(stuClasses.get(i).getClassId());
//            ClassType ct = mainFrame.getModel().getClassType(c);
            totalFeeBeforeMinusbalance += c.getFee();
            s.append(drawHTMLRow(c.getClassId(), c.getClassName(), c.getTuitionFeeString()));
        }

        s.append("</table>");
        s.append("<br/>");
        s.append("<hr></hr>");

        //balance row
        s.append("<table border='0'>");
        s.append("<tr><td width='110px' style='max-width:110px'></td>" // intentional make it 110px
                + "<td width='330px' style='max-width:330px' align='subLeft'><b>").append(getString("balance")).append(" </b></td>");
        s.append("<td width='130px' style='max-width:130px; word-wrap:break-word'>-")
                .append(invoice.getBalanceString()).append("</td></tr>");

        //total fee row
        s.append("<tr><td width='110px' style='max-width:110px'></td>" // intentional make it 110px
                + "<td width='330px' style='max-width:330px' align='subLeft'><b>").append(getString("total")).append(" </b></td>");
        s.append("<td width='130px' style='max-width:130px; word-wrap:break-word'>")
                .append(invoice.getTotalFeeString()).append("</td></tr>");

        s.append("</table></div>");
        s.append("</body></html>");
        content.setText(s.toString());
        revalidate();
        repaint();
    }

    private String drawHTMLRow(String id, String name, String fee) {
        String s = "<tr><td width='90px' style='max-width:90px; word-wrap:break-word'>" + id + "</td>"
                + "<td width='330px' style='max-width:330px; word-wrap:break-word'>" + name + "</td>"
                + "<td width='130px' style='max-width:130px; word-wrap:break-word'>" + fee + "</td></tr>";
        return s;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawChangePaidButton">
    private void drawChangePaidButton() {
        if (invoice.isPaid()) {
            changePaid.setTextDisplay(getString("markAsUnpaid"));
        } else {
            changePaid.setTextDisplay(getString("markAsPaid"));
        }

        if (!(mainFrame.getUser() instanceof Manager) && invoice.isPaid()) {
            changePaid.setVisible(false);
        } else {
            changePaid.setVisible(true);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="action methods for buttons">
    private void delete() {
        int returnValue = JOptionPane.showConfirmDialog(parentFrame,
                getString("deleteInvoiceConfirmContent"), getString("deleteConfirm"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (returnValue == JOptionPane.OK_OPTION) {
            if (mainFrame.getModel().deleteInvoice(invoice)) {
                JOptionPane.showMessageDialog(parentFrame,
                        getString("invoiceDeleteSuccessContent"), getString("success"),
                        JOptionPane.INFORMATION_MESSAGE);
                parentFrame.closeWindow(true);
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        getString("invoiceDeleteFailContent"),
                        getString("fail"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void ok() {
        invoice.setPaidNote(note.getText());
        mainFrame.getMenuView().update("Invoice");
        mainFrame.getModel().saveData();
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

    private void changePaid() {
        invoice.setPaid(!invoice.isPaid());
        if (invoice.isPaid()) {
            note.setVisible(true);
            noteTitle.setVisible(true);
            PaidMethodChooserFrame frame = new PaidMethodChooserFrame();
            frame.setVisible(true);
            parentFrame.setEnabled(false);

            Student student = mainFrame.getModel().getStudents().get(invoice.getStudentClass().get(0).getStudentId());

            if (student.getBalance() - totalFeeBeforeMinusbalance < 0) {
                student.setBalance(0);
            } else {
                student.setBalance(student.getBalance() - totalFeeBeforeMinusbalance);
            }
        } else {
            invoice.setPaidDate(null);
            invoice.setPaidMethod("");
            invoice.setPaidNote("");
            note.setVisible(false);
            noteTitle.setVisible(false);
            drawContent();
        }

        mainFrame.getModel().saveData();
        drawChangePaidButton();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="inner class PaidMethodChooser">
    private class PaidMethodChooserFrame extends JFrame {

        private JLabel methodTitle = new JLabel(getString("paidMethod"));
        private JLabel dateTitle = new JLabel(getString("paidDate"));
        private KulComboBox<String> method = new KulComboBox<>(new String[]{getString("cash"), getString("bankTransfer"), getString("creditCard")});
        private KulDayChooser date;
        private Box container = new Box(BoxLayout.Y_AXIS);
        private Box methodBox = new Box(BoxLayout.X_AXIS);
        private Box dateBox = new Box(BoxLayout.X_AXIS);
        private Box buttonsBox = new Box(BoxLayout.X_AXIS);
        private KulButton ok = new KulButton("Ok");

        public PaidMethodChooserFrame() {
            this.setSize(new Dimension(480, 110));
            this.setUndecorated(true);
            this.setResizable(false);
            this.add(container);
            this.setLocationRelativeTo(parentFrame);
            this.setTitle(getString("pleaseChoosePaidMethod"));

            this.getRootPane().setFocusable(false);
            this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "exit");
            this.getRootPane().getActionMap().put("exit", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeWindow();
                }
            });

            container.setOpaque(true);
            container.setBackground(Template.getBackground());
            container.setBorder(new CompoundBorder(
                    new LineBorder(Template.getLineBorderColor(), 4),
                    new LineBorder(Template.getBackground(), 5)));
            container.add(Box.createVerticalStrut(5));
            container.add(methodBox);
            container.add(Box.createVerticalStrut(8));
            container.add(dateBox);
            container.add(Box.createVerticalStrut(8));
            container.add(buttonsBox);

            methodTitle.setFont(f);
            methodTitle.setForeground(Template.getForeground());
            method.setFont(f);

            methodBox.add(Box.createHorizontalStrut(5));
            methodBox.add(methodTitle);
            methodBox.add(Box.createHorizontalStrut(15));
            methodBox.add(method);
            methodBox.add(Box.createHorizontalStrut(200));

            date = new KulDayChooser(f, 2000, new LocalDate().getYear(), 10, false, language);
            dateTitle.setFont(f);
            dateTitle.setForeground(Template.getForeground());

            dateBox.add(Box.createHorizontalStrut(5));
            dateBox.add(dateTitle);
            dateBox.add(Box.createHorizontalStrut(35));
            dateBox.add(date);
            methodBox.add(Box.createHorizontalStrut(5));

            drawButtons();
        }

        private void drawButtons() {
            ok.setPreferredSize(new Dimension(80, 25));
            ok.setMaximumSize(new Dimension(80, 25));
            buttonsBox.add(Box.createVerticalGlue());
            buttonsBox.add(ok);
            buttonsBox.add(Box.createVerticalGlue());

            ok.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    invoice.setPaidMethod((String) method.getSelectedItem());
                    invoice.setPaidDate(date.getDate());
                    InvoiceForm.this.drawContent();
                    InvoiceForm.this.mainFrame.getModel().saveData();
                    closeWindow();
                }
            });
        }

        private void closeWindow() {
            this.setVisible(false);
            parentFrame.setEnabled(true);
            parentFrame.setVisible(true);
        }
    }
    //</editor-fold>

    private String getString(String key) {
        return language.getString(key);
    }
}
