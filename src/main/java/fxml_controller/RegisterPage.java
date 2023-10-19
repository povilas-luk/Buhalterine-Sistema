package fxml_controller;


import hibernate.AccountingHibernateControl;
import hibernate.UserHibernateControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Accounting;
import model.Company;
import model.Consumer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterPage implements Initializable {

    @FXML public TextField username, email, firstName, lastName, phone, address, companyName;
    @FXML public Label alreadyExists, emptyFields, passwordMismatch;
    @FXML public PasswordField password, passwordRepeat;
    @FXML public CheckBox companyChBox, consumerChBox;
    @FXML public Button registerBtn, loginBtn;
    @FXML public Group companyInputField;

    Accounting accounting;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    AccountingHibernateControl accountingHibernateControl = new AccountingHibernateControl(emf);
    UserHibernateControl userHibernateControl = new UserHibernateControl(emf);

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void register() throws Exception {
        if (checkIfPasswordMatch() && !checkIfUsernameExists()) {
            if (!checkIfTextFieldFilled()) {return;}
            if (companyChBox.isSelected()) {
                Company company = new Company();
                company.setLoginName(username.getText());
                company.setPassword(password.getText());
                company.setFirstName(firstName.getText());
                company.setLastName(lastName.getText());
                company.setEmail(email.getText());
                company.setPhoneNumber(phone.getText());
                company.setAddress(address.getText());
                company.setCompanyName(companyName.getText());
                userHibernateControl.create(company);
                accountingHibernateControl.addUserToAccounting(accounting, company);

            } else if (consumerChBox.isSelected()) {
                Consumer consumer = new Consumer();
                consumer.setLoginName(username.getText());
                consumer.setPassword(password.getText());
                consumer.setFirstName(firstName.getText());
                consumer.setLastName(lastName.getText());
                consumer.setEmail(email.getText());
                consumer.setPhoneNumber(phone.getText());
                consumer.setAddress(address.getText());
                userHibernateControl.create(consumer);
                accountingHibernateControl.addUserToAccounting(accounting, consumer);
            }
            login();
        }
    }

    private boolean checkIfTextFieldFilled() {
        if (username == null || username.getText().trim().isEmpty() ||
                password == null || password.getText().trim().isEmpty() ||
                passwordRepeat == null || passwordRepeat.getText().trim().isEmpty() ||
                firstName == null || firstName.getText().trim().isEmpty() ||
                lastName == null || lastName.getText().trim().isEmpty() ||
                email == null || email.getText().trim().isEmpty() ||
                address == null || address.getText().trim().isEmpty() ||
                phone == null || phone.getText().trim().isEmpty()) {
            emptyFields.setVisible(true);
            return false;
        } else if (companyChBox.isSelected() && (companyName == null || companyName.getText().trim().isEmpty())) {
            emptyFields.setVisible(true);
            return false;
        } else {
            emptyFields.setVisible(false);
            return true;
        }
    }

    private boolean checkIfUsernameExists() {
        if (consumerChBox.isSelected()) {
            if (accounting.getUsers().stream().filter(con -> con.getLoginName().equals(username.getText())).findFirst().orElse(null) != null) {
                alreadyExists.setVisible(true);
                return true;
            }
        } else {
            alreadyExists.setVisible(false);
        }
        return false;
    }

    private boolean checkIfPasswordMatch() {
        if (password.getText().equals(passwordRepeat.getText())) {
            passwordMismatch.setVisible(false);
            return true;
        } else {
            passwordMismatch.setVisible(true);
            return false;
        }
    }

    public void login() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/LoginPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) registerBtn.getScene().getWindow();

        stage.setTitle("Accounting System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void consumerCheck() {
        if (consumerChBox.isSelected()) {
            companyChBox.setSelected(false);
            companyInputField.setVisible(false);
        }
    }

    public void companyCheck() {
        if (companyChBox.isSelected()) {
            consumerChBox.setSelected(false);
            companyInputField.setVisible(true);
        }
    }
}
