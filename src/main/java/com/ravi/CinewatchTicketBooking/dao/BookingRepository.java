package com.ravi.CinewatchTicketBooking.dao;

import com.ravi.CinewatchTicketBooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,String> {

    @Query("SELECT u FROM Booking u WHERE u.userId = ?1")
    List<Booking> findByUserId(String userId);

}
