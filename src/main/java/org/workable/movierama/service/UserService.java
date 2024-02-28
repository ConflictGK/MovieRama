package org.workable.movierama.service;

import org.workable.movierama.model.User;

/**
 * Service interface for managing {@link User} entities.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param user the {@link User} entity to be registered
     * @return the registered {@link User} entity
     */
    User registerUser(User user);

    /**
     * Finds a user by their username.
     * @param username the username of the user to find
     * @return the found {@link User} entity
     */
    User findUserByUsername(String username);
}
