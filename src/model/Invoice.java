package model;

import java.io.Serializable;
import java.util.ArrayList;
import org.joda.time.LocalDate;

/**
 *
 * @author Dam Linh
 */
public class Invoice implements Serializable {

    private String id;
    private ArrayList<StudentClass> studentClass;
    private LocalDate paidDate;
    private String paidMethod;
    private String paidNote;
    private long totalFee;
    private long balance;
    transient private boolean selected;// checking if this object is being selected or not
    transient private boolean beingUsed;// checking if this is being viewed by user

    public Invoice(String id, ArrayList<StudentClass> studentClass, LocalDate paidDate, String paidMethod, String paidNote, long totalFee, long balance) {
        this.id = id;
        this.studentClass = studentClass;
        this.paidDate = paidDate;
        this.paidMethod = paidMethod;
        this.paidNote = paidNote;
        this.totalFee = totalFee;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<StudentClass> getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(ArrayList<StudentClass> studentClasses) {
        this.studentClass = studentClasses;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaidMethod() {
        return paidMethod;
    }

    public void setPaidMethod(String paidMethod) {
        this.paidMethod = paidMethod;
    }

    public String getPaidNote() {
        return paidNote;
    }

    public void setPaidNote(String paidNote) {
        this.paidNote = paidNote;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isBeingUsed() {
        return beingUsed;
    }

    public void setBeingUsed(boolean beingUsed) {
        this.beingUsed = beingUsed;
    }

    public void reduceFee(model.Class c, ClassType ct) {
        totalFee = totalFee - c.getFee();
    }

    public boolean isPaid() {
        if (studentClass == null || studentClass.isEmpty()) {
            return true;
        } else {
            return studentClass.get(0).isPaid();
        }
    }

    public void setPaid(boolean paid) {
        if (studentClass != null) {
            for (int i = 0; i < studentClass.size(); i++) {
                studentClass.get(i).setPaid(paid);
            }
        }
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getBalanceString() {
        return formatCurrency(balance + "");
    }

    public String getTotalFeeString() {
        String total = totalFee + "";
        return formatCurrency(total);
    }

    private String formatCurrency(String money) {
        String temp = "";

        int count = 0;
        for (int i = money.length() - 1; i >= 0; i--, count++) {
            if (count % 3 == 0) {
                if (temp.equals("")) {
                    temp = money.charAt(i) + "";
                } else {
                    temp = money.charAt(i) + "," + temp;
                }
            } else {
                temp = money.charAt(i) + temp;
            }
        }
        return "VND " + temp;
    }

    public String printDetail() {
        ArrayList<StudentClass> sc = getStudentClass();
        String output = id + "\t,";

        String studentClassIdStr = "";
        for (int i = 0; i < sc.size(); i++) {
            studentClassIdStr += sc.get(i).getId();
            if (i != sc.size() - 1) {
                studentClassIdStr += "-";
            }
        }
        output = output + studentClassIdStr + "\t,";

        if (paidDate != null) {
            output += getPaidDate().toString();
        }
        output += "\t,";

        if (paidMethod != null) {
            output += getPaidMethod();
        }
        output += "\t,";

        if (paidNote != null) {
            output += getPaidNote();
        }
        output += "\t,";

        output = output + totalFee + "\t,"
                + balance + "\n";
        return output;
    }
}