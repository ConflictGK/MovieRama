package org.workable.movierama.service;

import org.springframework.data.domain.Page;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;

import java.util.Optional;

public interface MovieService {

    Page<Movie> findAll(String sortBy, int page);

    Optional<Movie> findById(Long movieId);
    void save(Movie movie);

    boolean alreadyLikedByUser(Movie movie, User user);

    boolean alreadyHatedByUser(Movie movie, User user);

    long countOpinions(Movie movie, OpinionType opinionType);
}
