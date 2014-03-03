package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Dam Linh
 */
public class ClassSession implements Serializable {

    private static int currentId = 1;
    private String classSessionId;
    private String classId;
    private int roomId;
    private boolean is45;
    private HashMap<Integer, Integer> times;

    public ClassSession(String classId, int roomId, HashMap<Integer, Integer> times, boolean is45) {
        classSessionId = "Session" + currentId;
        this.classId = classId;
        this.roomId = roomId;
        this.times = times;
        this.is45 = is45;
        currentId++;
    }

    public ClassSession(String id, String classId, int roomId, HashMap<Integer, Integer> times, boolean is45) {
        classSessionId = id;
        this.classId = classId;
        this.roomId = roomId;
        this.times = times;
        this.is45 = is45;
    }

    public boolean is45() {
        return is45;
    }

    public void setIs45(boolean is45) {
        this.is45 = is45;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        ClassSession.currentId = currentId;
    }

    public String getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(String classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;

    }

    public HashMap<Integer, Integer> getTimes() {
        return times;
    }

    public void setTimes(HashMap<Integer, Integer> times) {
        this.times = times;
    }

    public String getTimeString() {
        String s;
        String day;
        String min1;
        String min2;
        int hour2;
        int hour1;
        int smallest;

        Iterator<Integer> iter = times.keySet().iterator();

        smallest = iter.next();
        while (iter.hasNext()) {
            int i = iter.next();
            if (smallest > i) {
                smallest = i;
            }
        }
        int countTime = smallest;

        iter = times.keySet().iterator();
        iter.next();// reset iter;

        switch (countTime / 44) {
            case 0:
                day = "Monday";
                break;
            case 1:
                day = "Tuesday";
                break;
            case 2:
                day = "Wednesday";
                break;
            case 3:
                day = "Thursday";
                break;
            case 4:
                day = "Friday";
                break;
            case 5:
                day = "Saturday";
                break;
            default:
                day = "Sunday";
        }
        countTime = countTime % 44;
        if (day.equals("Saturday") || day.equals("Sunday")) {
            hour1 = countTime / 4 + 9;
        } else {
            hour1 = countTime / 4 + 16;
        }
        if (countTime % 4 == 0) {
            min1 = "00";
        } else if (countTime % 4 == 1) {
            min1 = "15";
        } else if (countTime % 4 == 2) {
            min1 = "30";
        } else {
            min1 = "45";
        }
        while (iter.hasNext()) {
            countTime++;
            iter.next();
        }

        if (day.equals("Saturday") || day.equals("Sunday")) {
            hour2 = countTime / 4 + 9;
        } else {
            hour2 = countTime / 4 + 16;
        }
        if (countTime % 4 == 0) {
            min2 = "15";
        } else if (countTime % 4 == 1) {
            min2 = "30";
        } else if (countTime % 4 == 2) {
            min2 = "45";
        } else {
            hour2++;
            min2 = "00";
        }

        s = String.format("%02d:%s - %02d:%s, %s", hour1, min1, hour2, min2, day);
        return s;
    }

    public String printDetail() {
        String output;
        output = classId + "\t,"
                + roomId + "\t,"
                + classSessionId + "\t,"
                + is45 + "\t,";

        Iterator<Integer> iter = times.keySet().iterator();
        int smallest = iter.next();
        while (iter.hasNext()) {
            int i = iter.next();
            if (smallest > i) {
                smallest = i;
            }
        }

        output += smallest + "\n";
        return output;
    }
}