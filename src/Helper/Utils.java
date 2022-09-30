package Helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Utils {
    public static void changeWindow(ActionEvent event, String fileName, String title) throws IOException {

        Parent scene = FXMLLoader.load(Objects.requireNonNull(Utils.class.getResource(fileName)));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.setTitle(title);
        stage.show();

    }

    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();

    }
}
