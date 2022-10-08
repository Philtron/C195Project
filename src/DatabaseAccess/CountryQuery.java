package DatabaseAccess;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

/** This class controls all queries to the database related to Contacts. */
public abstract class CountryQuery {
    /** Queries the database to find a country using a division ID.
     *
     * @param divisionID int division ID used to search for the country.
     * @return Country object the division belongs to.
     */
    public static Country selectFromDivisionID(int divisionID){
        String sql = "SELECT c.Country_ID, c.Country, c.Create_Date, c.Created_By, c.Last_Update, c.Last_Updated_By " +
                "FROM countries c JOIN first_level_divisions f ON f.Country_ID = c.Country_ID WHERE f.Division_ID= ? ";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int countryID = rs.getInt(1);
                String country = rs.getString(2);
                Date createDate = rs.getDate(3);
                LocalDate ldCreateDate = new java.sql.Date(createDate.getTime()).toLocalDate();
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
    /** Queries the database for all countries and returns them, as country objects, in an ObservableList.
     *
     * @return Observable list of all countries in the database.
     */
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
