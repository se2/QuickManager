package quickmanage;

import java.awt.Frame;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.UIManager;
import model.Constants;
import model.Manager;
import model.Room;
import model.School;
import model.User;
import org.joda.time.LocalDate;
import viewcontroller.form.Dashboard;
import viewcontroller.LoginFrame;
import viewcontroller.MainFrame;
import viewcontroller.Template;

public class QuickManage {

    private static School model;
    private static LoginFrame login;
    private static MainFrame mainFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        model = new School();

        // <editor-fold defaultstate="collapsed" desc="initialize 8 rooms, should be deleted later">
        HashMap<Integer, Room> rooms = new HashMap<>();
        Room r1 = new Room(1, model.getLanguage().getString("piano1"), 1);
        rooms.put(1, r1);
        Room r2 = new Room(2, model.getLanguage().getString("piano2"), 1);
        rooms.put(2, r2);
        Room r3 = new Room(3, model.getLanguage().getString("piano3"), 1);
        rooms.put(3, r3);
        Room r4 = new Room(4, model.getLanguage().getString("guitarViolin"), 1);
        rooms.put(4, r4);
        Room r5 = new Room(5, model.getLanguage().getString("painting"), 20);
        rooms.put(5, r5);
        Room r6 = new Room(6, model.getLanguage().getString("vocal"), 20);
        rooms.put(6, r6);
        Room r7 = new Room(7, model.getLanguage().getString("organ"), 20);
        rooms.put(7, r7);
        Room r8 = new Room(8, model.getLanguage().getString("dance"), 20);
        rooms.put(8, r8);
        model.setRooms(rooms);
        // </editor-fold>

        initializeView();
    }

    /**
     * It just sets UI Look and Feel and create an object of LoginFrame. The
     * other views are created after user logs in successfully
     */
    //<editor-fold defaultstate="collapsed" desc="initializeView">
    private static void initializeView() {
        login = new LoginFrame(model);
        login.setVisible(true);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setSize(400, 170);
        login.setResizable(false);
        login.setTitle(Template.getFrameTitle());
        login.setLocationRelativeTo(null);

//        Manager dummyManager = new Manager("m", Constants.DEFAULT_PASSWORD, "Manager", "", "default", "", "", new LocalDate(), "", null, true);
//        showDashboard(dummyManager);
//        startApplication(dummyManager);
    }
    // </editor-fold>

    /**
     * This method is called when user logs-out. This method changes the view
     * only
     */
    // <editor-fold defaultstate="collapsed" desc="log out">
    public static void logout() {
        Frame[] frames = JFrame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            frames[i].dispose();
        }

        login.dispose();
        initializeView();
    }
    // </editor-fold>

    /**
     * this method is called after user logs-in successfully. This method is
     * called in LoginFrame
     */
    // <editor-fold defaultstate="collapsed" desc="startApp">
    public static void startApplication(User user) {
        login.setVisible(false);

        mainFrame = new MainFrame(model, user);
        mainFrame.setVisible(true);
        model.addObserver(mainFrame);
    }
    // </editor-fold>

    public static void showDashboard(User user) {
        login.setVisible(false);

        Dashboard dashboard = new Dashboard(model, user);
        dashboard.setVisible(true);
    }
}
