package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.BookingRepository;
import com.ravi.CinewatchTicketBooking.model.Booking;
import com.ravi.CinewatchTicketBooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Override
    public Booking findById(String id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElse(null);
    }

    @Override
    public List<Booking> findByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }
}
