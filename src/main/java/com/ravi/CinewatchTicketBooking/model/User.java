package com.ravi.CinewatchTicketBooking.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Column(name="role", nullable = false,length = 20)
    private String role;


}
