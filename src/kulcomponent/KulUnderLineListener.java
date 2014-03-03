package kulcomponent;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 *
 * @author Dam Linh
 */
public class KulUnderLineListener extends MouseAdapter {

    private Font original;

    @Override
    public void mouseEntered(MouseEvent e) {
        original = e.getComponent().getFont();
        Map attributes = original.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        e.getComponent().setFont(original.deriveFont(attributes));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setFont(original);
    }
}
