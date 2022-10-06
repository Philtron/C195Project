package DatabaseAccess;

import Helper.Utils;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.Optional;

public class CustomerQuery {
    public static void modifyCustomer(int customerID, String name, String address, String postalCode, String phone,
                                      String createdBy, Timestamp lastUpdate,
                                      String lastUpdateBy, int divisionID){
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, " +
                "Postal_Code = ?, Phone=?, Last_Update = timestamp(now()), Last_Updated_By=?, " +
                "Division_ID=? WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
//            ps.setTimestamp(5, createDate);
//            ps.setString(6, createdBy);
//            ps.setTimestamp(5, lastUpdate);
            ps.setString(5, lastUpdateBy);
            ps.setInt(6, divisionID);
            ps.setInt(7, customerID);
            System.out.println(ps);


            int rowsAffected = ps.executeUpdate();
            Utils.updatePassFail(rowsAffected);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static Customer select(String customerName) {
        String sql = "SELECT * FROM customers where Customer_Name= ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1,customerName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public static Customer select(int customerID) {
        String sql = "SELECT * FROM customers where Customer_ID= ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
//                int customerID = rs.getInt("Customer_ID");
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

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public static void insertCustomer(String name, String address, String postalCode, String phone,
                                      Timestamp createDate, String createdBy, Timestamp lastUpdate,
                                      String lastUpdateBy, int divisionID){
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

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static ObservableList<Customer> selectAllToList(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerID = rs.getInt(1);
                String name = String.format("%-8s",rs.getString(2));
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

        } catch (SQLException e){

            e.printStackTrace();
        }
        return allCustomers;
    }
//    public static int getNextID(){
//        int nextID = -1;
//        String sql = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_name = 'customers'";
////        String sql = "SELECT last_insert_id() from customers";
//        try {
//            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()){
//                nextID = rs.getInt(1);
//                System.out.println(nextID);
//                return nextID+1;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return nextID;
//    }

    public static boolean deleteConfirm(int custID){
        String alertMessage;
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, custID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
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

    public static boolean delete(int customerID) {
        boolean deleted = false;
        String sql = "DELETE FROM customers WHERE Customer_ID= ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 1){
                deleted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }
}
