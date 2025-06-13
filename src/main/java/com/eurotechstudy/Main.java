package com.eurotechstudy;

import com.eurotechstudy.config.ConnectionManager;
import com.eurotechstudy.config.ConnectionPoolConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class Main {

    final static String url = "jdbc:mysql://localhost:3306/jdbclesson";
    final static String user = "root";
    final static String password = "a1u2a3u4";


    final static String SQL_CREATE = """
            CREATE TABLE product 
            (
                id INTEGER PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(50)
            )
            """;

    final static String SQL_INSERT = """
            INSERT INTO product(name) VALUES
             ("Computer"),
             ("Phone"),
             ("Apple")
            """;

    final static String SQL_SELECT_ALL = """
            SELECT * FROM product
            
            """;

    final static String SQL_UPDATE = """
            UPDATE product SET           
            name = "Notebook"
            WHERE id = 1
            """;

    final static String SQL_DELETE_BY_ID = """
            DELETE FROM product 
            WHERE id IN (4, 5)        
            """;


    public static void main(String[] args) {
        //  createTable();
        //insertData();

        //updateData();
        //deleteData();
        //selectData();
        testHikariPool();
        //testConnection();

    }

    static void testHikariPool(){
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            System.out.println(connection.getTransactionIsolation());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void testConnection() {
        try (Connection connection = ConnectionManager.openConnection();
             Statement statement = connection.createStatement()) {
            System.out.println(connection.getTransactionIsolation());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void createTable() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            boolean execute = statement.execute(SQL_CREATE);
            System.out.println(execute);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void insertData() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            int rowCount = statement.executeUpdate(SQL_INSERT);
            System.out.println("Count of inserted rows = " + rowCount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void selectData() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            while (resultSet.next()) {
                System.out.print(resultSet.getInt("id"));
                System.out.println(" / " + resultSet.getString("name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void updateData() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            int rowCount = statement.executeUpdate(SQL_UPDATE);
            System.out.println("Count of updated rows = " + rowCount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void deleteData() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            int deletedRow = statement.executeUpdate(SQL_DELETE_BY_ID);
            System.out.println("Count of deleted rows = " + deletedRow);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}


