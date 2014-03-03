package kulcomponent;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Dam Linh
 */
public class KulImageButton extends JButton {

    private ImageIcon icon;
    private ImageIcon mouseOverIcon;

    public KulImageButton(String filename, int w, int h) {
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        URL url1 = this.getClass().getClassLoader().getResource("Images/" + filename + ".png");
        URL url2 = this.getClass().getClassLoader().getResource("Images/" + filename + "Over.png");
        icon = new ImageIcon(url1);
        mouseOverIcon = new ImageIcon(url2);

        Image img1 = icon.getImage();
        Image newImg1 = img1.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(newImg1));

        Image img2 = mouseOverIcon.getImage();
        Image newImg2 = img2.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        setRolloverIcon(new ImageIcon(newImg2));

        setPreferredSize(new Dimension(w + 2, h));
        setMaximumSize(new Dimension(w + 2, h));
    }
}
