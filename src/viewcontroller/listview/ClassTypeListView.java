package viewcontroller.listview;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import model.ClassType;
import viewcontroller.KeyBindingSetter;
import viewcontroller.MainFrame;
import viewcontroller.Template;
import viewcontroller.TitleBar;
import viewcontroller.line.ClassTypeLine;

/**
 * @author Dam Linh
 */
public class ClassTypeListView extends JPanel {

    // <editor-fold defaultstate="collapsed" desc="GUI ONLY">
    private ArrayList<model.ClassType> classTypes;
    private JScrollPane scroll;
    private Box allClassTypesBox;
    private JPanel subCenterPanel;
    private JPanel centerPanel;
    private MainFrame mainFrame;
    private boolean isSmall;

    public ClassTypeListView(MainFrame mainFrame, boolean isSmall) {
        this.isSmall = isSmall;
        this.mainFrame = mainFrame;
        classTypes = mainFrame.getModel().getClassTypes();
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(15, 0, 0, 0, Template.getBackground()));//create top margin

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Template.getBackground());
        add(centerPanel);

        KeyBindingSetter.setWestButtonKeyBinding(mainFrame, this, "ClassType");

        subCenterPanel = new JPanel(new BorderLayout());
        centerPanel.add(subCenterPanel);

        drawCenterPanel();
    }

    private void drawCenterPanel() {
        allClassTypesBox = new Box(BoxLayout.Y_AXIS);
        scroll = new JScrollPane(allClassTypesBox);
        scroll.getViewport().setBackground(Template.getBackground());
        scroll.setBorder(null);
        subCenterPanel.add(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        subCenterPanel.add(new TitleBar(mainFrame, "classType"), BorderLayout.NORTH);

        if (isSmall) {
            drawAllClassTypes();
        }
    }

    private void drawAllClassTypes() {
        allClassTypesBox.removeAll();
        for (int i = classTypes.size() - 1; i >= 0; i--) {
            ClassType classType = mainFrame.getModel().getClassTypes().get(i);
            allClassTypesBox.add(new ClassTypeLine(mainFrame, classType));
        }
    }
    //</editor-fold>

    public void refresh() {
        drawAllClassTypes();
        allClassTypesBox.revalidate();
        allClassTypesBox.repaint();
    }
}
