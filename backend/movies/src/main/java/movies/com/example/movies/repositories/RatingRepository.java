package movies.com.example.movies.repositories;
import movies.com.example.movies.entities.Movie;
import movies.com.example.movies.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovie(Movie movie);
}
