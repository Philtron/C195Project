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

/** This class controls the add customer window. It facilitates inserting new customers into the database. */
public class AddCustomerWindowController implements Initializable {

    /** Textfield to capture the address entered by the user. */
    @FXML
    private TextField addressTextField;

    /** Button to discard changes and return to the main window. */
    @FXML
    private Button backButton;

    /** Combobox to hold the list of countries pulled from the database. */
    @FXML
    private ComboBox<Country> countryComboBox;

    /** Textfield illustrates that the customer ID is auto generate. */
    @FXML
    private TextField custIDTextField;

    /** Textfield to capture the entered customer name.  */
    @FXML
    private TextField custNameTextField;

    /** Combobox to display the first level divisions of the country selected in the country combobox. */
    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    /** Textfield to capture the entered phone number. */
    @FXML
    private TextField phoneNumberTextField;

    /** Textfield to capture the entered postal code. */
    @FXML
    private TextField postalCodeTextField;

    /** Button that initiates inserting the new customer into the database. */
    @FXML
    private Button saveButton;

    /** Back button. Returns to the customer view window.
     * @param event mouse click to initiate the method.
     * @throws IOException if customer view window isn't found. */
    @FXML
    void onClickCustomerView(ActionEvent event) throws IOException {
        if(Utils.confirmBack()) {
            Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
        }
    }

    /** Verifies all controls are filled then inserts the new customer into the database.
     *@param event mouse click to initiate the method.
     *@throws IOException if customer view window isn't found. */
    @FXML
    void onClickSaveCustomer(ActionEvent event) throws IOException {

        // If all textfields are filled.
        if((custNameTextField.getText().isBlank())||(addressTextField.getText().isBlank()) ||(postalCodeTextField.getText().isBlank())
                ||(phoneNumberTextField.getText().isBlank())){
            Utils.displayAlert("Please ensure there is a value in each text field");

        // If all comboboxes are filled.
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

    /** First method run when window is opened. Disables the customer ID text field.
     * Uses a lambda expression to fill the first level division comboboxes of the country selected in the country
     * combobox.
     * @param url The location used to resolve relative paths for the root object, or null if the location
     * is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     * object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
