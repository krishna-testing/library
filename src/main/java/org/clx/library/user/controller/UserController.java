package org.clx.library.user.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.clx.library.user.exception.UserException;
import org.clx.library.user.model.User;
import org.clx.library.user.service.UserService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
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
