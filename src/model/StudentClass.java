package model;

import java.io.Serializable;

/**
 *
 * @author Dam Linh
 */
public class StudentClass implements Serializable {

    private static int currentId = 1;
    private String id;
    private String studentId;
    private String classId;
    private boolean paid = false;
    private boolean invoiced = false;

    public StudentClass(String studentId, String classId) {
        this.id = "Enrollment" + currentId;
        currentId++;
        this.studentId = studentId;
        this.classId = classId;
    }

    public StudentClass(String id, String studentId, String classId, boolean paid, boolean invoiced) {
        this.id = id;
        this.studentId = studentId;
        this.classId = classId;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        StudentClass.currentId = currentId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public boolean isInvoiced() {
        return invoiced;
    }

    public void setInvoiced(boolean invoiced) {
        this.invoiced = invoiced;
    }

    public String printDetail() {
        String output;
        output = id + "\t,"
                + classId + "\t,"
                + studentId + "\t,"
                + (isPaid() == true ? "true\t," : "false\t,")
                + (isInvoiced() == true ? "true\n" : "false\n");

        return output;
    }
}
