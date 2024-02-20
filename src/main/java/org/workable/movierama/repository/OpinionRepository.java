package org.workable.movierama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workable.movierama.model.Opinion;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
}
