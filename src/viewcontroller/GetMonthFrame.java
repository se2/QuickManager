package viewcontroller;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import kulcomponent.KulButton;
import kulcomponent.KulDayChooser;
import kulcomponent.KulFrame;
import org.joda.time.LocalDate;

/**
 *
 * @author Dam Linh
 */
public class GetMonthFrame extends JFrame {

    private ResourceBundle language;
    private MainFrame mainFrame;
    private KulButton generate;
    private Box generateBox = new Box(BoxLayout.X_AXIS);
    private Box container;
    private KulFrame parentFrame; // to show JOptionPane
    private Font f = Template.getFont();
    private KulDayChooser dayChooser;

    public GetMonthFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        language = mainFrame.getModel().getLanguage();

        generate = new KulButton(getString("getReport"));

        setSize(new Dimension(285, 92));
        setUndecorated(true);
        setResizable(false);
        setTitle(getString("chooseAMonth"));
        setLocationRelativeTo(mainFrame);

        LocalDate today = new LocalDate();
        dayChooser = new KulDayChooser(f, 1990, today.getYear(), 25, true, language);

        container = new Box(BoxLayout.Y_AXIS);
        container.setOpaque(true);
        container.setBorder(new CompoundBorder(
                new LineBorder(Template.getLineBorderColor(), 4),
                new LineBorder(Template.getBackground(), 10)));
        container.setBackground(Template.getBackground());
        add(container);

        container.add(dayChooser);
        container.add(Box.createVerticalStrut(12));
        container.add(generateBox);

        setKeyBinding();
        drawButton();
    }

    private void drawButton() {
        generate.setPreferredSize(new Dimension(90, 25));
        generate.setMaximumSize(new Dimension(90, 25));
        generate.setFont(f);
        generate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LocalDate date = dayChooser.getDate();
                if (date.compareTo(new LocalDate()) > 0) {
                    JOptionPane.showMessageDialog(rootPane, getString("futureReportWarningContent"),
                            getString("caution"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    closeWindow();
                    mainFrame.showReportForm(date);
                }
            }
        });

        generateBox.add(Box.createHorizontalGlue());
        generateBox.add(generate);
        generateBox.add(Box.createHorizontalGlue());
    }

    private void setKeyBinding() {
        getRootPane().setFocusable(false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "exit");
        getRootPane().getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });
    }

    private void closeWindow() {
        this.setVisible(false);
        mainFrame.setEnabled(true);
        mainFrame.setState(Frame.NORMAL);
        mainFrame.toFront();
    }

    private String getString(String key) {
        return language.getString(key);
    }
}
