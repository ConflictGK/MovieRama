package org.workable.movierama.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.workable.movierama.model.User;
import org.workable.movierama.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Displays the user registration form.
     *
     * @param model the Spring MVC model to pass data to the view
     * @return the name of the view to render, in this case "signup"
     */
    @GetMapping("/signup")
    public String showRegisterUserForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Handles the registration of a new user account. If validation fails, it re-displays the registration form.
     * @param user the user object bound to the form submission, marked as valid according to the constraints
     * @param result contains the outcome of the binding and validation process
     * @return a redirect string to the login page if successful, or the registration form view name in case of validation errors
     */
    @PostMapping("/signup")
    public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }

    /**
     * Displays the login form.
     * @return the name of the view to render, in this case "login"
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
