package com.ravi.CinewatchTicketBooking.controller;

import com.google.common.collect.Lists;
import com.ravi.CinewatchTicketBooking.model.Movie;
import com.ravi.CinewatchTicketBooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/allMovies")
    public String listMoviePage(Model model) {

        List<List<Movie>> movieLists = Lists.partition(movieService.getAllMovies(),5);
        model.addAttribute("movieLists",movieLists);
        return "all_movies";
    }

}
