package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.SeatingRepository;
import com.ravi.CinewatchTicketBooking.model.Booking;
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

    @Override
    public Seating getSeats(Seating seating, Booking booking, String[] str) {
        if (seating!=null) {
            String[] str1 = seating.getSeats();
            String[] str2 = new String[str1.length+str.length];
            System.arraycopy(str, 0, str2, 0, str.length);
            System.arraycopy(str1, 0, str2, str.length, str1.length);
            seating.setSeats(str2);
        }
        else {
            Seating seating1 = new Seating();
            seating1.setMovieTitle(booking.getMovieTitle());
            seating1.setBookingDate(booking.getBookingDate());
            seating1.setTiming(booking.getTime());
            seating1.setTheatre(booking.getTheatre());
            seating1.setSeats(str);
            seating = seating1;
        }
        return seating;
    }
}
