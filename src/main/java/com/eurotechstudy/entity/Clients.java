package com.eurotechstudy.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Clients {
    private Integer client_id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String phone;
}
