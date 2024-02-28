package org.workable.movierama.service;

import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;

/**
 * Service interface for managing opinions on {@link Movie} entities.
 */
public interface OpinionService {

    /**
     * Processes an opinion made by a user on a movie.
     *
     * @param movie the movie on which the opinion is made
     * @param user  the user making the opinion
     * @param type  the type of opinion (e.g., "like" or "hate")
     */
    void processOpinion(Movie movie, User user, String type);

}
