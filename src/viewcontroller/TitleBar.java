package viewcontroller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Dam Linh
 *
 * This class is to draw the title bar in the XxxListView. It mostly to draw GUI
 * Action perform is not supported yet
 */
public class TitleBar extends Box {

    //<editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private MainFrame mainFrame;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private int w1;
    private int w2;
    private int w3;
    private int h = 40;
    private String type;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private Font f = Template.getFont().deriveFont(15f);
    private ResourceBundle language;

    public TitleBar(MainFrame mainFrame, String type) {
        super(BoxLayout.X_AXIS);
        this.mainFrame = mainFrame;
        this.type = type;
        this.setOpaque(true);
        this.setBackground(Template.getTitleBar());
        language = mainFrame.getModel().getLanguage();

        initStat();
        init();
    }

    private void initStat() {
        text1 = getString("id");
        switch (type) {
            case "class":
                text2 = getString("startDate");
                text3 = getString("endDate");
                text4 = getString("teacher");
                w1 = 80;
                w2 = 180;
                w3 = 180;
                break;
            case "account":
                text2 = getString("name");
                text3 = getString("accType");
                text4 = getString("email");
                w1 = 80;
                w2 = 190;
                w3 = 120;
                break;
            case "teacher":
                text2 = getString("name");
                text3 = getString("email");
                text4 = getString("skills");
                w1 = 80;
                w2 = 190;
                w3 = 240;
                break;
            case "room":
                text1 = getString("roomNumber");
                text2 = getString("roomCapacity");
                text3 = getString("roomName");
                w1 = 100;
                w2 = 100;
                w3 = 1000;
                break;
            case "invoice":
                text2 = getString("student");
                text3 = getString("status");
                text4 = getString("total");
                w1 = 140;
                w2 = 400;
                w3 = 90;
                break;
            case "classType":
                text1 = getString("class");
                text2 = getString("classType");
                text3 = getString("lessonPerWeek1");
                text4 = getString("remark");
                w1 = 150;
                w2 = 120;
                w3 = 120;
                break;
            default:
                text2 = getString("name");
                text3 = getString("email");
                text4 = getString("guardianContact");
                w1 = 80;
                w2 = 190;
                w3 = 240;
        }
    }

    private void init() {
        label1 = new JLabel(text1);
        label2 = new JLabel(text2);
        label3 = new JLabel(text3);
        label4 = new JLabel(text4);

        initLabel(label1, w1, h);
        add(Box.createHorizontalStrut(8));
        initLabel(label2, w2, h);
        add(Box.createHorizontalStrut(8));
        if (type.equals("room")) {
            label3.setPreferredSize(new Dimension(0, h));
            label3.setMaximumSize(new Dimension(1000, h));
            label3.setFont(f);
            label3.setHorizontalAlignment(SwingConstants.CENTER);
            label3.setBorder(new LineBorder(Template.getTitleBar(), 2));
            label3.addMouseListener(new TitleBarListener());
            add(label3);
        } else {
            initLabel(label3, w3, h);
            add(Box.createHorizontalStrut(8));
            label4.setPreferredSize(new Dimension(0, h));
            label4.setMaximumSize(new Dimension(1000, h));
            label4.setFont(f);
            label4.setHorizontalAlignment(SwingConstants.CENTER);
            label4.setBorder(new LineBorder(Template.getTitleBar(), 2));
            label4.addMouseListener(new TitleBarListener());
            add(label4);
        }
    }

    private void initLabel(JLabel label, int w, int h) {
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        label.setFont(f);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(new LineBorder(Template.getTitleBar(), 2));
        label.addMouseListener(new TitleBarListener());
        add(label);
    }

    // this listener is just to make beautiful view
    private class TitleBarListener extends MouseAdapter {

        private boolean isMouseOvered;
        private Color borderColor = Template.getLineBorderColor();
        private Border mouseClickedBorder = new CompoundBorder(new MatteBorder(0, 0, 0, 0, Template.getTitleBar()), new MatteBorder(2, 2, 2, 2, borderColor));
        private Border mouseOverBorder = new CompoundBorder(new MatteBorder(1, 1, 1, 1, borderColor), new MatteBorder(1, 2, 1, 0, Template.getTitleBar()));

        @Override
        public void mouseEntered(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            label.setBorder(mouseOverBorder);
            isMouseOvered = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            label.setBorder(new LineBorder(Template.getTitleBar(), 2));
            isMouseOvered = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            label.setBorder(mouseClickedBorder);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            if (isMouseOvered) {
                label.setBorder(mouseOverBorder);
            }
        }
    }

    private String getString(String key) {
        return language.getString(key);
    }
    //</editor-fold>
}
