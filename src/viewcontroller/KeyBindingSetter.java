package viewcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import model.Student;

/**
 *
 * @author Dam Linh
 */
public class KeyBindingSetter {

    public static void setWestButtonKeyBinding(final MainFrame mainFrame, final JComponent c, final String identifier) {
        switch (identifier) {
            // <editor-fold defaultstate="collapsed" desc="User">
            case "User":
                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, true), "selectAll");
                c.getActionMap().put("selectAll", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.getModel().selectAllUser();
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, true), "addManager");
                c.getActionMap().put("addManager", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showAccountForm(null, true);
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "addStaff");
                c.getActionMap().put("addStaff", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showAccountForm(null, false);
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, true), "delete");
                c.getActionMap().put("delete", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.getModel().deleteSeletedUser();
                    }
                });
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Teacher">
            case "Teacher":
                c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, true), "selectAll");
                c.getActionMap().put("selectAll", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.getModel().selectAllTeacher();
                    }
                });

                c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "add");
                c.getActionMap().put("add", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showTeacherForm(null, true);

                    }
                });

                c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, true), "delete");
                c.getActionMap().put("delete", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] options = {"Sure", "No"};
                        int choice = JOptionPane.showOptionDialog(null,
                                "Are you sure you want to delete selected teachers?", "Delete Selected Teachers",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[1]);
                        if (choice == JOptionPane.OK_OPTION) {
                            if (mainFrame.getModel().deleteSeletedTeacher()) {
                            }
                        }
                    }
                });
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Student">
            case "Student":
                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, true), "selectAll");
                c.getActionMap().put("selectAll", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.getModel().selectAllStudent();
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "add");
                c.getActionMap().put("add", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showStudentForm(null, true);
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "enroll");
                c.getActionMap().put("enroll", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        HashMap<String, Student> students = new HashMap<>();
                        Iterator<Map.Entry<String, Student>> iter = mainFrame.getModel().getStudents().entrySet().iterator();
                        while (iter.hasNext()) {
                            Student s = iter.next().getValue();
                            if (s.isSelected()) {
                                students.put(s.getId(), s);
                            }
                        }
                        mainFrame.showEnrollForm(students, "Student");
                    }
                });
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Class">
            case "Class":
                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, true), "selectAll");
                c.getActionMap().put("selectAll", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.getModel().selectAllClass();
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "add");
                c.getActionMap().put("add", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showClassForm(null, false, true);
                    }
                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "enroll");
                c.getActionMap().put("enroll", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        HashMap<String, model.Class> classes = new HashMap<>();
                        Iterator<Map.Entry<String, model.Class>> iter = mainFrame.getModel().getClasses().entrySet().iterator();
                        while (iter.hasNext()) {
                            model.Class c = iter.next().getValue();
                            if (c.isSelected()) {
                                classes.put(c.getClassId(), c);
                            }
                        }
                        mainFrame.showEnrollForm(classes, "Class");
                    }
                });
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="ClassType">
            case "ClassType":
//                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, true), "selectAll");
//                c.getActionMap().put("selectAll", new AbstractAction() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        mainFrame.getModel().selectAllClass();
//                    }
//                });

                c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "add");
                c.getActionMap().put("add", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showClassTypeForm(null);
                    }
                });
                break;
            // </editor-fold>
        }


        // <editor-fold defaultstate="collapsed" desc="activate/deactivate">
        c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true), "activate");
        c.getActionMap().put("activate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getModel().setIsActiveSelected(identifier, true);
            }
        });

        c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "deactivate");
        c.getActionMap().put("deactivate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getModel().setIsActiveSelected(identifier, false);
            }
        });
        // </editor-fold>
    }
}
