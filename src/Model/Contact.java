package Model;

public class Contact {
    private int ContactID;
    private String ContactName;
    private String email;

    public Contact(int contact_ID, String contact_Name, String email) {
        ContactID = contact_ID;
        ContactName = contact_Name;
        this.email = email;
    }

    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}