package Controller;

import DatabaseAccess.AppointmentQuery;
import Helper.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

/** This class controls the reports window. Queries the database to complete and display the reports. */
public class ReportsWindowController {
    /** Back button, changes window back to the main window */
    @FXML
    private Button back;

    /** Initiates the appointments by contact report */
    @FXML
    private ToggleButton contactToggle;

    /** Label that describes which report is being displayed. */
    @FXML
    private Label reportDescLabel;

    /** Text area where the requested reports are displayed. */
    @FXML
    private TextArea reportTextArea;

    /** Toggle group the repoirt buttons belong to */
    @FXML
    private ToggleGroup reportToggle;

    /** Initiates the types per month report. */
    @FXML
    private ToggleButton typeMonthToggle;

    /** Initiates the appointments per user report. */
    @FXML
    private ToggleButton userToggle;

    /** Returns to the main window.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if the main window fxml file isn't found.
     */
    @FXML
    void onClickToMainWindow(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main-Window");
    }

    /** Queries the database for all appointments and sorts and displays them by contact.
     *
      * @param event mouseclick that activates the button.
     */
    @FXML
    void onToggleFilterByContact(ActionEvent event) {
        String reportString = AppointmentQuery.appointmentsByContacts();
        reportTextArea.setText(reportString);
    }

    /** Queries the database for all appointment types, then groups them by month before finally displaying them
     * in the TextArea.
     *
     * @param event mouseclick that activates the button.
     */
    @FXML
    void onToggleFilterByMonthType(ActionEvent event) {
        String reportString = AppointmentQuery.appointmentsByTypeMonth();
        reportTextArea.setText(reportString);

    }

    /** Queries the database for all appointments, then sorts and displays them by user.
     *
     * @param event mouseclick that activates the button.
     */
    @FXML
    void onToggleFilterByUser(ActionEvent event) {
        String reportString = AppointmentQuery.appointmentsByUser();
        reportTextArea.setText(reportString);
    }
}
