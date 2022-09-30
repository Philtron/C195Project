package DatabaseAccess;

import Helper.Utils;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class AppointmentQuery {
    public static void insert(String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end,
                             ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdatedBy,
                             int customerID, int userID, int contactID) {
        String sql = "INSERT INTO appointments VALUES(Null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start.toLocalDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(end.toLocalDateTime()));
            ps.setTimestamp(7, Timestamp.valueOf(createDate.toLocalDateTime()));
            ps.setString(8, createdBy);
            ps.setTimestamp(9, Timestamp.valueOf(lastUpdate.toLocalDateTime()));
            ps.setString(10, lastUpdatedBy);
            ps.setInt(11, customerID);
            ps.setInt(12, userID);
            ps.setInt(13, contactID);

            int rowsAffected = ps.executeUpdate();
            Utils.updatePassFail(rowsAffected);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public static ObservableList<Appointment> selectAllToTableViewList(){
        ObservableList<Appointment> allAppointments =FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End,   co.Contact_Name FROM customers cu JOIN appointments a ON " +
                "a.Customer_ID = cu.Customer_ID JOIN contacts co ON a.Contact_ID = co.Contact_ID";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime time = rs.getTimestamp("Start").toLocalDateTime();
                ZonedDateTime start = ZonedDateTime.of(time, ZoneId.systemDefault());
                time = rs.getTimestamp("End").toLocalDateTime();
                ZonedDateTime end = ZonedDateTime.of(time, ZoneId.systemDefault());
                String conName = rs.getString("Contact_Name");
                Appointment newAppointment = new Appointment(appointmentID, custID, cusName, title, location, type, description, start, end, conName);
                allAppointments.add(newAppointment);
            }

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return allAppointments;
    }
}
