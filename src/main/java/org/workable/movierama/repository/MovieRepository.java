package org.workable.movierama.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.workable.movierama.model.Movie;

/**
 * Spring Data repository interface for {@link Movie} entities.
 * It extends {@link PagingAndSortingRepository} and {@link JpaRepository} to provide basic CRUD operations and additional JPA functionalities for {@link Movie} entities.
 */
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, JpaRepository<Movie, Long> {

    /**
     * Finds a page of {@link Movie} entities posted by a specific user, identified by username.
     *
     * @param username    the username of the user whose movies are to be found
     * @param pageRequest the {@link PageRequest} object encapsulating pagination and sorting information
     * @return a {@link Page} of {@link Movie} entities
     */
    Page<Movie> findByUserUsername(String username, PageRequest pageRequest);
}
