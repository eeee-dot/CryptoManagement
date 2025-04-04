package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.UserDTO;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController extends GenericController<User> {
    private final UserService userService;

    public UserController(UserService userService) {
        super(userService, User.class);
        this.userService = userService;
    }

    @PatchMapping("/user")
    public ResponseEntity<User> updateUser() {
        userService.update(1L, new UserDTO());
        return ResponseEntity.ok().build();
    }


}
