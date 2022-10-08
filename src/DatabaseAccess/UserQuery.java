package DatabaseAccess;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class controls all queries to the database related to Users. */
public class UserQuery {

    /** Checks the supplied username and password against users in the database.
     *
     * @param uName string username compared to users in the database.
     * @param password string password compared to passwords attached to users in the database.
     * @return true if both strings match, false otherwise.
     */
    public static boolean logIn(String uName, String password){
        String sql = "SELECT * FROM users where User_Name= ? AND Password= ? ";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, uName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /** Queries the database and returns a user object of any user that matches the supplied username parameter.
     *
     * @param userName string username of user to search for.
     * @return User object matching the supplied userName.
     */
    public static User selectUser(String userName) {
        User newUser = null;
        String sql = "SELECT * FROM users WHERE User_Name= ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, userName);
//            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int userID = rs.getInt(1);
                userName = rs.getString(2);
                newUser = new User(userID, userName);
            }

        }
        catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return newUser;
    }

    /** Queries the database for all Users and returns them as User Objects in an ObservableList.
     *
     * @return Observable list of all Users in the database.
     */
    public static ObservableList<User> selectAllToList(){
        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String sql = "SELECT * FROM users";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userID = rs.getInt(1);
                String userName = rs.getString(2);

                allUsers.add(new User(userID, userName));
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return allUsers;
    }
}
