package model;

import java.io.Serializable;

/**
 *
 * @author Dam Linh
 */
public class Room implements Serializable {

    private int roomNumber;
    private String roomName;
    private int capacity;
    private boolean selected;
    private boolean beingUsed;// checking if this is being viewed by user

    public Room(int roomNumber, String roomName, int capacity) {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public String printDetail() {
        String output;
        output = roomNumber + "\t,"
                + roomName + "\t,"
                + capacity + "\n";
        return output;
    }
}
