package fxml_controller;

import hibernate.CategoryHibernateControl;
import hibernate.UserHibernateControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;
import model.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoriesToUser implements Initializable {

   @FXML public ListView<User> listView;
   @FXML public TextField userSearchTextField;
   @FXML public Button addBtn;

    Category category;
    User user;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(emf);
    UserHibernateControl userHibernateControl = new UserHibernateControl(emf);

    ObservableList<User> userList = FXCollections.observableArrayList();

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(userList);
    }

    public void textFieldKeyPressed() {
        if (userHibernateControl.findUser(userSearchTextField.getText()) != null) {
            if (!userList.contains(userHibernateControl.findUser(userSearchTextField.getText()))) {
                userList.add(userHibernateControl.findUser(userSearchTextField.getText()));
                listView.getSelectionModel().selectFirst();
            }
        } else userList.clear();
    }

    public void addBtnClicked() throws Exception {
        User user = listView.getSelectionModel().getSelectedItem();
        if (categoryHibernateControl.findCategory(category.getId()) != null && !user.checkIfCategoryExists(category.getId())) {
            userHibernateControl.addCategoryToUser(user, category);
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Category already exists!");
            alert.showAndWait();
        }
    }

}
