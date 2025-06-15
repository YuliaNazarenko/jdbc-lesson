package com.eurotechstudy.dao;

import com.eurotechstudy.config.ConnectionPoolConfig;
import com.eurotechstudy.entity.Clients;
import com.eurotechstudy.entity.Invoices;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDao implements Dao <Clients, Integer>{

    @Getter
    private static final ClientDao INSTANCE = new ClientDao();

    private static final String SAVE_SQL = """
            INSERT INTO clients 
            (client_id,
             name,
             address,
             city,
             state,
             phone)
            VALUE (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE clients
            SET  address = ?,
            name = ?,
             city = ?,
             state = ?,
             phone = ?
            WHERE client_id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT client_id,
             name,
             address,
             city,
             state,
             phone
            FROM clients
            WHERE client_id = ?
            """;
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM clients
            WHERE client_id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT client_id,
             name,
             address,
             city,
             state,
             phone
            FROM clients
            """;
    private static final String SUBSELECT_SQL = """
            SELECT client_id,
             name,
             address,
             city,
             state,
             phone
            FROM clients
            WHERE city IN (
                SELECT city
                FROM clients
                GROUP BY city
                HAVING COUNT(client_id) > 1);
            """;



    @Override
    public Clients save(Clients clients) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL);
            preparedStatement.setInt(1,clients.getClient_id());
            preparedStatement.setString(2,clients.getName());
            preparedStatement.setString(3,clients.getAddress());
            preparedStatement.setString(4,clients.getCity());
            preparedStatement.setString(5, clients.getState());
            preparedStatement.setString(6, clients.getPhone());


            int rowEffected = preparedStatement.executeUpdate();

            if (rowEffected > 0) {
                System.out.println("Clients saved " + rowEffected + " rows");
            }else {
                System.out.println("Clients save failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    @Override
    public void update(Clients clients) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1,clients.getAddress());
            preparedStatement.setString(2,clients.getName());
            preparedStatement.setString(3,clients.getCity());
            preparedStatement.setString(4, clients.getState());
            preparedStatement.setString(5, clients.getPhone());
            preparedStatement.setInt(6,clients.getClient_id());

            int rowEffected = preparedStatement.executeUpdate();
            if (rowEffected > 0) {
                System.out.println("Clients updated " + rowEffected + " rows");
            }else {
                System.out.println("Clients update failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL);
            preparedStatement.setInt(1, id);
            int rowEffected = preparedStatement.executeUpdate();
            if (rowEffected > 0) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Clients> findById(Integer id) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Clients clients = null;
            if (resultSet.next()) {
                clients = new Clients (
                        resultSet.getObject(1,Integer.class),
                        resultSet.getObject(2,String.class),
                        resultSet.getObject(3,String.class),
                        resultSet.getObject(4, String.class),
                        resultSet.getObject(5, String.class),
                        resultSet.getObject(6,String.class)
                );
            }
            return Optional.ofNullable(clients);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Clients> findAll() {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Clients> clients = new ArrayList<>();
            Clients clientsObject = null;
            while (resultSet.next()) {
                clientsObject = new Clients(
                        resultSet.getObject(1, Integer.class),
                        resultSet.getObject(2, String.class),
                        resultSet.getObject(3, String.class),
                        resultSet.getObject(4, String.class),
                        resultSet.getObject(5, String.class),
                        resultSet.getObject(6, String.class)
                );
                clients.add(clientsObject);
            }
            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void subSelectMethod(){
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SUBSELECT_SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%20s",metaData.getColumnName(i));
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%20s",resultSet.getObject(i));
                }
                System.out.println();
            }
            System.out.println();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
