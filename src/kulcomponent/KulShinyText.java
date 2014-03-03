package kulcomponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JEditorPane;
import javax.swing.Timer;
import javax.swing.text.html.HTMLEditorKit;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulShinyText extends JEditorPane {

    private String textDisplay;
    private Timer timer;
    private int curPosition = 1;
    private Font f;
    private String fontHTML;

    public KulShinyText(String text, Font f) {
        this.f = f;
        setForeground(Template.getForeground());
        setEditorKit(new HTMLEditorKit());
        setTextDisplay(text);
        setBackground(Template.getBackground());
        setEditable(false);
        init();
    }

    final public void setTextDisplay(String text) {
        this.textDisplay = text;
        String size = (int) f.getSize2D() + "";
        String family = f.getFamily();

        String hexCodeColor = Integer.toHexString(getForeground().getRGB());
        hexCodeColor = hexCodeColor.substring(2, hexCodeColor.length());

        fontHTML = "<font color='" + hexCodeColor + "' size = '" + size
                + "' face ='" + family + "'>";
        setText(stringToHTML(text));
    }

    private String stringToHTML(String s) {
        String temp = "<html>" + fontHTML + s + "</font></html>";
        return temp;
    }

    private void init() {
        timer = new Timer(60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (curPosition >= textDisplay.length() - 3) {
                    curPosition = 1;
                } else {
                    curPosition++;
                }
                styleText();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                timer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                timer.stop();
                setText(stringToHTML(textDisplay));
            }
        });
    }

    private void styleText() {
        Color color1 = Template.getBorderContrast();
        Color color2 = Template.getButtonBGMouseOver();
        Color color3 = color2.darker();
        Color color4 = color3.darker();

        String hexCodeColor1 = Integer.toHexString(color1.getRGB());
        String hexCodeColor2 = Integer.toHexString(color2.getRGB());
        String hexCodeColor3 = Integer.toHexString(color3.getRGB());
        String hexCodeColor4 = Integer.toHexString(color4.getRGB());
        hexCodeColor1 = hexCodeColor1.substring(2, hexCodeColor1.length());
        hexCodeColor2 = hexCodeColor2.substring(2, hexCodeColor2.length());
        hexCodeColor3 = hexCodeColor3.substring(2, hexCodeColor3.length());
        hexCodeColor4 = hexCodeColor4.substring(2, hexCodeColor4.length());

        String temp = textDisplay;

        String firstSub = temp.substring(0, curPosition - 1);
        char firstChar = temp.charAt(curPosition - 1);
        char secondChar = temp.charAt(curPosition);
        char thirdChar = temp.charAt(curPosition + 1);
        char forthChar = temp.charAt(curPosition + 2);
        String sndSub = temp.substring(curPosition + 3, temp.length());

        temp = firstSub
                + "<font color='#" + hexCodeColor1 + "'>" + firstChar + "</font>"
                + "<font color='#" + hexCodeColor2 + "'>" + secondChar + "</font>"
                + "<font color='#" + hexCodeColor3 + "'>" + thirdChar + "</font>"
                + "<font color='#" + hexCodeColor4 + "'>" + forthChar + "</font>" + sndSub;

        setText(stringToHTML(temp));
    }
}
