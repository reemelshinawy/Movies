package movies.com.example.movies.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import movies.com.example.movies.entities.Movie;
import movies.com.example.movies.repositories.MovieRepo;
import movies.com.example.movies.services.OMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("*")

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private MovieRepo movieRepository;

    @Autowired
    private OMDBService omdbService;

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCombined(@RequestParam String title) {
        List<Movie> dbMovies = movieRepository.findByTitleContainingIgnoreCase(title);
        JsonNode apiResults = omdbService.searchMovies(title);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ArrayNode mergedResults = mapper.createArrayNode();

        Set<String> dbImdbIds = dbMovies.stream()
                .map(Movie::getImdbId)
                .collect(Collectors.toSet());

        if (apiResults != null && apiResults.has("Search")) {
            for (JsonNode movieNode : apiResults.get("Search")) {
                ObjectNode movieObj = movieNode.deepCopy();
                String imdbID = movieObj.path("imdbID").asText();
                boolean existsInDb = dbImdbIds.contains(imdbID);
                movieObj.put("existsInDb", existsInDb);
                mergedResults.add(movieObj);
            }
        }

        result.set("results", mergedResults);
        return ResponseEntity.ok(result);
    }


//    @PostMapping("/movies")
//    public ResponseEntity<Object> addMovie(@RequestParam String title) {
//        JsonNode movieData = omdbService.fetchMovie(title);
//        if (movieData == null || movieData.has("Error")) {
//            return ResponseEntity.badRequest()
//                    .body("Movie not found in OMDB database or an error occurred.");
//        }
//
//        String imdbId = movieData.path("imdbID").asText();
//
//        // ✅ Check by imdbID to allow different versions of same title
//        if (movieRepository.findByImdbId(imdbId).isPresent()) {
//            return ResponseEntity.badRequest()
//                    .body("🚫 Movie already exists in the database.");
//        }
//
//        Movie movie = new Movie();
//        movie.setTitle(movieData.path("Title").asText());
//        movie.setImdbId(imdbId);
//        movie.setYear(movieData.path("Year").asText());
//        movie.setPoster(movieData.path("Poster").asText());
//
//        movie.setRated(movieData.path("Rated").asText());
//        movie.setReleased(movieData.path("Released").asText());
//        movie.setRuntime(movieData.path("Runtime").asText());
//        movie.setGenre(movieData.path("Genre").asText());
//        movie.setDirector(movieData.path("Director").asText());
//        movie.setWriter(movieData.path("Writer").asText());
//        movie.setActors(movieData.path("Actors").asText());
//        movie.setPlot(movieData.path("Plot").asText());
//        movie.setLanguage(movieData.path("Language").asText());
//        movie.setCountry(movieData.path("Country").asText());
//        movie.setAwards(movieData.path("Awards").asText());
//        movie.setMetascore(movieData.path("Metascore").asText());
//        movie.setImdbRating(movieData.path("imdbRating").asText());
//        movie.setImdbVotes(movieData.path("imdbVotes").asText());
//        movie.setType(movieData.path("Type").asText());
//        movie.setDvd(movieData.path("DVD").asText());
//        movie.setBoxOffice(movieData.path("BoxOffice").asText());
//        movie.setProduction(movieData.path("Production").asText());
//        movie.setWebsite(movieData.path("Website").asText());
//
//        movie.setRatings(new ArrayList<>());
//
//        return ResponseEntity.ok(movieRepository.save(movie));
//    }


    @PostMapping("/movies")
    public ResponseEntity<Object> addMovie(@RequestParam String imdbID) {
        JsonNode movieData = omdbService.fetchMovieById(imdbID);
        if (movieData == null || movieData.has("Error")) {
            return ResponseEntity.badRequest().body("Movie not found or an error occurred.");
        }

        if (movieRepository.findByImdbId(imdbID).isPresent()) {
            return ResponseEntity.badRequest().body("Movie already exists in the database.");
        }

        Movie movie = new Movie();
        movie.setTitle(movieData.path("Title").asText());
        movie.setImdbId(imdbID);
        movie.setYear(movieData.path("Year").asText());
        movie.setPoster(movieData.path("Poster").asText());
        movie.setRated(movieData.path("Rated").asText());
        movie.setReleased(movieData.path("Released").asText());
        movie.setRuntime(movieData.path("Runtime").asText());
        movie.setGenre(movieData.path("Genre").asText());
        movie.setDirector(movieData.path("Director").asText());
        movie.setWriter(movieData.path("Writer").asText());
        movie.setActors(movieData.path("Actors").asText());
        movie.setPlot(movieData.path("Plot").asText());
        movie.setLanguage(movieData.path("Language").asText());
        movie.setCountry(movieData.path("Country").asText());
        movie.setAwards(movieData.path("Awards").asText());
        movie.setMetascore(movieData.path("Metascore").asText());
        movie.setImdbRating(movieData.path("imdbRating").asText());
        movie.setImdbVotes(movieData.path("imdbVotes").asText());
        movie.setType(movieData.path("Type").asText());
        movie.setDvd(movieData.path("DVD").asText());
        movie.setBoxOffice(movieData.path("BoxOffice").asText());
        movie.setProduction(movieData.path("Production").asText());
        movie.setWebsite(movieData.path("Website").asText());

        movie.setRatings(new ArrayList<>());
        return ResponseEntity.ok(movieRepository.save(movie));
    }



    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> removeMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAddedMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }



}
