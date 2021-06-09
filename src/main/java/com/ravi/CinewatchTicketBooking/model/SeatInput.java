package com.ravi.CinewatchTicketBooking.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatInput {
    String seats;
    Long bookingId;
}
