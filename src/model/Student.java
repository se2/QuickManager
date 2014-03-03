package model;

import java.io.Serializable;
import javax.swing.ImageIcon;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Student extends Person implements Serializable {

    private String contactPhone;
    private String contactEmail;
    private String contactName;
    private String contactAddress;
    private String contactRelationship;
    private long balance;

    public Student(String contactPhone, String contactEmail, String contactName,
            String contactAddress, String contactRelationship, String lastname,
            String middlename, String firstname, String email, String phoneNumber,
            LocalDate DOB, String address, ImageIcon photo, boolean isMale) {
        super(lastname, middlename, firstname, email, phoneNumber, DOB, address, photo, isMale);
        super.setId(Person.generateId());
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.contactName = contactName;
        this.contactAddress = contactAddress;
        this.contactEmail = contactEmail;
    }

    public Student(String id, String contactPhone, String contactEmail, String contactName,
            String contactAddress, String contactRelationship, String lastname,
            String middlename, String firstname, String email, String phoneNumber,
            LocalDate DOB, String address, ImageIcon photo, boolean isMale) {
        super(lastname, middlename, firstname, email, phoneNumber, DOB, address, photo, isMale);
        super.setId(id);
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.contactName = contactName;
        this.contactAddress = contactAddress;
        this.contactEmail = contactEmail;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactRelationship() {
        return contactRelationship;
    }

    public void setContactRelationship(String contactRelationship) {
        this.contactRelationship = contactRelationship;
    }

    @Override
    public String toString() {
        DateTimeFormatter formater = DateTimeFormat.forPattern("dd MM yyyy");
        String dob = formater.print(getDOB());
        String s = getId() + " " + getFullname() + " " + getEmail() + " "
                + getPhoneNumber() + " " + dob + " " + getAddress() + " "
                + contactName + " " + contactEmail + " " + contactPhone + " "
                + contactAddress + " " + contactRelationship;

        return s;
    }

    public String printDetail() {
        String output;
        output = getId() + "\t,"
                + getFullname() + "\t,";
        output = output + (isIsMale() == true ? "true\t," : "False\t,");
        output = output + getDOB().toString() + "\t,"
                + getEmail() + "\t,"
                + getPhoneNumber() + "\t,";

        String address = getAddress();
        if (address.equals("")) {
            address = " ";
        }
        address = address.replaceAll("[\n\t]", " ");
        output += address + "\t,";

        output = output + getContactName() + "\t,"
                + getContactRelationship() + "\t,"
                + getContactEmail() + "\t,"
                + getContactPhone() + "\t,";

        String contactAddressTemp = getContactAddress();
        if (contactAddressTemp.equals("")) {
            contactAddressTemp = " ";
        }
        contactAddressTemp = contactAddressTemp.replace("\n", " ");
        output += contactAddressTemp + "\n";

        return output;
    }
}
