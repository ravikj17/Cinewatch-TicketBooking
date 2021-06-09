package com.ravi.CinewatchTicketBooking.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "movies")
public class Movie {

    @Id
    String Title;
    String Genre;
    String imdbRating;
    String Plot;
    String Released;
    String Runtime;
    String Director;
    String Actors;
    String Poster;
    String Rated;
    String Language;

}
