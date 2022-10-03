package DatabaseAccess;

import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactQuery {

    public static ObservableList<Contact> selectAllToList(){
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacts";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int contactID = rs.getInt(1);
                String contactName = rs.getString(2);
                String email = rs.getString(3);
                Contact newContact = new Contact(contactID, contactName, email);
                allContacts.add(newContact);
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
        return allContacts;
    }

}
