package org.workable.movierama.service;

import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    List<Movie> findAll();

    Optional<Movie> findById(Long movieId);
    void save(Movie movie);

    boolean alreadyLikedByUser(Movie movie, User user);

    boolean alreadyHatedByUser(Movie movie, User user);

    long countOpinions(Movie movie, OpinionType opinionType);
}
