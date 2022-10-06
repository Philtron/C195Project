package Model;

import javafx.collections.ObservableList;

public class User {
    private int userID;
    private String userName;


    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;

    }



    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return ("User: " + userName);
    }

}
