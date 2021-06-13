package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.Booking;
import com.ravi.CinewatchTicketBooking.model.Movie;
import com.ravi.CinewatchTicketBooking.model.Seating;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    void addMovie(String movieName) throws IOException;

    List<Movie> getAllMovies();

    Movie getMovieByTitle(String title);

    void addSeats(Model model, Seating seating);

    ModelAndView bookingFirstPage(boolean flag, StringBuilder booked, Booking booking);

    void deleteMovie(String movieName);
}
