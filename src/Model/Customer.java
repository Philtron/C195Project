package Model;

import java.sql.Timestamp;

/** Customer class file. */
public class Customer {
    private final int customerID;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phoneNumber;
    private final Timestamp createDate;
    private final String createdBy;
    private final Timestamp lastUpdate;
    private final String lastUpdatedBy;
    private final int divisionID;

    /** Full constructor
     *
     * @param customerID int ID - the primary key
     * @param name String name of customer.
     * @param address String address of customer.
     * @param postalCode String postal code of customer.
     * @param phoneNumber String phone number of customer.
     * @param createDate Timestamp date this record was created.
     * @param createdBy String user who created this record.
     * @param lastUpdate Timestamp  last time this record was updated.
     * @param lastUpdatedBy String user who last updated this record.
     * @param divisionID int division ID foreign key. The division the customer lives in.
     */
    public Customer(int customerID, String name, String address, String postalCode, String phoneNumber, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int divisionID) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }

    /**
     *
     * @return int the customer ID - the primary key.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @return String name of customer.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return String address of customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return String postal code of customer.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @return String the phone number of the customer.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @return Timestamp date this record was created.
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     *
     * @return String user who created this record.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @return Timestamp date this record was last updated.
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     *
     * @return int division ID foreign key. The division the customer lives in.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /** Overloaded to format for comboboxes. */
    public String toString(){
        return (this.name);
    }

}
