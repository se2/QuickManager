package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.joda.time.LocalDate;

/**
 * @author Dam Linh
 */
public class PaySlip implements Serializable {

    private static int currentId;
    private String id;
    private String teacherId;
    private LocalDate month;
    private HashMap<String, Long> listClasses; // K is classId; V is salary for the class

    public PaySlip(String teacherId, LocalDate month, HashMap<String, Long> listClasses) {
        this.teacherId = teacherId;
        this.month = month;
        this.listClasses = listClasses;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        PaySlip.currentId = currentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public HashMap<String, Long> getListClasses() {
        return listClasses;
    }

    public void setListClasses(HashMap<String, Long> listClasses) {
        this.listClasses = listClasses;
    }

    public String printDetail() {
        String output = id + "\t,"
                + teacherId + "\t,"
                + month.toString() + "\t,";

        String listClassString = "";
        Iterator<Entry<String, Long>> iter = listClasses.entrySet().iterator();
        int count = 0;
        while (iter.hasNext()) {
            Entry<String, Long> entry = iter.next();
            listClassString += entry.getKey() + "-" + entry.getValue();

            if (count != listClasses.size() - 1) {
                listClassString += " ";
            }
            count++;
        }
        output += listClassString + "\n";

        return output;
    }
}
