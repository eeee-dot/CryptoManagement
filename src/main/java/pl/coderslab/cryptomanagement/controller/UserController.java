package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.cryptomanagement.dto.UserDTO;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.exception.UnmatchedPasswordsException;
import pl.coderslab.cryptomanagement.service.PortfolioService;
import pl.coderslab.cryptomanagement.service.UserService;

import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, PortfolioService portfolioService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PatchMapping("/user")
    public ResponseEntity<User> updateUser() {
        userService.update(1L, new UserDTO());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/add")
    public String addUserView() {
        return "add-user-form";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String repeatedPassword,
                          RedirectAttributes redirectAttributes) {

        if (!Objects.equals(password, repeatedPassword)) {
            throw new UnmatchedPasswordsException();
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);

        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPasswordHash(hashedPassword);

        userService.add(newUser);

        redirectAttributes.addFlashAttribute("message", "Account created successfully.<br>Please log in.");
        return "redirect:/login";
    }
}
