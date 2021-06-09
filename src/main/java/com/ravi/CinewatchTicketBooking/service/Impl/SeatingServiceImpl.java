package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.SeatingRepository;
import com.ravi.CinewatchTicketBooking.model.Seating;
import com.ravi.CinewatchTicketBooking.service.SeatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SeatingServiceImpl implements SeatingService {
    @Autowired
    SeatingRepository seatingRepository;
    @Override
    public Seating findBookedSeats(String movieTitle, Date bookingDate, String time, String theatre) {
        return seatingRepository.findBookedSeats(movieTitle,bookingDate,time,theatre);
    }

    @Override
    public void save(Seating seating) {
        seatingRepository.save(seating);
    }
}
