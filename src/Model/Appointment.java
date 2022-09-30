package Model;

import java.time.ZonedDateTime;

public class Appointment {
    private int appointmentID;
    private int customerID;
    private String customerName;
    private String title;
    private String location;
    private String type;
    private String description;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private String contactName;

    public Appointment(int appointmentID, int customerID, String customerName, String title, String location,
                       String type, String description, ZonedDateTime start, ZonedDateTime end, String contactName) {

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
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
