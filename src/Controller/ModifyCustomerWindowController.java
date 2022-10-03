package Controller;

import DatabaseAccess.CountryQuery;
import DatabaseAccess.FirstLevelDivisionQuery;
import Helper.Utils;
import Model.Country;
import Model.Customer;
import Model.FirstLevelDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyCustomerWindowController implements Initializable {
    public static Customer customer;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private TextField custIDTextField;

    @FXML
    private TextField custNameTextField;

    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private Button saveButton;

    @FXML
    void onClickModifyCustomer(ActionEvent event) {

    }

    @FXML
    void onClickToCustomerView(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/CustomerViewWindow.fxml", "Customer View");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addressTextField.setText(customer.getAddress());
        postalCodeTextField.setText(customer.getPostalCode());
        phoneTextField.setText(customer.getPhoneNumber());
        custIDTextField.setText(String.valueOf(customer.getCustomerID()));
        custNameTextField.setText(customer.getName());
        Country country = CountryQuery.selectFromDivisionID(customer.getDivisionID());
        FirstLevelDivision division = FirstLevelDivisionQuery.select(customer.getDivisionID());

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
        countryComboBox.getSelectionModel().select(country);
        divisionComboBox.getSelectionModel().select(division);


    }
}
