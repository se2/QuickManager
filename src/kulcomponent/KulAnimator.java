package kulcomponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 *
 * @author Dam Linh
 */
public class KulAnimator {

    private static int delay = 5;
    private Timer timer;
    private JComponent parent;
    private JComponent child;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private int width;
    private int height;
    private int d;

    public KulAnimator(JComponent parent, JComponent child,
            int x1, int x2, int y1, int y2, int width, int height) {
        this.parent = parent;
        this.child = child;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.width = width;
        this.height = height;
        if (x1 < x2) {
            d = 30;
        } else {
            d = -30;
        }
    }

    public void slideHorizontal() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Math.abs(x1 - x2) < Math.abs(d)) {// when animation almost finishes
                    //then do the last animation
                    child.setBounds(x2, y2, width, height);
                    timer.stop();
                } else {
//                    decrease d after half of the way
//                    int halfWay = Math.abs((x2 - x1) / 2);
//                    if ((halfWay - d) > x1 && (halfWay + d) < x1) {
//                        if (d < 0) {
//                            System.out.println("animation slows down");
//                            d = -0;
//                        }
//                    }
                    x1 += d;
                    child.setBounds(x1, y1, width, height);
                }
                parent.revalidate();
                parent.repaint();
            }
        });

        timer.start();
    }

    public void scaleHorizontal(final int width2) {
        if (width < width2) {
            d = 20;
        } else {
            d = -20;
        }
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Math.abs(width - width2) < Math.abs(d)) {// when animation almost finishes
                    //then do the last animation
                    child.setBounds(x2, y2, width2, height);
                    timer.stop();
                } else {
                    width += d;
                    child.setBounds(x1, y1, width, height);
                }
                parent.revalidate();
                parent.repaint();
            }
        });

        timer.start();
    }
}
