package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    private int userID;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
}
