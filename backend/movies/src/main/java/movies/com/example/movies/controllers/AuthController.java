package movies.com.example.movies.controllers;

import lombok.RequiredArgsConstructor;
import movies.com.example.movies.dto.AuthenticationRequest;
import movies.com.example.movies.dto.SignupRequest;
import movies.com.example.movies.dto.UserDTO;
import movies.com.example.movies.entities.User;
import movies.com.example.movies.repositories.UserRepository;
import movies.com.example.movies.services.auth.AuthService;
import movies.com.example.movies.services.jwt.UserService;
import movies.com.example.movies.utils.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    private final AuthService authService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public AuthController(AuthService authService, JWTUtil jwtUtil, UserRepository userRepository, AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private final UserRepository userRepository;



    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (signupRequest == null || signupRequest.getEmail() == null || signupRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid request data\"}");
        }

        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("{\"error\": \"Email already exists\"}");
        }

        UserDTO userDto = authService.signUp(signupRequest);
        if (userDto == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"User registration failed\"}");
        }

        return ResponseEntity.ok().body("{\"message\": \"User registered successfully\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        LOGGER.info("Attempting login for user: " + authenticationRequest.getEmail());

        // Retrieve user from database
        Optional<UserDetails> userOptional = Optional.ofNullable(
                userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail())
        );

        if (userOptional.isEmpty()) {
            LOGGER.warning("User not found: " + authenticationRequest.getEmail());
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "User not found"));
        }

        UserDetails userDetails = userOptional.get();

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            LOGGER.warning("Incorrect password for user: " + authenticationRequest.getEmail());
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Incorrect username or password"));
        }


        // Generate JWT token
        final String jwt = jwtUtil.generateToken((User) userDetails);
        LOGGER.info("User authenticated successfully: " + authenticationRequest.getEmail());

        // Retrieve user role from database
        Optional<User> userEntityOptional = userRepository.findFirstByEmail(authenticationRequest.getEmail());

        if (userEntityOptional.isEmpty()) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "User role retrieval failed"));
        }

        User user = userEntityOptional.get();
        String userRole = user.getUserRole().name();
        String userId = String.valueOf(user.getId()); // Ensure userID is properly formatted

        // Construct a valid JSON response
        Map<String, String> response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("userRole", userRole);
        response.put("userID", userId); // Properly formatted JSON response

        return ResponseEntity.ok(response);
    }



}
