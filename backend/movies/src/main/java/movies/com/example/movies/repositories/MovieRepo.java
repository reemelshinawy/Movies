package movies.com.example.movies.repositories;
import movies.com.example.movies.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepo extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Optional<Movie> findByImdbId(String imdbId);


}

