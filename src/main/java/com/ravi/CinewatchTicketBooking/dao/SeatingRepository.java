package com.ravi.CinewatchTicketBooking.dao;

import com.ravi.CinewatchTicketBooking.model.Seating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface SeatingRepository extends JpaRepository<Seating,Long> {

    @Query("SELECT u FROM Seating u WHERE u.movieTitle = ?1 and u.bookingDate = ?2 and u.timing = ?3 and u.theatre = ?4")
    Seating findBookedSeats(String movieTitle, Date bookingDate, String timing, String theatre);

//    @Modifying
//    @Query("update Seating u set u.seats = ?1 WHERE u.movieTitle = ?2 and u.bookingDate = ?2 and u.timing = ?4 and u.theatre = ?5")
//    void updateSeats(String[] seats,String movieTitle, Date bookingDate, String timing, String theatre);

}
