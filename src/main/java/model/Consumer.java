package model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Consumer extends User {
    public static final String TYPE = "consumer";

    public Consumer() {
        type = TYPE;
    }

    public Consumer(String loginName, String password, String firstName, String lastName, String email, String phoneNumber, String address, List<Category> categories) {
        super(loginName, password, firstName, lastName, email, phoneNumber, address, TYPE, categories);
    }

    public static String getTYPE() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "Username: " + loginName +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName;
    }
}
