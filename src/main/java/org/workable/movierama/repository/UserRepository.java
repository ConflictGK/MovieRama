package org.workable.movierama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workable.movierama.model.User;

/**
 * Spring Data JPA repository interface for {@link User} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations and JPA functionalities for {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} entity based on the username.
     *
     * @param username the username of the user to be found
     * @return the found {@link User} entity
     */
    User findByUsername(String username);
}
