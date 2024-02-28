package org.workable.movierama.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.enumeration.SortingProperty;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.Opinion;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.MovieRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;
    private User user;

    @BeforeEach
    void setUp() {
        movieService = new MovieServiceImpl(movieRepository);
        ReflectionTestUtils.setField(movieService, "pageSize", 5);
        movie = new Movie();
        user = new User();
    }

    @Test
    void testFindAll_WithPostedBy() {
        when(movieRepository.findByUserUsername(anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(movie)));

        Page<Movie> result = movieService.findAll(SortingProperty.BY_DATE.getSortBy(), 0, "username");

        assertFalse(result.isEmpty());
        verify(movieRepository, times(1)).findByUserUsername("username", PageRequest.of(0, 5));
    }

    @Test
    void testFindAll_WithSortBy() {
        when(movieRepository.findAll()).thenReturn(sampleMoviesWithOpinions());

        Page<Movie> result = movieService.findAll(SortingProperty.BY_LIKES.getSortBy(), 0, null);

        assertFalse(result.isEmpty());
        verify(movieRepository, times(1)).findAll();

        // Movie 3 should come 1st
        assertEquals(3L, result.getContent().get(0).getId());

        // Movie 2 should come 2nd
        assertEquals(2L, result.getContent().get(1).getId());

        // Movie 1 should come 3rd
        assertEquals(1L, result.getContent().get(2).getId());
    }

    @Test
    void testFindAll_ByDate() {
        when(movieRepository.findAll(SortingProperty.BY_DATE.getPageRequest(0, 5)))
                .thenReturn(new PageImpl<>(sampleMoviesWithOpinions()));

        Page<Movie> result = movieService.findAll(SortingProperty.BY_DATE.getSortBy(), 0, null);

        assertFalse(result.isEmpty());
        verify(movieRepository, times(1)).findAll(SortingProperty.BY_DATE.getPageRequest(0, 5));

        assertEquals(3, result.getContent().size());
    }

    @Test
    void testSave() {
        movieService.save(movie);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testFindById_Found() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));

        Optional<Movie> result = movieService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(movie, result.get());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testAlreadyLikedByUser_True() {
        Opinion likeOpinion = new Opinion();
        likeOpinion.setOpinionType(OpinionType.LIKE);
        likeOpinion.setUser(user);
        movie.setOpinions(new HashSet<>(List.of(likeOpinion)));

        assertTrue(movieService.alreadyLikedByUser(movie, user));
    }

    @Test
    void testAlreadyLikedByUser_False() {
        assertFalse(movieService.alreadyLikedByUser(movie, user));
    }

    @Test
    void testAlreadyHatedByUser_True() {
        Opinion likeOpinion = new Opinion();
        likeOpinion.setOpinionType(OpinionType.HATE);
        likeOpinion.setUser(user);
        movie.setOpinions(new HashSet<>(List.of(likeOpinion)));

        assertTrue(movieService.alreadyHatedByUser(movie, user));
    }

    @Test
    void testAlreadyHatedByUser_False() {
        assertFalse(movieService.alreadyLikedByUser(movie, user));
    }

    private List<Movie> sampleMoviesWithOpinions() {
        // Movie 1 with 0 likes/hates
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setOpinions(new HashSet<>()); // No opinions added

        // Movie 2 with 1 like
        Movie movie2 = new Movie();
        movie2.setId(2L);
        Opinion opinion2 = new Opinion();
        opinion2.setOpinionType(OpinionType.LIKE);
        movie2.setOpinions(new HashSet<>(List.of(opinion2)));

        // Movie 3 with 2 likes and 1 hate
        Movie movie3 = new Movie();
        movie3.setId(3L);
        Opinion opinion3Like1 = new Opinion();
        opinion3Like1.setOpinionType(OpinionType.LIKE);
        Opinion opinion3Like2 = new Opinion();
        opinion3Like2.setOpinionType(OpinionType.LIKE);
        Opinion opinion3Hate = new Opinion();
        opinion3Hate.setOpinionType(OpinionType.HATE);
        movie3.setOpinions(new HashSet<>(List.of(opinion3Like1, opinion3Like2, opinion3Hate)));

        return List.of(movie1, movie2, movie3);
    }
}
