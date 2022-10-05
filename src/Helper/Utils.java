package Helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
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

    public static Boolean verifyBusinessHours(ZonedDateTime start, ZonedDateTime end) {
//        System.out.println(ZonedDateTime.ofInstant(start.toInstant(), ZoneId.of("US/Eastern")));
//        System.out.println("zdtStart: " + start + " zdtEnd: " + end);
        ZonedDateTime businessOpen = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(8,0), ZoneId.of("US/Eastern"));
        ZonedDateTime businessClose = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(22, 0), ZoneId.of("US/Eastern"));
//        System.out.println("busStart: " + businessOpen + " busEnd: " + businessClose);

//
//        ZonedDateTime zStart = ZonedDateTime.ofInstant(start.toInstant(), ZoneId.of("US/Eastern"));
//        ZonedDateTime zEnd = ZonedDateTime.ofInstant(end.toInstant(), ZoneId.of("US/Eastern"));
//        int startHour = zStart.getHour();
//        int endHour = zEnd.getHour();
//        int busStart = businessOpen.getHour();
//        int busEnd = businessClose.getHour();
//        System.out.println("Start Hour: " + startHour);
//        System.out.println("Business Open: " + businessOpen.getHour());
//        System.out.println("End Hour: " + endHour);
//        System.out.println("Business Close: " + businessClose.getHour());
//        if((startHour < busStart) || (startHour > busEnd) || (endHour < busStart) || (endHour > busEnd)) {
        if((start.isBefore(businessOpen)) || (start.isAfter(businessClose)) || (end.isBefore(businessOpen)) || (end.isAfter(businessClose))){
            return false;
//        } else if (((startHour == busEnd) || (endHour == busEnd))) {
//            if ((zStart.getMinute() != 0) || (zEnd.getMinute() != 0)){
//                System.out.println("Failed at Minutes!");
//                return false;
//            }
        } else {
            return true;
        }
//        return true;
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
