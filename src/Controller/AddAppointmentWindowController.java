package Controller;

import DatabaseAccess.AppointmentQuery;
import DatabaseAccess.ContactQuery;
import DatabaseAccess.CustomerQuery;
import Helper.Utils;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import jdk.jshell.execution.Util;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class AddAppointmentWindowController implements Initializable {

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
            int customerID = custComboBox.getValue().getCustomerID();
            User user = LogInWindowController.CurrentUser;

            LocalDate date = startDatePicker.getValue();

            int startHour = startHourComboBox.getValue();
            int startMinute = startMinuteComboBox.getValue();

            int endHour = endHourComboBox.getValue();
            int endMinute = endMinuteComboBox.getValue();

            LocalDateTime ldtStart = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), startHour, startMinute);
            LocalDateTime ldtEnd = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), endHour, endMinute);

            ZonedDateTime zdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
            ZonedDateTime zdtEnd = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());

            String title = titleTextField.getText();
            String description = descTextField.getText();
            String location = locTextField.getText();
            String type = typeTextField.getText();

            // TODO Check for overlapping Customers
            if (Utils.verifyBusinessHours(zdtStart, zdtEnd)) {
                AppointmentQuery.insert(title, description, location, type, zdtStart, zdtEnd, ZonedDateTime.now(),
                        user.getUserName(), ZonedDateTime.now(), user.getUserName(), customerID, user.getUserID(), contactID);
                Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
            } else {
                Utils.displayAlert("Please schedule the appointment during business hours (0800-2200 EST).");
            }
        }
    }
    public void setCombos(){
//        LocalDateTime ldtNow = LocalDateTime.now();
//        ZoneId easternZID = ZoneId.of("US/Eastern");
//        ZonedDateTime zdtNow = ZonedDateTime.of(ldtNow,ZoneId.systemDefault());
//        int localOffset  = zdtNow.getOffset().getTotalSeconds();
//        localOffset /= 3600;
//        ZonedDateTime easternZDT = ZonedDateTime.ofInstant(zdtNow.toInstant(), easternZID);
//        int easternOffset = easternZDT.getOffset().getTotalSeconds();
//        easternOffset /= 3600;
//        int offsetDifference = localOffset - easternOffset;
//        int localStart = 8 + offsetDifference;
//        int localStop = localStart + 14;
//        Utils.displayAlert(localStart + " " + localStop);


        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
//        int hourStart = Utils.getLocalBusinessStartHour();
//        int hourStop = hourStart+14;
//        System.out.println("Start: " + hourStart + " Stop: " + hourStop);
        int hourStart = 0;
        int hourStop =24;
        for(int i = hourStart; i <= hourStop; i++){
            hours.add(i);
        }
        for(int i = 0; i < 60; i+=5){
            minutes.add(i);
        }
        startHourComboBox.setItems(hours);
        endHourComboBox.setItems(hours);
        startMinuteComboBox.setItems(minutes);
        endMinuteComboBox.setItems(minutes);
    }

    @FXML
    void onClickToMainWindow(ActionEvent event) throws IOException {
        if(Utils.confirmBack()) {
            Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCombos();

        custIDTextField.setEditable(false);
        custIDTextField.setDisable(true);
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

    }
}
