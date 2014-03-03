package model;

import java.io.Serializable;
import javax.swing.ImageIcon;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import viewcontroller.Template;

/**
 *
 * @author Dam Linh
 */
public abstract class Person implements Serializable {

    private static int currentId = 1;
    private String id;
    private String lastname;
    private String middlename;
    private String firstname;
    private String email;
    private String phoneNumber;
    private LocalDate DOB;
    private String address;
    private boolean active;
    private ImageIcon photo;
    private boolean isMale;
    private static LocalDate previousMonth = new LocalDate();
    transient private boolean selected;// checking if this object is being selected or not
    transient private boolean beingUsed;// checking if this is being viewed by user

    public Person(String lastname, String middlename, String firstname,
            String email, String phoneNumber, LocalDate DOB, String address, ImageIcon photo, boolean isMale) {
        this.lastname = lastname;
        this.middlename = middlename;
        this.firstname = firstname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.DOB = DOB;
        this.address = address;
        this.isMale = isMale;
        this.active = true;
        if (photo != null) {
            this.photo = photo;
        } else {
            this.photo = new ImageIcon(Template.getPhotoURLDefault());
        }
    }

    public static LocalDate getCurrentMonth() {
        return previousMonth;
    }

    public static void setPreviousMonth(LocalDate previousMonth) {
        Person.previousMonth = previousMonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setIsActive(boolean active) {
        this.active = active;
    }

    public ImageIcon getPhoto() {
        if (photo.getImage() == null) {
            this.photo = new ImageIcon(Template.getPhotoURLDefault());
        }
        return photo;
    }

    public void setPhoto(ImageIcon photo) {
        this.photo = photo;
    }

    public boolean isIsMale() {
        return isMale;
    }

    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
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

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        Person.currentId = currentId;
    }

    /**
     * this method is for getting full name of this Person conveniently
     *
     * @return fullname: String
     */
    public String getFullname() {
        String fullname = firstname + " ";
        if (!middlename.equals("")) {
            fullname += middlename + " ";
        }
        return fullname + lastname;
    }

    public static String generateId() {
        if (isIdNeedReset()) {
            Person.setCurrentId(1);
            previousMonth = new LocalDate();
        }
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyMM");
        String idTemp = dtf.print(previousMonth) + String.format("%03d", currentId);
        currentId++;
        return idTemp;
    }

    private static boolean isIdNeedReset() {
        LocalDate today = new LocalDate();
        LocalDate lastDayOfMonth = previousMonth.dayOfMonth().withMaximumValue();
        if (today.compareTo(lastDayOfMonth) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
