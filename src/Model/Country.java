package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Country {
    private final int CountryID;
    private final String country;
    private final LocalDate createDate;
    private final String createdBy;
    private final Timestamp lastUpdate;
    private final String updatedBy;

    public Country(int countryID, String country, LocalDate createDate, String createdBy, Timestamp lastUpdate, String updatedBy) {
        this.CountryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
    }

    public Integer getCountryID() {
        return CountryID;
    }

    public String getCountry() {
        return country;
    }



    public LocalDate getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public String toString(){

        return ("Country: " + this.country + " CountryID: " + this.CountryID);
    }
}
