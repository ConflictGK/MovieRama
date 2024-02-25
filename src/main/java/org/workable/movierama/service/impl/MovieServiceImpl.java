package org.workable.movierama.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.enumeration.SortingProperty;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.MovieRepository;
import org.workable.movierama.service.MovieService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Value("${movierama.pageSize}")
    private Integer pageSize;

    @Override
    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public Page<Movie> findAll(String sortBy, int page, String postedBy) {
        if (postedBy != null) {
            return movieRepository.findByUserUsername(postedBy, PageRequest.of(page, pageSize));
        }

        SortingProperty sortingProperty = SortingProperty.of(sortBy);
        if (sortingProperty.getHasOpinion()) {
            List<Movie> unsortedMovies = movieRepository.findAll();
            List<Movie> sortedMovies = unsortedMovies.stream()
                    .sorted(Comparator.comparingLong(m -> countOpinions(m, sortingProperty.getOpinionType())))
                    .collect(Collectors.toList());

            // Reversing sort order from asc to desc
            Collections.reverse(sortedMovies);
            return listToPage(sortedMovies, page);
        }

        return movieRepository.findAll(sortingProperty.getPageRequest(page, pageSize));
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

    private Page<Movie> listToPage(List<Movie> movies, int page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), movies.size());
        List<Movie> moviesSubList = movies.subList(start, end);
        return new PageImpl<>(moviesSubList, pageRequest, movies.size());
    }

}
