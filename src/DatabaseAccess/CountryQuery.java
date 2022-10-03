package DatabaseAccess;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public abstract class CountryQuery {
    public static Country selectFromDivisionID(int divisionID){
        String sql = "SELECT c.Country_ID, c.Country, c.Create_Date, c.Created_By, c.Last_Update, c.Last_Updated_By " +
                "FROM countries c JOIN first_level_divisions f ON f.Country_ID = c.Country_ID WHERE f.Division_ID= ? ";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionID);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int countryID = rs.getInt(1);
                String country = rs.getString(2);
                Date createDate = rs.getDate(3);
                LocalDate ldCreateDate = new java.sql.Date(createDate.getTime()).toLocalDate();

//                LocalDate ldCreateDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                return new Country(countryID, country, ldCreateDate, createdBy, lastUpdate, lastUpdateBy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
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

                LocalDate ldCreateDate = new java.sql.Date(createDate.getTime()).toLocalDate();
//                LocalDate ldCreateDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


                String createdBy = rs.getString(4);
                Timestamp lastUpdate = rs.getTimestamp(5);
                String lastUpdateBy = rs.getString(6);
                Country newCountry = new Country(countryID, country, ldCreateDate, createdBy, lastUpdate, lastUpdateBy);
                allCountries.add(newCountry);
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return allCountries;
    }

}
