package Controller;

import DatabaseAccess.AppointmentQuery;
import Helper.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class ReportsWindowController {

    @FXML
    private Button back;

    @FXML
    private ToggleButton contactToggle;

    @FXML
    private Label reportDescLabel;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private ToggleGroup reportToggle;

    @FXML
    private ToggleButton typeMonthToggle;

    @FXML
    private ToggleButton userToggle;

    @FXML
    void onClickToMainWindow(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main-Window");
    }

    @FXML
    void onToggleFilterByContact(ActionEvent event) {
        String reportString = AppointmentQuery.appointmentsByContacts();
        reportTextArea.setText(reportString);
    }

    @FXML
    void onToggleFilterByMonthType(ActionEvent event) {
        String reportString = AppointmentQuery.appointmentsByTypeMonth();
        reportTextArea.setText(reportString);

    }

    @FXML
    void onToggleFilterByUser(ActionEvent event) {
        String reportString = AppointmentQuery.appointmentsByUser();
        reportTextArea.setText(reportString);
    }
}
