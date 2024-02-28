package org.workable.movierama.service;

import org.springframework.data.domain.Page;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;

import java.util.Optional;

/**
 * Service interface for managing {@link Movie} entities.
 */
public interface MovieService {

    /**
     * Finds all movies, optionally filtered by the poster's username, and sorted by a specified attribute.
     *
     * @param sortBy   the attribute to sort the movies by
     * @param page     the page number for pagination
     * @param postedBy the username of the user who posted the movies, nullable
     * @return a {@link Page} of {@link Movie} entities
     */
    Page<Movie> findAll(String sortBy, int page, String postedBy);

    /**
     * Finds a movie by its ID.
     * @param movieId the ID of the movie to find
     * @return an {@link Optional} containing the found movie if it exists, or empty otherwise
     */
    Optional<Movie> findById(Long movieId);

    /**
     * Saves a {@link Movie} entity to the database.
     * @param movie the movie to save
     */
    void save(Movie movie);

    /**
     * Checks if a given movie has already been liked by a specific user.
     * @param movie the movie to check
     * @param user the user to check against
     * @return true if the user has already liked the movie, false otherwise
     */
    boolean alreadyLikedByUser(Movie movie, User user);

    /**
     * Checks if a given movie has already been hated by a specific user.
     * @param movie the movie to check
     * @param user the user to check against
     * @return true if the user has already hated the movie, false otherwise
     */
    boolean alreadyHatedByUser(Movie movie, User user);

    /**
     * Counts the number of opinions of a specific type for a given movie.
     * @param movie the movie to count opinions for
     * @param opinionType the type of opinions to count
     * @return the number of opinions of the specified type for the given movie
     */
    long countOpinions(Movie movie, OpinionType opinionType);
}
