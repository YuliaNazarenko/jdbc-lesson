package com.eurotechstudy.entity;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class BankAccount {
    private Integer account_id;
    private Integer customer_id;
    private String account_type;
    private BigDecimal balance;
    private Timestamp created_at;

    public BankAccount(Integer customer_id, String account_type, BigDecimal balance, Timestamp created_at) {
        this.customer_id = customer_id;
        this.account_type = account_type;
        this.balance = balance;
        this.created_at = created_at;
    }
}
