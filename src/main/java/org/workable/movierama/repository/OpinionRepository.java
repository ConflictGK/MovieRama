package org.workable.movierama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workable.movierama.model.Opinion;

/**
 * Spring Data JPA repository interface for {@link Opinion} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations and JPA functionalities for {@link Opinion} entities.
 */
public interface OpinionRepository extends JpaRepository<Opinion, Long> {
}
