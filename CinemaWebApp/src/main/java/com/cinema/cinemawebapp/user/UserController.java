package com.cinema.cinemawebapp.user;

import com.cinema.cinemawebapp.exceptions.SameUserEmailException;
import com.cinema.cinemawebapp.exceptions.UserNotFoundException;
import com.cinema.cinemawebapp.user.models.LoginModel;
import com.cinema.cinemawebapp.user.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User newUser) throws SameUserEmailException {
        if(checkIfEmailExistsAlready(newUser.getEmail()))
            throw new SameUserEmailException();

        User createdUser = userRepository.save(newUser);

        createdUser.setPassword("");
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) throws UserNotFoundException, SameUserEmailException {
        Optional<User> foundUser = userRepository.findById(updatedUser.getId());

        if(foundUser.isPresent()){
            if(checkIfEmailExistsAlready(updatedUser.getEmail()))
                throw new SameUserEmailException();

            userRepository.save(updatedUser);

            updatedUser.setPassword("");
            return ResponseEntity.ok(updatedUser);
        }else
            throw new UserNotFoundException();
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> logIn(@RequestBody LoginModel loginModel) throws UserNotFoundException {
        String foundPassword = userRepository.findPasswordByEmail(loginModel.getEmail());

        if(foundPassword == null)
            throw new UserNotFoundException();

        return ResponseEntity.ok(loginModel.getPassword().equals(foundPassword));
    }

    private boolean checkIfEmailExistsAlready(String email){
        return userRepository.findAllByEmail(email).size() > 0;
    }
}
