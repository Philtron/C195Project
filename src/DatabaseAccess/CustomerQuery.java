package DatabaseAccess;

import Helper.Utils;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.Optional;

/** This class controls all queries to the database related to Customers. */
public class CustomerQuery {
    /** Sends a modify customer query to the database. Modifies the customer whose ID matches the customerID parameter.
     *
     * @param customerID int ID of customer to be modified.
     * @param name string name. What the customer's name will be after modify
     * @param address string address. What the customer's address will be after modify.
     * @param postalCode string postalCode. What the customer's postalCode will be after modify.
     * @param phone string phone. What the customer's phone will be after modify.
     * @param lastUpdateBy string name of user modifying record.
     * @param divisionID int divisionID. What the customer's divisionID will be after modify.
     */
    public static void modifyCustomer(int customerID, String name, String address, String postalCode, String phone,
                                      String lastUpdateBy, int divisionID) {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, " +
                "Postal_Code = ?, Phone=?, Last_Update = timestamp(now()), Last_Updated_By=?, " +
                "Division_ID=? WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setString(5, lastUpdateBy);
            ps.setInt(6, divisionID);
            ps.setInt(7, customerID);
            System.out.println(ps);

            int rowsAffected = ps.executeUpdate();

            // Notifies the user if the update was successful.
            Utils.updatePassFail(rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Queries the database for the customer whose name matches the customerName parameter.
     *
     * @param customerName string name to search for.
     * @return Customer object that matches the name, or null if not found.
     */
    public static Customer select(String customerName) {
        String sql = "SELECT * FROM customers where Customer_Name= ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String custName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String zipCode = rs.getString("Postal_Code");
                String phoneNum = rs.getString("Phone");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divID = rs.getInt("Division_ID");
                return new Customer(customerID, custName, address, zipCode, phoneNum,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divID);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /** Queries the database for a customer whose ID matches the supplied customerID parameter.
     *
     * @param customerID customer ID to search for.
     * @return Customer object whose ID matched the customer ID parameter. Null if not found.
     */
    public static Customer select(int customerID) {
        String sql = "SELECT * FROM customers where Customer_ID= ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String custName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String zipCode = rs.getString("Postal_Code");
                String phoneNum = rs.getString("Phone");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divID = rs.getInt("Division_ID");
                return new Customer(customerID, custName, address, zipCode, phoneNum,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divID);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /** Function to insert a new customer into the database.
     *
     * @param name string name of new customer.
     * @param address string address of new customer.
     * @param postalCode string postal code of new customer.
     * @param phone string phone number of new customer.
     * @param createDate Timestamp date of insertion of new customer.
     * @param createdBy string name of user inserting new customer.
     * @param lastUpdate Timestamp date of insertion of new customer.
     * @param lastUpdateBy string name of user inserting new customer.
     * @param divisionID int division ID where new customer resides.
     */
    public static void insertCustomer(String name, String address, String postalCode, String phone,
                                      Timestamp createDate, String createdBy, Timestamp lastUpdate,
                                      String lastUpdateBy, int divisionID) {
        String sql = "INSERT INTO customers VALUES(Null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setTimestamp(5, createDate);
            ps.setString(6, createdBy);
            ps.setTimestamp(7, lastUpdate);
            ps.setString(8, lastUpdateBy);
            ps.setInt(9, divisionID);
            System.out.println(ps);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int customerID = rs.getInt(1);
            System.out.println("Customer inserted with Customer ID: " + customerID);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Queries the database for all customers and returns them as Customer Objects in an ObservableList.
     *
     * @return Observable list of all customers in the database.
     */
    public static ObservableList<Customer> selectAllToList() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerID = rs.getInt(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                String zip = rs.getString(4);
                String phone = rs.getString(5);
                Timestamp createDate = rs.getTimestamp(6);
                String createdBy = rs.getString(7);
                Timestamp lastUpdate = rs.getTimestamp(8);
                String lastUpdatedBy = rs.getString(9);
                int divisionID = rs.getInt(10);

                Customer newCustomer = new Customer(customerID, name, address, zip, phone, createDate, createdBy,
                        lastUpdate, lastUpdatedBy, divisionID);
                allCustomers.add(newCustomer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    /** Queries the database to see if there are any appointments with the customer ID parameter. If any are found  it
     * notifies the user that the customer has appointments and seeks confirmation on if they should continue with
     * deleting the customer.
     *
     * @param custID int ID primary key of customer to be deleted.
     * @return returns true if user confirms, false otherwise. 
     */
    public static boolean deleteConfirm(int custID) {
        String alertMessage;
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, custID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alertMessage = "This customer has appointments. Are you sure you would like to delete this customer" +
                        " and their appointments?";
            } else {
                alertMessage = "Are you sure you would like to delete this customer?";
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return (result.isPresent()) && (result.get() == ButtonType.OK);
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /** Deletes the appointments that match the customer ID supplied in the parameter, then deletes the Customer whose
     * ID matches.
     *
     * @param customerID int ID of appointments and customer to be deleted.
     * @return true if customer was deleted. False otherwise.
     */
    public static boolean delete(int customerID) {
        boolean deleted = false;
        String sql = "DELETE FROM appointments where Customer_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        String sql2 = "DELETE FROM customers WHERE Customer_ID= ?";
        try {
            PreparedStatement ps2 = JDBC.connection.prepareStatement(sql2);
            ps2.setInt(1, customerID);
            int rowsAffected = ps2.executeUpdate();
            if (rowsAffected == 1) {
                deleted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }
}
