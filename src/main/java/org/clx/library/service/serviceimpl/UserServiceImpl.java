package org.clx.library.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.clx.library.config.JwtProvider;
import org.clx.library.model.User;
import org.clx.library.repository.UserRepository;
import org.clx.library.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public User registerUser(User user) {
        User newUser = new User();

        newUser.setId(user.getId());
        newUser.setName(user.getName());
        newUser.setCollegeName(user.getCollegeName());
        newUser.setContactNumber(user.getContactNumber());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setGender(user.getGender());

        User savedUser = userRepository.save(newUser);
        return savedUser;
    }

    @Override
    public User findUserByJwt(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email);
    }
}
