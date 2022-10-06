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
    @FXML
    private ToggleGroup filterToggle;
    @FXML
    private RadioButton noFilterRadio;
    @FXML
    private RadioButton weekRadioFilter;
    @FXML
    private RadioButton monthRadioFilter;

    @FXML
    private Label toggleLabel;
    @FXML
    private Label weekFilterLabel;
//    @FXML
//    private ToggleGroup FilterToggle;
//    @FXML
//    private ToggleButton weekFilterButton;
//    @FXML
//    private ToggleButton monthFilterButton;
//    @FXML
//    private ToggleButton noFilterButton;
    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> apptIDCol;

    @FXML
    private TableColumn<Appointment, String> contNameCol;

    @FXML
    private TableColumn<Appointment, Integer> custIDCol;

    @FXML
    private TableColumn<Appointment, String> custNameCol;

    @FXML
    private TableColumn<Appointment, String> descCol;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> endCol;

    @FXML
    private TableColumn<Appointment, String> locCol;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> startCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<?, ?> userIDCol;


    @FXML
    private Button CustomerViewButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button reportsButton;

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


    @FXML
    void onClickToCustomerView(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
    }
    public void setTable(ObservableList<Appointment> appointments){
        appointmentTable.setItems(appointments);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void onClickDeleteAppointment(ActionEvent event) {
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            Appointment appt = appointmentTable.getSelectionModel().getSelectedItem();
            int apptID = appt.getAppointmentID();
            String apptType = appt.getType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                if (AppointmentQuery.delete(appt.getAppointmentID())) {
                    setTable(AppointmentQuery.selectAllToTableViewList());
                    String message = "Appointment ID: " + apptID + " Type: " + apptType + " successfully deleted.";
                    Utils.displayAlert(message);
                }
            }
        } else {
            Utils.displayAlert("Please select an appointment to Delete.");
        }
    }

    public void onClickAddAppointment(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.ADD_APPOINTMENT_WINDOW_LOCATION, "Add Appointment");
    }

    public void onClickEditAppointment(ActionEvent event) throws IOException {
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            ModifyAppointmentWindowController.appointment = appointmentTable.getSelectionModel().getSelectedItem();
            Utils.changeWindow(event, Utils.MODIFY_APPOINTMENT_WINDOW_LOCATION, "Modify Appointment");
        } else {
            Utils.displayAlert("Please select an appointment to modify");
        }
    }


//    public void onToggleNoFilter(ActionEvent event) {
//            toggleLabel.setText("All");
//            setTable(AppointmentQuery.selectAllToTableViewList());
//    }
//
//
//    public void onToggleFilterByWeek(ActionEvent event) {
//            toggleLabel.setText("Week");
//            setTable(AppointmentQuery.filterByWeek(LocalDateTime.now()));
//    }
//
//    public void onToggleMonthFilter(ActionEvent event) {
//            toggleLabel.setText("Month");
//            setTable(AppointmentQuery.filterByMonth());
//    }

    public void onSelectFilterByWeek(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        weekFilterLabel.setVisible(true);
        toggleLabel.setVisible(false);
        weekFilterLabel.setText(now.withNano(0) + " to " + now.plusDays(6).withNano(0));
        setTable(AppointmentQuery.filterByWeek(LocalDateTime.now()));
    }

    public void onSelectNoFilter(ActionEvent event) {
        weekFilterLabel.setVisible(false);
        toggleLabel.setVisible(true);
        toggleLabel.setText("All");
        setTable(AppointmentQuery.selectAllToTableViewList());
    }

    public void onSelectFilterByMonth(ActionEvent event) {
        weekFilterLabel.setVisible(false);
        toggleLabel.setVisible(true);
        toggleLabel.setText(LocalDateTime.now().getMonth().toString());
        setTable(AppointmentQuery.filterByMonth());
    }
    @FXML
    void onClickToReports(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.REPORTS_WINDOW_LOCATION, "Reports");
    }
}
