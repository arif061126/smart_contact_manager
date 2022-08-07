package com.smartContactManager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cId;
    private String firstName;
    private String lastName;
    private String profession;
    private String email;
    private String phone;
    private String image;

    @Column(length = 5000)
    private String description;

    @ManyToOne
    private User user;
}
