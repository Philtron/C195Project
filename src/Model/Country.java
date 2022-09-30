package Model;

import java.sql.Date;
import java.sql.Timestamp;

public class Country {
    private int CountryID;
    private String country;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String updatedBy;

    public Country(int countryID, String country, Date createDate, String createdBy, Timestamp lastUpdate, String updatedBy) {
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



    public Date getCreateDate() {
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
