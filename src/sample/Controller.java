package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Button LogInButton;

    @FXML
    private Label PasswordLabel;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Label UNameLabel;

    @FXML
    private TextField UNameTextField;

    @FXML
    void OnClickLogIn(ActionEvent event) {
        System.out.println("Clicked!");
    }

}