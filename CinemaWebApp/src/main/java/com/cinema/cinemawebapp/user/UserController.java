package com.cinema.cinemawebapp.user;

import com.cinema.cinemawebapp.exceptions.SameUserEmailException;
import com.cinema.cinemawebapp.exceptions.UserNotFoundException;
import com.cinema.cinemawebapp.user.models.LoginModel;
import com.cinema.cinemawebapp.user.models.SSOModel;
import com.cinema.cinemawebapp.user.models.User;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
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

        createdUser.setPassword(newUser.getPassword());
        createdUser.setName(newUser.getName());
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

    @PostMapping("/login")
    public ResponseEntity<User> logIn(@RequestBody LoginModel loginModel) throws UserNotFoundException {
        Optional<User> foundUser = userRepository.findById(userRepository.findAllByEmail(loginModel.getEmail()).get(0));

        if(foundUser.isEmpty())
            throw new UserNotFoundException();

        if(!loginModel.getPassword().equals(foundUser.get().getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(foundUser.get());
    }

    @PostMapping( "/login/google")
    public ResponseEntity<User> logInSSO(@RequestBody SSOModel ssoModel) throws UserNotFoundException, GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("735910041717-ji8oqfak3s04iu9j5sh9it61g5ene86d.apps.googleusercontent.com"))
                .build();


        GoogleIdToken idToken = verifier.verify(ssoModel.getCredential());
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String familyName = (String) payload.get("family_name");

            System.out.println(payload);

            if(userRepository.findAllByEmail(email).isEmpty()){
                var newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setSurname(familyName);
                newUser.setPassword("");
                userRepository.save(newUser);
                return ResponseEntity.ok(newUser);
            }
            User foundUser = userRepository.findById(userRepository.findAllByEmail(email).get(0)).orElseThrow(UserNotFoundException::new);
            return ResponseEntity.ok(foundUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean checkIfEmailExistsAlready(String email){
        return userRepository.findAllByEmail(email).size() > 0;
    }
}
