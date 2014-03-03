package kulcomponent;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulTextField extends JTextField {

    public KulTextField(String text) {
        super(text);
        init();
    }

    public KulTextField() {
        init();
    }

    public KulTextField(int column) {
        super(column);
        init();
    }

    private void init() {
//        setEditable(false);
        setBorder(new EmptyBorder(5, 3, 5, 3));
        setBackground(Template.getBackground());

        addMouseListener(new Listener());
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
//                setEditable(false);
                setBorder(new EmptyBorder(5, 3, 5, 3));
            }

            @Override
            public void focusGained(FocusEvent e) {
//                setEditable(true);
                setBorder(new CompoundBorder(
                        new LineBorder(Template.getBorderContrast()),
                        new EmptyBorder(3, 3, 2, 3)));
                KulTextField.this.selectAll();
            }
        });
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        if (editable) {
            setBorder(new CompoundBorder(
                    new LineBorder(Template.getBorderContrast()),
                    new EmptyBorder(3, 3, 2, 3)));
        } else {
            setBorder(new EmptyBorder(5, 3, 5, 3));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="double click listener">
    private class Listener extends MouseAdapter implements ActionListener {

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
        }

        private void doubleClick(MouseEvent e) {
            setEditable(true);
        }
    }
    // </editor-fold>
}
