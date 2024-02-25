package org.workable.movierama.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;
import org.workable.movierama.service.MovieService;
import org.workable.movierama.service.OpinionService;
import org.workable.movierama.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;
    private final OpinionService opinionService;

    @GetMapping("/movies")
    public String listMovies(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Movie> movies = movieService.findAll();

        model.addAttribute("movies", movies);
        movies.forEach(movie -> {
            movie.setLikes(movieService.countOpinions(movie, OpinionType.LIKE));
            movie.setHates(movieService.countOpinions(movie, OpinionType.HATE));
        });

        if (userDetails != null) {
            // User is logged in
            User user = userService.findUserByUsername(userDetails.getUsername());
            model.addAttribute("username", user.getUsername());
            movies.forEach(movie -> {
                movie.setAlreadyLiked(movieService.alreadyLikedByUser(movie, user));
                movie.setAlreadyHated(movieService.alreadyHatedByUser(movie, user));
            });
        }
        return "movies";
    }

    @GetMapping("/movies/new")
    public String showRegisterMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "registerMovie";
    }

    @PostMapping("/movies")
    public String handleMovieRegistration(@ModelAttribute("movie") Movie movie,
                                          BindingResult bindingResult,
                                          @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return "registerMovie";
        }

        User user = userService.findUserByUsername(userDetails.getUsername());
        movie.setUser(user);
        movie.setDateAdded(LocalDate.now());

        movieService.save(movie);
        return "redirect:/movies";
    }

    @PostMapping("/movies/{movieId}/vote")
    public String handleVote(@PathVariable Long movieId,
                             @RequestParam("opinion") String opinion,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Movie movie = movieService.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Movie with id = %d does not exist", movieId)));

        User user = userService.findUserByUsername(userDetails.getUsername());
        opinionService.processOpinion(movie, user, opinion);
        return "redirect:/movies";
    }

}
