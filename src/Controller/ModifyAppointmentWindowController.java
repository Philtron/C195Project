package Controller;

import DatabaseAccess.AppointmentQuery;
import DatabaseAccess.ContactQuery;
import DatabaseAccess.CustomerQuery;
import Helper.Utils;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class ModifyAppointmentWindowController implements Initializable {
    public static Appointment appointment;
    @FXML
    private Button backButton;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private ComboBox<Customer> custComboBox;

    @FXML
    private TextField custIDTextField;

    @FXML
    private TextField descTextField;

    @FXML
    private ComboBox<Integer> endHourComboBox;

    @FXML
    private ComboBox<Integer> endMinuteComboBox;

    @FXML
    private TextField locTextField;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<Integer> startHourComboBox;

    @FXML
    private ComboBox<Integer> startMinuteComboBox;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private TextField appointmentIDTextField;

    @FXML
    void onClickSaveAppointment(ActionEvent event) throws IOException {
        if((titleTextField.getText() == "") ||( typeTextField.getText() == "")
                || (descTextField.getText() == "") || (locTextField.getText() == "")){

            Utils.displayAlert("Please enter a value in all text fields.");

        } else if((custComboBox.getValue() == null) || contactComboBox.getValue() == null ) {
            Utils.displayAlert("Please ensure a value is selected in each combo box.");
        } else if((startDatePicker.getValue() == null) || (startHourComboBox.getValue()== null) ||
                (startMinuteComboBox.getValue() == null) || (endHourComboBox.getValue() == null) ||
                (endMinuteComboBox.getValue()== null)) {
            Utils.displayAlert("Please select the date and start and finish hours and minutes. ");
        } else {
            int contactID = contactComboBox.getValue().getContactID();
            int customerID = Integer.valueOf(custIDTextField.getText());
//            String finalStartHour = "";
//            String finalStartMinute = "";
//            String finalEndHour;
//            String finalEndMinute;
//            String cusName = custComboBox.getValue().getName();
            LocalDate date = startDatePicker.getValue();

            int startHour = startHourComboBox.getValue();
            int startMinute = startMinuteComboBox.getValue();

            int endHour = endHourComboBox.getValue();
            int endMinute = endMinuteComboBox.getValue();
            String title = titleTextField.getText();
            String type = typeTextField.getText();
            LocalDateTime ldtStart = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), startHour, startMinute);
            LocalDateTime ldtEnd = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), endHour, endMinute);

            ZonedDateTime zdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
            ZonedDateTime zdtEnd = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());

            /*
            LocalDate startDate = startDatePicker.getValue();
            String startHour = String.valueOf(startHourComboBox.getValue());
            if (startHour.length() < 2) {
                finalStartHour = "0" + startHour;
            } else {
                finalStartHour = startHour;
            }
            String startMinute = String.valueOf(startMinuteComboBox.getValue());
            if (startMinute.length() < 2) {
                finalStartMinute = "0" + startMinute;
            } else {
                finalStartMinute = startMinute;
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            String strStartTime = finalStartHour + ":" + finalStartMinute;
            System.out.println(strStartTime);
            LocalTime startTime = LocalTime.parse(strStartTime, dtf);
            LocalDateTime ldtStartTime = LocalDateTime.of(startDate, startTime);
            ZonedDateTime zStartTIme = ZonedDateTime.of(ldtStartTime, ZoneId.systemDefault());
            String endHour = String.valueOf(endHourComboBox.getValue());
            if (startHour.length() < 2) {
                finalEndHour = "0" + endHour;
            } else {
                finalEndHour = endHour;
            }
            String endMinute = String.valueOf(endMinuteComboBox.getValue());
            if (endMinute.length() < 2) {
                finalEndMinute = "0" + endMinute;
            } else {
                finalEndMinute = endMinute;
            }
            String strEndTime = finalEndHour + ":" + finalEndMinute;
            LocalTime endTime = LocalTime.parse(strEndTime, dtf);
            LocalDateTime ldtEndTime = LocalDateTime.of(startDate, endTime);
            ZonedDateTime zEndTime = ZonedDateTime.of(ldtEndTime, ZoneId.systemDefault());

             */
            String location = locTextField.getText();
            int userID = LogInWindowController.loggedInUser.getUserID();

            String description = descTextField.getText();

            if(Utils.verifyBusinessHours(zdtStart, zdtEnd)) {
                AppointmentQuery.modifyAppointment(title, description, location, type, zdtStart, zdtEnd,
                        Timestamp.valueOf(LocalDateTime.now()), LogInWindowController.loggedInUser.getUserName(), customerID,
                        userID, contactID, appointment.getAppointmentID());

                Utils.changeWindow(event, "../View/MainWindow.fxml", "Main Window");
            } else {
                Utils.displayAlert("Please schedule the appointment during business hours (0800-2200 UTC).");
            }
        }
    }

    @FXML
    void onClickToMainWindow(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/MainWindow.fxml", "Main Window");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer selCustomer = CustomerQuery.select(appointment.getCustomerName());
        Contact selContact = ContactQuery.select(appointment.getContactName());
        System.out.println(selCustomer.getName());
        System.out.println(selContact.getContactName());
        setCombos();

        appointmentIDTextField.setText(String.valueOf(appointment.getAppointmentID()));
        appointmentIDTextField.setDisable(true);

        custComboBox.setItems(CustomerQuery.selectAllToList());
        contactComboBox.setItems(ContactQuery.selectAllToList());

        custComboBox.valueProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                custIDTextField.clear();
            }
            else {
                Customer customer = custComboBox.getValue();
                custIDTextField.setText(String.valueOf(customer.getCustomerID()));
            }
        });
        titleTextField.setText(appointment.getTitle());
        typeTextField.setText(appointment.getType());
        descTextField.setText(appointment.getDescription());
        locTextField.setText(appointment.getLocation());
        custComboBox.getSelectionModel().select(selCustomer);
        contactComboBox.getSelectionModel().select(selContact);
        startDatePicker.setValue(appointment.getStart().toLocalDate());
        String startTime = appointment.getStart().toLocalTime().toString();
        String[] splitStartTime = startTime.split(":");
        startHourComboBox.setValue(Integer.valueOf(splitStartTime[0]));
        startMinuteComboBox.setValue(Integer.valueOf(splitStartTime[1]));
        String endTime = appointment.getEnd().toLocalTime().toString();
        String[] splitEndTime = endTime.split(":");
        endHourComboBox.setValue(Integer.valueOf(splitEndTime[0]));
        endMinuteComboBox.setValue(Integer.valueOf(splitEndTime[1]));
    }

    public void setCombos(){
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for(int i = 1; i <= 24; i++){
            hours.add(i);
        }
        for(int i = 0; i < 60; i++){
            minutes.add(i);
        }
        startHourComboBox.setItems(hours);
        endHourComboBox.setItems(hours);
        startMinuteComboBox.setItems(minutes);
        endMinuteComboBox.setItems(minutes);
    }
}
