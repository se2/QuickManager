package kulcomponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import model.ClassType;
import viewcontroller.MainFrame;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public class KulSkillChooser extends Box {

    private ResourceBundle language;
    private ArrayList<KulComboBox> skillGUI = new ArrayList<>();
    private ArrayList<KulTextField> payRateGUI = new ArrayList<>();
    private ArrayList<Box> lines = new ArrayList<>();
    private JScrollPane scroll;
    private Box container;
    private MainFrame mainFrame;
    private String[] allSkills;
    private ArrayList<Integer> hiddenIndex = new ArrayList<>();
    private Font f = Template.getFont().deriveFont(16f);

    public KulSkillChooser(MainFrame mainFrame) {
        super(BoxLayout.Y_AXIS);
        this.mainFrame = mainFrame;
        language = mainFrame.getModel().getLanguage();

        container = new Box(BoxLayout.Y_AXIS);

        //<editor-fold defaultstate="collapsed" desc="top">
        Box top = new Box(BoxLayout.X_AXIS);
        JLabel title = new JLabel(language.getString("skills1"));
        title.setFont(f);
        title.setForeground(Template.getForeground());
        title.setFocusable(false);

        KulImageButton add = new KulImageButton("add", 20, 20);
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addSkill();
            }
        });

        top.add(title);
        top.add(Box.createHorizontalGlue());
        top.add(add);
//        top.add(Box.createHorizontalStrut(10));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="getAllSkills">
        ArrayList<ClassType> allCT = mainFrame.getModel().getClassTypes();
        ArrayList<String> skillsString = new ArrayList(allCT.size());
        outer:
        for (int i = 0; i < allCT.size(); i++) {
            // check duplicate
            for (int j = 0; j < skillsString.size(); j++) {
                if (j >= skillsString.size() || skillsString.get(j) == null) {
                    continue;
                }
                if (allCT.get(i).getSkill().equals(skillsString.get(j))) {
                    continue outer;
                }
            }
            skillsString.add(allCT.get(i).getSkill());
        }
        allSkills = skillsString.toArray(new String[]{""});
        //</editor-fold>

        add(top);
        scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Template.getBackground());
        add(scroll);

        drawContent();
    }

    private void addSkill() {
        KulComboBox<String> psGUI = new KulComboBox<>(allSkills);
        KulTextField rateGUI = new KulTextField();

        psGUI.setPreferredSize(new Dimension(300, 25));
        psGUI.setMaximumSize(new Dimension(300, 25));
        psGUI.setFont(f);

        rateGUI.setPreferredSize(new Dimension(60, 25));
        rateGUI.setMaximumSize(new Dimension(60, 25));
        rateGUI.setEditable(true);

        KulImageButton remove = new KulImageButton("cancel", 20, 20);

        Box line = new Box(BoxLayout.X_AXIS);
        line.setOpaque(true);
        line.setBackground(Template.getBackground());

        line.add(psGUI);
        line.add(Box.createHorizontalGlue());
        line.add(rateGUI);
        line.add(remove);
        line.add(Box.createHorizontalStrut(10));
        line.setPreferredSize(new Dimension(400, 25));
        line.setMaximumSize(new Dimension(400, 25));

        skillGUI.add(psGUI);
        payRateGUI.add(rateGUI);
        lines.add(line);

        remove.addMouseListener(new Remove(lines.size() - 1));
        drawContent();
    }

    private void drawContent() {
        container.removeAll();

        for (int i = 0; i < lines.size(); i++) {
            container.add(lines.get(i));
            container.add(Box.createVerticalStrut(3));
        }

        for (int i = 0; i < hiddenIndex.size(); i++) {
            lines.get(hiddenIndex.get(i)).setVisible(false);
        }

        revalidate();
        repaint();
    }

    public boolean isSkillValid() {
        boolean isValid = true;
        if (lines.isEmpty()) {
            return false;
        }
        outter:
        for (int i = 0; i < payRateGUI.size(); i++) {
            for (int j = 0; j < hiddenIndex.size(); j++) {
                if (i == hiddenIndex.get(j)) {
                    continue outter;
                }
            }
            lines.get(i).setBackground(Template.getBackground());
            KulTextField temp = payRateGUI.get(i);

            if (!temp.getText().matches("^[0-9]{1,9}$")) {
                lines.get(i).setBackground(Color.red.brighter().brighter());
                isValid = false;
            }

            for (int j = 0; j < lines.size(); j++) {
                if (skillGUI.get(i).getSelectedItem().equals(skillGUI.get(j).getSelectedItem())
                        && j != i && lines.get(i).isVisible() && lines.get(j).isVisible()) {
                    lines.get(i).setBackground(Color.red.brighter().brighter());
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    public String[][] getSkills() {
        ArrayList<String> temp1 = new ArrayList<>(lines.size() - hiddenIndex.size());
        ArrayList<String> temp2 = new ArrayList<>(lines.size() - hiddenIndex.size());

        hiddenIndex.trimToSize();
        outter:
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < hiddenIndex.size(); j++) {
                if (i == hiddenIndex.get(j)) {
                    continue outter;
                }
            }
            temp1.add(skillGUI.get(i).getSelectedItem() + "");
            System.out.println("line 153 -- skill[" + i + "] is " + skillGUI.get(i).getSelectedItem());
            temp2.add(payRateGUI.get(i).getText());
        }

        temp1.trimToSize();
        temp2.trimToSize();
        String[][] returnString = new String[temp1.size()][2];
        for (int i = 0; i < returnString.length; i++) {
            returnString[i][0] = temp1.get(i);
            returnString[i][1] = temp2.get(i);
        }
        return returnString;
    }

    public void setSkill(String[][] skills) {
        payRateGUI = new ArrayList<>(skills.length);
        skillGUI = new ArrayList<>(skills.length);
        lines = new ArrayList<>(skills.length);

        for (int i = 0; i < skills.length; i++) {
            KulTextField rate = new KulTextField(skills[i][1]);
            payRateGUI.add(rate);

            KulComboBox<String> temp = new KulComboBox<>(allSkills);
            temp.setSelectedItem(skills[i][1]);
            skillGUI.add(temp);

            temp.setPreferredSize(new Dimension(300, 25));
            temp.setMaximumSize(new Dimension(300, 25));
            temp.setFont(f);

            rate.setPreferredSize(new Dimension(60, 25));
            rate.setMaximumSize(new Dimension(60, 25));
            rate.setEditable(true);

            KulImageButton remove = new KulImageButton("cancel", 20, 20);

            Box line = new Box(BoxLayout.X_AXIS);
            line.setOpaque(true);
            line.setBackground(Template.getBackground());
            line.setPreferredSize(new Dimension(400, 25));
            line.setMaximumSize(new Dimension(400, 25));

            line.add(temp);
            line.add(Box.createHorizontalGlue());
            line.add(rate);
            line.add(remove);
            line.add(Box.createHorizontalStrut(10));
            lines.add(line);
            remove.addMouseListener(new Remove(lines.size() - 1));
        }
        drawContent();
    }

    private class Remove extends MouseAdapter {

        private int index;

        public Remove(int index) {
            this.index = index;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            hiddenIndex.add(index);
            drawContent();
        }
    }
}
