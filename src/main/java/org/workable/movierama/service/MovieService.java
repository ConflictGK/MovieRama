package org.workable.movierama.service;

import org.workable.movierama.model.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> findAll();

    void save(Movie movie);
}
