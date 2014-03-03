package viewcontroller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import kulcomponent.KulButton;
import model.Manager;

/**
 *
 * @author Dam Linh
 *
 * This class is to draw the buttons to switch between 'tabs' and their
 * listeners When user clicks on a button, the listener of the button calls the
 * method in the mainFrame to show the list view and the buttons based on the
 * button clicked
 */
public class ButtonBar extends Box {

    private ResourceBundle language;
    private Color color1 = Template.getButtonBG1();
    private Color color2 = Template.getButtonBG2();
    private Color color3 = Template.getButtonBG3();
    private Color color4 = Template.getButtonBG4();
    private Color color5 = Template.getButtonBG5();
    private Color color6 = Template.getButtonBG6();
    private Color color7 = Template.getButtonBG7();
    private Color lineColor = color1;
    private MainFrame mainFrame;
    private KulButton acc;
    private KulButton teacher;
    private KulButton student;
    private KulButton classes;
    private KulButton invoice;
    private KulButton room;
    private KulButton classType;
    private Color mouseOveredButtonColor = Template.getButtonMouseOveredColor();
    private Color defaultButtonColor = Template.getBackground();
    private int w = 135;
    private int h = 75;
    private int wStrut = 9;
    private int x1 = wStrut;
    private int y1 = h + 15;
    private int x2 = wStrut + w;
    private Timer timer;

    public ButtonBar(MainFrame mainFrame) {
        super(BoxLayout.X_AXIS);
        this.mainFrame = mainFrame;
        language = mainFrame.getModel().getLanguage();

        init();
        addKeyListenerForGUI();
        addMouseListener();
    }

    // <editor-fold defaultstate="collapsed" desc="GUI Only">
    //<editor-fold defaultstate="collapsed" desc="init">
    private void init() {
        setPreferredSize(new Dimension(2000, h + 20));

        teacher = new KulButton(language.getString("teachersTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        teacher.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(teacher, color1);

        student = new KulButton(language.getString("studentsTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        student.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(student, color2);

        classes = new KulButton(language.getString("classesTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        classes.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(classes, color3);

        room = new KulButton(language.getString("roomsTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        room.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(room, color4);

        invoice = new KulButton(language.getString("invoicesTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        invoice.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(invoice, color5);

        classType = new KulButton(language.getString("classTypesTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        classType.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(classType, color6);
        classType.setFont(Template.getFont().deriveFont(18f));

        acc = new KulButton(language.getString("accountsTitle"), defaultButtonColor, mouseOveredButtonColor, false);
        acc.setMouseOveredBorder(new LineBorder(Template.getButtonMouseOveredColor()));
        initButton(acc, color7);

        add(Box.createHorizontalStrut(wStrut));
        add(teacher);
        add(Box.createHorizontalStrut(wStrut));
        add(student);
        add(Box.createHorizontalStrut(wStrut));
        add(classes);
        add(Box.createHorizontalStrut(wStrut));
        add(room);
        add(Box.createHorizontalStrut(wStrut));
        add(invoice);
        if (mainFrame.getUser() instanceof Manager) {
            add(Box.createHorizontalStrut(wStrut));
            add(classType);
            add(Box.createHorizontalStrut(wStrut));
            add(acc);
        }
    }
    //</editor-fold>

    private void addKeyListenerForGUI() {
        teacher.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x1Des = wStrut;
                int x2Des = w + wStrut;
                moveLineWithAnimation(1, x1Des, x2Des);
            }
        });

        student.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x1Des = 2 * wStrut + w;
                int x2Des = 2 * (w + wStrut);
                moveLineWithAnimation(2, x1Des, x2Des);
            }
        });


        classes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x1Des = 3 * wStrut + 2 * w;
                int x2Des = 3 * (w + wStrut);
                moveLineWithAnimation(3, x1Des, x2Des);
            }
        });

        room.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x1Des = 4 * wStrut + 3 * w;
                int x2Des = 4 * (w + wStrut);
                moveLineWithAnimation(4, x1Des, x2Des);
            }
        });

        invoice.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x1Des = 5 * wStrut + 4 * w;
                int x2Des = 5 * (w + wStrut);
                moveLineWithAnimation(5, x1Des, x2Des);
            }
        });

        if (mainFrame.getUser() instanceof Manager) {
            classType.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int x1Des = 6 * wStrut + 5 * w;
                    int x2Des = 6 * (w + wStrut);
                    moveLineWithAnimation(6, x1Des, x2Des);
                }
            });

            acc.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int x1Des = 7 * wStrut + 6 * w;
                    int x2Des = 7 * (w + wStrut);
                    moveLineWithAnimation(7, x1Des, x2Des);
                }
            });
        }
    }

    private void moveLineWithAnimation(final int desBut, final int x1Des, final int x2Des) {
        int distance = x1Des - x1;
        int loopTimes = 39;
        int delay = 2;
        if (Math.abs(distance) > 350 && Math.abs(distance) < 500) {
            loopTimes = 49;
            delay = 1;
        } else if (Math.abs(distance) >= 500) {
            loopTimes = 79;
            delay = 1;
        }

        final int dividor = loopTimes;
        final float d = distance / dividor;

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(delay, new ActionListener() {
            private int count = 1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (count >= dividor + 9) {
                    x1 = x1Des;
                    x2 = x2Des;
                    ButtonBar.this.repaint();
                    timer.stop();
                    return;
                } else if (count == dividor + 1) {
                    timer.setDelay(timer.getDelay() + 3);
                } else if (count == dividor / 2) {
                    switch (desBut) {
                        case 1:
                            lineColor = color1;
                            break;
                        case 2:
                            lineColor = color2;
                            break;
                        case 3:
                            lineColor = color3;
                            break;
                        case 4:
                            lineColor = color4;
                            break;
                        case 5:
                            lineColor = color5;
                            break;
                        case 6:
                            lineColor = color6;
                            break;
                        default:
                            lineColor = color7;
                    }
                    x1 = x1 + (int) d;
                    x2 = x2 + (int) d;
                } else {
                    x1 = x1 + (int) d;
                    x2 = x2 + (int) d;
                }
                count++;
                ButtonBar.this.repaint();
            }
        });
        timer.start();
    }
    // </editor-fold>

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(lineColor);
        g.fillRect(x1 - 1, y1, x2 - x1 + 2, 4);

    }

    //<editor-fold defaultstate="collapsed" desc="convenient method">
    private void initButton(KulButton but, Color color) {
        but.setPreferredSize(new Dimension(w, h));
        but.setMaximumSize(new Dimension(w, h));
        if (mainFrame.getModel().isEnglish()) {
            but.setFont(Template.getFont().deriveFont(20f));
        } else {
            but.setFont(Template.getFont().deriveFont(20f));
        }
        but.setOpaque(true);
        but.setBackground(color);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="mouse listeners">
    private void addMouseListener() {
        acc.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showAccount();
            }
        });

        teacher.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showTeacher();
            }
        });

        classes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showClass();
            }
        });

        room.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showRoom();
            }
        });

        invoice.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showInvoice();
            }
        });

        student.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showStudent();
            }
        });

        classType.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainFrame.getMenuView().showClassType();
            }
        });
    }
    //</editor-fold>
}
