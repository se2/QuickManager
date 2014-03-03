package kulcomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulComboBox<E> extends JComboBox<E> {

    private Font elementFont;
    private boolean disable = false;

    public KulComboBox() {
        init();
    }

    public KulComboBox(E[] items) {
        super(items);
        init();
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    private void init() {
        elementFont = Template.getFont().deriveFont(16f);
        this.setBackground(Template.getBackground());
        this.setUI(ColorArrowUI.createUI(this));
        this.setBorder(new LineBorder(Template.getBorderContrast()));
        this.setRenderer(new MyListCellRender());

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (disable) {
                    return;
                }
                if (e.getWheelRotation() < 0) {
                    if (KulComboBox.this.getSelectedIndex() <= 0) {
                        KulComboBox.this.setSelectedIndex(KulComboBox.this.getItemCount() - 1);
                    } else {
                        KulComboBox.this.setSelectedIndex(KulComboBox.this.getSelectedIndex() - 1);
                    }
                } else {
                    if (KulComboBox.this.getSelectedIndex() >= KulComboBox.this.getItemCount() - 1) {
                        KulComboBox.this.setSelectedIndex(0);
                    } else {
                        KulComboBox.this.setSelectedIndex(KulComboBox.this.getSelectedIndex() + 1);
                    }
                }
            }
        });
    }

    public void setElementFont(Font elementFont) {
        this.elementFont = elementFont;
        this.setRenderer(new MyListCellRender());
    }

    private class MyListCellRender implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final JLabel renderer = new JLabel(value.toString());
            renderer.setOpaque(true);
            renderer.setFont(KulComboBox.this.elementFont);
            renderer.setBackground(Template.getBackground());
            if (isSelected) {
                renderer.setForeground(Color.BLACK);
                renderer.setBackground(Template.getTimeTableSelected());
            }
            return renderer;
        }
    }
}

class ColorArrowUI extends BasicComboBoxUI {

    public static ComboBoxUI createUI(JComponent c) {
        return new ColorArrowUI();
    }

    @Override
    protected JButton createArrowButton() {
        return new BasicArrowButton(
                BasicArrowButton.SOUTH, Template.getBackground(),
                Template.getBorderContrast(), Template.getBorderContrast(),
                Template.getBorderContrast());
    }
}