package org.workable.movierama.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;
    private final OpinionService opinionService;

    /**
     * Lists movies sorted by a specified attribute and paginates them. Also, enriches movies with like/hate counts and user-specific interactions if the user is logged in.
     *
     * @param model       the Spring MVC model to pass data to the view
     * @param sortBy      the attribute to sort the movies by, defaults to "date"
     * @param page        the page number for pagination, defaults to 0
     * @param postedBy    the username of the user who posted the movies, nullable
     * @param userDetails the Spring Security UserDetails of the currently authenticated user, nullable
     * @return the name of the view to render, in this case "movies"
     */
    @GetMapping("/movies")
    public String listMovies(Model model,
                             @RequestParam(defaultValue = "date") String sortBy,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(required = false) String postedBy,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Page<Movie> moviePage = movieService.findAll(sortBy, page, postedBy);

        model.addAttribute("movies", moviePage);
        model.addAttribute("page", moviePage);
        model.addAttribute("sortBy", sortBy);
        moviePage.forEach(movie -> {
            movie.setLikes(movieService.countOpinions(movie, OpinionType.LIKE));
            movie.setHates(movieService.countOpinions(movie, OpinionType.HATE));
        });

        if (userDetails != null) {
            // User is logged in
            User user = userService.findUserByUsername(userDetails.getUsername());
            model.addAttribute("username", user.getUsername());
            moviePage.forEach(movie -> {
                movie.setAlreadyLiked(movieService.alreadyLikedByUser(movie, user));
                movie.setAlreadyHated(movieService.alreadyHatedByUser(movie, user));
            });
        }
        return "movies";
    }

    /**
     * Shows the form for registering a new movie.
     * @param model the Spring MVC model to pass data to the view
     * @return the name of the view to render, in this case "registerMovie"
     */
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

    /**
     * Processes a vote (like or hate) on a movie by the currently authenticated user.
     * @param movieId the ID of the movie to vote on
     * @param opinion the opinion of the vote, expected to be either "like" or "hate"
     * @param userDetails the Spring Security UserDetails of the currently authenticated user
     * @return a redirect string to the movies listing page
     */
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
