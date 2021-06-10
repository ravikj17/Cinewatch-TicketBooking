package com.ravi.CinewatchTicketBooking.controller;

import com.ravi.CinewatchTicketBooking.model.Movie;
import com.ravi.CinewatchTicketBooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/")
    public String me() {
        return "admin";
    }

    @Autowired
    MovieService movieService;

    @GetMapping("/addMoviePage")
    public String addMoviePage() {
        return "add_movie";
    }

    @GetMapping("/listMoviePage")
    public String listMoviePage(Model model) {
        List<Movie> movies;
        movies = movieService.getAllMovies();
        model.addAttribute("movies",movies);
        return "list_all_movies";
    }

    @PostMapping("/addMovie")
    String addMovie(@RequestParam String movieName) throws IOException {
        movieService.addMovie(movieName);
        return "admin";
    }

}
