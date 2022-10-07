package Controller;

import DatabaseAccess.CountryQuery;
import DatabaseAccess.CustomerQuery;
import DatabaseAccess.FirstLevelDivisionQuery;
import Helper.Utils;
import Model.Country;
import Model.Customer;
import Model.FirstLevelDivision;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ModifyCustomerWindowController implements Initializable {
    /** Customer to be modified. It is set when the customer is selected in the customer view window. */
    public static Customer customer;

    /** Textfield to capture address. */
    @FXML
    private TextField addressTextField;

    /** Cancel button, returns to the customer view window. */
    @FXML
    private Button backButton;

    /** Combobox holding list of countries pulled from the database. */
    @FXML
    private ComboBox<Country> countryComboBox;

    /** Textfield to display the customer ID, cannot be edited. */
    @FXML
    private TextField custIDTextField;

    /** Textfield to capture the customer's name. */
    @FXML
    private TextField custNameTextField;

    /** Combobox that holds first level divisions of whichever country is selected in the country combobox. */
    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    /** Captures the entered phone number. */
    @FXML
    private TextField phoneTextField;

    /** Text field to capture the entered postal code. */
    @FXML
    private TextField postalCodeTextField;

    /** Button to call the function that modifies the customer in the database. */
    @FXML
    private Button saveButton;

    /** Checks to make sure all controls have data entered then modifies the customer in the database.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if the customer view fxml file isn't found.
     */
    @FXML
    void onClickModifyCustomer(ActionEvent event) throws IOException {

        // If all TextFields aren't filled.
        if((custNameTextField.getText().isBlank())||(addressTextField.getText().isBlank())||(postalCodeTextField.getText().isBlank())
                ||(phoneTextField.getText().isBlank())){
            Utils.displayAlert("Please ensure there is a value in each text field");

        // If all comboboxes aren't filled.
        } else if ((countryComboBox.getValue()==null)||(divisionComboBox.getValue()==null)) {
            Utils.displayAlert("Please enter a country and division in the comboboxes.");
        } else {
            int customerID = Integer.parseInt(custIDTextField.getText());
            String customerName = custNameTextField.getText();
            String address = addressTextField.getText();
            String zip = postalCodeTextField.getText();
            String phone = phoneTextField.getText();
            int divisionID = divisionComboBox.getValue().getDivisionID();
            User createUser = LogInWindowController.CurrentUser;
            CustomerQuery.modifyCustomer(customerID, customerName, address, zip, phone,
                    createUser.getUserName(), Timestamp.valueOf(LocalDateTime.now()), createUser.getUserName(), divisionID);
            Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
        }
    }

    /** Change window to customer view window
     *
     * @param event mouseclick that activates button.
     * @throws IOException if customer view fxml file isn't found.
     */
    @FXML
    void onClickToCustomerView(ActionEvent event) throws IOException {
        if(Utils.confirmBack()) {
            Utils.changeWindow(event, Utils.CUSTOMER_VIEW_WINDOW, "Customer View");
        }
    }

    /** First method called when window is created. Fills the controls with the data corresponding to the customer
     * selected to be modified in the previous window. Uses a lambda expression to add a listener to the country
     * combobox that fills the division combobox with that country's divisions.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location
     * is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     * object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addressTextField.setText(customer.getAddress());
        postalCodeTextField.setText(customer.getPostalCode());
        phoneTextField.setText(customer.getPhoneNumber());
        custIDTextField.setText(String.valueOf(customer.getCustomerID()));
        custIDTextField.setDisable(true);
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
