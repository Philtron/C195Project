package Controller;

import DatabaseAccess.AppointmentQuery;
import DatabaseAccess.ContactQuery;
import DatabaseAccess.CustomerQuery;
import Helper.Utils;
import Model.Appointment;
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
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/** This class controls the modify appointment window. It facilitates updating existing appointments. */
public class ModifyAppointmentWindowController implements Initializable {
    /** Appointment to modify. Set when the appointment is selected at the main window. */
    public static Appointment appointment;

    /** Label to display converted end time of appointment in EST. */
    @FXML
    private Label easternEndLabel;

    /** Label to display converted start time of appointment in EST. */
    @FXML
    private Label easternStartLabel;

    /** Label to display selected end time in user's local timezone. */
    @FXML
    private Label localEndLabel;

    /** Label to display selected start time in user's local timezone. */
    @FXML
    private Label localStartLabel;

    /** Button to convert selected time to display in local and EST timezones. */
    @FXML
    private Button convertTimeButton;

    /** Cancel button, returns GUI to main menu. */
    @FXML
    private Button backButton;

    /** Combobox to display and allow selection of all contacts in the database. */
    @FXML
    private ComboBox<Contact> contactComboBox;

    /** Combobox to display and allow selection of all customers in the database. */
    @FXML
    private ComboBox<Customer> custComboBox;

    /** Textfield to display the customer ID of the customer selected in the combobox.
     * It is disabled to the user. The ID is automatically generated from the combobox. */
    @FXML
    private TextField custIDTextField;

    /** Textfield for capturing the entered description. */
    @FXML
    private TextField descTextField;

    /** Combobox with list of hours to schedule the end time of the appointment. */
    @FXML
    private ComboBox<Integer> endHourComboBox;

    /** Combobox with list of minutes to schedule the end of the appointment. */
    @FXML
    private ComboBox<Integer> endMinuteComboBox;

    /** Textfield to capture entered location. */
    @FXML
    private TextField locTextField;

    /** Button to modify the appointment in the database. */
    @FXML
    private Button saveButton;

    /** Datepicker control to select the start date of the appointment. */
    @FXML
    private DatePicker startDatePicker;

    /** Datepicker control to select the start date of the appointment. */
    @FXML
    private DatePicker endDatePicker;

    /** Combobox with list of hours to select the start hour of the appointment. */
    @FXML
    private ComboBox<Integer> startHourComboBox;

    /** Combobox with list of minutes to select the start minute of the appointment. */
    @FXML
    private ComboBox<Integer> startMinuteComboBox;

    /** Textfield to capture the entered title of the appointment. */
    @FXML
    private TextField titleTextField;

    /** Textfield to capture the entered appointment type. */
    @FXML
    private TextField typeTextField;

    /** Textfield displaying the appointment ID of the selected appointment. Cannot be edited. */
    @FXML
    private TextField appointmentIDTextField;

    /** Method to modify the appointment in the database.
     * Checks to make sure the user has filled each control. Converts the time controls into a
     * ZonedDateTime. Then calls methods to verify business hours and customer overlaps before updating
     * the appointment in the database. It then returns to the main window.
     * @param event mouse click of button to activate method.
     * @throws IOException if the main window fxml file isn't found. */
    @FXML
    void onClickSaveAppointment(ActionEvent event) throws IOException {
        // If all text fields are filled.
        if((titleTextField.getText().isBlank()) ||( typeTextField.getText().isBlank())
                || (descTextField.getText().isBlank()) || (locTextField.getText().isBlank())){
            Utils.displayAlert("Please enter a value in all text fields.");

            // If all comboboxes are displaying a value.
        } else if((custComboBox.getValue() == null) || contactComboBox.getValue() == null ) {
            Utils.displayAlert("Please ensure a value is selected in each combo box.");

            // If all date and time controls are utilized.
        } else if((startDatePicker.getValue() == null) || (startHourComboBox.getValue()== null) ||
                (startMinuteComboBox.getValue() == null) || (endHourComboBox.getValue() == null) ||
                (endMinuteComboBox.getValue()== null)) {
            Utils.displayAlert("Please select the date and start and finish hours and minutes. ");
        } else {
            int appointmentID = Integer.parseInt(appointmentIDTextField.getText());
            int contactID = contactComboBox.getValue().getContactID();
            int customerID = custComboBox.getValue().getCustomerID();
            User user = LogInWindowController.CurrentUser;

            LocalDate date = startDatePicker.getValue();

            int startHour = startHourComboBox.getValue();
            int startMinute = startMinuteComboBox.getValue();

            int endHour = endHourComboBox.getValue();
            int endMinute = endMinuteComboBox.getValue();

            // Translating the date/time controls to a LocalDateTime.
            LocalDateTime ldtStart = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), startHour, startMinute);
            LocalDateTime ldtEnd = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), endHour, endMinute);

            // Create ZoneDateTImes from the LocalDateTimes used to compare against business hours and customer overlaps.
            ZonedDateTime zdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
            ZonedDateTime zdtEnd = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());

            String title = titleTextField.getText();
            String description = descTextField.getText();
            String location = locTextField.getText();
            String type = typeTextField.getText();
            String customerName = custComboBox.getValue().getName();

            if(zdtEnd.isBefore(zdtStart)) {
                Utils.displayAlert("End cannot be before start. ");

                // Method to verify appointment is within the eastern business hours.
            }else if(!Utils.verifyBusinessHours(zdtStart, zdtEnd)){
                Utils.displayAlert("Please schedule during business hours (0800-2200 EST)");

                // Method to check customers other appointments and ensure this one doesn't overlap with any of them.
            } else if (!Utils.checkCustomerOverlap(zdtStart,zdtEnd,customerID, appointmentID)){
                Utils.displayAlert(customerName + " has another appointment that overlaps these times.");
            } else {
                AppointmentQuery.modifyAppointment(title, description, location, type, ldtStart, ldtEnd,
                        Timestamp.valueOf(LocalDateTime.now()), user.getUserName(), customerID,
                        user.getUserID(), contactID, appointment.getAppointmentID());
                    Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
            }
        }
    }

    /** Back button, returns to the Main window.
     *
     * @param event mouseclick to activate button.
     * @throws IOException if main window fxml file isn't found.
     */
    @FXML
    void onClickToMainWindow(ActionEvent event) throws IOException {
        if(Utils.confirmBack()) {
            Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
        }
    }

    /** Sets the time combo boxes. Creates and fills two observable lists. One for hours, one for minutes - the minute
     * list is filled with multiples of five. Then
     * assigns the lists to the corresponding comboboxes.
     */
    public void setTimeCombos(){
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for(int i = 1; i <= 24; i++){
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

    /** Converts the times that were entered in the corresponding time controls, and displays them in the labels at
     * the bottom of the page. Displays the local time of the user on the left side and the Eastern Standard time
     * equivalent on the right side.
     * @param event
     */
    public void onClickConvertTime(ActionEvent event) {
        // If all the time controls were filled in.
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

    /** The first method called when the window is created. Sets the customer and contact comboboxes. Calls the method
     * to set up the time combo boxes. Uses a lambda expression to add a listener to the customer combobox that updates
     * the customer ID text field with the ID of the customer selected in the customer combobox.
     * It then uses the selected appointment from the previous window to autofill all controls with the current data
     * corresponding to that appointment.
     * @param url The location used to resolve relative paths for the root object, or null if the location
     * is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     * object was not localized.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer selCustomer = CustomerQuery.select(appointment.getCustomerName());
        Contact selContact = ContactQuery.select(appointment.getContactName());
        setTimeCombos();

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
        endDatePicker.setValue(appointment.getEnd().toLocalDate());
        String startTime = appointment.getStart().toLocalTime().toString();
        String[] splitStartTime = startTime.split(":");
        startHourComboBox.setValue(Integer.valueOf(splitStartTime[0]));
        startMinuteComboBox.setValue(Integer.valueOf(splitStartTime[1]));
        String endTime = appointment.getEnd().toLocalTime().toString();
        String[] splitEndTime = endTime.split(":");
        endHourComboBox.setValue(Integer.valueOf(splitEndTime[0]));
        endMinuteComboBox.setValue(Integer.valueOf(splitEndTime[1]));
    }
}
