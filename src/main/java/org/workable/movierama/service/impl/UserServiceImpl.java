package org.workable.movierama.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.UserRepository;
import org.workable.movierama.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        // TODO: Implement registration logic, e.g., encoding passwords, validating user details
        return userRepository.save(user);
    }
}
