package com.ravi.CinewatchTicketBooking.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "booking")
public class Booking {

    @Id
    String bookingId;
    String movieTitle;
    String rated;
    String runTime;
    String transactionAmount;
    int seats;
    String[] seatNumber;
    String time;
    String theatre;
    String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

}
