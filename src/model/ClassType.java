package model;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * @author Dam Linh
 */
public class ClassType implements Serializable {

    public static final int SINGLE = 0;
    public static final int DUAL = 1;
    public static final int GROUP = 2;
    private String skill;
    private int type;
    private int lessonPerWeek;
    private long feeFor45Min;
    private long feeFor60Min;
    private String note;
    transient private boolean selected;// checking if this object is being selected or not
    transient private boolean beingUsed;// checking if this is being viewed by user

    public ClassType(String skill, int type, int lessonPerWeek, long feeFor45Min, long feeFor60Min, String note) {
        this.skill = skill;
        this.type = type;
        this.lessonPerWeek = lessonPerWeek;
        this.feeFor45Min = feeFor45Min;
        this.feeFor60Min = feeFor60Min;
        this.note = note;
    }

    public String printDetail() {
        String output;
        output = skill + "\t,"
                + type + "\t,"
                + lessonPerWeek + "\t,"
                + feeFor45Min + "\t,"
                + feeFor60Min + "\t,";
        if (note != null) {
            String noteTemp = note.replaceAll("[\n\t]", " ");
            output += noteTemp + "\n";
        } else {
            output += "Empty Remark\n";
        }

        return output;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLessonPerWeek() {
        return lessonPerWeek;
    }

    public void setLessonPerWeek(int lessonPerWeek) {
        this.lessonPerWeek = lessonPerWeek;
    }

    public long getFeeFor45Min() {
        return feeFor45Min;
    }

    public void setFeeFor45Min(long feeFor45Min) {
        this.feeFor45Min = feeFor45Min;
    }

    public long getFeeFor60Min() {
        return feeFor60Min;
    }

    public void setFeeFor60Min(long feeFor60Min) {
        this.feeFor60Min = feeFor60Min;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getLessonPerWeekString(ResourceBundle lang) {
        switch (lessonPerWeek) {
            case 1:
                return "1";
            case 2:
                return "2";
            default:
                return "1 " + lang.getString("or") + " 2";
        }
    }

    public String getTypeString(ResourceBundle lang) {
        switch (type) {
            case 0:
                return lang.getString("individual");
            case 1:
                return lang.getString("dual");
            default:
                return lang.getString("group");
        }
    }
}
