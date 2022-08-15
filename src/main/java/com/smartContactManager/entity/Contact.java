package com.smartContactManager.entity;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cId;

    @NotBlank(message = "First Name can not be null!")
    private String firstName;

    @NotBlank(message = "Last Name can not be null!")
    private String lastName;

    @NotBlank(message = "Profession can not be null!")
    private String profession;

    @NotBlank(message = "Email can not be null!")
    private String email;

    @NotBlank(message = "Phone number can not be null!")
    private String phone;

    private String image;

    @Column(length = 5000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
