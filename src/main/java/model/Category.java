package model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "responsibleCategory", orphanRemoval = true/*, *//*cascade = CascadeType.ALL,*//* orphanRemoval = true*/)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "parentCategory", orphanRemoval = true/*, *//*cascade = CascadeType.ALL,*//* orphanRemoval = true*/)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Category> subCategories = new ArrayList<>();

    @ManyToOne
    private Category parentCategory = null;

    public Category() {
    }

    public Category(String name, String description, List<Transaction> transactions, List<Category> subCategories, Category parentCategory) {
        this.name = name;
        this.description = description;
        this.transactions = transactions;
        this.subCategories = subCategories;
        this.parentCategory = parentCategory;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void removeTransaction(int id) {
        transactions.removeIf(t -> t.getId() == id);
    }

    public String getSubCategoryIdAsString() {
        String subCat = "";
        for(Category c : subCategories) {
            subCat += c.getId() +",";
        }
        return subCat;
    }


    public int getIncome() {
        int income = 0;
        for (Transaction t : transactions) {
            if (t.getType().equals("income")) {
                income += t.getMoney();
            }
        }
        return income;
    }

    public int getExpense() {
        int expense = 0;
        for (Transaction t : transactions) {
            if (t.getType().equals("expense")) {
                expense += t.getMoney();
            }
        }
        return expense;
    }

    public int getBalance() {
        return getIncome() - getExpense();
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " NAME: " + name +
                " DESCRIPTION: " + description +
                " BALANCE: " + getBalance() +
                "PARENT CATEGORY NAME: " + parentCategory.getName() + "\n";
    }
}