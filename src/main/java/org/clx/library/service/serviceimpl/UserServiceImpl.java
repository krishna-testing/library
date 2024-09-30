package org.clx.library.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.clx.library.config.JwtProvider;
import org.clx.library.model.Staff;
import org.clx.library.model.Student;
import org.clx.library.model.User;
import org.clx.library.repository.UserRepository;
import org.clx.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import SLF4J Logger
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); // Create Logger instance

    private final UserRepository userRepository;

    public User registerUser(User user) {
        logger.info("Registering user: {}", user.getEmail());

        User newUser;

        if (user instanceof Student) {
            newUser = new Student();
            ((Student) newUser).setCourse(((Student) user).getCourse());
            ((Student) newUser).setYear(((Student) user).getYear());
        } else if (user instanceof Staff) {
            newUser = new Staff();
            ((Staff) newUser).setDepartment(((Staff) user).getDepartment());
            ((Staff) newUser).setPosition(((Staff) user).getPosition());
        } else {
            newUser = new User();
        }

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
        logger.info("Finding user by JWT: {}", jwt); // Log the JWT used for finding a user
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            logger.info("User found: {}", user.getEmail()); // Log found user
        } else {
            logger.warn("No user found for email: {}", email); // Log warning if no user found
        }
        return user;
    }
}
