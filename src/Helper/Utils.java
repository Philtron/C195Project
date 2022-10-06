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

public class Utils {
    public static final String LOG_IN_WINDOW_LOCATION = "../View/LogInWindow.fxml";
    public static final String MAIN_WINDOW_LOCATION = "../View/MainWindow.fxml";
    public static final String ADD_APPOINTMENT_WINDOW_LOCATION = "../View/AddAppointmentWindow.fxml";
    public static final String MODIFY_APPOINTMENT_WINDOW_LOCATION = "../View/ModifyAppointmentWindow.fxml";
    public static final String CUSTOMER_VIEW_WINDOW = "../View/CustomerViewWindow.fxml";
    public static final String ADD_CUSTOMER_WINDOW_LOCATION = "../View/AddCustomerWindow.fxml";
    public static final String MODIFY_CUSTOMER_WINDOW_LOCATION = "../View/ModifyCustomerWindow.fxml";
    public static final String REPORTS_WINDOW_LOCATION = "../View/ReportsWindow.fxml";

    public static void changeWindow(ActionEvent event, String fileName, String title) throws IOException {

        Parent scene = FXMLLoader.load(Objects.requireNonNull(Utils.class.getResource(fileName)));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.setTitle(title);
        stage.show();

    }
    public static boolean confirmBack(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Nothing will be saved, are you sure you wish to leave?");
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            return true;
        }
        return false;
    }

    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
    public static void displayAlert(String message, String headerStr){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(headerStr);
        alert.showAndWait();
    }
    public static void updatePassFail(int rowsAffected) {
        if (rowsAffected > 0) {
            displayAlert("Update Successful");
        } else {
            displayAlert("UPDATE FAILED");
        }
    }

    public static boolean verifyBusinessHours(ZonedDateTime start, ZonedDateTime end) {

        ZonedDateTime businessOpen = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(8,0), ZoneId.of("US/Eastern"));
        ZonedDateTime businessClose = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(22, 0), ZoneId.of("US/Eastern"));

        if((start.isBefore(businessOpen)) || (start.isAfter(businessClose)) || (end.isBefore(businessOpen)) || (end.isAfter(businessClose))){
            return false;

        } else {
            return true;
        }
    }

    public static boolean checkCustomerOverlap(ZonedDateTime startOne, ZonedDateTime endOne, int customerID){
        Customer customer = CustomerQuery.select(customerID);
        ObservableList<Appointment> appointments = AppointmentQuery.filterByCustomerID(customerID);
//        System.out.println("check customer: " + appointments);
        for(int i=0; i < appointments.size();i++){
            ZonedDateTime startTwo = ZonedDateTime.of(appointments.get(i).getStart(),ZoneId.systemDefault());
            ZonedDateTime endTwo = ZonedDateTime.of(appointments.get(i).getEnd(), ZoneId.systemDefault());
//            System.out.println("S1: " + startOne + " S2: " +startTwo);
//            System.out.println("E1: " + endOne + " E2: " + endTwo);
            if((startOne.isBefore(startTwo) && endOne.isAfter(startTwo)) ||
                    (startOne.isAfter(startTwo) && startOne.isBefore(endTwo)) ||
                    (endOne.isAfter(startTwo) && endOne.isBefore(endTwo)) ||
                    (startOne.isEqual(startTwo))) {
                return false;
            }

        }
        return true;
    }

    public static int getLocalBusinessStartHour(){
        LocalDateTime ldtNow = LocalDateTime.now();
        ZoneId easternZID = ZoneId.of("US/Eastern");
        ZonedDateTime zdtNow = ZonedDateTime.of(ldtNow,ZoneId.systemDefault());
        int localOffset  = zdtNow.getOffset().getTotalSeconds();
        localOffset /= 3600;
        ZonedDateTime easternZDT = ZonedDateTime.ofInstant(zdtNow.toInstant(), easternZID);
        int easternOffset = easternZDT.getOffset().getTotalSeconds();
        easternOffset /= 3600;
        int offsetDifference = localOffset - easternOffset;
        int localStart = 8 + offsetDifference;
        if (localStart>24) {
            localStart = 8 + localStart %24;
        }
        int localStop = localStart + 14;

        System.out.println("localoffset: " + localOffset + " easternoffset: " + easternOffset);
        System.out.println("Offset difference: " + offsetDifference);
        System.out.println("start: " + localStart + " stop: " + localStop);


        return localStart;
    }
}
