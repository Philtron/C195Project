package Controller;

import DatabaseAccess.CountryQuery;
import DatabaseAccess.CustomerQuery;
import DatabaseAccess.FirstLevelDivisionQuery;
import Helper.Utils;
import Model.Country;
import Model.FirstLevelDivision;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddCustomerWindowController implements Initializable {

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private Label countryLabel;

    @FXML
    private Label custIDLabel;

    @FXML
    private TextField custIDTextField;

    @FXML
    private Label custNameLabel;

    @FXML
    private TextField custNameTextField;

    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML
    private Label divisionLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Label postalCodeLabel;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private Button saveButton;

    @FXML
    void onClickCustomerView(ActionEvent event) throws IOException {
        if(Utils.confirmBack()) {
            Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
        }
    }

    @FXML
    void onClickSaveCustomer(ActionEvent event) throws IOException {
        if((custNameTextField.getText() == "")||(addressTextField.getText() == "")||(postalCodeTextField.getText()== "")
                ||(phoneNumberTextField.getText() =="")){
            Utils.displayAlert("Please ensure there is a value in each text field");
        } else if ((countryComboBox.getValue()==null)||(divisionComboBox.getValue()==null)) {
            Utils.displayAlert("Please enter a country and division in the comboboxes.");
        } else {
            String customerName = custNameTextField.getText();
            String address = addressTextField.getText();
            String zip = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            String country = countryComboBox.getValue().getCountry();
            int divisionID = divisionComboBox.getValue().getDivisionID();
            User createUser = LogInWindowController.CurrentUser;
            CustomerQuery.insertCustomer(customerName, address, zip, phone, Timestamp.valueOf(LocalDateTime.now()),
                    createUser.getUserName(), Timestamp.valueOf(LocalDateTime.now()), createUser.getUserName(), divisionID);
            Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        CountryQuery.selectAllToList(allCountries);
//        custIDTextField.setText(String.valueOf(CustomerQuery.getNextID()));
        custIDTextField.setDisable(true);

        countryComboBox.setItems(CountryQuery.selectAllToList());
        countryComboBox.valueProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                divisionComboBox.getItems().clear();
                divisionComboBox.setDisable(true);
            }
            else {
                divisionComboBox.setDisable(false);
                try {
                    divisionComboBox.getItems().clear();
                    Country selCountry = countryComboBox.getValue();
                    divisionComboBox.setItems(FirstLevelDivisionQuery.getFilteredDivisions(selCountry));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}
