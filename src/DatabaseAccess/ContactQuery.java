package DatabaseAccess;

import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class controls all queries to the database related to Contacts. */
public class ContactQuery {
    /** Queries the database for contacts by name
     *
     * @param contactName String name of contact searched for.
     * @return Contact object of the contact whose name matches the parameter, or null if not found.
     */
    public static Contact select(String contactName) {
        String sql = "SELECT * FROM contacts where Contact_Name = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1,contactName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int contactID = rs.getInt(1);
                String email = rs.getString(3);

                return new Contact(contactID, contactName, email);
            }

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /** Queries the database for all contacts and returns them as Contact objects in an ObservableList.
     *
     * @return Observable list of all contacts in the database.
     */
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
