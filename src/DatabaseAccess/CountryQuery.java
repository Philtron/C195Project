package DatabaseAccess;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CountryQuery {
    public static ObservableList<Country> selectAllToList(){
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT * FROM countries";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int countryID = rs.getInt(1);
                String country = rs.getString(2);
                Date createDate = rs.getDate(3);
//                LocalDate createDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                Country newCountry = new Country(countryID, country, createDate, createdBy, lastUpdate, lastUpdateBy);
                allCountries.add(newCountry);
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return allCountries;
    }
}
