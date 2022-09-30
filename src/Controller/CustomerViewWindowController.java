package Controller;

import DatabaseAccess.CustomerQuery;
import DatabaseAccess.JDBC;
import Helper.Utils;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewWindowController implements Initializable {

    @FXML
    private Button addCustomerButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button modifyCustomerButton;


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
    void onClickExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)){
            JDBC.closeConnection();
            System.exit(0);
        }
    }

    @FXML
    void onClickMainWindow(ActionEvent event) throws IOException {
        Utils.changeWindow(event, "../View/MainWindow.fxml", "Main Window");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        CustomerQuery.selectAllToList(allCustomers);
        customerTable.setItems(CustomerQuery.selectAllToList());
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
    }
}
