import DatabaseAccess.AppointmentQuery;
import DatabaseAccess.JDBC;
import DatabaseAccess.UserQuery;
import Model.Appointment;
import Model.User;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/LogInWindow.fxml"));
//        primaryStage.centerOnScreen();
        primaryStage.setTitle("C195 Philip Sauer.");
        primaryStage.setScene(new Scene(root, 480, 250));
        primaryStage.show();
    }


    public static void main(String[] args) {
        JDBC.openConnection();


        launch(args);
        JDBC.closeConnection();
    }
}
