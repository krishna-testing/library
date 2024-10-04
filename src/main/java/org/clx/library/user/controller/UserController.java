package org.clx.library.user.controller;

import lombok.RequiredArgsConstructor;
import org.clx.library.user.exception.UserException;
import org.clx.library.user.model.User;
import org.clx.library.user.repository.UserRepository;
import org.clx.library.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/api/users")
    public List<User> getUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @PutMapping("/api/users")
    public User updateUser(@RequestHeader("Authorization") String jwt, @RequestBody User user) throws UserException {
        User reqUser = userService.findUserByJwt(jwt);
        User updatedUser = userService.updateUser(user, reqUser.getId());
        return updatedUser;
    }
    @GetMapping("/api/users/{userid}")
    public User getUserById(@PathVariable("userid") Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        return user;
    }

}
