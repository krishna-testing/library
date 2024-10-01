package org.clx.library.user.service;

import org.clx.library.user.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User registerUser(User user);
    public User findUserByJwt(String jwt);


}
