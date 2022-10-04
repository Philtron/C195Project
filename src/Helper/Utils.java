package Helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Utils {
    public static void changeWindow(ActionEvent event, String fileName, String title) throws IOException {

        Parent scene = FXMLLoader.load(Objects.requireNonNull(Utils.class.getResource(fileName)));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.setTitle(title);
        stage.show();

    }

    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();

    }
    public static void updatePassFail(int rowsAffected) {
        if (rowsAffected > 0) {
            displayAlert("Update Successful");
        } else {
            displayAlert("UPDATE FAILED");
        }
    }

    public static Boolean verifyBusinessHours(ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime businessOpen = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(8,0), ZoneId.of("US/Eastern"));
        ZonedDateTime businessClose = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(22, 0), ZoneId.of("US/Eastern"));

        if((start.isBefore(businessOpen)) || (start.isAfter(businessClose)) || (end.isBefore(businessOpen)) || (end.isAfter(businessClose))){
            return false;
        } else {
            return true;
        }
    }
}
