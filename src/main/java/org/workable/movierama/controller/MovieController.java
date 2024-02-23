package org.workable.movierama.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;
import org.workable.movierama.service.MovieService;
import org.workable.movierama.service.UserService;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;

    @GetMapping("/movies")
    public String listMovies(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("movies", movieService.findAll());
        if (currentUser != null) {
            model.addAttribute("username", currentUser.getUsername());
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
}
