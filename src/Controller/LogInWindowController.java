package Controller;

import DatabaseAccess.AppointmentQuery;
import DatabaseAccess.JDBC;
import DatabaseAccess.UserQuery;
import Helper.Logging;
import Helper.Utils;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInWindowController implements Initializable {
    public static User CurrentUser;

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
    void OnClickLogIn(ActionEvent event) throws IOException {

        String uname = UNameTextField.getText();
        String password = PasswordTextField.getText();

        if(UserQuery.logIn(uname, password)){
            Logging.logLogIn(true, uname);
            CurrentUser = UserQuery.selectUser(uname);
            Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
            ArrayList<String> appointmentsInFifteenMinutes = AppointmentQuery.appointmentsInFifteenMinutes();
            if(appointmentsInFifteenMinutes.size()>=1){
                for(int i = 0; i < appointmentsInFifteenMinutes.size(); i++) {
                    Utils.displayAlert(appointmentsInFifteenMinutes.get(i), "Appointments in 15 Minutes or Less: ");
                    System.out.println(appointmentsInFifteenMinutes.get(i));
                }
            } else {
                Utils.displayAlert("No appointments scheduled in the next fifteen minutes.");
            }

        } else {
            Logging.logLogIn(false, uname);
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

