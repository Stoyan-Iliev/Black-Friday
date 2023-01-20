package com.store.service;

import com.store.entity.ERole;
import com.store.entity.Role;
import com.store.entity.User;
import com.store.exception.UserAlreadyExistException;
import com.store.payload.request.LoginRequest;
import com.store.payload.request.SignUpRequest;
import com.store.payload.response.JwtResponse;
import com.store.repository.RoleRepository;
import com.store.repository.UserRepository;
import com.store.security.jwt.JwtUtils;
import com.store.security.services.UserDetailsImpl;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private static final ERole DEFAULT_ROLE = ERole.ROLE_CLIENT;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final EmailService emailService;

    @Autowired
    public AuthenticationService(UserRepository repository, PasswordEncoder encoder,
                                 RoleRepository roleRepository, AuthenticationManager authenticationManager,
                                 JwtUtils jwtUtils, EmailService emailService) {
        this.userRepository = repository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    @Transactional
    public void registerNewUserAccount(SignUpRequest signUpRequest, String siteURL) throws MessagingException, UnsupportedEncodingException {
        User user = generateUser(signUpRequest);

        ensureUserNotExist(user);

        Role role = getDefaultRole();
        user.setRoles(new HashSet<>(Collections.singletonList(role)));

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        userRepository.save(user);

        emailService.sendVerificationEmail(user, siteURL);
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(DEFAULT_ROLE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }

    private User generateUser(SignUpRequest signUpRequest) {
        return new User(signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());
    }

    private void ensureUserNotExist(User user) {
        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " +
                    user.getEmail());
        }

        if (usernameExist(user.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: " +
                    user.getUsername());
        }
    }

    private boolean usernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
