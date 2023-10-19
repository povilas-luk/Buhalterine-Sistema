package model;

import javax.persistence.*;

@Entity
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    protected String name;
    protected String description;
    protected int money;
    @ManyToOne
    protected Category responsibleCategory = null;
    protected String type;

    public Transaction() {
    }

    public Transaction(String name, String description, int money, Category responsibleCategory, String type) {
        this.name = name;
        this.description = description;
        this.money = money;
        this.responsibleCategory = responsibleCategory;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Category getResponsibleCategory() {
        return responsibleCategory;
    }

    public void setResponsibleCategory(Category responsibleCategory) {
        this.responsibleCategory = responsibleCategory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
