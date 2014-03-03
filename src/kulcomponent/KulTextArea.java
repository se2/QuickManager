package kulcomponent;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulTextArea extends JScrollPane {

    private JTextArea textArea;

    public KulTextArea(String text) {
        textArea = new JTextArea(text);
        init();
    }

    public KulTextArea(int row, int column) {
        textArea = new JTextArea(row, column);
        init();
    }

    public KulTextArea() {
        init();
    }

    private void init() {
        setViewportView(textArea);
        setBorder(null);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        textArea.setBackground(Template.getBackground());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.addMouseListener(new Listener());
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setBorder(null);
            }

            @Override
            public void focusGained(FocusEvent e) {
                setBorder(new LineBorder(Template.getBorderContrast()));
                KulTextArea.this.textArea.selectAll();
            }
        });
    }

    public void setEditable(boolean editable) {
        textArea.setEditable(editable);
        if (editable) {
            setBorder(new LineBorder(Template.getBorderContrast()));
        } else {
            setBorder(null);
        }
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public String getText() {
        return textArea.getText();
    }

    @Override
    public void setEnabled(boolean enabled) {
        textArea.setEnabled(enabled);
    }

    @Override
    public void requestFocus() {
        textArea.requestFocus();
    }

    @Override
    public boolean hasFocus() {
        return textArea.hasFocus();
    }

    @Override
    public void setFont(Font f) {
        super.setFont(f);
        if (textArea != null) {
            textArea.setFont(f);
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
