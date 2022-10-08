package Controller;

import DatabaseAccess.CustomerQuery;
import Helper.Utils;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the customer view window. It displays a list of all customers in the database and allows
 * customer deletion and access to the add customer and modify customer windows. */
public class CustomerViewWindowController implements Initializable {

    /** Customer tableview. Contains and displays all customers in the database.  */
    @FXML
    private TableView<Customer> customerTable;

    /** TableView column to hold address*/
    @FXML
    private TableColumn<Customer, String> addressCol;

    /** TableView column to hold  customer IDs. */
    @FXML
    private TableColumn<Customer, Integer> custIDCol;

    /** TableView column to hold division IDs. */
    @FXML
    private TableColumn<Customer, Integer> divisionIDCol;

    /** TableView column to hold customer names. */
    @FXML
    private TableColumn<Customer, String> nameCol;

    /** TableView column to hold phone numbers in string format. */
    @FXML
    private TableColumn<Customer, String> phoneCol;

    /** TableView column to hold postal codes. */
    @FXML
    private TableColumn<Customer, String> postalCodeCol;


    /** Changes the window to the add customer window.
     *
     * @param event mouseclick to activate the button.
     * @throws IOException if add customer fxml file isn't found.
     */
    @FXML
    void onClickAddCustomer(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.ADD_CUSTOMER_WINDOW_LOCATION, "Add Customer");
    }

    /** Exit button. Gives choice to exit program, log out and return to log in window, or return to the program.
     *
     * @param event mouseclick to activate the button
     * @throws IOException if the log in window fxml file isn't found.
     */
    @FXML
    void onClickExit(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Would you like to log out or exit completely?");

        ButtonType logout = new ButtonType("Log Out");
        ButtonType exit = new ButtonType("Exit Program");
        ButtonType cancel = new ButtonType("Return to Program");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(logout, exit, cancel);
        Optional<ButtonType> result = alert.showAndWait();

        // If logout button clicked.
        if (result.get() == logout){
            Utils.changeWindow(event, Utils.LOG_IN_WINDOW_LOCATION, "Log In");
            LogInWindowController.CurrentUser = null;
        // If exit button clicked.
        } else if (result.get() == exit){
            System.exit(0);
        // If cancel button is clicked.
        } else if (result.get() == cancel) {
            alert.close();
        }
    }

    /** Back button. Returns to the main window.
     *
     * @param event mouseclick to activate button
     * @throws IOException if main window fxml file isn't found.
     */
    @FXML
    void onClickMainWindow(ActionEvent event) throws IOException {
        Utils.changeWindow(event, Utils.MAIN_WINDOW_LOCATION, "Main Window");
    }

    /** Attaches an observable list of all customers in the database to the customer tableview.*/
    public void setTable(){
        customerTable.setItems(CustomerQuery.selectAllToList());
    }

    /** Deletes the selected customer from the database and resets the table with the new list obtained after deletion.
      */
    public void onClickDeleteCustomer() {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Utils.displayAlert("Please select a customer to delete. ");
        } else {
            Customer cust = customerTable.getSelectionModel().getSelectedItem();
            int custID = cust.getCustomerID();
            if (CustomerQuery.deleteConfirm(custID)) {
                if (CustomerQuery.delete(cust.getCustomerID())) {
                    setTable();
                    Utils.displayAlert("Successfully deleted Customer with ID: " + custID);
                }
            }
        }
    }

    /** Sets a static customer variable in the modify customer window to the selected customer. Then changes the window
     * to the modify customer window.
     * @param event mouseclick to activate the button.
     * @throws IOException if the modify customer window fxml file isn't found.
     */
    public void onClickModifyCustomer(ActionEvent event) throws IOException {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Utils.displayAlert("Please Select a customer to Modify.");
        } else {
            ModifyCustomerWindowController.customer = customerTable.getSelectionModel().getSelectedItem();
            Utils.changeWindow(event, Utils.MODIFY_CUSTOMER_WINDOW_LOCATION, "Modify Customer");
        }
    }

    /** First method runs when the window is created. Initializes the customer tableview.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location
     * is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     * object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
    }
}
