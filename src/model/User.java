package model;

import java.io.Serializable;
import javax.swing.ImageIcon;
import org.joda.time.LocalDate;

/**
 *
 * @author Dam Linh
 */
public abstract class User extends Person implements Serializable {

    private String password;

    public User(String id, String password, String lastname, String middlename, String firstname, String email, String phoneNumber, LocalDate DOB, String address, ImageIcon photo, boolean isMale) {
        super(lastname, middlename, firstname, email, phoneNumber, DOB, address, photo, isMale);
        this.password = password;
        super.setId(id);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract String printDetail();
}
