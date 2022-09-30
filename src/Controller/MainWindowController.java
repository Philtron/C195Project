package Controller;

import DatabaseAccess.JDBC;
import Helper.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class MainWindowController {

    @FXML
    private Button CustomerViewButton;

    @FXML
    private Button exitButton;

    @FXML
    void onClickExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)){
            JDBC.closeConnection();
            System.exit(0);
        }
    }

    @FXML
    void onClickToCustomerView(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/CustomerViewWindow.fxml", "Customer View");
    }
}
