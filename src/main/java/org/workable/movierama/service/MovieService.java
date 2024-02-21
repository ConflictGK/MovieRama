package org.workable.movierama.service;

import org.workable.movierama.model.Movie;

import java.util.List;

public interface MovieService {

    void registerMovie(Movie movie);

    List<Movie> findAll();
}
