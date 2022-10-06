import DatabaseAccess.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/LogInWindow.fxml"));
        primaryStage.setTitle("C195 Philip Sauer");
        primaryStage.setScene(new Scene(root, 480, 250));
        primaryStage.setX(300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        JDBC.openConnection();
//        Locale.setDefault(new Locale("fr"));

        launch(args);
        JDBC.closeConnection();
    }
}
