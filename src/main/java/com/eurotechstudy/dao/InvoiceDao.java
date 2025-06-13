package com.eurotechstudy.dao;

import com.eurotechstudy.config.ConnectionPoolConfig;
import com.eurotechstudy.entity.Invoices;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDao implements Dao <Invoices,Integer>{

@Getter
    private static final InvoiceDao INSTANCE = new InvoiceDao();

    private static final String SAVE_SQL = """
            INSERT INTO invoices(INVOICE_ID, NUMBER, CLIENT_ID, INVOICE_TOTAL, PAYMENT_TOTAL, INVOICE_DATE, DUE_DATE, PAYMENT_DATE) 
            VALUE (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_ALL_SQL = """
            SELECT invoices.invoice_id,number,client_id,invoice_total,payment_total,invoice_date,due_date,payment_date
            FROM invoices
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT invoice_id, number, client_id, invoice_total, payment_total, invoice_date, due_date, payment_date
            from invoices
            where invoice_id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE invoices
            SET NUMBER  = ?,
            client_id = ?,
            invoice_total = ?,
            payment_total= ?,
            invoice_date = ?,
            due_date = ?,
            payment_date = ?
            
            where invoice_id = ?
            
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM invoices 
            WHERE invoice_id = ?
            """;

    private static final String JOIN_SQL = """
           
            SELECT invoice_id,
            number, c.name
            FROM invoices i
            JOIN clients c ON c.client_id = i.client_id
            WHERE invoice_id  = ?
            """;

    private static final String SUBSELECT_SQL = """
            SELECT invoice_id, number, client_id, invoice_total
            FROM invoices
            WHERE invoice_total > 
            (select avg (invoice_total)
            from invoices)
            
            """;


    private InvoiceDao(){

    }


    @Override
    public Invoices save(Invoices invoices) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL);
            preparedStatement.setInt(1,invoices.getInvoice_id());
            preparedStatement.setString(2,invoices.getNumber());
            preparedStatement.setInt(3,invoices.getClient_id());
            preparedStatement.setBigDecimal(4,invoices.getInvoice_total());
            preparedStatement.setBigDecimal(5,invoices.getPayment_total());
            preparedStatement.setDate(6, Date.valueOf(invoices.getInvoice_date()));
            preparedStatement.setDate(7,Date.valueOf(invoices.getDue_date()));
            preparedStatement.setDate(8, Date.valueOf(invoices.getPayment_date()));


            int rowEffected = preparedStatement.executeUpdate();

            if (rowEffected > 0) {
                System.out.println("Invoice saved " + rowEffected + " rows");
            }else {
                System.out.println("Invoice save failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return invoices;
    }

    @Override
    public void update(Invoices invoices) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, invoices.getNumber());
            preparedStatement.setInt(2, invoices.getClient_id());
            preparedStatement.setBigDecimal(3, invoices.getInvoice_total());
            preparedStatement.setBigDecimal(4, invoices.getPayment_total());
            preparedStatement.setDate(5, Date.valueOf(invoices.getInvoice_date()));
            preparedStatement.setDate(6, Date.valueOf(invoices.getDue_date()));
            preparedStatement.setObject(7, invoices.getPayment_date()!=null?invoices.getPayment_date():null);
            preparedStatement.setInt(8, invoices.getInvoice_id());
            int rowEffected = preparedStatement.executeUpdate();
            if (rowEffected > 0) {
                System.out.println("Invoice updated " + rowEffected + " rows");
            }else {
                System.out.println("Invoice update failed");
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
    public Optional<Invoices> findById(Integer id) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Invoices invoices = null;
            if (resultSet.next()) {
                invoices = new Invoices(
                        resultSet.getObject(1,Integer.class),
                        resultSet.getObject(2,String.class),
                        resultSet.getObject(3,Integer.class),
                        resultSet.getObject(4, BigDecimal.class),
                        resultSet.getObject(5, BigDecimal.class),
                        resultSet.getObject(6,LocalDate.class),
                        resultSet.getObject(7,LocalDate.class),
                        resultSet.getObject(8,LocalDate.class)
                );
            }
            return Optional.ofNullable(invoices);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Invoices> findAll() {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Invoices> invoices = new ArrayList<>();
            Invoices invoicesObject = null;
            while (resultSet.next()) {
                invoicesObject = new Invoices(
                        resultSet.getObject(1, Integer.class),
                        resultSet.getObject(2, String.class),
                        resultSet.getObject(3, Integer.class),
                        resultSet.getObject(4, BigDecimal.class),
                        resultSet.getObject(5, BigDecimal.class),
                        resultSet.getObject(6, LocalDate.class),
                        resultSet.getObject(7, LocalDate.class),
                        resultSet.getObject(8, LocalDate.class)
                );
                invoices.add(invoicesObject);
            }
            return invoices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinMethod(){
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(JOIN_SQL);
            preparedStatement.setInt(1,1);
            Integer invoice_id = null;
            String number = null;
            String name = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                invoice_id = resultSet.getObject(1,Integer.class);
                number = resultSet.getObject(2,String.class);
                name = resultSet.getObject(3,String.class);
            }
            System.out.println(invoice_id + " " + number + " " + name);

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
                System.out.printf("%15s",metaData.getColumnName(i));
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%15s",resultSet.getObject(i));
                }
                System.out.println();
            }
            System.out.println();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
