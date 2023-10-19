package fxml_controller;

import hibernate.AccountingHibernateControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Accounting;
import model.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {

  @FXML public Button loginBtn;
  @FXML public TextField usernameTextField;
  @FXML public PasswordField passwordField;
  @FXML public Hyperlink registerHyperlink;
  @FXML public Label userFound;

    private Accounting accounting = null;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    AccountingHibernateControl accountingHibernateControl = new AccountingHibernateControl(emf);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accounting = accountingHibernateControl.findAccounting("accounting");
        if (accounting == null) {
            try {
                accounting = new Accounting();
                accounting.setAccName("accounting");
                accountingHibernateControl.create(accounting);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void login() throws IOException {
        User user = accounting.getUsers().stream().filter(con -> con.getLoginName().equals(usernameTextField.getText()) && con.getPassword().equals(passwordField.getText())).findFirst().orElse(null);
        if (user == null){
            userFound.setVisible(true);
        } else {
            userFound.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/CategoryManagement.fxml"));
            Parent root = loader.load();

            CategoryManagement categoryManagement = loader.getController();
            categoryManagement.setAccounting(accounting);
            categoryManagement.setUser(user);

            Stage stage = (Stage) loginBtn.getScene().getWindow();

            stage.setTitle("Accounting System");
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("logged in");
        }
    }

    public void register() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/RegisterPage.fxml"));
        Parent root = loader.load();

        RegisterPage registerPage = loader.getController();
        registerPage.setAccounting(accounting);

        Stage stage = (Stage) loginBtn.getScene().getWindow();

        stage.setTitle("Accounting System");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
