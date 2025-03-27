package movies.com.example.movies.services;

import movies.com.example.movies.entities.Movie;
import movies.com.example.movies.entities.Rating;
import movies.com.example.movies.entities.User;
import movies.com.example.movies.repositories.MovieRepo;
import movies.com.example.movies.repositories.RatingRepository;
import movies.com.example.movies.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private UserRepository userRepository;

    public Rating rateMovie(Long movieId, double ratingValue) {
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Get the currently authenticated user
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findFirstByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating = new Rating();
        rating.setMovie(movie);
        rating.setUser(user);
        rating.setRating(ratingValue);

        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForMovie(Long movieId) {
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return ratingRepository.findByMovie(movie);
    }
}
