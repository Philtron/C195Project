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

public class CustomerViewWindowController implements Initializable {

//    @FXML
//    private Button addCustomerButton;
//    @FXML
//    private Button deleteCustomer;
//    @FXML
//    private Button backButton;
//    @FXML
//    private Button exitButton;
//    @FXML
//    private Button modifyCustomerButton;


    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, Integer> custIDCol;

    @FXML
    private TableColumn<Customer, Integer> divisionIDCol;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableColumn<Customer, String> zipCol;




    @FXML
    void onClickAddCustomer(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/AddCustomerWindow.fxml", "Add Customer");
    }

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


        if (result.get() == logout){
            Utils.changeWindow(event, "../View/LogInWindow.fxml", "Log In");
            LogInWindowController.CurrentUser = null;
        } else if (result.get() == exit){
            System.exit(0);
        } else if (result.get() == cancel) {
            alert.close();
        }
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if ((result.isPresent()) && (result.get() == ButtonType.OK)){
//            JDBC.closeConnection();
//            System.exit(0);
//        }
    }

    @FXML
    void onClickMainWindow(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/MainWindow.fxml", "Main Window");
    }

    public void setTable(){
        customerTable.setItems(CustomerQuery.selectAllToList());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
    }

    public void onClickDeleteCustomer() {
        Customer cust = customerTable.getSelectionModel().getSelectedItem();
        int custID = cust.getCustomerID();
        if(CustomerQuery.deleteConfirm(custID)){
            if(CustomerQuery.delete(cust.getCustomerID())) {
                setTable();
                Utils.displayAlert("Successfully deleted Customer with ID: " + custID);
            }
        }
    }

    public void onClickModifyCustomer(ActionEvent event) throws IOException {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Utils.displayAlert("Please Select a customer to Modify.");
        } else {
            ModifyCustomerWindowController.customer = customerTable.getSelectionModel().getSelectedItem();
            Utils.changeWindow(event, "../View/ModifyCustomerWindow.fxml", "Modify Customer");
        }
    }
}
