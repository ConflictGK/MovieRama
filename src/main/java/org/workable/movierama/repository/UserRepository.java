package org.workable.movierama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workable.movierama.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
