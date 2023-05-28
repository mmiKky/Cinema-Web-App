package com.cinema.cinemawebapp.User;

import com.cinema.cinemawebapp.exceptions.UserNotFoundException;
import com.cinema.cinemawebapp.User.models.LoginModel;
import com.cinema.cinemawebapp.User.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public @ResponseBody HttpStatus addUser(@RequestBody User newUser) {
        userRepository.save(newUser);

        return HttpStatus.OK;
    }

    @GetMapping("/login")
    public boolean logIn(@RequestBody LoginModel loginModel) throws UserNotFoundException {
        String foundPassword = userRepository.findPasswordByEmail(loginModel.getEmail());

        if(foundPassword == null)
            throw new UserNotFoundException();

        return loginModel.getPassword().equals(foundPassword);
    }
}
