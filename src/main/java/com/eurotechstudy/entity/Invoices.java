package com.eurotechstudy.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Invoices {

    private Integer invoice_id;
    private String number;
    private Integer client_id;
    private BigDecimal invoice_total;
    private BigDecimal payment_total;
    private LocalDate invoice_date;
    private LocalDate due_date;
    private LocalDate payment_date;


}
