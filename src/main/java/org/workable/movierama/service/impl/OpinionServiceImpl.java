package org.workable.movierama.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.Opinion;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.OpinionRepository;
import org.workable.movierama.service.OpinionService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpinionServiceImpl implements OpinionService {

    private final OpinionRepository opinionRepository;

    @Transactional
    @Override
    public void processOpinion(Movie movie, User user, String type) {
        Optional<Opinion> userOpinionOptional = movie.getOpinions().stream()
                .filter(o -> o.getUser().equals(user)).findAny();

        if (userOpinionOptional.isEmpty()) {
            createOpinion(movie, user, OpinionType.fromType(type));
            return;
        }

        Opinion userOpinion = userOpinionOptional.get();
        if (userOpinion.getOpinionType().equals(OpinionType.fromType(type))) {
            // Same opinion type, the user wants to retract their opinion
            retractOpinion(userOpinion);
        } else {
            // Different opinion type, the user wants to switch their opinion
            switchOpinion(userOpinion);
        }

    }

    private void createOpinion(Movie movie, User user, OpinionType opinionType) {
        Opinion opinion = Opinion.builder()
                .movie(movie)
                .user(user)
                .opinionType(opinionType)
                .build();

        opinionRepository.save(opinion);
    }

    private void retractOpinion(Opinion opinion) {
        opinionRepository.delete(opinion);
    }

    private void switchOpinion(Opinion opinion) {
        // Switch to opposite opinion
        opinion.setOpinionType(opinion.getOpinionType().equals(OpinionType.LIKE) ?
                OpinionType.HATE : OpinionType.LIKE);

        opinionRepository.save(opinion);
    }
}
