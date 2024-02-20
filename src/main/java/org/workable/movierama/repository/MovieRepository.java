package org.workable.movierama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workable.movierama.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
