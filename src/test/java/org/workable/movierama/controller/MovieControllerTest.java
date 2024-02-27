package org.workable.movierama.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;
import org.workable.movierama.service.MovieService;
import org.workable.movierama.service.OpinionService;
import org.workable.movierama.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

    @MockBean
    private MovieService movieService;

    @MockBean
    private UserService userService;

    @MockBean
    private OpinionService opinionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testListMovies() throws Exception {
        when(movieService.findAll(anyString(), anyInt(), any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("sortBy"));
    }

    @Test
    @WithMockUser(username = "user")
    void testShowRegisterMovieForm() throws Exception {
        mockMvc.perform(get("/movies/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("movie"))
                .andExpect(view().name("registerMovie"));
    }

    @Test
    @WithMockUser(username = "user")
    void testHandleMovieRegistration() throws Exception {
        // given
        Movie movie = new Movie();
        movie.setTitle("New Movie");
        movie.setDescription("Description of new movie");


        // verify
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .sessionAttr("movie", movie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));

        verify(movieService).save(any(Movie.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testHandleVoteWithMockUser() throws Exception {
        Long movieId = 1L;
        String opinion = OpinionType.LIKE.getType();

        Movie movie = new Movie();
        movie.setId(movieId);
        User user = new User();
        user.setUsername("testUser");

        when(movieService.findById(movieId)).thenReturn(Optional.of(movie));
        when(userService.findUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(post("/movies/{movieId}/vote", movieId)
                        .param("opinion", opinion))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));

        verify(opinionService).processOpinion(any(Movie.class), any(User.class), eq(opinion));
    }


}