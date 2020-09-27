package com.company.app.server;

import com.company.app.network.Constants;
import com.company.app.network.LoginRequest;
import com.company.app.network.RegisterRequest;

import java.sql.*;

public class DBManager {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private final String url;
    private final String login;
    private final String password;

    public DBManager(String IP, String PORT, String dbName, String login, String password) {
        this.url = "jdbc:mysql://" + IP + ":" + PORT + "/" + dbName;
        this.login = login;
        this.password = password;
    }

    public DBManager() {
        this.url = "jdbc:mysql://" + Constants.JDBC_IP +":" + Constants.JDBC_PORT + "/"+ Constants.DB_NAME +"?serverTimezone=Europe/Moscow";
        this.login = Constants.LOGIN;
        this.password = Constants.PASSWORD;
    }

    public boolean loginQuery(LoginRequest loginRequest) {
        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where name=? and password=?");
            preparedStatement.setString(1, loginRequest.getLogin());
            preparedStatement.setString(2, loginRequest.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось выполнить запрос :/");
            return false;
        }
        return false;
    }

    public boolean regQuery(RegisterRequest registerRequest) {
        try {


            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement preparedCheck = connection.prepareStatement("select * from users where name=?");
            preparedCheck.setString(1, registerRequest.getLogin());
            ResultSet rs = preparedCheck.executeQuery();
            if (rs.next()) return false;


            PreparedStatement preparedStatement = connection.prepareStatement("insert into users values(null, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, registerRequest.getLogin());
            preparedStatement.setString(2, registerRequest.getPassword());
            preparedStatement.setString(3, registerRequest.getEmail());
            preparedStatement.setString(4, null);
            preparedStatement.setInt(5, 1);
            System.out.println(preparedStatement.executeUpdate() + " rows added");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось выполнить запрос :/");
        } finally {
            try {
                connection.close();
            } catch (SQLException ec) {
                ec.printStackTrace();
                System.out.println("Не удалось закрыть соединение :/");
            }
        }
        return false;
    }
}
