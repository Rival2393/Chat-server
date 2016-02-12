package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataAccessObject {

    private Connection connection;

    public static final String getStatusQuery = "select status from users u where u.ip = ?";

    public static final String insertUserQuery = "insert into users values (?, ?, 'OK')";

    public static final String setBanQuery = "update users set status = 'BANNED' where ip = ?";

    public DataAccessObject(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getStatus(String login, String ip) {
        String status = "";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(getStatusQuery);
            statement.setString(1, ip);

            ResultSet set = statement.executeQuery();
            set.next();
            status = set.getString("status");
        } catch (SQLException e) {
            insertUser(login, ip);
            status = "OK";
        }
        return status;
    }

    private void insertUser(String login, String ip) {
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement(insertUserQuery);
            statement.setString(1, login);
            statement.setString(2, ip);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBan(String ip){
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement(setBanQuery);
            statement.setString(1, ip);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
