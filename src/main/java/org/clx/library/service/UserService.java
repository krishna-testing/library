package org.clx.library.service;

import org.clx.library.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User registerUser(User user);
    public User findUserByJwt(String jwt);


}
