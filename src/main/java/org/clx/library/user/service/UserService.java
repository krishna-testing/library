package org.clx.library.user.service;

import org.clx.library.user.exception.UserException;
import org.clx.library.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    public User registerUser(User user, Integer id);
    public User findUserByJwt(String jwt);
    public User findUserById(Integer userId) throws UserException;
    public User updateUser(User user, Integer userId) throws UserException;
    public List<User> searchUser(String query);




}
