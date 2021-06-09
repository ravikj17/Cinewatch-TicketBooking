package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.MovieRepository;
import com.ravi.CinewatchTicketBooking.model.Movie;
import com.ravi.CinewatchTicketBooking.model.Seating;
import com.ravi.CinewatchTicketBooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Override
    public void addMovie(String movieName) throws IOException {
        final String uri = "http://omdbapi.com/?apikey=6180cef1&t=" + movieName;
        Movie movie = new Movie();
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> result = restTemplate.getForObject(uri,HashMap.class);
        assert result != null;
        result.forEach((key, value) -> {
            System.out.println(key);
            System.out.println(value);
            if (key.equals("Title")) {
                movie.setTitle((String) value);
            }
            if (key.equals("Genre")) {
                movie.setGenre((String) value);
            }
            if (key.equals("imdbRating")) {
                movie.setImdbRating((String) value);
            }
            if (key.equals("Plot")) {
                movie.setPlot((String) value);
            }
            if (key.equals("Released")) {
                movie.setReleased((String) value);
            }
            if (key.equals("Runtime")) {
                movie.setRuntime((String) value);
            }
            if (key.equals("Director")) {
                movie.setDirector((String) value);
            }
            if (key.equals("Actors")) {
                movie.setActors((String) value);
            }
            if (key.equals("Poster")) {
                movie.setPoster((String) value);
            }
            if (key.equals("Rated")) {
                movie.setRated((String) value);
            }
            if (key.equals("Language")) {
                movie.setLanguage((String) value);
            }
        });
        movieRepository.save(movie);
        System.out.println(movie);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieByTitle(String title) {
        Optional<Movie> optionalMovie = movieRepository.findById(title);
        return optionalMovie.orElse(null);
    }

    @Override
    public void addSeats(Model model, Seating seating) {
        HashMap<String,Boolean> mapA = new LinkedHashMap<>();
        HashMap<String,Boolean> mapB = new LinkedHashMap<>();
        HashMap<String,Boolean> mapC = new LinkedHashMap<>();
        HashMap<String,Boolean> mapD = new LinkedHashMap<>();
        String[] bookedSeats = null;
        if (seating!=null)
            bookedSeats = seating.getSeats();
        for (int i = 1;i<=6;i++) {
            String key = "A" + i;
            boolean flag = bookedSeats != null && Arrays.asList(bookedSeats).contains(key);
            if (flag)
                mapA.put(key, false);
            else
                mapA.put(key, true);
        }
        for (int i = 1;i<=6;i++) {
            String key = "B" + i;
            boolean flag = bookedSeats != null && Arrays.asList(bookedSeats).contains(key);
            if (flag)
                mapB.put(key, false);
            else
                mapB.put(key, true);
        }
        for (int i = 1;i<=6;i++) {
            String key = "C" + i;
            boolean flag = bookedSeats != null && Arrays.asList(bookedSeats).contains(key);
            if (flag)
                mapC.put(key, false);
            else
                mapC.put(key, true);
        }
        for (int i = 1;i<=6;i++) {
            String key = "D" + i;
            boolean flag = bookedSeats != null && Arrays.asList(bookedSeats).contains(key);
            if (flag)
                mapD.put(key, false);
            else
                mapD.put(key, true);
        }
        model.addAttribute("mapA",mapA);
        model.addAttribute("mapB",mapB);
        model.addAttribute("mapC",mapC);
        model.addAttribute("mapD",mapD);
    }

}
