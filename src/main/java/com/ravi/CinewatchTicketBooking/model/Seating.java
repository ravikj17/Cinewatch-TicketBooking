package com.ravi.CinewatchTicketBooking.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "seating")
public class Seating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seating_Id;

    @Column(name = "seats")
    String[] seats;

    String movieTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    String timing;

    String theatre;

}
