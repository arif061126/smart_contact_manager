package com.smartContactManager.entity;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "user name can not be empty!")
    @Size(min=3, max = 20, message = "user name between 3 to 20 characters!!")
    private String name;

    //@Column(unique = true)
    @NotBlank(message = "user email can not be empty!")
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    //@NotBlank(message = "password can not be empty!!")
    private String password;
    private String role;
    private boolean enabled;
    private String imageUrl;

    @Column(length = 1000)
    private String about;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Contact> contacts = new ArrayList<>();
}
