package Controller;

import DatabaseAccess.JDBC;
import Helper.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInWindowController implements Initializable {
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
        Connection con = JDBC.getConnection();
        PreparedStatement query = con.prepareStatement("SELECT * FROM users WHERE User_Name =\"" + uname + "\" AND " +
                "Password =\"" + password + "\"");
        System.out.println("SELECT * FROM users WHERE User_Name =\"" + uname + "\" AND " +
                "Password =\"" + password + "\"");
        ResultSet rs = query.executeQuery();
        if(rs.next()){
            System.out.println("Logged In");
            Utils.changeWindow(event, "../View/MainWindow.fxml", "Main Window");

        } else {
            System.out.println("INVALID LOG IN");
            Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID LOG IN");
            alert.showAndWait();
        }

    }
    @FXML
    void OnClickExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)){
            JDBC.closeConnection();
            System.exit(0);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocaleLabel.setText(String.valueOf(ZoneId.systemDefault()));
    }
}

