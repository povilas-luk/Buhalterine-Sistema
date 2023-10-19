package model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Company extends User {
    public static final String TYPE = "company";

    private String companyName;

    public Company() {
        type = TYPE;
    }

    public Company(String loginName, String password, String firstName, String lastName, String email, String phoneNumber, String address, List<Category> categories) {
        super(loginName, password, firstName, lastName, email, phoneNumber, address, TYPE, categories);
    }

    public Company(String companyName) {
        this.companyName = companyName;
        type = TYPE;
    }

    public Company(String loginName, String password, String firstName, String lastName, String email, String phoneNumber, String address, List<Category> categories, String companyName) {
        super(loginName, password, firstName, lastName, email, phoneNumber, address, TYPE, categories);
        this.companyName = companyName;
    }

    public static String getTYPE() {
        return TYPE;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Username: " + loginName +
                "\nCompany Name: " + companyName;
    }
}
