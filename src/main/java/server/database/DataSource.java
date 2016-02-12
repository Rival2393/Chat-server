package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "chat";
    private static final String PASSWORD = "root";

    private static Connection connection;

    public static Connection getConnection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (ClassNotFoundException e){
            System.err.println("No driver");
        } catch (SQLException e) {
            System.err.println("Unable to connect");
        }
        return null;
    }
}
