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
import java.time.Instant;
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
    public static boolean delete(int appointmentID){
        boolean deleted = false;
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, appointmentID);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 1){
                deleted = true;
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return deleted;
    }
    public static ObservableList<Appointment> selectAllToTableViewList(){
        ObservableList<Appointment> allAppointments =FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name FROM customers cu JOIN appointments a ON " +
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
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                Appointment newAppointment = new Appointment(appointmentID, custID, cusName, title, location, type, description, start, end, conName, userID);
                allAppointments.add(newAppointment);
            }

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return allAppointments;
    }

    public static void modifyAppointment(String title, String description, String location, String type,
                                         ZonedDateTime startTime, ZonedDateTime endTime, Timestamp lastUpdate,
                                         String lastUpdateBy, int customerID, int userID, int contactID, int appointmentID) {

        String sql = "UPDATE appointments SET Title = ?, Description=?, Location=?, Start=?, End=?, Last_Update = ?, " +
                "Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=?, Type=? WHERE Appointment_ID=?";
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            Instant startInstant = startTime.toInstant();
            ps.setTimestamp(4, Timestamp.from(startInstant));
            Instant endInstant = endTime.toInstant();
            ps.setTimestamp(5, Timestamp.from(endInstant));
            Instant lastUpdateInstant = lastUpdate.toInstant();
            ps.setTimestamp(6, Timestamp.from(lastUpdateInstant));
            ps.setString(7, lastUpdateBy);
            ps.setInt(8, customerID);
            ps.setInt(9, userID);
            ps.setInt(10, contactID);
            ps.setString(11, type);
            ps.setInt(12, appointmentID);

            System.out.println(sql);
            int rowsAffected = ps.executeUpdate();
            Utils.updatePassFail(rowsAffected);

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
