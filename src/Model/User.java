package Model;
/** User class file */
public class User {
    private final int userID;
    private final String userName;

    /** Full constructor
     *
     * @param userID int primary key
     * @param userName String name of user
     */
    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;

    }


    /**
     *
     * @return int primary key
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @return String name of user
     */
    public String getUserName() {
        return ("User: " + userName);
    }

}
