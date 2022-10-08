package Model;

import java.sql.Date;
import java.sql.Timestamp;

/** FirstLevelDivision Class file */
public class FirstLevelDivision {
    private final int divisionID;
    private final String division;
    private final Date createDate;
    private final String createdBy;
    private final Timestamp lastUpdate;
    private final String lastUpdatedBy;
    private final int countryID;

    /** Full constructor
     *
     * @param divisionID int the primary key.
     * @param division String name of the FirstLevelDivision.
     * @param createDate Date this record was created.
     * @param createdBy String user who created this record.
     * @param lastUpdate Timestamp date this record was last updated.
     * @param lastUpdatedBy String user who last updated this record.
     * @param countryID int the ID of the country this division belongs to.
     */
    public FirstLevelDivision(int divisionID, String division, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    /**
     *
     * @return int the primary key.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @return int the ID of the country this division belongs to.
     */
    public int getCountryID() {
        return countryID;
    }

    /** Overloaded to format for comboboxes.
     *
     * @return String division name and ID.
     */
    @Override
    public String toString(){
        return (this.division + " Division ID: " + this.divisionID);
    }
}
