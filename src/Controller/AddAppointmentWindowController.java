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
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class AddAppointmentWindowController implements Initializable {
    @FXML
    private Label easternEndLabel;
    @FXML
    private Label easternStartLabel;
    @FXML
    private Label localEndLabel;
    @FXML
    private Label localStartLabel;

    @FXML
    private Button convertTimeButton;

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
    private DatePicker endDatePicker;

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
            String customerName = custComboBox.getValue().getName();
            // TODO Check for overlapping Customers
            if(!Utils.verifyBusinessHours(zdtStart, zdtEnd)){
                Utils.displayAlert("Please schedule during business hours (0800-2200 EST)");
            } else if (!Utils.checkCustomerOverlap(zdtStart,zdtEnd,customerID)){
                Utils.displayAlert(customerName + " has another appointment that overlaps these times.");
            } else {
                AppointmentQuery.insert(title, description, location, type, zdtStart, zdtEnd, ZonedDateTime.now(),
                        user.getUserName(), ZonedDateTime.now(), user.getUserName(), customerID, user.getUserID(), contactID);
                Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
            }
//            if ((Utils.verifyBusinessHours(zdtStart, zdtEnd))) {
//                AppointmentQuery.insert(title, description, location, type, zdtStart, zdtEnd, ZonedDateTime.now(),
//                        user.getUserName(), ZonedDateTime.now(), user.getUserName(), customerID, user.getUserID(), contactID);
//                Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
//            } else {
//                Utils.displayAlert("Please schedule the appointment during business hours (0800-2200 EST).");
//            }
        }
    }
    public void setCombos(){



        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();

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

    public void onClickConvertTime(ActionEvent event) {
        if((startDatePicker.getValue() == null) || (startHourComboBox.getValue()== null) ||
                (startMinuteComboBox.getValue() == null) || (endHourComboBox.getValue() == null) ||
                (endMinuteComboBox.getValue()== null) || (endDatePicker.getValue() == null)) {
            Utils.displayAlert("Please fill in the time controls before clicking Convert Time.");
        } else {

            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            int startHour = startHourComboBox.getValue();
            int startMinute = startMinuteComboBox.getValue();
            int endHour = endHourComboBox.getValue();
            int endMinute = endMinuteComboBox.getValue();

            LocalDateTime ldtStart = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), startHour, startMinute);
            LocalDateTime ldtEnd = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), endHour, endMinute);


            ZonedDateTime zdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
            ZonedDateTime zdtEnd = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());

            localStartLabel.setText(zdtStart.toString());
            localEndLabel.setText(zdtEnd.toString());
            ZoneId eastID = ZoneId.of("US/Eastern");
            ZonedDateTime eastStart = ZonedDateTime.ofInstant(zdtStart.toInstant(), eastID);
            ZonedDateTime eastEnd = ZonedDateTime.ofInstant(zdtEnd.toInstant(), eastID);
            easternStartLabel.setText(eastStart.withNano(0).toString());
            easternEndLabel.setText(eastEnd.withNano(0).toString());
        }
    }
}
