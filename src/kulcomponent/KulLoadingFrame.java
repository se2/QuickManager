package kulcomponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import viewcontroller.Template;

/**
 * @author Dam Linh
 */
public class KulLoadingFrame extends JFrame {

    private JProgressBar progress = new JProgressBar();
    private JPanel container = new JPanel(new BorderLayout());

    public KulLoadingFrame(String title) {
        setUndecorated(true);
        setResizable(false);
        setTitle(title);

        container.setBackground(Template.getBackground());
        container.setBorder(new LineBorder(Template.getLineBorderColor(), 2));
        add(container);

        JLabel loadingString = new JLabel(title);
        loadingString.setFont(Template.getFont().deriveFont(12f));
        loadingString.setForeground(Template.getForeground());

        Box tempBox = new Box(BoxLayout.X_AXIS);
        tempBox.add(Box.createHorizontalGlue());
        tempBox.add(loadingString);
        tempBox.add(Box.createHorizontalGlue());
        container.add(tempBox, BorderLayout.SOUTH);
        container.add(progress);

        progress.setIndeterminate(true);
        progress.setUI(new MyProgressUI());
        progress.setBackground(Template.getBackground());
        pack();
        toFront();

        setLocationRelativeTo(null);
    }

    public void stopLoading() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
            }
        });
    }

    private static class MyProgressUI extends BasicProgressBarUI {

        private Rectangle r = new Rectangle();

        @Override
        protected void paintIndeterminate(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            r = getBox(r);

            Color color1 = Color.WHITE;
            Color color2 = Template.getBorderContrast();;
            GradientPaint gradient1 = new GradientPaint(0, 0, color2, r.width, r.height, color1, true);
            g2d.setPaint(gradient1);

            g.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);
        }
    }
}
