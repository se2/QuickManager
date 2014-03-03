package viewcontroller;

import exeception.InvalidCSVFormatException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import kulcomponent.KulCSVFileChooser;
import kulcomponent.KulComboBox;
import kulcomponent.KulImageButton;
import kulcomponent.KulLoadingFrame;
import kulcomponent.KulShinyText;
import model.Constants;
import model.Manager;
import model.User;
import quickmanage.QuickManage;
import viewcontroller.listview.AccountListView;
import viewcontroller.listview.ClassListView;
import viewcontroller.listview.ClassTypeListView;
import viewcontroller.listview.InvoiceListView;
import viewcontroller.listview.RoomListView;
import viewcontroller.listview.StudentListView;
import viewcontroller.listview.TeacherListView;
import viewcontroller.westbutton.AccountButton;
import viewcontroller.westbutton.ClassButton;
import viewcontroller.westbutton.ClassTypeButton;
import viewcontroller.westbutton.InvoiceButton;
import viewcontroller.westbutton.StudentButton;
import viewcontroller.westbutton.TeacherButton;
import viewcontroller.westbutton.WestButtonBar;

/**
 *
 * @author Dam Linh
 */
public class MenuView extends JPanel {

    private ResourceBundle language;
    private MainFrame mainFrame;
    private Box north;
    private CardLayout cardSubWest;
    private CardLayout cardSubCenter;
    private JPanel subCenter;
    private JPanel subWest;
    private JLabel photo;
    private JPanel center;
    private KulShinyText username;
    private JPanel usernamePanel = new JPanel(new BorderLayout(1, 1));
    private KulComboBox<String> theme;
    private KulImageButton logout;
    private KulImageButton help;
    private KulImageButton dashBoard;
    private KulImageButton exportCSV;
    private KulImageButton importCSV;
    private KulImageButton getReport;
    private WestButtonBar studentBut;
    private WestButtonBar classBut;
    private WestButtonBar accountBut;
    private WestButtonBar teacherBut;
    private WestButtonBar invoiceBut;
    private WestButtonBar classTypeBut;
    private Box roomBut;
    private TeacherListView teacherList;
    private StudentListView studentList;
    private ClassListView classList;
    private AccountListView accountList;
    private InvoiceListView invoiceList;
    private ClassTypeListView classTypeList;
    private RoomListView roomList;

    public MenuView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setBackground(Template.getBackground());
        this.setLayout(new BorderLayout());
        language = mainFrame.getModel().getLanguage();

        drawNorth();
        drawCenter();

        //<editor-fold defaultstate="collapsed" desc="keyBinding">
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
                getKeyStroke(KeyEvent.VK_F5, 0, true), "refresh");
        this.getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (teacherList.isShowing()) {
                    update("Teacher");
                } else if (studentList.isShowing()) {
                    update("Student");
                } else if (classList.isShowing()) {
                    update("Class");
                } else if (accountList.isShowing()) {
                    update("Account");
                } else if (invoiceList.isShowing()) {
                    update("Invoice");
                } else if (classTypeList.isShowing()) {
                    update("ClassType");
//                } else if (studentList.isShowing()) {
                }
            }
        });
        //</editor-fold>
    }

    // <editor-fold defaultstate="collapsed" desc="drawNorth">
    private void drawNorth() {
        final User user = mainFrame.getUser();

        north = new Box(BoxLayout.X_AXIS);
        north.setPreferredSize(new Dimension(2000, 50));
        north.setMaximumSize(new Dimension(2000, 50));
        add(north, BorderLayout.NORTH);

        Image img = user.getPhoto().getImage();
        Image newImg = img.getScaledInstance(30, 40, Image.SCALE_SMOOTH);
        photo = new JLabel(new ImageIcon(newImg));

        theme = new KulComboBox<>(new String[]{language.getString("water"), language.getString("wood")});
        theme.setFont(Template.getFont().deriveFont(14f));
        theme.setPreferredSize(new Dimension(150, 30));
        theme.setMaximumSize(new Dimension(150, 30));
        theme.setElementFont(Template.getFont().deriveFont(14f));
        theme.setFocusable(false);
        theme.setBorder(null);
        theme.removeAll();
        theme.setToolTipText(language.getString("changeTheme"));
        if (Template.getCurrentTheme().equals("Wood")) {
            theme.setSelectedIndex(1);
        } else {
            theme.setSelectedIndex(0);
        }
        theme.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    mainFrame.getModel().changeTheme(theme.getSelectedIndex());
                    JOptionPane.showMessageDialog(mainFrame, language.getString("restartToChangeThemeContent"),
                            language.getString("restartToChangeThemeTitle"), JOptionPane.INFORMATION_MESSAGE);
                    QuickManage.logout();
                }
            }
        });

        drawUsername();

        if (mainFrame.getUser() instanceof Manager) {
            getReport = new KulImageButton("report", 25, 25);
            getReport.setToolTipText(language.getString("getReport"));
            getReport.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.generateReport();
                }
            });
        }

        logout = new KulImageButton("logout", 25, 25);
        logout.setToolTipText(language.getString("logout"));
        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int input = JOptionPane.showConfirmDialog(mainFrame,
                        language.getString("logoutConfirmContent"),
                        language.getString("logoutConfirmTitle"), JOptionPane.OK_CANCEL_OPTION);
                if (input == JOptionPane.OK_OPTION) {
                    QuickManage.logout();
                }
            }
        });

        help = new KulImageButton("help", 25, 25);
        help.setToolTipText(language.getString("help"));
        help.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showUserGuide();
            }
        });
        help.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "help");
        help.getActionMap().put("help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserGuide();
            }
        });

        //<editor-fold defaultstate="collapsed" desc="dashBoard">
        dashBoard = new KulImageButton("dashboard", 25, 25);
        dashBoard.setToolTipText(language.getString("dashboard"));
        dashBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                QuickManage.showDashboard(user);
                mainFrame.setVisible(false);
                mainFrame.dispose();
            }
        });
        dashBoard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "dashBoard");
        dashBoard.getActionMap().put("dashBoard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuickManage.showDashboard(user);
                mainFrame.setVisible(false);
                mainFrame.dispose();
            }
        });
        //</editor-fold>
        if (user instanceof Manager) {
            importCSV = new KulImageButton("importcsv", 25, 25);
            importCSV.setToolTipText(language.getString("importHint"));
            importCSV.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    importCSV();
                }
            });
            importCSV.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK), "import");
            importCSV.getActionMap().put("import", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    importCSV();
                }
            });

            exportCSV = new KulImageButton("exportcsv", 25, 25);
            exportCSV.setToolTipText(language.getString("exportHint"));
            exportCSV.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    exportCSV();

                }
            });
            exportCSV.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), "export");
            exportCSV.getActionMap().put("export", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    exportCSV();
                }
            });
        }
        north.add(Box.createHorizontalStrut(25));
        north.add(photo);
        north.add(Box.createHorizontalStrut(15));
        north.add(usernamePanel);
        north.add(Box.createHorizontalGlue());
        north.add(theme);
        north.add(Box.createHorizontalStrut(10));
        if (user instanceof Manager) {
            north.add(getReport);
            north.add(Box.createHorizontalStrut(15));
            north.add(importCSV);
            north.add(Box.createHorizontalStrut(15));
            north.add(exportCSV);
            north.add(Box.createHorizontalStrut(15));
        }
        north.add(dashBoard);
        north.add(Box.createHorizontalStrut(15));
        north.add(help);
        north.add(Box.createHorizontalStrut(15));
        north.add(logout);
        north.add(Box.createHorizontalStrut(15));
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="draw username">
    private void drawUsername() {
        SpringLayout spring = new SpringLayout();
        usernamePanel = new JPanel(spring);
        usernamePanel.setBackground(Template.getBackground());
        username = new KulShinyText(mainFrame.getUser().getFirstname() + " " + mainFrame.getUser().getLastname(), Template.getFont().deriveFont(4f));
        username.setToolTipText(language.getString("viewProfile"));
        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showAccountForm(mainFrame.getUser(), false);
            }
        });
        usernamePanel.add(username);

        spring.putConstraint(SpringLayout.WEST, username, 0, SpringLayout.WEST, usernamePanel);
        spring.putConstraint(SpringLayout.NORTH, username, 17, SpringLayout.NORTH, usernamePanel);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawCenter">
    private void drawCenter() {
        center = new JPanel(new BorderLayout());
        center.setBackground(Template.getBackground());
        add(center);

        ButtonBar buttonBar = new ButtonBar(mainFrame);
        center.add(buttonBar, BorderLayout.NORTH);

        drawSubWest();
        drawSubCenter();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawSubWest">
    private void drawSubWest() {
        teacherBut = new TeacherButton(mainFrame);
        studentBut = new StudentButton(mainFrame);
        classBut = new ClassButton((mainFrame));
        accountBut = new AccountButton((mainFrame));
        invoiceBut = new InvoiceButton(mainFrame);
        classTypeBut = new ClassTypeButton(mainFrame);
        roomBut = new Box(BoxLayout.Y_AXIS);
        cardSubWest = new CardLayout();
        subWest = new JPanel(cardSubWest);
        center.add(subWest, BorderLayout.WEST);
        subWest.setBackground(Template.getBackground());
        subWest.add(teacherBut, "teacherBut");
        subWest.add(studentBut, "studentBut");
        subWest.add(classBut, "classBut");
        subWest.add(accountBut, "accountBut");
        subWest.add(roomBut, "roomBut");
        subWest.add(invoiceBut, "invoiceBut");
        subWest.add(classTypeBut, "classTypeBut");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="drawSubCenter">
    private void drawSubCenter() {
        cardSubCenter = new CardLayout();
        subCenter = new JPanel(cardSubCenter);
        subCenter.setBackground(Template.getBackground());
        center.add(subCenter);

        teacherList = new TeacherListView(mainFrame, true);
        studentList = new StudentListView(mainFrame, true);
        classList = new ClassListView(mainFrame, true);
        accountList = new AccountListView(mainFrame, true);
        invoiceList = new InvoiceListView(mainFrame, true);
        roomList = new RoomListView(mainFrame);
        classTypeList = new ClassTypeListView(mainFrame, true);

        subCenter.add(teacherList, "teacherList");
        subCenter.add(studentList, "studentList");
        subCenter.add(classList, "classList");
        subCenter.add(accountList, "accountList");
        subCenter.add(roomList, "roomList");
        subCenter.add(invoiceList, "invoiceList");
        subCenter.add(classTypeList, "classTypeList");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="showUserGuide">
    private void showUserGuide() {
        if (Desktop.isDesktopSupported()) {
            try {
                URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
                String temp;
                if (mainFrame.getModel().isEnglish()) {
                    temp = Constants.USERGUIDE_ENGLISH_FILE_LOCATION;
                } else {
                    temp = Constants.USERGUIDE_VIETNAMESE_FILE_LOCATION;
                }

                String userguideLocation = new File(url.toURI()).getParent() + temp;
                Desktop.getDesktop().open(new File(userguideLocation));
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CSV methods">
    private void exportCSV() {
        KulCSVFileChooser fileChooser = new KulCSVFileChooser();
        int returnValue = fileChooser.showDialog(mainFrame, language.getString("select"));
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final KulLoadingFrame loading = new KulLoadingFrame(language.getString("exporting"));
            final File file = fileChooser.getSelectedFile();
            loading.setVisible(true);
            loading.toFront();
            mainFrame.setEnabled(false);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mainFrame.getModel().exportToCSV(file);
                    loading.stopLoading();
                    mainFrame.setEnabled(true);
                    mainFrame.toFront();
                }
            };
            new Thread(runnable).start();
        }
    }

    private void importCSV() {
        KulCSVFileChooser fileChooser = new KulCSVFileChooser();
        int returnValue = fileChooser.showOpenDialog(mainFrame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final File file = fileChooser.getSelectedFile();
            final KulLoadingFrame loading = new KulLoadingFrame(language.getString("exporting"));
            loading.setVisible(true);
            loading.toFront();
            mainFrame.setEnabled(false);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<String>[] errorString = null;
                    try {
                        errorString = mainFrame.getModel().importFromCSV(file);
                    } catch (InvalidCSVFormatException ex) {
                        System.err.println("Catch exception: " + ex.toString());
                    } finally {
                        loading.stopLoading();
                        mainFrame.setEnabled(true);
                        mainFrame.toFront();
                        ImportResult result = new ImportResult(errorString, mainFrame);
                        result.setSize(600, 730);
                        result.setLocationRelativeTo(mainFrame);
                        result.setVisible(true);
                    }
                }
            };
            new Thread(runnable).start();
        }
    }
    //</editor-fold>

    /*
     * these methods are to display the SMALL listView base on the button clicked.
     * These are invoked from the buttons in the ButtonBar class
     */
    // <editor-fold defaultstate="collapsed" desc="showXxx">
    void showTeacher() {
        cardSubWest.show(subWest, "teacherBut");
        cardSubCenter.show(subCenter, "teacherList");
        teacherList.requestFocus();
    }

    void showStudent() {
        cardSubWest.show(subWest, "studentBut");
        cardSubCenter.show(subCenter, "studentList");
        studentList.requestFocus();
    }

    void showClass() {
        cardSubWest.show(subWest, "classBut");
        cardSubCenter.show(subCenter, "classList");
        classList.requestFocus();
    }

    void showRoom() {
        cardSubWest.show(subWest, "roomBut");
        cardSubCenter.show(subCenter, "roomList");
        roomList.requestFocus();
    }

    void showInvoice() {
        cardSubWest.show(subWest, "invoiceBut");
        cardSubCenter.show(subCenter, "invoiceList");
        invoiceList.requestFocus();
    }

    void showAccount() {
        cardSubWest.show(subWest, "accountBut");
        cardSubCenter.show(subCenter, "accountList");
        accountList.requestFocus();
    }

    void showClassType() {
        cardSubWest.show(subWest, "classTypeBut");
        cardSubCenter.show(subCenter, "classTypeList");
        classTypeList.requestFocus();
    }
    // </editor-fold>

    /*
     * This methos is to update. Invoked by MainFrame.update()
     */
    public void update(Object o) {
        if (o instanceof String) {
            String s = (String) o;
            switch (s) {
                case "User":
                    accountList.refresh();
                    break;
                case "Class":
                    classList.refresh();
                    break;
                case "Teacher":
                    teacherList.refresh();
                    break;
                case "Student":
                    studentList.refresh();
                    break;
                case "Invoice":
                    invoiceList.refresh();
                case "Class Type":
                    classTypeList.refresh();
            }
        }
    }
}
