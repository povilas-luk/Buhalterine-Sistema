package model;

import javax.persistence.Entity;

@Entity
public class Expense extends Transaction {
    private static final String TYPE = "expense";

    public Expense() {
        type = TYPE;
    }

    public Expense(String name, String description, int money, Category responsibleCategory) {
        super(name, description, money, responsibleCategory, TYPE);
    }


    public static String getTYPE() {
        return TYPE;
    }
}
