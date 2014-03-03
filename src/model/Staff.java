package model;

import java.io.Serializable;
import javax.swing.ImageIcon;
import org.joda.time.LocalDate;

/**
 *
 * @author Dam Linh
 */
public class Staff extends User implements Serializable {

    public Staff(String id, String password, String lastname, String middlename,
            String firstname, String email, String phoneNumber, LocalDate DOB,
            String address, ImageIcon photo, boolean isMale) {
        super(id, password, lastname, middlename, firstname, email, phoneNumber, DOB, address, photo, isMale);
    }

    @Override
    public String printDetail() {
        String output;
        output = getId() + "\t,"
                + getPassword() + "\t,"
                + "Staff\t,"
                + getFullname() + "\t,";
        output = output + (isIsMale() == true ? "true\t," : "false\t,");
        output = output + getDOB().toString() + "\t,"
                + getEmail() + "\t,"
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
