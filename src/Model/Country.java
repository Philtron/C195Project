package Model;

import java.sql.Timestamp;
import java.time.LocalDate;

/** Country class file */
public class Country {
    private final int CountryID;
    private final String country;
    private final LocalDate createDate;
    private final String createdBy;
    private final Timestamp lastUpdate;
    private final String updatedBy;

    /** Full constructor
     *
     * @param countryID int CountryID - primary key.
     * @param country String name of country.
     * @param createDate LocalDate date this record was created.
     * @param createdBy String user who created this record.
     * @param lastUpdate Timestamp last time this record was updated.
     * @param updatedBy String name of user who updated this record.
     */
    public Country(int countryID, String country, LocalDate createDate, String createdBy, Timestamp lastUpdate, String updatedBy) {
        this.CountryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
    }

    /**
     *
     * @return int country ID, the primary key
     */
    public Integer getCountryID() {
        return CountryID;
    }

    /**
     *
     * @return String name of country
     */
    public String getCountry() {
        return country;
    }

    /** Overloaded to format for filling comboboxes.
     *
     * @return String country name and countryID.
     */
    @Override
    public String toString(){

        return ("Country: " + this.country + " CountryID: " + this.CountryID);
    }
}
