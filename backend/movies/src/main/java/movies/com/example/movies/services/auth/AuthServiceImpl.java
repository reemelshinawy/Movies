package movies.com.example.movies.services.auth;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import movies.com.example.movies.Enums.UserRole;
import movies.com.example.movies.dto.SignupRequest;
import movies.com.example.movies.dto.UserDTO;
import movies.com.example.movies.entities.Movie;
import movies.com.example.movies.entities.User;
import movies.com.example.movies.repositories.MovieRepo;
import movies.com.example.movies.repositories.UserRepository;
import movies.com.example.movies.services.OMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private MovieRepo movieRepo;

    @Autowired
    private OMDBService omdbService;

    @PostConstruct
    private void createAdminAccount() {
        List<User> optionalAdmin = userRepository.findByUserRole(UserRole.ADMIN);

        if (optionalAdmin.isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@test.com");
            admin.setUserRole(UserRole.ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode("password"));
            userRepository.save(admin);
            System.out.println("Admin account created successfully");

        }
    }
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDTO signUp(SignupRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String name = signUpRequest.getName();
        String password = signUpRequest.getPassword();

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setUserRole(UserRole.CUSTOMER);

        return userRepository.save(user).toUserDTO();



    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    public Movie addMovie(String title) {
        JsonNode movieData = omdbService.fetchMovie(title); // Use instance method

        if (movieData == null || movieData.has("Error")) {
            throw new RuntimeException("Movie not found in OMDB API");
        }

        Movie movie = new Movie();
        movie.setTitle(movieData.get("Title").asText());
        movie.setImdbId(movieData.get("imdbID").asText());
        movie.setYear(movieData.get("Year").asText());
        movie.setPoster(movieData.get("Poster").asText());

        return movieRepo.save(movie);
    }

    public void removeMovie(Long movieId) {
        movieRepo.deleteById(movieId);
    }
}
