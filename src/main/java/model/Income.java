package model;

import javax.persistence.Entity;

@Entity
public class Income extends Transaction {
    private static final String TYPE = "income";

    public Income() {
        type = TYPE;
    }

    public Income(String name, String description, int money, Category responsibleCategory) {
        super(name, description, money, responsibleCategory, TYPE);
    }

    public static String getTYPE() {
        return TYPE;
    }
}
