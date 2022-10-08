package Helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZonedDateTime;

/** This class controls logging log in attempts. */
public class Logging {

    /** Opens or creates a login_activity.txt file and records the username, time in UTC, and whether the login attempt
     * was successful or not.
     *
     * @param success boolean whether the login attempt was successful or not.
     * @param userName String username of individual attempting the login.
     * @throws IOException if the file is not found.
     */
    public static void logLogIn(boolean success, String userName) throws IOException {
        File file = new File("src/login_activity.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        Instant now = ZonedDateTime.now().toInstant();

        if (success){
            printWriter.println("User: " + userName + " Log in: SUCCESS at " + now.toString() + " UTC.");
        } else {
            printWriter.println("User: " + userName + " Log in: FAIL at " + now.toString()+ " UTC.");
        }

        printWriter.close();
        fileWriter.close();
    }
}
