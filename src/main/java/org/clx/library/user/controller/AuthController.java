package org.clx.library.user.controller;

import lombok.AllArgsConstructor;
import org.clx.library.user.config.JwtProvider;
import org.clx.library.user.model.User;
import org.clx.library.user.repository.UserRepository;
import org.clx.library.user.request.LoginRequest;
import org.clx.library.user.response.AuthResponse;
import org.clx.library.user.service.CustomUserDetailsService;
import org.clx.library.user.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody User user) throws Exception {
        User isExist = userRepository.findByEmail(user.getEmail());
        if (isExist != null){
            throw new Exception("This email is already used with another account");
        }
        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setContactNumber(user.getContactNumber());
        newUser.setCollegeName(user.getCollegeName());
        newUser.setGender(user.getGender());

        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getPassword(), savedUser.getEmail());
        String token = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(token, "Register Success");

        return res;
    }

    @PostMapping("/signin")
    public AuthResponse signin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String token = JwtProvider.generateToken(authentication);

        return new AuthResponse(token, "Login Success");
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username.");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Password not matched.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}