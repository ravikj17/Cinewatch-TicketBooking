package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.Booking;
import com.ravi.CinewatchTicketBooking.model.Seating;

import java.util.Date;

public interface SeatingService {
    Seating findBookedSeats(String movieTitle, Date bookingDate, String time, String theatre);

    void save(Seating seating);

    Seating getSeats(Seating seating, Booking booking, String[] str);
}
