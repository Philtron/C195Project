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


/** This class controls the log in window. It captures the entered username and password and verifies them against the
 * database. It then changes window to the main window.
 */
public class LogInWindowController implements Initializable {

    /** Saves the info of the user whose credentials were entered and verified in the uname and password textfields. */
    public static User CurrentUser;

    /** Label that displays title in English or French depending on user's locale. */
    @FXML
    private Label titleLabel;

    /** Label that displays the user's locale. */
    @FXML
    private Label LocaleLabel;

    /** Button to initiate login credential checking and if successful changes window to main window. */
    @FXML
    private Button LogInButton;

    /** Exit button. Shuts program down after confirmation. */
    @FXML
    private Button exitButton;

    /** Labels the password textfield in English or French depending on user's locale. */
    @FXML
    private Label PasswordLabel;

    /** Captures the entered password. */
    @FXML
    private TextField PasswordTextField;

    /** Lables the username textfield in English or French depending on the user's locale. */
    @FXML
    private Label UNameLabel;

    /** Captures entered username */
    @FXML
    private TextField UNameTextField;

    /** Activates logging in. Verfies the username and password entered against known users in the database. If
     * successful sets a static user variable and changes the window to the main window. All attempts, successful or
     * otherwise, are logged in a login activity file. When logging in, the database is queried to check if any
     * appointments are scheduled in the next fiften minutes.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if the main window fxml file isn't found.
     */
    @FXML
    void OnClickLogIn(ActionEvent event) throws IOException {

        String uname = UNameTextField.getText();
        String password = PasswordTextField.getText();

        // If credentials are correct, log in.
        if(UserQuery.logIn(uname, password)){
            Logging.logLogIn(true, uname);
            CurrentUser = UserQuery.selectUser(uname);
            Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
            // Build a list of appointments schedule in the next fifteen minutes.
            ArrayList<String> appointmentsInFifteenMinutes = AppointmentQuery.appointmentsInFifteenMinutes();

            // If the list contains anything.
            if(appointmentsInFifteenMinutes.size()>=1){
                for(int i = 0; i < appointmentsInFifteenMinutes.size(); i++) {
                    Utils.displayAlert(appointmentsInFifteenMinutes.get(i),
                            "Appointments in 15 Minutes or Less: ");
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

    /** Exit button, confirms with the user, then closes the database connection and shuts the program down.
     *
     * @param event mouseclick that activates the button.
     */
    @FXML
    void OnClickExit(ActionEvent event) {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("LogIn");
        String exitConfirmation = resourceBundle.getString("exitConfirmation");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, exitConfirmation);
        Optional<ButtonType> result = alert.showAndWait();

        // If the confirm button is clicked.
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            JDBC.closeConnection();
            System.exit(0);
        }
    }

    /** The first method run when the window is created. Sets the labels in English or French depending on
     * the user's locale.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location
     * is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     * object was not localized.
     */
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

