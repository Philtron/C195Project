package DatabaseAccess;

import Model.Country;
import Model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/** This class controls all queries to the database related to First Level Divisions. */
public abstract class FirstLevelDivisionQuery {

    /** Queries the database for all first level divisions and returns them as FirstLevelDivision Objects in an
     * ObservableList.
     *
     * @return Observable list of all first level divisions in the database.
     */
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

                FirstLevelDivision newFirstLevelDivision = new FirstLevelDivision(divisionID, division, createDate,
                        createdBy, lastUpdate, lastUpdateBy, countryID);
                allFirstLevelDivisions.add(newFirstLevelDivision);
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return allFirstLevelDivisions;
    }

    /** Queries the database and returns all divisions related to the supplied country parameter.
     *
     * @param inputCountry Country object used to filter first level divisions.
     * @return Observable list of FirstLevelDivision objects that are related to the supplied country.
     */
    public static ObservableList<FirstLevelDivision> getFilteredDivisions(Country inputCountry) {
        ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList();
        int countryID = inputCountry.getCountryID();
        for(FirstLevelDivision division : selectAllToList()){
            if(division.getCountryID() == countryID) {
                filteredDivisions.add(division);
            }
        }
        return filteredDivisions;
    }

    /** Queries the database and returns a FirstLevelDivision object matching the supplied divisionID parameter.
     *
     * @param divisionID int ID of division to search for.
     * @return FirstLevelDivision object that matches the supplied divisionID parameter or null if not found.
     */
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

                return (new FirstLevelDivision(divisionID, division, createDate,
                        createdBy, lastUpdate, lastUpdateBy, countryID));

            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return null;
    }
}
