package kulcomponent;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

/**
 * @author Dam Linh
 */
public class KulBorder extends AbstractBorder {

    private int size;
    private Color color;
    private float alpha;

    public KulBorder(Color outerColor, int size, float alpha) {
        this.size = size;
        this.color = outerColor;
        this.alpha = alpha;
    }

    public KulBorder() {
        this(new Color(0, 0, 0), 3, 0.3f);
    }

    public KulBorder(int size, float alpha) {
        this(new Color(0, 0, 0), size, alpha);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(size, size, size, size);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = size;
        insets.left = size;
        insets.bottom = size;
        insets.right = size;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (color != null) {
            g2d.setColor(color);
            g2d.drawRect(0, 0, width - 1, height - 1);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP, alpha));

        g2d.fillRect(0, 0, width - size, size);
        g2d.fillRect(width - size, 0, size, height - size);
        g2d.fillRect(0, size, size, height - size);
        g2d.fillRect(size, height - size, width - size, size);
    }
}
