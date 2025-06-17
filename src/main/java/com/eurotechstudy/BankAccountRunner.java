package com.eurotechstudy;

import com.eurotechstudy.dao.BankAccountDao;
import com.eurotechstudy.entity.BankAccount;

import java.math.BigDecimal;

public class BankAccountRunner {
    public static void main(String[] args) {


        BankAccountDao bankAccountDao = BankAccountDao.getINSTANCE();

//        BankAccount bankAccount = bankAccountDao.findById(2).get();
//
//        bankAccount.setBalance(BigDecimal.valueOf(2000));
//        bankAccountDao.update(bankAccount);

        //String result =bankAccountDao.withdrawalMoney(1, BigDecimal.valueOf(50));
        //String result =bankAccountDao.depositMoney(1, BigDecimal.valueOf(10));
        //System.out.println(result);

        bankAccountDao.transferMoney(2, 1, BigDecimal.valueOf(500));

    }
}
