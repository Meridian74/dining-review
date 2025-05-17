package com.example.diningreview.controller;

import com.example.diningreview.model.User;
import com.example.diningreview.model.UserCommand;
import com.example.diningreview.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;


    @GetMapping("/")
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser(@NotNull @Valid @RequestBody UserCommand userCommand) {
        Optional<User> optionalUser = this.userRepository.findByUsername(userCommand.getUsername());
        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This username has been already reserved: " +
                    userCommand.getUsername());
        }

        User newUser = convertUserCommandToUser(userCommand);
        return this.userRepository.save(newUser);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User modifyUser(@NotNull @Valid @RequestBody UserCommand userCommand) {
        Optional<User> optionalUser = tryTofindUserByUserName(userCommand.getUsername());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This username doesn't exist: " +
                    userCommand.getUsername());
        }

        User updatedUser = modifyExistingUser(optionalUser.get(), userCommand);
        return this.userRepository.save(updatedUser);
    }

    @GetMapping()
    public User getUser(@NotNull @PathParam("username") String username) {
        Optional<User> optionalUser = tryTofindUserByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This username doesn't exist: " + username);
        }

        return optionalUser.get();
    }

    // for general background using
    public Optional<User> tryTofindUserByUserName(String username) {
        return this.userRepository.findByUsername(username);
    }

    private User convertUserCommandToUser(UserCommand userCommand) {
        User user = new User();
        user.setUsername(userCommand.getUsername());
        user.setCity(userCommand.getCity());
        user.setZipCode(userCommand.getZipCode());
        user.setState(userCommand.getState());
        user.setInterestedPeanutAllergy(userCommand.getInterestedPeanutAllergy());
        user.setInterestedEggAllergy(userCommand.getInterestedEggAllergy());
        user.setInterestedDairyAllergy(userCommand.getInterestedDairyAllergy());
        return user;
    }

    private User modifyExistingUser(User existingUser, UserCommand userCommand) {
        if (userCommand.getCity() != null) {
            existingUser.setCity(userCommand.getCity());
        }
        if (userCommand.getState() != null) {
            existingUser.setState(userCommand.getState());
        }
        if (userCommand.getZipCode() != null) {
            existingUser.setZipCode(userCommand.getZipCode());
        }
        if (userCommand.getInterestedPeanutAllergy() != null) {
            existingUser.setInterestedPeanutAllergy(userCommand.getInterestedPeanutAllergy());
        }
        if (userCommand.getInterestedEggAllergy() != null) {
            existingUser.setInterestedEggAllergy(userCommand.getInterestedEggAllergy());
        }
        if (userCommand.getInterestedDairyAllergy() != null) {
            existingUser.setInterestedDairyAllergy(userCommand.getInterestedDairyAllergy());
        }
        return existingUser;
    }

}
