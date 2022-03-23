package com.Model;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Size(min=1, max=30)
    private String username;
    @Size(min=6, max=30)
    private String password;
    @Size(min=1, max=30)
    private String firstname;
    @Size(min=1, max=30)
    private String lastname;
    @Email
    private String email;

}
