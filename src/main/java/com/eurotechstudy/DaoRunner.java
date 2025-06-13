package com.eurotechstudy;

import com.eurotechstudy.dao.InvoiceDao;
import com.eurotechstudy.entity.Invoices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
        InvoiceDao invoiceDao = InvoiceDao.getINSTANCE();
        Invoices invoices = new Invoices(20,"1111-1111",1, BigDecimal.valueOf(100), BigDecimal.valueOf(50),
                LocalDate.of(2020,1,1),LocalDate.of(2020,2,1),LocalDate.of(2020,3,1));

        //SAVE A ROW
        //invoiceDao.save(invoices);

        //SELECT ALL
        //List<Invoices> invoicesList = invoiceDao.findAll();
        //ivoicesList.forEach(System.out::println);

        //FIND By Id
//        Optional<Invoices> invoiceById = invoiceDao.findById(1);
//        System.out.println(invoiceById);

        //UPDATE
//        Invoices invoiceById = invoiceDao.findById(19).get();
//        invoiceById.setPayment_total(invoiceById.getPayment_total().add(BigDecimal.valueOf(10)));
//        invoiceDao.update(invoiceById);

        //UPDATE
//        Optional<Invoices> invoiceDaoById = invoiceDao.findById(19);
//        invoiceDaoById.ifPresent(entity->{
//            entity.setPayment_total(BigDecimal.valueOf(50));
//            invoiceDao.update(entity);
//        });

        //DELETE A ROW
//        System.out.println(invoiceDao.delete(20));
//
//        //JOIN
//        invoiceDao.joinMethod();

        //Sub_Query
        invoiceDao.subSelectMethod();

    }
}
