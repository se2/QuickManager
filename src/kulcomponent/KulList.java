package kulcomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulList<E> extends JList<E> {

    private Font elementFont;

    public KulList(E[] listData) {
        super(listData);
        init();
    }

    public KulList() {
        this(null);
    }

    private void init() {
        elementFont = Template.getFont().deriveFont(16f);
        this.setBackground(Template.getBackground());
        this.setCellRenderer(new MyListCellRender());
    }

    public void setElementFont(Font elementFont) {
        this.elementFont = elementFont;
        this.setCellRenderer(new MyListCellRender());
    }

    class MyListCellRender implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final JLabel renderer = new JLabel(value.toString());
            renderer.setOpaque(true);
            renderer.setFont(KulList.this.elementFont);
            renderer.setBackground(Template.getBackground());
            if (isSelected) {
                renderer.setForeground(Color.BLACK);
                renderer.setBackground(Template.getTimeTableSelected());
            }
            return renderer;
        }
    }
}
