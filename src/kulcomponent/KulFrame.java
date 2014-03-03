package kulcomponent;

import com.sun.awt.AWTUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import model.Class;
import model.ClassType;
import model.Invoice;
import model.Person;
import model.Room;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulFrame extends JFrame {

    private Object obIsBeingUsed;
    private boolean flag = true;
    public static final int FRAME_HEIGHT = 730;

    public KulFrame(String title) {
        this.setSize(new Dimension(1024, 730));
        this.setResizable(false);
        this.setTitle(Template.getFrameTitle());
        this.setLocationRelativeTo(null);
        this.setTitle(title);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag) {
                    closeWindow(true);
                } else {
                    flag = true;
                }
            }
        });
    }

    public KulFrame(final Object obIsBeingUsed, String title) {
        this(title);
        this.obIsBeingUsed = obIsBeingUsed;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setBeingUsed(false);
            }
        });
    }

    public void closeWindow(boolean isReleaseLock) {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        if (!isReleaseLock) {
            setBeingUsed(true);
        }
    }

    private void setBeingUsed(boolean isBeingUsed) {
        if (obIsBeingUsed == null) {
            System.out.println("KulFrame obIsBeingUsed == null => does not release lock");
            return;
        }
        if (obIsBeingUsed instanceof Person) {
            Person person = (Person) obIsBeingUsed;
            person.setBeingUsed(isBeingUsed);
        } else if (obIsBeingUsed instanceof Class) {
            Class c = (Class) obIsBeingUsed;
            c.setBeingUsed(isBeingUsed);
        } else if (obIsBeingUsed instanceof Room) {
            Room r = (Room) obIsBeingUsed;
            r.setBeingUsed(isBeingUsed);
        } else if (obIsBeingUsed instanceof Invoice) {
            Invoice inv = (Invoice) obIsBeingUsed;
            System.out.println("KulFrame releases lock for invocie");
            inv.setBeingUsed(isBeingUsed);
        } else if (obIsBeingUsed instanceof ClassType) {
            ClassType ct = (ClassType) obIsBeingUsed;
            ct.setBeingUsed(isBeingUsed);
        }
    }

    protected void disableCloseKeyBinding() {
        flag = false;
    }

    protected void enableCloseKeyBinding() {
        flag = true;
    }
}
