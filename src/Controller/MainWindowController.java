package Controller;

import DatabaseAccess.AppointmentQuery;
import Helper.Utils;
import Model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    /** ToggleGroup for radio buttons. Controls filtering the list displayed in the appointment TableView. */
    @FXML
    private ToggleGroup filterToggle;
    /** When selected, no filter is applied and all appointments are displayed. */
    @FXML
    private RadioButton noFilterRadio;
    /** When selected, displays appointments in the next week. */
    @FXML
    private RadioButton weekRadioFilter;
    /** When selected, displays all appointments in the current month. */
    @FXML
    private RadioButton monthRadioFilter;
    /** Displays the currently activated radio button filter. */
    @FXML
    private Label toggleLabel;
    /** Displays the time period of the week when week filter is selected */
    @FXML
    private Label weekFilterLabel;

    /** TableView to hold and display appointments. Initialized with all appointments. */
    @FXML
    private TableView<Appointment> appointmentTable;

    /** Column to hold appointment IDs. */
    @FXML
    private TableColumn<Appointment, Integer> apptIDCol;

    /** Column to hold contact names. */
    @FXML
    private TableColumn<Appointment, String> contNameCol;

    /** Column to hold customer IDs. */
    @FXML
    private TableColumn<Appointment, Integer> custIDCol;

    /** Column to hold customer names. */
    @FXML
    private TableColumn<Appointment, String> custNameCol;

    /** Column to hold appointment descriptions. */
    @FXML
    private TableColumn<Appointment, String> descCol;

    /** Column to hold appointment end ZonedDateTimes. */
    @FXML
    private TableColumn<Appointment, ZonedDateTime> endCol;

    /** Column to hold appointment locations. */
    @FXML
    private TableColumn<Appointment, String> locCol;

    /** Column to hold appointment start ZonedDateTimes. */
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startCol;

    /** Column to hold appointment titles. */
    @FXML
    private TableColumn<Appointment, String> titleCol;

    /** Column to hold appointment types. */
    @FXML
    private TableColumn<Appointment, String> typeCol;

    /** Column to hold user IDs. */
    @FXML
    private TableColumn<Appointment, Integer> userIDCol;

    /** Button to change the window to customer view. */
    @FXML
    private Button CustomerViewButton;

    /** Button to exit the program. */
    @FXML
    private Button exitButton;

    /** Button to change the window to the reports window. */
    @FXML
    private Button reportsButton;

    /** Exit button. Gives choice to exit program, log out and return to log in window, or return to the program.
     *
     * @param event mouseclick to activate the button
     * @throws IOException if the log in window fxml file isn't found.
     */
    @FXML
    void onClickExit(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Would you like to log out or exit completely?");
        alert.setTitle("Exit?");

        ButtonType logout = new ButtonType("Log Out");
        ButtonType exit = new ButtonType("Exit Program");
        ButtonType cancel = new ButtonType("Return to Program");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(logout, exit, cancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == logout){
            Utils.changeWindow(event, Utils.LOG_IN_WINDOW_LOCATION, "Log In");
            LogInWindowController.CurrentUser = null;
        } else if (result.get() == exit){
            System.exit(0);
        } else if (result.get() == cancel) {
            alert.close();
        }
    }

    /** Changes the window to the customer view window.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if customer view fxml file isn't found.
     */
    @FXML
    void onClickToCustomerView(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
    }
    public void setTable(ObservableList<Appointment> appointments){
        appointmentTable.setItems(appointments);
    }

    /** Deletes the selected appointment from the database then refills the list of appointments and applies it
     * to the appointment table. It also resets the filter to no filter and displays the new complete list of
     * appointments.
     *
     * @param event the mouseclick that activates the button.
     */
    public void onClickDeleteAppointment(ActionEvent event) {
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            Appointment appt = appointmentTable.getSelectionModel().getSelectedItem();
            int apptID = appt.getAppointmentID();
            String apptType = appt.getType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                if (AppointmentQuery.delete(appt.getAppointmentID())) {
                    noFilterRadio.setSelected(true);
                    setTable(AppointmentQuery.selectAllToTableViewList());
                    String message = "Appointment ID: " + apptID + " Type: " + apptType + " successfully deleted.";
                    Utils.displayAlert(message);
                }
            }
        } else {
            Utils.displayAlert("Please select an appointment to Delete.");
        }
    }

    /** Changes the window to the add appointment window.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if the add appointment fxml files isn't found.
     */
    public void onClickAddAppointment(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.ADD_APPOINTMENT_WINDOW_LOCATION, "Add Appointment");
    }

    /** Captures the selected appointment, sends it to the modify appointment window, then changes the window to
     * the modify appointment window.
      * @param event the mouseclick that activates the button.
     * @throws IOException if the modify appointment window fxml file isn't found.
     */
    public void onClickEditAppointment(ActionEvent event) throws IOException {
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            ModifyAppointmentWindowController.appointment = appointmentTable.getSelectionModel().getSelectedItem();
            Utils.changeWindow(event, Utils.MODIFY_APPOINTMENT_WINDOW_LOCATION, "Modify Appointment");
        } else {
            Utils.displayAlert("Please select an appointment to modify");
        }
    }

    /** creates a list of appointments in the next week then sets a label to display this time range, hiding the default
     * label. It then applies the new list to the appointment TableView.
     *
     * @param event activating the radio button.
     */
    public void onSelectFilterByWeek(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        weekFilterLabel.setVisible(true);
        toggleLabel.setVisible(false);
        weekFilterLabel.setText(now.withNano(0) + " to " + now.plusDays(6).withNano(0));
        setTable(AppointmentQuery.filterByWeek(LocalDateTime.now()));
    }

    /** Sets the appointment TableView to show all appointments. Hides the week range label and displays the default
     * label.
     * @param event activating the radio button.
     */
    public void onSelectNoFilter(ActionEvent event) {
        weekFilterLabel.setVisible(false);
        toggleLabel.setVisible(true);
        toggleLabel.setText("All");
        setTable(AppointmentQuery.selectAllToTableViewList());
    }

    /** Creates a list of appointments that take place in the current month and sets the appointment TableView to
     * display them. Hides the week range label and displays the default label.
     * @param event activating the radio button.
     */
    public void onSelectFilterByMonth(ActionEvent event) {
        weekFilterLabel.setVisible(false);
        toggleLabel.setVisible(true);
        toggleLabel.setText(LocalDateTime.now().getMonth().toString());
        setTable(AppointmentQuery.filterByMonth());
    }

    /** Changes the window to the reports window.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if the reports window fxml file isn't found.
     */
    @FXML
    void onClickToReports(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.REPORTS_WINDOW_LOCATION, "Reports");
    }

    /** First method called when the window is created. Selects the no filter radio button. Then initializes the
     * appointment TableView.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location
     * is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     * object was not localized.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noFilterRadio.setSelected(true);
        toggleLabel.setText(LocalDateTime.now().withNano(0).toString());
        setTable(AppointmentQuery.selectAllToTableViewList());
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        contNameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        weekFilterLabel.setVisible(false);
    }
}
