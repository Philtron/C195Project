package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    private int userID;
    private String userName;


    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;

    }
    public String toString(){
        return String.valueOf(this.userID) + ": " + this.userName;
    }
}
