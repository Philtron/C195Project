package Helper;

import Controller.LogInWindowController;
import Model.User;

import java.io.*;
import java.time.Instant;
import java.time.ZonedDateTime;

public class Logging {


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
