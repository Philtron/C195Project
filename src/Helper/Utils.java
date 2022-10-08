package Helper;

import DatabaseAccess.AppointmentQuery;
import DatabaseAccess.CustomerQuery;
import Model.Appointment;
import Model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/** Collection of useful methods for the C195 Software Scheduling program */
public class Utils {

    /** Location of fxml files for all windows used in this program. Saved here for the ability to change them all
     * in one place.
     */
    public static final String LOG_IN_WINDOW_LOCATION = "../View/LogInWindow.fxml";
    /** main window location */
    public static final String MAIN_WINDOW_LOCATION = "../View/MainWindow.fxml";
    /** app appointment window location */
    public static final String ADD_APPOINTMENT_WINDOW_LOCATION = "../View/AddAppointmentWindow.fxml";
    /** modify window location */
    public static final String MODIFY_APPOINTMENT_WINDOW_LOCATION = "../View/ModifyAppointmentWindow.fxml";
    /** customer view window location */
    public static final String CUSTOMER_VIEW_WINDOW = "../View/CustomerViewWindow.fxml";
    /** add customer window location */
    public static final String ADD_CUSTOMER_WINDOW_LOCATION = "../View/AddCustomerWindow.fxml";
    /** modify customer window location */
    public static final String MODIFY_CUSTOMER_WINDOW_LOCATION = "../View/ModifyCustomerWindow.fxml";
    /** reports window location */
    public static final String REPORTS_WINDOW_LOCATION = "../View/ReportsWindow.fxml";

    /** Changes the window to the window matching the fileName parameter.
     *
     * @param event mouseclick that activated the button.
     * @param fileName String name of the file that the window will be changed to.
     * @param title String title the new window should have.
     * @throws IOException if the fxml file to be changed to isn't found.
     */
    public static void changeWindow(ActionEvent event, String fileName, String title) throws IOException {

        Parent scene = FXMLLoader.load(Objects.requireNonNull(Utils.class.getResource(fileName)));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.setTitle(title);
        stage.show();

    }

    /** Creates and displays an alert whenever leaving a window that has multiple controls that may be populated with
     * user entered data.
     * @return true if customer agrees to continue, or false otherwise.
     */
    public static boolean confirmBack(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Nothing will be saved, are you sure you wish to leave?");
        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent()) && (result.get() == ButtonType.OK);
    }

    /** Generic information alert created and displayed with the String message parameter as its content.
     *
     * @param message String message to be displayed in the information alert.
     */
    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    /** Overloaded generic alert displaying method, adds the ability to change the alert header.
     *
     * @param message String message to be displayed in the information alert.
     * @param headerStr String to change the alert's header to.
     */
    public static void displayAlert(String message, String headerStr){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(headerStr);
        alert.showAndWait();
    }

    /** Method to inform the user if a query was successful or not, based on the number of rows affected in a
     * PreparedStatement execution.
     *
     * @param rowsAffected int number of rows affected in a PreparedStatement.
     * @return true if successful, false otherwise.
     * */
    public static boolean updatePassFail(int rowsAffected) {
        if (rowsAffected > 0) {
            displayAlert("Update Successful");
            return true;
        } else {
            displayAlert("UPDATE FAILED");
            return false;
        }
    }

    /** Checks that a new appointment's hours coincides with the companies business hours in Eastern Time.
     *
     * @param start ZonedDateTime of new appointment's start time.
     * @param end ZonedDateTime of new appointment's end time.
     * @return true if the new hours fall within business hours, false otherwise.
     */
    public static boolean verifyBusinessHours(ZonedDateTime start, ZonedDateTime end) {


        ZonedDateTime businessOpen = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(8,0),
                ZoneId.of("US/Eastern"));
        ZonedDateTime businessClose = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(22, 0),
                ZoneId.of("US/Eastern"));

        // Start and end aren't before business open or after business close = true
        return (!start.isBefore(businessOpen)) && (!start.isAfter(businessClose)) && (!end.isBefore(businessOpen)) &&
                (!end.isAfter(businessClose));
    }

    /** Queries the database and creates a list of all appointments a customer is scheduled to attend, then checks to
     * make sure that a new appointment does not overlap any of the already scheduled ones.
     *
     * @param startOne ZonedDateTime of new appointments start time.
     * @param endOne ZonedDateTime of new appointments end time.
     * @param customerID int customerID to filter appointments by.
     * @return true if the hours don't overlap. False otherwise.
     */
    public static boolean checkCustomerOverlap(ZonedDateTime startOne, ZonedDateTime endOne, int customerID){
        ObservableList<Appointment> appointments = AppointmentQuery.filterByCustomerID(customerID);
        for(int i=0; i < appointments.size();i++){
            ZonedDateTime startTwo = ZonedDateTime.of(appointments.get(i).getStart(),ZoneId.systemDefault());
            ZonedDateTime endTwo = ZonedDateTime.of(appointments.get(i).getEnd(), ZoneId.systemDefault());

            if((startOne.isBefore(startTwo) && endOne.isAfter(startTwo)) ||
                    (startOne.isAfter(startTwo) && startOne.isBefore(endTwo)) ||
                    (endOne.isAfter(startTwo) && endOne.isBefore(endTwo)) ||
                    (startOne.isEqual(startTwo))) {
                return false;
            }
        }
        return true;
    }

    /** Overloaded method. Adds an appointment ID so that when modifying appointments, it doesn't look at the existing
     * appointment as an overlap.
     * Queries the database and creates a list of all appointments a customer is scheduled to attend, then checks to
     * make sure that a new appointment does not overlap any of the already scheduled ones.
     *
     * @param startOne ZonedDateTime of new appointments start time.
     * @param endOne ZonedDateTime of new appointments end time.
     * @param customerID int customerID to filter appointments by.
     * @param appointmentID int appointmentID to filter out looking at this appointment when checking hours.
     * @return true if the hours don't overlap. False otherwise.
     */
    public static boolean checkCustomerOverlap(ZonedDateTime startOne, ZonedDateTime endOne, int customerID,
                                               int appointmentID){
        Customer customer = CustomerQuery.select(customerID);
        ObservableList<Appointment> appointments = AppointmentQuery.filterByCustomerID(customerID);
        for(int i=0; i < appointments.size();i++){
            ZonedDateTime startTwo = ZonedDateTime.of(appointments.get(i).getStart(),ZoneId.systemDefault());
            ZonedDateTime endTwo = ZonedDateTime.of(appointments.get(i).getEnd(), ZoneId.systemDefault());

            if((startOne.isBefore(startTwo) && endOne.isAfter(startTwo)) ||
                    (startOne.isAfter(startTwo) && startOne.isBefore(endTwo)) ||
                    (endOne.isAfter(startTwo) && endOne.isBefore(endTwo)) ||
                    (startOne.isEqual(startTwo))) {
                if(appointments.get(i).getAppointmentID() != appointmentID){
                    return false;
                }
            }
        }
        return true;
    }

}
