package org.clx.library.user.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.clx.library.user.config.JwtProvider;
import org.clx.library.user.model.User;
import org.clx.library.user.repository.UserRepository;
import org.clx.library.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); // Create Logger instance

    private final UserRepository userRepository;

    public User registerUser(User user) {
        logger.info("Registering user: {}", user.getEmail());

        User newUser = new User();

        newUser.setId(user.getId());
        newUser.setName(user.getName());
        newUser.setCollegeName(user.getCollegeName());
        newUser.setContactNumber(user.getContactNumber());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setGender(user.getGender());

        logger.info("Saving user with email: {}", newUser.getEmail());
        User savedUser = userRepository.save(newUser);
        logger.info("User registered successfully: {}", savedUser.getEmail());

        return savedUser;
    }


    @Override
    public User findUserByJwt(String jwt) {
        logger.info("Finding user by JWT: {}", jwt);
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            logger.info("User found: {}", user.getEmail());
        } else {
            logger.warn("No user found for email: {}", email);
        }
        return user;
    }
}
