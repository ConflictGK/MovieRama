package org.workable.movierama.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.workable.movierama.model.Movie;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, JpaRepository<Movie, Long> {

    Page<Movie> findByUserUsername(String username, PageRequest pageRequest);
}
