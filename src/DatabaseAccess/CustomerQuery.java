package DatabaseAccess;

import Model.Customer;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerQuery {
    public static void createCustomer(String name, String address, String postalCode, String phone,
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
    public static void selectAllToList(ObservableList<Customer> allCustomers){
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
//                System.out.println(String.format("%-2d", customerID) + " | Name: " + name + " | Address: " + address
//                        + " | Zip Code: " + zip + " | Phone: " + phone + " | Created: " + createDate + " | By: " + createdBy +
//                        " | Last Update: " + lastUpdate +  " | By: " + lastUpdatedBy + " | Division ID: " + divisionID);
                Customer newCustomer = new Customer(customerID, name, address, zip, phone, createDate, createdBy,
                        lastUpdate, lastUpdatedBy, divisionID);
//                System.out.println("Adding: " + newCustomer.getName());
                allCustomers.add(newCustomer);
            }
//            for(int i = 0; i < allCustomers.size(); i++){
//                System.out.println("Size: " + allCustomers.size());
//                System.out.println(allCustomers.get(i).getName());
//            }
        } catch (SQLException e){

            e.printStackTrace();
        }
    }
}
