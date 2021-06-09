package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.Movie;
import com.ravi.CinewatchTicketBooking.model.Seating;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    void addMovie(String movieName) throws IOException;

    List<Movie> getAllMovies();

    Movie getMovieByTitle(String title);

    void addSeats(Model model, Seating seating);
}
