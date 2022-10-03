package DatabaseAccess;

import Model.Country;
import Model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public abstract class FirstLevelDivisionQuery {

    public static void printSelectAll(){
        String sql = "SELECT * FROM first_level_divisions";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionID = rs.getInt(1);
                String division = String.format("%-8s", rs.getString(2));
                Date createDate = rs.getDate(3);
                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                int countryID = rs.getInt(7);
                System.out.println(String.format("%-2d", divisionID) + " | Division:  " + division + " | Created: " +
                        createDate + " | By:" + createdBy + " | Last Update: " + lastUpdate + " | By: " + lastUpdateBy
                + " | Country ID: " + countryID);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void printSelect(String searchCol, String searchItem){
        String sql = "SELECT * FROM first_level_divisions WHERE " + searchCol+ "= ?";


        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, searchItem);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionID = rs.getInt(1);
                String division = String.format("%-8s", rs.getString(2));
                Date createDate = rs.getDate(3);
                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                int countryID = rs.getInt(7);
                System.out.println(String.format("%-2d", divisionID) + " | Division:  " + division + " | Created: " +
                        createDate + " | By:" + createdBy + " | Last Update: " + lastUpdate + " | By: " + lastUpdateBy
                        + " | Country ID: " + countryID);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static ObservableList<FirstLevelDivision> selectAllToList(){
        ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionID = rs.getInt(1);
                String division = rs.getString(2);
                Date createDate = rs.getDate(3);
                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                int countryID = rs.getInt(7);
//                System.out.println(String.format("%-2d", divisionID) + " | Division:  " + division + " | Created: " +
//                        createDate + " | By:" + createdBy + " | Last Update: " + lastUpdate + " | By: " + lastUpdateBy
//                        + " | Country ID: " + countryID);
                FirstLevelDivision newFirstLevelDivision = new FirstLevelDivision(divisionID, division, createDate,
                        createdBy, lastUpdate, lastUpdateBy, countryID);
                allFirstLevelDivisions.add(newFirstLevelDivision);
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return allFirstLevelDivisions;
    }

    public static ObservableList<FirstLevelDivision> getFilteredDivisions(Country inputCountry) throws SQLException {
        ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList();
        int countryID = inputCountry.getCountryID();
        for(FirstLevelDivision division : selectAllToList()){
            if(division.getCountryID() == countryID) {
                filteredDivisions.add(division);
            }
        }
        return filteredDivisions;
    }

    public static FirstLevelDivision select(int divisionID) {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String division = rs.getString(2);
                Date createDate = rs.getDate(3);
                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                int countryID = rs.getInt(7);
//                System.out.println(String.format("%-2d", divisionID) + " | Division:  " + division + " | Created: " +
//                        createDate + " | By:" + createdBy + " | Last Update: " + lastUpdate + " | By: " + lastUpdateBy
//                        + " | Country ID: " + countryID);
                return (new FirstLevelDivision(divisionID, division, createDate,
                        createdBy, lastUpdate, lastUpdateBy, countryID));

            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return null;
    }
}
