package model;

import java.io.Serializable;
import javax.swing.ImageIcon;
import org.joda.time.LocalDate;

public class Teacher extends Person implements Serializable {

    private String[][] skills;

    public Teacher(String[][] skills, String lastname,
            String middlename, String firstname, String email, String phoneNumber,
            LocalDate DOB, String address, ImageIcon photo, boolean isMale) {
        super(lastname, middlename, firstname, email, phoneNumber, DOB, address, photo, isMale);
        super.setId(Person.generateId());
        this.skills = skills;
    }

    public Teacher(String id, String[][] skills, String lastname,
            String middlename, String firstname, String email, String phoneNumber,
            LocalDate DOB, String address, ImageIcon photo, boolean isMale) {
        super(lastname, middlename, firstname, email, phoneNumber, DOB, address, photo, isMale);
        super.setId(id);
        this.skills = skills;
    }

    public String[][] getSkills() {
        return skills;
    }

    public void setSkills(String[][] skills) {
        this.skills = skills;
    }

    public String getSkillString() {
        String skillString = "";
        for (int i = 0; i < skills.length; i++) {
            skillString += skills[i][0];
            if (i + 1 < skills.length && skills[i + 1] != null) {
                skillString += ", ";
            }
        }

        return skillString;
    }

    public String printDetail() {
        String output;
        output = getId() + "\t,"
                + getFullname() + "\t,";
        output = output + (isIsMale() == true ? "true\t," : "false\t,");
        output = output + getDOB().toString() + "\t,";

        String strSkills = "";
        for (int i = 0; i < skills.length; i++) {
            strSkills += skills[i][0] + "-" + skills[i][1];
            if (i != skills.length - 1) {
                strSkills += " ";
            }
        }
        output += strSkills + "\t,";

        output = output + getEmail() + "\t,"
                + getPhoneNumber() + "\t,";

        String address = getAddress();
        address = address.replaceAll("[\n\t]", " ");
        if (address.equals("")) {
            address = " ";
        }
        output += address + "\n";

        return output;
    }
}
