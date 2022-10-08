package Model;

import java.time.LocalDateTime;

/**
 * Appointment class file.
 */
public class Appointment {
    private final int appointmentID;
    private int customerID;
    private String customerName;
    private String title;
    private String location;
    private String type;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String contactName;
    private final int userID;


    /**
     * Full constructor
     *
     * @param appointmentID int ID to be primary key
     * @param customerID    int ID opf customer associated with appointment. Foreign key, customer table's primary key.
     * @param customerName  string customer name attached to the appointment
     * @param title         string title of appointment
     * @param location      string location of appointment
     * @param type          string type of appointment
     * @param description   string description of appointment
     * @param start         LocalDateTime of start of appointment
     * @param end           LocalDateTime of end of appointment
     * @param contactName   String name of contact associated with the appointment
     * @param userID        int ID of user associated with the appointment. Foreign key, user table's primary key.
     */
    public Appointment(int appointmentID, int customerID, String customerName, String title, String location,
                       String type, String description, LocalDateTime start, LocalDateTime end, String contactName, int userID) {

        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.contactName = contactName;
        this.userID = userID;
    }

    /**
     * @return int the user ID associated with the appointment.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return int the appointment ID, which is the primary key in the database.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @return string name of customer associated with the appointment.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @return int customerID - Foreign Key
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return string title of appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return string location of appointment.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return string type of appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * @return String description of appointment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return LocalDateTime start of appointment.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return LocalDateTime end of appointment.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @return String name of contact associated with appointment.
     */
    public String getContactName() {
        return contactName;
    }

    public String toString(){
        return("ID: " + this.getAppointmentID() + " Title: " + this.title + " Type: " + this.type +
                " Description: " + this.description + " Start: " + this.start + " End: " + this.end +
                " CustomerID: " + this.customerID);
    }

}
