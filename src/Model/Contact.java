package Model;

/** Contact class file */
public class Contact {
    private int ContactID;
    private String ContactName;
    private String email;

    /** Full constructor
     *
     * @param contact_ID int ID to be primary key.
     * @param contact_Name String name of contact.
     * @param email String email of contact.
     */
    public Contact(int contact_ID, String contact_Name, String email) {
        ContactID = contact_ID;
        ContactName = contact_Name;
        this.email = email;
    }

    /**
     *
     * @return int contact ID, primary key of contact.
     */
    public int getContactID() {
        return ContactID;
    }

    /**
     *
     * @return String name of contact.
     */
    public String getContactName() {
        return ContactName;
    }

    /**Overloaded to format Comboboxes of contacts.
     *
     * @return String name of contact.
     */
    public String toString(){
        return (this.ContactName);
    }
}