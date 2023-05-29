package com.cinema.cinemawebapp.user;

import com.cinema.cinemawebapp.exceptions.UserNotFoundException;
import com.cinema.cinemawebapp.user.models.LoginModel;
import com.cinema.cinemawebapp.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public @ResponseBody HttpStatus addUser(@RequestBody User newUser) {
        userRepository.save(newUser);

        return HttpStatus.OK;
    }

    @GetMapping("/login")
    public @ResponseBody boolean logIn(@RequestBody LoginModel loginModel) throws UserNotFoundException {
        String foundPassword = userRepository.findPasswordByEmail(loginModel.getEmail());

        if(foundPassword == null)
            throw new UserNotFoundException();

        return loginModel.getPassword().equals(foundPassword);
    }
}
