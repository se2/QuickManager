package kulcomponent;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulPhoneChooser extends Box {

    private KulComboBox<String> zip;
    private JTextField t1;
    private JTextField t2;
    private JLabel hyphen = new JLabel(" - ");
    private Font f;
    private final int FOUR_DIGIT_WIDTH = 41;
    private JLabel phone = new JLabel();
    private int limitText1 = 4;
    private boolean isValid = false;

    // <editor-fold defaultstate="collapsed" desc="init">
    public KulPhoneChooser() {
        this(Template.getFont().deriveFont(16f));
    }

    public KulPhoneChooser(Font f) {
        // <editor-fold defaultstate="collapsed" desc="init">
        super(BoxLayout.X_AXIS);
        this.f = f;
        this.setPreferredSize(new Dimension(400, 25));
        this.setMaximumSize(new Dimension(400, 25));

        String[] prefix = new String[]{"08", "0120", "0121", "0122", "0123", "0124",
            "0125", "0126", "0127", "0128", "0129", "0163", "0164", "0165", "0166",
            "0167", "0168", "0169", "0188", "0199", "090", "091", "092", "093",
            "094", "095", "096", "097", "098", "099"};
        zip = new KulComboBox<>(prefix);
        zip.setFont(f);
        zip.setPreferredSize(new Dimension(75, 30));
        zip.setMaximumSize(new Dimension(75, 30));
        zip.addItemListener(new KulItemListener());

        t1 = new JTextField();
        t2 = new JTextField();
        t1.setPreferredSize(new Dimension(FOUR_DIGIT_WIDTH, 30));
        t1.setMaximumSize(new Dimension(FOUR_DIGIT_WIDTH, 30));
        t2.setPreferredSize(new Dimension(FOUR_DIGIT_WIDTH, 30));
        t2.setMaximumSize(new Dimension(FOUR_DIGIT_WIDTH, 30));
        t1.setBorder(new LineBorder(Template.getBorderContrast()));
        t2.setBorder(new LineBorder(Template.getBorderContrast()));
        hyphen.setFont(f.deriveFont(16f));
        t1.setFont(f);
        t2.setFont(f);
        t2.setDocument(new KulDocFilter());
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="keyListener">
        t1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c < '0' || c > '9') {
                    e.consume();
                } else {
                    isValid = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (t1.getText().length() >= limitText1) {
//                    if (isText1SelectAll) {
//                        isText1SelectAll = false;
//                    } else {
                    t2.requestFocus();
//                    }
                }
//                if (isText1SelectAll) {// this is NOT redundant
//                    isText1SelectAll = false;
//                }
            }
        });

        t2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c < '0' || c > '9') {
                    e.consume();
                } else {
                    isValid = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                char c = e.getKeyChar();
                if (c < '0' || c > '9') {
                    e.consume();
                }
                if (t2.getText().length() >= 4) {
//                    if (isText2SelectAll) {
//                        isText2SelectAll = false;
//                    } else {
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    manager.focusNextComponent();
//                    }
//                    if (isText2SelectAll) {// this is NOT redundant
//                        isText2SelectAll = false;
//                    }
                }
            }
        });
        //</editor-fold>

        phone.addMouseListener(new DoubleClickListener());
        phone.setFont(f);

        addFocusListener();
//        System.out.println("KulPhone before disableEdit in init() t1.getText = " + t1.getText());
        disableEdit();

        add(phone);
        add(zip);
        add(Box.createHorizontalStrut(14));
        add(t1);
        add(hyphen);
        add(t2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addFocusListener">
    private void addFocusListener() {
        zip.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                t1.requestFocus();
            }
        });

        t1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                t2.requestFocus();
            }

            @Override
            public void focusGained(FocusEvent e) {
                t1.selectAll();
            }
        });

        t2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("KulPhone.t2 loses focus: t1.getText = " + t1.getText());
                disableEdit();
            }

            @Override
            public void focusGained(FocusEvent e) {
                t2.selectAll();
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="enable/ disable">
    public void enableEdit() {
        zip.setVisible(true);
        t1.setVisible(true);
        t2.setVisible(true);
        hyphen.setVisible(true);
        phone.setVisible(false);
        zip.requestFocus();

        if (phone.getText().equals("")) {
            zip.setSelectedIndex(0);
            t1.setText("");
            t2.setText("");
        } else {
            String[] s = phone.getText().split("\\s-\\s");
            zip.setSelectedItem(s[0]);
            t1.setText(s[1]);
            t2.setText(s[2]);
        }
    }

    public void disableEdit() {
//        System.out.println("t1.getText before checkThenAppend " + t1.getText());
        checkThenAppend(t1, limitText1);
        checkThenAppend(t2, 4);

        phone.setText(zip.getSelectedItem() + " - " + t1.getText() + " - " + t2.getText());

        zip.setVisible(false);
        t1.setVisible(false);
        t2.setVisible(false);
        hyphen.setVisible(false);
        phone.setVisible(true);
    }

    private void checkThenAppend(JTextField tf, int limit) {
        String s = tf.getText();
//        System.out.println("kulPhoneChooser.checkThenAppend() line 226: " + s);
        int length = s.length();
        if (length < limit) {
            for (int i = 0; i < limit - length; i++) {
                s += "0";
                isValid = false;
            }
        } else {
            s = s.substring(0, limit);
        }
        tf.setText(s);
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
    //</editor-fold>

    public String getPhone() {
        disableEdit();
        return phone.getText();
    }

    public void setPhone(String phone) {
        if (phone.equals("")) {
            isValid = false;
//            System.out.println("phone chooser set isValid = false");
            phone = "08 - 0000 - 0000";
            this.phone.setText(phone);
        } else {
            isValid = true;
            this.phone.setText(phone);
            String[] s = this.phone.getText().split("\\s-\\s");
            zip.setSelectedItem(s[0]);
            t1.setText(s[1]);
            t2.setText(s[2]);
        }
//        System.out.println("KulPhone.setPhone() phone.getText = " + this.phone.getText());
    }

    // <editor-fold defaultstate="collapsed" desc="listeners">
    private class KulDocFilter extends PlainDocument {

        int limit = 4;

        public KulDocFilter() {
            setDocumentFilter(null);
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

    private class KulItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (zip.getSelectedIndex() == 0) {// home phone
//                    filter1.limit = 4;
                    limitText1 = 4;
                } else { // mobile phone
//                    filter1.limit = 3;
                    limitText1 = 3;
                }
                t1.setText("");
                t2.setText("");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="double click listener">
    private class DoubleClickListener extends MouseAdapter implements ActionListener {

//        private int interval = (Integer) Toolkit.getDefaultToolkit().
//                getDesktopProperty("awt.multiClickInterval");
//        private Timer timer = new Timer(interval, this);
//        private MouseEvent lastEvent;
        @Override
        public void mouseClicked(MouseEvent e) {
//            if (e.getClickCount() > 2 || SwingUtilities.isRightMouseButton(e)) {
//                return;
//            }
//            lastEvent = e;
//
//            if (timer.isRunning()) {
//                timer.stop();
//                doubleClick(lastEvent);
//            } else {
//                timer.restart();
//            }
            doubleClick(e);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            timer.stop();
        }

        private void doubleClick(MouseEvent e) {
            enableEdit();
        }
    }
    // </editor-fold>
}
