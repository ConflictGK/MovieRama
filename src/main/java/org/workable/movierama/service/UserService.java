package org.workable.movierama.service;

import org.workable.movierama.model.User;

public interface UserService {

    User registerUser(User user);

    User findUserByUsername(String username);
}
