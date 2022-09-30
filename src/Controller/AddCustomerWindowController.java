package Controller;

import Helper.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AddCustomerWindowController {

    @FXML
    private Button backButton;

    @FXML
    void onClickCustomerView(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/CustomerViewWindow.fxml", "Customer View");
    }
}
