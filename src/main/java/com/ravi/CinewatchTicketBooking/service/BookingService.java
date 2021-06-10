package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.Booking;
import org.springframework.ui.Model;

import java.util.List;

public interface BookingService {

    Booking findById(String id);

    List<Booking> findByUserId(String userId);

    void save(Booking booking);

    void delete(Booking booking);

    int getAmount(String[] str);

    void cancelBooking(String bookingId, Model model);
}
