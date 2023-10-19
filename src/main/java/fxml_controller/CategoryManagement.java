package fxml_controller;


import hibernate.AccountingHibernateControl;
import hibernate.CategoryHibernateControl;
import hibernate.TransactionsHibernateControl;
import hibernate.UserHibernateControl;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CategoryManagement implements Initializable {

    @FXML public TreeTableColumn<Category, String> tableCollName;
    @FXML public TreeTableColumn<Category, String> tableCollDescription;
    @FXML public TreeTableColumn<Category, Number> tableCollIncome;
    @FXML public TreeTableColumn<Category, Number>  tableCollExpense;
    @FXML public TreeTableColumn<Category, Number>  tableCollBalance;
    @FXML public TreeTableView<Category> treeTableView;
    @FXML public TextField nameInput, descriptionInput;
    @FXML public Button addCategoryBtn, deleteBtn, deleteUserBtn, assignCatToUserBtn, logout;
    @FXML public ColorPicker colorPicker;
    @FXML public Label usernameLb, firstNameLb, passwordLb, lastNameLb, emailLb, phoneLb, companyLb, addressLb, companyTextLb;
    @FXML public ComboBox<String> categoryComboBox;
    @FXML public Button transactionsBtn;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(emf);
    UserHibernateControl userHibernateControl = new UserHibernateControl(emf);
    AccountingHibernateControl accountingHibernateControl = new AccountingHibernateControl(emf);
    TransactionsHibernateControl transactionsHibernateControl = new TransactionsHibernateControl(emf);

    Accounting accounting;
    User user;
    Category rootCategory;

    ObservableList<String> categoryNameList = FXCollections.observableArrayList();
    ObservableList<Category> categoryList = FXCollections.observableArrayList();

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    public void setUser(User user) {
        this.user = user;
        loadCategoriesToTable();
        categoryComboBox.setItems(categoryNameList);
        loadUserData();
    }


    private void loadUserData() {
        if (user.getType().equals("consumer")) {
            usernameLb.setText(user.getLoginName());
            passwordLb.setText(user.getPassword());
            firstNameLb.setText(user.getFirstName());
            lastNameLb.setText(user.getLastName());
            phoneLb.setText(user.getPhoneNumber());
            addressLb.setText(user.getAddress());
            emailLb.setText(user.getEmail());
            companyTextLb.setVisible(false);
            companyLb.setVisible(false);

        } else if (user.getType().equals("company")) {
            usernameLb.setText(user.getLoginName());
            passwordLb.setText(user.getPassword());
            companyLb.setText(((Company) user).getCompanyName());
            firstNameLb.setText(user.getFirstName());
            lastNameLb.setText(user.getLastName());
            phoneLb.setText(user.getPhoneNumber());
            addressLb.setText(user.getAddress());
            emailLb.setText(user.getEmail());
            companyTextLb.setVisible(true);
            companyLb.setVisible(true);
        }
    }

    private void loadCategoriesToTable() {
        TreeItem<Category> cat = new TreeItem<>(rootCategory);
        loadCategories(userHibernateControl.findUser(user.getLastName()).getCategories(), cat);
        rootCategory.setTransactions(getAllTransactions());
        tableCollName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, String> param) -> new SimpleStringProperty(param.getValue().getValue().getName()));
        tableCollDescription.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, String> param) -> new SimpleStringProperty(param.getValue().getValue().getDescription()));
        tableCollIncome.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, Number> param) -> new SimpleIntegerProperty(param.getValue().getValue().getIncome()));
        tableCollExpense.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, Number> param) -> new SimpleIntegerProperty(param.getValue().getValue().getExpense()));
        tableCollBalance.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, Number> param) -> new SimpleIntegerProperty(param.getValue().getValue().getBalance()));

        tableCollName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        tableCollDescription.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        cat.setExpanded(true);
        treeTableView.setEditable(true);
        treeTableView.setRoot(cat);
    }

    public void loadCategories(List<Category> categories, TreeItem<Category> treeCategories) {
        categoryNameList.clear();
        categoryList.clear();
        for (Category c : categories) {
            TreeItem<Category> cat = new TreeItem<>(c);
            treeCategories.getChildren().add(cat);
            loadCategories(c.getSubCategories(), cat);
            categoryNameList.add(c.getName());
            categoryList.add(c);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootCategory = new Category();
        rootCategory.setName("Categories");
        rootCategory.setDescription("Description");
    }


    public void editedName(TreeTableColumn.CellEditEvent<Category, String> categoryStringCellEditEvent) throws Exception {
        TreeItem<Category> current = treeTableView.getTreeItem(categoryStringCellEditEvent.getTreeTablePosition().getRow());
        current.getValue().setName(categoryStringCellEditEvent.getNewValue());
        categoryHibernateControl.edit(current.getValue());
        loadCategoriesToTable();
    }

    public void editedDescription(TreeTableColumn.CellEditEvent<Category, String> categoryStringCellEditEvent) throws Exception {
        TreeItem<Category> current = treeTableView.getTreeItem(categoryStringCellEditEvent.getTreeTablePosition().getRow());
        current.getValue().setDescription(categoryStringCellEditEvent.getNewValue());
        categoryHibernateControl.edit(current.getValue());
        loadCategoriesToTable();
    }

    public void addCategoryBtnPressed() throws Exception {
        TreeItem<Category> current = treeTableView.getSelectionModel().getSelectedItem();
        if (current == null) {
            categoryNotSelectedAlert();
            return;
        }
        if (!categoryNameList.contains(nameInput.getText()) && checkIfInputNotEmpty())
        {
            System.out.println(current.getValue().getName());
            Category newCategory = new Category();
            newCategory.setName(nameInput.getText());
            newCategory.setDescription(descriptionInput.getText());
            if (current.getValue().getName().equals("Categories")) {
                categoryHibernateControl.create(newCategory);
                userHibernateControl.addCategoryToUser(user, categoryHibernateControl.findCategory(newCategory.getId()));

            } else {
                newCategory.setParentCategory(categoryHibernateControl.findCategory(current.getValue().getId()));
                categoryHibernateControl.create(newCategory);
                //categoryHibernateControl.createSubCategory(newCategory, categoryHibernateControl.findCategory(current.getValue().getId()));
            }
            categoryNameList.add(newCategory.getName());
            treeTableView.refresh();
            current.getChildren().add(new TreeItem<>(newCategory));
        }

    }

    private boolean checkIfInputNotEmpty() {
        return nameInput != null && !nameInput.getText().trim().isEmpty() &&
                descriptionInput != null && !descriptionInput.getText().trim().isEmpty();
    }

    public void deleteSelected() throws Exception {
        TreeItem<Category> current = treeTableView.getSelectionModel().getSelectedItem();
        if (current == null) {
            categoryNotSelectedAlert();
            return;
        }
        Category category = categoryHibernateControl.findCategory(current.getValue().getId());
        if (category != null) {
            categoryNameList.remove(category.getName());
            Category parent = category.getParentCategory();
            if (parent == null) {
                deleteCategoryFromUser(category);
            } else {
                categoryHibernateControl.destroy(category.getId());
            }
            try {
                categoryHibernateControl.destroy(category.getId());
            } catch (Exception ignored) {}
            current.getParent().getChildren().remove(current);
            treeTableView.refresh();
        }
    }

    public void deleteCategoryFromUser(Category category) throws Exception {
        for (Category c : category.getSubCategories()) {
            deleteTransactionsFromCategory(c);
            categoryHibernateControl.destroy(c.getId());
        }
        category.getSubCategories().clear();
        userHibernateControl.removeCategoryFromUser(user, category);
    }

    public void deleteTransactionsFromCategory(Category category) throws Exception {
        for (Transaction t : category.getTransactions()) {
            categoryHibernateControl.removeTransactionFromCategory(category, t);
            transactionsHibernateControl.destroy(t.getId());
            category.removeTransaction(t.getId());
        }
        category.getTransactions().clear();
    }

    public void categoryNotSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Select Category!");
        alert.show();
    }

    public void colorPicked() {
        usernameLb.setTextFill(colorPicker.getValue());
        passwordLb.setTextFill(colorPicker.getValue());
        lastNameLb.setTextFill(colorPicker.getValue());
        firstNameLb.setTextFill(colorPicker.getValue());
        companyLb.setTextFill(colorPicker.getValue());
        addressLb.setTextFill(colorPicker.getValue());
        emailLb.setTextFill(colorPicker.getValue());
        phoneLb.setTextFill(colorPicker.getValue());
    }

    public void CategoryComboBoxSelected() {
        if (categoryComboBox.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Category Information");
            alert.setHeaderText(null);
            alert.setContentText(Objects.requireNonNull(categoryList.stream().filter(c -> c.getName().equals(categoryComboBox.getSelectionModel().getSelectedItem())).findAny().orElse(null)).toString());
            alert.showAndWait();
        }
    }

    public void deleteUser() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete User ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            accountingHibernateControl.removeUserFromAccounting(accounting,user);
            for (Category c : user.getCategories()) {
                try {
                    deleteCategoryFromUser(c);
                    categoryHibernateControl.destroy(c.getId());
                } catch (Exception ignored){}
            }
            userHibernateControl.destroy(user.getLoginName());
            login();
        }
    }

    public void login() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/LoginPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) deleteBtn.getScene().getWindow();

        stage.setTitle("Accounting System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void assignCatToUserBtnClicked() throws IOException {
        Category category = null;
        if (!treeTableView.getSelectionModel().isEmpty()) {
            category =  categoryHibernateControl.findCategory(treeTableView.getSelectionModel().getSelectedItem().getValue().getId());
        }
        if (category == null) {
            categoryNotSelectedAlert();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AddCategoriesToUser.fxml"));
            Parent root = loader.load();

            AddCategoriesToUser addCategoriesToUser = loader.getController();
            addCategoriesToUser.setUser(user);
            addCategoriesToUser.setCategory(category);

            Stage stage = new Stage();

            stage.setTitle("Select User");
            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    public void logoutBtnPressed() throws IOException {
        login();
    }

    public void transactionsBtnPressed() throws IOException {
        Category category = null;
        if (!treeTableView.getSelectionModel().isEmpty()) {
            category =  categoryHibernateControl.findCategory(treeTableView.getSelectionModel().getSelectedItem().getValue().getId());
        }
        if (category == null) {
            categoryNotSelectedAlert();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/TransactionsPage.fxml"));
            Parent root = loader.load();

            TransactionsPage transactionsPage = loader.getController();
            transactionsPage.setAccounting(accounting);
            transactionsPage.setCategory(category);

            Stage stage = new Stage();

            stage.setTitle("Transactions");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadCategoriesToTable();
            treeTableView.refresh();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (Category c : categoryList) {
            transactions.addAll(c.getTransactions());
        }
        return transactions;
    }

}

