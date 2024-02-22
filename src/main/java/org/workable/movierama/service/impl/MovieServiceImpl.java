package org.workable.movierama.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.workable.movierama.model.Movie;
import org.workable.movierama.repository.MovieRepository;
import org.workable.movierama.service.MovieService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void registerMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public List<Movie> findAll() {
        Movie movie = new Movie();
        movie.setDescription("test movie");
        movie.setTitle("test movie");

        return List.of(movie);
    }
}
