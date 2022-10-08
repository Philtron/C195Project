import DatabaseAccess.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

/** This is the main class for the C195 Scheduling Program Assessment. This program uses a graphical interface
 * for a scheduling program that works with a MYSQL database.
 *
 * Javadocs folder and README.txt are located in the C195Project/Docs folder
 *
 * @author Philip Sauer completed 10/07/2022
 */
public class Main extends Application {

    /** Standard javafx scene setting method. Places the log in window slightly to the left of the screen
     * so that when switching to the main window, nothing is placed outside the  screen. */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/LogInWindow.fxml"));
        primaryStage.setTitle("C195 Philip Sauer");
        primaryStage.setScene(new Scene(root, 480, 250));
        primaryStage.setX(300);
        primaryStage.show();
    }

    /** Main method connects to the Database and then launches the GUI
     *
     * @param args String array variable to store command line arguments. */
    public static void main(String[] args) {
        JDBC.openConnection();
//        Locale.setDefault(new Locale("fr"));

        launch(args);
        JDBC.closeConnection();
    }
}
