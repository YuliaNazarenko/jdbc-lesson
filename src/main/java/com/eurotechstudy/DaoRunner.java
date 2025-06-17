package com.eurotechstudy;

import com.eurotechstudy.dao.ClientDao;
import com.eurotechstudy.dao.InvoiceDao;
import com.eurotechstudy.entity.Clients;
import com.eurotechstudy.entity.Invoices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
        InvoiceDao invoiceDao = InvoiceDao.getINSTANCE();
        Invoices invoices = new Invoices(20, "1111-1111", 1, BigDecimal.valueOf(100), BigDecimal.valueOf(50),
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1));

        ClientDao clientDao = ClientDao.getINSTANCE();
        Clients clients = new Clients
                (7, "Victor", "Main Street, 102", "New Castle", "DE", "506-23-25");

        //SAVE A ROW
        //invoiceDao.save(invoices);
        //clientDao.save(clients);

        //SELECT ALL
        //List<Invoices> invoicesList = invoiceDao.findAll();
        //invoicesList.forEach(System.out::println);
//        List<Clients> clientsList = clientDao.findAll();
//        clientsList.forEach(System.out::println);

        //FIND By Id
//        Optional<Invoices> invoiceById = invoiceDao.findById(1);
//        System.out.println(invoiceById);
//        Optional<Clients> clientsDaoById = clientDao.findById(6);
//        System.out.println("clientsDaoById = " + clientsDaoById);

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

//        Optional<Clients> clientsDaoById = clientDao.findById(6);
//        clientsDaoById.ifPresent(entity->{
//            entity.setAddress("Main Street, 12");
//            clientDao.update(entity);
//        });

        //DELETE A ROW
//        System.out.println(invoiceDao.delete(20));
        //System.out.println(clientDao.delete(6));
//
//        //JOIN
//        invoiceDao.joinMethod();

        //Sub_Query
        //invoiceDao.subSelectMethod();
        clientDao.subSelectMethod();

    }
}
