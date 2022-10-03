package Controller;

import DatabaseAccess.JDBC;
import DatabaseAccess.UserQuery;
import Helper.Utils;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInWindowController implements Initializable {
    public static User loggedInUser;

    @FXML
    private Label titleLabel;

    @FXML
    private Label LocaleLabel;

    @FXML
    private Button LogInButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label PasswordLabel;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Label UNameLabel;

    @FXML
    private TextField UNameTextField;

    @FXML
    void OnClickLogIn(ActionEvent event) throws SQLException, IOException {

        String uname = UNameTextField.getText();
        String password = PasswordTextField.getText();

        if(UserQuery.logIn(uname, password)){
            Utils.changeWindow(event, "../View/MainWindow.fxml", "Main Window");
            loggedInUser = UserQuery.selectUser(uname);
            System.out.println(loggedInUser);
        } else {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("LogIn");
            String logInError = resourceBundle.getString("InvalidLogIn");
            Alert alert = new Alert(Alert.AlertType.ERROR, logInError);
            alert.showAndWait();
        }

    }
    @FXML
    void OnClickExit(ActionEvent event) {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("LogIn");
        String exitConfirmation = resourceBundle.getString("exitConfirmation");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, exitConfirmation);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            JDBC.closeConnection();
            System.exit(0);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocaleLabel.setText(String.valueOf(ZoneId.systemDefault()));
        resourceBundle = ResourceBundle.getBundle("LogIn");
        UNameLabel.setText(resourceBundle.getString("UNameLabel"));
        PasswordLabel.setText(resourceBundle.getString("PasswordLabel"));
        exitButton.setText(resourceBundle.getString("exitButton"));
        LogInButton.setText(resourceBundle.getString("LogInButton"));
        titleLabel.setText(resourceBundle.getString("TitleLabel"));
    }
}

