package DatabaseAccess;

import Helper.Utils;
import Model.Contact;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserQuery {
    public static boolean logIn(String uName, String password){


        try {
            Connection con = JDBC.getConnection();
            PreparedStatement query = con.prepareStatement("SELECT * FROM users WHERE User_Name =\"" + uName + "\" AND " +
                    "Password =\"" + password + "\"");

            ResultSet rs = query.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

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
