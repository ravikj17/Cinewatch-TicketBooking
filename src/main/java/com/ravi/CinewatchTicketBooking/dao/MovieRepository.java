package com.ravi.CinewatchTicketBooking.dao;

import com.ravi.CinewatchTicketBooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}
