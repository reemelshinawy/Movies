package movies.com.example.movies.controllers;


import movies.com.example.movies.dto.MovieDTO;
import movies.com.example.movies.entities.Movie;
import movies.com.example.movies.entities.Rating;
import movies.com.example.movies.entities.User;
import movies.com.example.movies.repositories.MovieRepo;
import movies.com.example.movies.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private MovieRepo movieRepository;

    @Autowired
    private RatingService ratingService;



    @GetMapping("/movies")
    public ResponseEntity<List<MovieDTO>> getAllMoviesForUser(@AuthenticationPrincipal User user) {
        List<Movie> movies = movieRepository.findAll();

        List<MovieDTO> dtos = movies.stream().map(movie -> {
            double average = movie.getRatings().stream()
                    .mapToDouble(Rating::getRating)
                    .average().orElse(0.0);

            Double userRating = movie.getRatings().stream()
                    .filter(r -> r.getUser() != null && r.getUser().getId().equals(user.getId()))
                    .map(Rating::getRating)
                    .findFirst()
                    .orElse(null);


            return new MovieDTO(movie, average, userRating);
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String title) {
        System.out.println("Searching for: " + title);
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
        System.out.println(" Found: " + movies.size());
        return ResponseEntity.ok(movies);
    }



    @PostMapping("/movies/{movieId}/rate")
    public ResponseEntity<Rating> rateMovie(@PathVariable Long movieId, @RequestParam double rating) {
        return ResponseEntity.ok(ratingService.rateMovie(movieId, rating));
    }


}
