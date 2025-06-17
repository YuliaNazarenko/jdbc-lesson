package com.eurotechstudy.dao;

import com.eurotechstudy.config.ConnectionPoolConfig;
import com.eurotechstudy.entity.BankAccount;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class BankAccountDao implements Dao<BankAccount, Integer> {

    @Getter
    private static final BankAccountDao INSTANCE = new BankAccountDao();


    private static final String FIND_BY_ID_SQL = """
            SELECT *
            from bank_account
            where account_id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE bank_account
            SET account_type  = ?,
            balance = ?
            
            
            where account_id  = ?
            
            """;


    @Override
    public BankAccount save(BankAccount bankAccount) {
        return null;
    }

    @Override
    public void update(BankAccount bankAccount) {

        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, bankAccount.getAccount_type());
            preparedStatement.setBigDecimal(2, bankAccount.getBalance());
            preparedStatement.setInt(3, bankAccount.getAccount_id());
            int rowEffected = preparedStatement.executeUpdate();
            if (rowEffected > 0) {
                System.out.println("Invoice updated " + rowEffected + " rows");
            } else {
                System.out.println("Invoice update failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Optional<BankAccount> findById(Integer id) {
        try (HikariDataSource hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
             Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            BankAccount bankAccount = null;
            if (resultSet.next()) {
                bankAccount = new BankAccount(
                        resultSet.getObject(1, Integer.class),
                        resultSet.getObject(2, Integer.class),
                        resultSet.getObject(3, String.class),
                        resultSet.getObject(4, BigDecimal.class),
                        resultSet.getObject(5, Timestamp.class)
                );
            }
            return Optional.ofNullable(bankAccount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BankAccount> findAll() {
        return List.of();
    }

    public String withdrawalMoney(Integer bankAccountId, BigDecimal amount) {
        Optional<BankAccount> optionalBankAccount = findById(bankAccountId);
        if (optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            if (bankAccount.getBalance().compareTo(amount) > 0) {
                bankAccount.setBalance(bankAccount.getBalance().subtract(amount));
                update(bankAccount);
                return amount + "$ was successfully withdrawn. Your balance = " + bankAccount.getBalance() + "$";
            } else {
                System.out.println("Insufficient amount. " +
                        "Entered amount = " + amount + " is more, than account balance. " +
                        "Your account balance = " + bankAccount.getBalance());
            }
        } else return "NO such account";
        return "";
    }


    public String depositMoney(Integer bankAccountId, BigDecimal amount) {
        Optional<BankAccount> optionalBankAccount = findById(bankAccountId);
        if (optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            if (bankAccount.getBalance().compareTo(amount) > 0) {
                bankAccount.setBalance(bankAccount.getBalance().add(amount));
                update(bankAccount);
                return amount + "$ was successfully credited. Your balance = " + bankAccount.getBalance() + "$";
            } else {
                System.out.println("Insufficient amount. " +
                        "Entered amount = " + amount + " is more, than allowed");
            }
        } else return "NO such account";
        return "";
    }

    public void transferMoney(Integer fromBankAccountId, Integer toBankAccountId, BigDecimal amount) {
        HikariDataSource hikariDataSource = null;
        Connection connection = null;
        try {
            hikariDataSource = ConnectionPoolConfig.getHikariDataSource();
            connection = hikariDataSource.getConnection();
            connection.setAutoCommit(false);

            Optional<BankAccount> optionalBankAccount1 = findById(fromBankAccountId);
            Optional<BankAccount> optionalBankAccount2 = findById(toBankAccountId);

            if (optionalBankAccount1.isPresent()) {
                BankAccount bankAccount1 = optionalBankAccount1.get();
                if (optionalBankAccount2.isPresent()) {
                    BankAccount bankAccount2 = optionalBankAccount2.get();
                    if (bankAccount1.getBalance().compareTo(amount) < 0) {
                        System.out.println("Insufficient amount. " +
                                "Entered amount " + amount + "$ is more, than account balance. " +
                                "Your account balance = " + bankAccount1.getBalance());
                    } else {
                        bankAccount1.setBalance(bankAccount1.getBalance().subtract(amount));
                        bankAccount2.setBalance(bankAccount2.getBalance().add(amount));
                        update(bankAccount1);
                        update(bankAccount2);
                        System.out.println("Amount " + amount + "$ was successfully transferred. Your balance: " + bankAccount1.getBalance() + "$");
                    }
                } else System.out.println("NO account associated with the credit request was found");
            }else System.out.println("NO account associated with the withdrawal request was found");

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Database error during money transfer", e);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to close connection", e);
                }
            }
            if (hikariDataSource != null) {
                hikariDataSource.close();
            }
        }
    }
}


