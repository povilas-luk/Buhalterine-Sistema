package fxml_controller;

import hibernate.CategoryHibernateControl;
import hibernate.TransactionsHibernateControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class TransactionsPage implements Initializable {

    @FXML public TableView<Income> incomeTable;
    @FXML public TableColumn<Income, String> incomeNameCol;
    @FXML public TableColumn<Income, String> incomeDescriptionCol;
    @FXML public TableColumn<Income, Number> incomeIncomeCol;
    @FXML public TextField incomeNameTxtField, incomeDescTxtField, incomeIncomeTxtField;
    @FXML public Button incomeAddBtn, incomeRemoveBtn;

    @FXML public TableView<Expense> expenseTable;
    @FXML public TableColumn<Expense, String> expenseNameCol;
    @FXML public TableColumn<Expense, String> expenseDescriptionCol;
    @FXML public TableColumn<Expense, Number> expenseExpenseCol;
    @FXML public TextField expenseNameTxtField, expenseDescTxtField, expenseExpenseTxtField;
    @FXML public Button expenseAddBtn, expenseRemoveBtn;

    @FXML public Button backBtn;

    Accounting accounting;
    Category category;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(emf);
    TransactionsHibernateControl transactionsHibernateControl = new TransactionsHibernateControl(emf);

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    public void setCategory(Category category) {
        this.category = category;
        setIncomeTable();
        setExpenseTable();
    }

    public void setIncomeTable() {
        incomeTable.setItems(getIncomes());
        incomeTable.refresh();
    }

    public void setExpenseTable() {
        expenseTable.setItems(getExpenses());
        expenseTable.refresh();
    }

    public ObservableList<Income> getIncomes() {
        ObservableList<Income> incomes = FXCollections.observableArrayList();
        for (Transaction t : category.getTransactions()) {
            if (t.getType().equals("income")) {
                incomes.add((Income) t);
            }
        }
        return incomes;
    }

    public ObservableList<Expense> getExpenses() {
        ObservableList<Expense> expenses = FXCollections.observableArrayList();
        for (Transaction t : category.getTransactions()) {
            if (t.getType().equals("expense")) {
                expenses.add((Expense) t);
            }
        }
        return expenses;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        incomeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        incomeDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        incomeIncomeCol.setCellValueFactory(new PropertyValueFactory<>("money"));

        expenseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        expenseDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        expenseExpenseCol.setCellValueFactory(new PropertyValueFactory<>("money"));

        incomeTable.setEditable(true);
        incomeNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        incomeDescriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        incomeIncomeCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        expenseTable.setEditable(true);
        expenseNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        expenseDescriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        expenseExpenseCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    }

    public void incomeAddBtnPressed() throws Exception {
        Income income = new Income(incomeNameTxtField.getText(), incomeDescTxtField.getText(), Integer.parseInt(incomeIncomeTxtField.getText()), categoryHibernateControl.findCategory(category.getId()));
        transactionsHibernateControl.create(income);
        categoryHibernateControl.addTransactionToCategory(categoryHibernateControl.findCategory(category.getId()), income);
        category.getTransactions().add(income);
        setIncomeTable();
    }

    public void expenseAddBtnPressed() throws Exception {
        Expense expense = new Expense(expenseNameTxtField.getText(), expenseDescTxtField.getText(), Integer.parseInt(expenseExpenseTxtField.getText()), categoryHibernateControl.findCategory(category.getId()));
        transactionsHibernateControl.create(expense);
        categoryHibernateControl.addTransactionToCategory(categoryHibernateControl.findCategory(category.getId()), expense);
        category.getTransactions().add(expense);
        setExpenseTable();
    }

    public void IncomeRemoveBtnPressed() throws Exception {
        Income income = incomeTable.getSelectionModel().getSelectedItem();
        if (income != null) {
            categoryHibernateControl.removeTransactionFromCategory(category, income);
            transactionsHibernateControl.destroy(income.getId());
            category.removeTransaction(income.getId());
            setIncomeTable();
        }
    }

    public void expenseRemoveBtnPressed() throws Exception {
        Expense expense = expenseTable.getSelectionModel().getSelectedItem();
        if (expense != null) {
            categoryHibernateControl.removeTransactionFromCategory(category, expense);
            transactionsHibernateControl.destroy(expense.getId());
            category.removeTransaction(expense.getId());
            setExpenseTable();
        }
    }

    public void backBtnPressed() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    public void incomeNameEdited(TableColumn.CellEditEvent<Income, String> incomeStringCellEditEvent) throws Exception {
        Income income = incomeTable.getSelectionModel().getSelectedItem();
        income.setName(incomeStringCellEditEvent.getNewValue());
        transactionsHibernateControl.edit(income);
    }

    public void incomeDescriptionEdited(TableColumn.CellEditEvent<Income, String> incomeStringCellEditEvent) throws Exception {
        Income income = incomeTable.getSelectionModel().getSelectedItem();
        income.setDescription(incomeStringCellEditEvent.getNewValue());
        transactionsHibernateControl.edit(income);
    }

    public void incomeIncomeEdited(TableColumn.CellEditEvent<Income, Number> incomeNumberCellEditEvent) throws Exception {
        Income income = incomeTable.getSelectionModel().getSelectedItem();
        income.setMoney(incomeNumberCellEditEvent.getNewValue().intValue());
        transactionsHibernateControl.edit(income);
    }

    public void expenseNameEdited(TableColumn.CellEditEvent<Expense, String> expenseStringCellEditEvent) throws Exception {
        Expense expense = expenseTable.getSelectionModel().getSelectedItem();
        expense.setName(expenseStringCellEditEvent.getNewValue());
        transactionsHibernateControl.edit(expense);
    }

    public void expenseDescriptionEdited(TableColumn.CellEditEvent<Expense, String> expenseStringCellEditEvent) throws Exception {
        Expense expense = expenseTable.getSelectionModel().getSelectedItem();
        expense.setDescription(expenseStringCellEditEvent.getNewValue());
        transactionsHibernateControl.edit(expense);
    }

    public void expenseExpenseEdited(TableColumn.CellEditEvent<Expense, Number> expenseNumberCellEditEvent) throws Exception {
        Expense expense = expenseTable.getSelectionModel().getSelectedItem();
        expense.setMoney(expenseNumberCellEditEvent.getNewValue().intValue());
        transactionsHibernateControl.edit(expense);
    }
}
