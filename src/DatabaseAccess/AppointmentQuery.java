package DatabaseAccess;

import Controller.LogInWindowController;
import Helper.Utils;
import Model.Appointment;
import Model.Contact;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;

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
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
//                LocalDateTime start = ZonedDateTime.of(time, ZoneId.systemDefault());
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
//                LocalDateTime end = ZonedDateTime.of(time, ZoneId.systemDefault());
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
                                         LocalDateTime startTime, LocalDateTime endTime, Timestamp lastUpdate,
                                         String lastUpdateBy, int customerID, int userID, int contactID, int appointmentID) {

        String sql = "UPDATE appointments SET Title = ?, Description=?, Location=?, Start=?, End=?, Last_Update = ?, " +
                "Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=?, Type=? WHERE Appointment_ID=?";
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
//            Instant startInstant = startTime.toInstant();
//            ps.setTimestamp(4, Timestamp.from(startInstant));
//            Instant endInstant = endTime.toInstant();
//            ps.setTimestamp(5, Timestamp.from(endInstant));
            ps.setTimestamp(4, Timestamp.valueOf(startTime));
//            Instant lastUpdateInstant = lastUpdate.toInstant();
            ps.setTimestamp(5, Timestamp.valueOf(endTime));
            ps.setTimestamp(6, lastUpdate);
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
    public static ArrayList<String> appointmentsInFifteenMinutes(){
        StringBuilder apptString = new StringBuilder();
        ArrayList<String> appointmentsInFifteen = new ArrayList<>();

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTIme = currentTime.plusMinutes(15);


        String sql = "SELECT a.Appointment_ID, a.Start, c.Customer_Name FROM appointments a " +
                "JOIN customers c ON a.Customer_ID = c.Customer_ID " +
                "WHERE Start BETWEEN ? AND ? ";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(currentTime));
            ps.setTimestamp(2, Timestamp.valueOf(startTIme));
            System.out.println("HI!" + ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                Timestamp start = rs.getTimestamp("Start");
                String custName = rs.getString("Customer_Name");

                System.out.println("Appending: " + String.valueOf(apptID) + " " + start );
                apptString.append("Appointment ID: ");
                apptString.append(String.valueOf(apptID));
                apptString.append("\nStart: ");
                apptString.append(start);
                apptString.append("\nCustomer: ");
                apptString.append(custName);
                appointmentsInFifteen.add(apptString.toString());
                System.out.println("Added! " + appointmentsInFifteen.size());
                apptString.setLength(0);

            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentsInFifteen;
    }
    public static ObservableList<Appointment> filterByCustomerID(int customerID){
        ObservableList<Appointment> filteredList = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name FROM customers cu JOIN appointments a ON " +
                "a.Customer_ID = cu.Customer_ID JOIN contacts co ON a.Contact_ID = co.Contact_ID WHERE a.Customer_ID=?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                Appointment newAppointment = new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID);
                filteredList.add(newAppointment);
            }

        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return filteredList;
    }

    public static ObservableList<Appointment> filterByWeek(LocalDateTime today){
        LocalDateTime weekFromNow = today.plusDays(6);
        ObservableList<Appointment> appointmentsByWeek = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name FROM customers cu JOIN appointments a ON " +
                "a.Customer_ID = cu.Customer_ID JOIN contacts co ON a.Contact_ID = co.Contact_ID WHERE Start BETWEEN " +
                "? AND ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(today));
            ps.setTimestamp(2, Timestamp.valueOf(weekFromNow));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                Appointment newAppointment = new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID);
                appointmentsByWeek.add(newAppointment);
            }
        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentsByWeek;
    }
    public static ObservableList<Appointment> filterByMonth(){
//        LocalDateTime weekFromNow = today.plusDays();
        ObservableList<Appointment> appointmentsByWeek = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name FROM customers cu JOIN appointments a ON " +
                "a.Customer_ID = cu.Customer_ID JOIN contacts co ON a.Contact_ID = co.Contact_ID WHERE " +
                "MONTH(Start) = MONTH(current_date())";

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
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                Appointment newAppointment = new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID);
                appointmentsByWeek.add(newAppointment);
            }
        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentsByWeek;
    }
    public static String appointmentsByTypeMonth() {


        String sql = "SELECT Type, month(Start) AS month, count(*) AS count FROM appointments " +
                "GROUP BY Type, month(Start) ORDER BY month;";
        StringBuilder reportString = new StringBuilder("APPOINTMENTS BY TYPE PER MONTH\n");

        try {
            int monthCounter = 0;
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("month") > monthCounter) {
//                    reportString.append(String.format("%s\n", Month.of(rs.getInt("month"))));
                    reportString.append("\n" + Month.of(rs.getInt("month"))+ "\n");
                    monthCounter = rs.getInt("month");
                }
//                reportString.append(String.format("%s - Total: %d\n", rs.getString("Type"), rs.getInt("count")));
                reportString.append(rs.getString("Type")+ " - Total: " + rs.getInt("count") + "\n");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return reportString.toString();
    }
    public static String appointmentsByContacts() {
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name, co.Contact_ID FROM customers cu " +
                "JOIN appointments a ON a.Customer_ID = cu.Customer_ID JOIN contacts co " +
                "ON a.Contact_ID = co.Contact_ID ORDER BY Contact_ID";
        StringBuilder reportString = new StringBuilder("APPOINTMENTS BY CONTACT\n");
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<Contact> contacts = ContactQuery.selectAllToList();

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
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID));
            }
            for(int i = 0; i < contacts.size(); i++){
                reportString.append(contacts.get(i).getContactName());
                reportString.append("\n");
                for(int j= 0; j < appointments.size();j++){
                    if(appointments.get(j).getContactName().equals(contacts.get(i).getContactName())){
                        reportString.append(appointments.get(j));
                        reportString.append("\n");
                    }
                }
                reportString.append("\n");
            }


        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return reportString.toString();

    }
    public static String appointmentsByUser() {
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name, co.Contact_ID FROM customers cu " +
                "JOIN appointments a ON a.Customer_ID = cu.Customer_ID JOIN contacts co " +
                "ON a.Contact_ID = co.Contact_ID ORDER BY User_ID";
        StringBuilder reportString = new StringBuilder("APPOINTMENTS BY USER\n");
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<User> users = UserQuery.selectAllToList();

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
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID));
            }
            for(int i = 0; i < users.size(); i++){
                reportString.append(users.get(i).getUserName());
                reportString.append("\n");
                for(int j= 0; j < appointments.size();j++){
                    if(appointments.get(j).getUserID() == users.get(i).getUserID()){
                        reportString.append(appointments.get(j));
                        reportString.append("\n");
                    }
                }
                reportString.append("\n");
            }


        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return reportString.toString();

    }


}
