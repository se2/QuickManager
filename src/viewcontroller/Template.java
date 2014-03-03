package viewcontroller;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author Dam Linh
 */
public class Template {

    private static String currentTheme = "Wood";
    private static Color background = new Color(255, 255, 224);
    private static Color titleBar = new Color(255, 230, 125);
    private static Color buttonDefaultColor = new Color(255, 230, 125);
    private static Color buttonMouseOveredColor = new Color(255, 230, 125);
    private static Color buttonBGMouseOver = new Color(244, 164, 96, 40);
    private static Color borderContrast = new Color(244, 164, 60);
    private static Color buttonBGMouseClicked = new Color(255, 160, 122, 90);
    private static Color timeTableOccupied = Color.PINK;
    private static Color timeTableMouseOvered = new Color(255, 250, 205);
    private static Color timeTableSelected = new Color(255, 213, 140);
    private static Color lineBackground = new Color(255, 250, 205);
    private static Color lineBorderColor = new Color(222, 184, 135);
    private static Color buttonBG1 = new Color(20, 208, 255);
    private static Color buttonBG2 = new Color(216, 191, 216);
    private static Color buttonBG3 = new Color(250, 110, 100);
    private static Color buttonBG4 = new Color(254, 184, 24);
    private static Color buttonBG5 = new Color(107, 194, 141);
    private static Color buttonBG6 = new Color(255, 151, 66);
    private static Color buttonBG7 = new Color(235, 188, 127);
    private static Color foreground = new Color(139, 69, 19);
    private static Color paidColor = new Color(154, 205, 50);
    private static Color unPaidColor = new Color(254, 184, 24);
    private static URL photoURLDefault = Template.class.getClassLoader().getResource("Images/default.jpg");
    private static Font font = new Font("Arial", 0, 14);
    private static Font fontAlter = new Font("Tempus Sans ITC", 0, 14);
    private static String frameTitle = "QuickManage";
    // <editor-fold defaultstate="collapsed" desc="wood constants">
    private static final Color BACKGROUND_SUMMER = new Color(255, 255, 224);
    private static final Color TITLE_BAR_SUMMER = new Color(255, 230, 125);
    private static final Color BUTTON_DEFAULT_SUMMER = new Color(244, 164, 96);
    private static final Color BUTTON_MOUSE_OVER_SUMMER = new Color(222, 184, 135);
    private static final Color BUTTON_BG_MOUSE_OVER_SUMMER = new Color(244, 164, 96, 40);
    private static final Color BUTTON_BG_MOUSE_CLICK_SUMMER = new Color(255, 160, 122, 90);
    private static final Color BORDER_CONTRAST_SUMMER = new Color(244, 164, 96);
    private static final Color TIMETABLE_OCCUPIED_SUMMER = Color.PINK;
    private static final Color TIMETABLE_MOUSE_OVER_SUMMER = new Color(255, 240, 185);
    private static final Color TIMETABLE_SELECTED_SUMMER = new Color(255, 213, 140);
    private static final Color LINE_BACKGROUND_SUMMER = new Color(255, 250, 205);
    private static final Color LINE_BORDER_SUMMER = new Color(222, 184, 135);
    private static final Color BUTTON_BG_1_SUMMER = new Color(20, 208, 255);
    private static final Color BUTTON_BG_2_SUMMER = new Color(216, 191, 216);
    private static final Color BUTTON_BG_3_SUMMER = new Color(250, 110, 100);
    private static final Color BUTTON_BG_4_SUMMER = new Color(254, 184, 24);
    private static final Color BUTTON_BG_5_SUMMER = new Color(107, 194, 141);
    private static final Color BUTTON_BG_6_SUMMER = new Color(255, 151, 66);
    private static final Color BUTTON_BG_7_SUMMER = new Color(235, 188, 127);
    private static final Color FOREGROUND_SUMMER = new Color(139, 69, 19);
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ocean constants">
    private static final Color BACKGROUND_WINTER = new Color(224, 255, 255);
    private static final Color TITLE_BAR_WINTER = new Color(0, 191, 255);
    private static final Color BUTTON_DEFAULT_WINTER = new Color(0, 153, 204);
    private static final Color BUTTON_MOUSE_OVER_WINTER = new Color(51, 102, 153);
    private static final Color BUTTON_BG_MOUSE_OVER_WINTER = new Color(0, 153, 204, 40);
    private static final Color BUTTON_BG_MOUSE_CLICK_WINTER = new Color(0, 153, 204, 140);
    private static final Color BORDER_CONTRAST_WINTER = new Color(0, 153, 204);
    private static final Color TIMETABLE_OCCUPIED_WINTER = new Color(123, 104, 238);
    private static final Color TIMETABLE_MOUSE_OVER_WINTER = new Color(204, 255, 204);
    private static final Color TIMETABLE_SELECTED_WINTER = new Color(102, 204, 255);
    private static final Color LINE_BACKGROUND_WINTER = new Color(230, 255, 255);
    private static final Color LINE_BORDER_WINTER = new Color(0, 51, 102);
    private static final Color BUTTON_BG_1_WINTER = new Color(20, 208, 255);
    private static final Color BUTTON_BG_2_WINTER = new Color(216, 191, 216);
    private static final Color BUTTON_BG_3_WINTER = new Color(250, 110, 100);
    private static final Color BUTTON_BG_4_WINTER = new Color(254, 184, 24);
    private static final Color BUTTON_BG_5_WINTER = new Color(107, 194, 141);
    private static final Color BUTTON_BG_6_WINTER = new Color(255, 151, 66);
    private static final Color BUTTON_BG_7_WINTER = new Color(235, 188, 127);
    private static final Color FOREGROUND_WINTER = new Color(70, 130, 180);
    //</editor-fold>

    public static URL getPhotoURLDefault() {
        if (photoURLDefault == null) {
            JOptionPane.showMessageDialog(null, "Fatal ERROR: Missing file default.jpg", "Fatal ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return photoURLDefault;
    }

    public static void changeTheme(String theme) {
        switch (theme) {
            case "Wood":
                background = BACKGROUND_SUMMER;
                titleBar = TITLE_BAR_SUMMER;
                buttonDefaultColor = BUTTON_DEFAULT_SUMMER;
                buttonMouseOveredColor = BUTTON_MOUSE_OVER_SUMMER;
                buttonBGMouseClicked = BUTTON_BG_MOUSE_CLICK_SUMMER;
                buttonBGMouseOver = BUTTON_BG_MOUSE_OVER_SUMMER;
                timeTableMouseOvered = TIMETABLE_MOUSE_OVER_SUMMER;
                timeTableOccupied = TIMETABLE_OCCUPIED_SUMMER;
                timeTableSelected = TIMETABLE_SELECTED_SUMMER;
                lineBackground = LINE_BACKGROUND_SUMMER;
                lineBorderColor = LINE_BORDER_SUMMER;
                buttonBG1 = BUTTON_BG_1_SUMMER;
                buttonBG2 = BUTTON_BG_2_SUMMER;
                buttonBG3 = BUTTON_BG_3_SUMMER;
                buttonBG4 = BUTTON_BG_4_SUMMER;
                buttonBG5 = BUTTON_BG_5_SUMMER;
                buttonBG6 = BUTTON_BG_6_SUMMER;
                buttonBG7 = BUTTON_BG_7_SUMMER;
                foreground = FOREGROUND_SUMMER;
                borderContrast = BORDER_CONTRAST_SUMMER;
                currentTheme = "Wood";
                break;
            case "Ocean":
                background = BACKGROUND_WINTER;
                titleBar = TITLE_BAR_WINTER;
                buttonDefaultColor = BUTTON_DEFAULT_WINTER;
                buttonMouseOveredColor = BUTTON_MOUSE_OVER_WINTER;
                buttonBGMouseClicked = BUTTON_BG_MOUSE_CLICK_WINTER;
                buttonBGMouseOver = BUTTON_BG_MOUSE_OVER_WINTER;
                timeTableMouseOvered = TIMETABLE_MOUSE_OVER_WINTER;
                timeTableOccupied = TIMETABLE_OCCUPIED_WINTER;
                timeTableSelected = TIMETABLE_SELECTED_WINTER;
                lineBackground = LINE_BACKGROUND_WINTER;
                lineBorderColor = LINE_BORDER_WINTER;
                buttonBG1 = BUTTON_BG_1_WINTER;
                buttonBG2 = BUTTON_BG_2_WINTER;
                buttonBG3 = BUTTON_BG_3_WINTER;
                buttonBG4 = BUTTON_BG_4_WINTER;
                buttonBG5 = BUTTON_BG_5_WINTER;
                buttonBG6 = BUTTON_BG_6_WINTER;
                buttonBG7 = BUTTON_BG_7_WINTER;
                foreground = FOREGROUND_WINTER;
                borderContrast = BORDER_CONTRAST_WINTER;
                currentTheme = "Ocean";
                break;
        }
    }

    public static Color getPaidColor() {
        return paidColor;
    }

    public static void setPaidColor(Color paidColor) {
        Template.paidColor = paidColor;
    }

    public static Color getUnPaidColor() {
        return unPaidColor;
    }

    public static void setUnPaidColor(Color unPaidColor) {
        Template.unPaidColor = unPaidColor;
    }

    public static Color getButtonBG7() {
        return buttonBG7;
    }

    public static void setButtonBG7(Color buttonBG7) {
        Template.buttonBG7 = buttonBG7;
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    public static Color getBackground() {
        return background;
    }

    public static Color getTitleBar() {
        return titleBar;
    }

    public static Color getButtonDefaultColor() {
        return buttonDefaultColor;
    }

    public static Color getButtonMouseOveredColor() {
        return buttonMouseOveredColor;
    }

    public static Color getTimeTableOccupied() {
        return timeTableOccupied;
    }

    public static Color getTimeTableMouseOvered() {
        return timeTableMouseOvered;
    }

    public static Color getTimeTableSelected() {
        return timeTableSelected;
    }

    public static Color getLineBackground() {
        return lineBackground;
    }

    public static Color getLineBorderColor() {
        return lineBorderColor;
    }

    public static Color getButtonBG1() {
        return buttonBG1;
    }

    public static Color getButtonBG2() {
        return buttonBG2;
    }

    public static Color getButtonBG3() {
        return buttonBG3;
    }

    public static Color getButtonBG4() {
        return buttonBG4;
    }

    public static Color getButtonBG5() {
        return buttonBG5;
    }

    public static Color getForeground() {
        return foreground;
    }

    public static Font getFont() {
        return font;
    }

    public static Font getFontAlter() {
        return fontAlter;
    }

    public static String getFrameTitle() {
        return frameTitle;
    }

    public static Color getButtonBGMouseOver() {
        return buttonBGMouseOver;
    }

    public static void setButtonBGMouseOver(Color buttonBGMouseOver) {
        Template.buttonBGMouseOver = buttonBGMouseOver;
    }

    public static Color getButtonBGMouseClicked() {
        return buttonBGMouseClicked;
    }

    public static void setButtonBGMouseClicked(Color buttonBGMouseClicked) {
        Template.buttonBGMouseClicked = buttonBGMouseClicked;
    }

    public static Color getBorderContrast() {
        return borderContrast;
    }

    public static void setBorderContrast(Color borderContrast) {
        Template.borderContrast = borderContrast;
    }

    public static Color getButtonBG6() {
        return buttonBG6;
    }

    public static void setButtonBG6(Color buttonBG6) {
        Template.buttonBG6 = buttonBG6;
    }

    public static String getVNDString(long money){
        String temp = "";
        String salaryString = money + "";

        int count = 0;
        for (int i = salaryString.length() - 1; i >= 0; i--, count++) {
            if (count % 3 == 0) {
                if (temp.equals("")) {
                    temp = salaryString.charAt(i) + "";
                } else {
                    temp = salaryString.charAt(i) + "," + temp;
                }
            } else {
                temp = salaryString.charAt(i) + temp;
            }
        }
        return "VND " + temp;
    }
}
