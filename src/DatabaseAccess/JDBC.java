package DatabaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;

/** Class that handles connecting to the client_schedule database required for C195 Software II project.
 *  This file was created while watching the "C195 getting The DBConnection Class Project Ready (02-13-2021)" webinar
 *  listed on the C195 Webinar archive and is borrowed from that video.
 */
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";

    /** Connection the DAO classes use */
    public static Connection connection;

    /** Attempts to connect to the database. */
    public static void openConnection()
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Returns the connection created when openConnection was run.
     *
     * @return Connection object used for accessing the database.
     */
    public static Connection getConnection(){
        return connection;
    }

    /** Terminate the connection */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }



}
