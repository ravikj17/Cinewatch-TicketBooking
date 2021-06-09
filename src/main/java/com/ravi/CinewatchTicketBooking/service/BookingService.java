package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking findById(String id);

    List<Booking> findByUserId(String userId);

    void save(Booking booking);

    void delete(Booking booking);
}
