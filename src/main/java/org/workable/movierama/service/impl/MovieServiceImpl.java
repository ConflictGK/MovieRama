package org.workable.movierama.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.MovieRepository;
import org.workable.movierama.service.MovieService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    @Override
    public boolean alreadyLikedByUser(Movie movie, User user) {
        return movie.getOpinions().stream()
                .filter(o -> o.getUser().equals(user))
                .anyMatch(o -> OpinionType.LIKE.equals(o.getOpinionType()));
    }

    @Override
    public boolean alreadyHatedByUser(Movie movie, User user) {
        return movie.getOpinions().stream()
                .filter(o -> o.getUser().equals(user))
                .anyMatch(o -> OpinionType.HATE.equals(o.getOpinionType()));
    }

    @Override
    public long countOpinions(Movie movie, OpinionType opinionType) {
        return movie.getOpinions().stream().filter(o -> opinionType.equals(o.getOpinionType())).count();
    }

}
