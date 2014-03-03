package model;

import java.io.Serializable;
import model.Class;
import org.joda.time.LocalDate;

public class Class implements Serializable {

    private static int currentId = 1;
    private String classId;
    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private int currentNumberOfStudent;
    private String textBook;
    private long fee;
    private String[] classType;// [0] is Skill; [1] is Type
    transient private boolean selected;// checking if this object is being selected or not
    transient private boolean beingUsed;// checking if this is being viewed by user

    public Class(String className, LocalDate startDate, LocalDate endDate, String textBook, ClassType ct, long fee) {
        this.classId = "Class" + currentId;
        this.className = className;
        this.startDate = startDate;
        this.endDate = endDate;
        this.textBook = textBook;
        this.fee = fee;
        classType = new String[]{ct.getSkill(), ct.getType() + ""};
        currentId++;
    }

    public Class(String id, String className, LocalDate startDate, LocalDate endDate, String textBook, ClassType ct, long fee) {
        this.classId = id;
        this.className = className;
        this.startDate = startDate;
        this.endDate = endDate;
        this.textBook = textBook;
        this.fee = fee;
        classType = new String[]{ct.getSkill(), ct.getType() + ""};
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
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

    public int getCurrentNumberOfStudent() {
        return currentNumberOfStudent;
    }

    public void setCurrentNumberOfStudent(int currentNumberOfStudent) {
        this.currentNumberOfStudent = currentNumberOfStudent;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        Class.currentId = currentId;
    }

    public String[] getClassType() {
        return classType;
    }

    public void setClassType(String[] classType) {
        this.classType = classType;
    }

    public String getTextBook() {
        return textBook;
    }

    public void setTextBook(String textBook) {
        this.textBook = textBook;
    }

    public int getCapacity(ClassType ct) {
        switch (ct.getType()) {
            case 0:
                return 1;
            case 1:
                return 2;
            default:
                return 20;
        }
    }

    public String getTuitionFeeString() {
        String total = fee + "";
        String temp = "";

        int count = 0;
        for (int i = total.length() - 1; i >= 0; i--, count++) {
            if (count % 3 == 0) {
                if (temp.equals("")) {
                    temp = total.charAt(i) + "";
                } else {
                    temp = total.charAt(i) + "," + temp;
                }
            } else {
                temp = total.charAt(i) + temp;
            }
        }
        return "VND " + temp;
    }

    public String printDetail() {
        String output;
        output = classId + "\t,"
                + className + "\t,"
                + startDate.toString() + "\t,"
                + endDate.toString() + "\t,"
                + currentNumberOfStudent + "\t,"
                + fee + "\t,"
                + textBook + "\t,";
        String classTypeStr = classType[0] + "-" + classType[1] + "\n";
        output += classTypeStr;

        return output;
    }
}