package movies.com.example.movies.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OMDBService {
    private final String API_KEY = "3d10beac";

    public JsonNode searchMovies(String query) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.omdbapi.com/?s=" + query + "&apikey=" + API_KEY;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode fetchMovie(String title) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.omdbapi.com/?t=" + title + "&apikey=" + API_KEY;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode fetchMovieById(String imdbId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.omdbapi.com/?i=" + imdbId + "&apikey=" + API_KEY;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String body = response.getBody();

            // Log for debugging
            System.out.println("OMDB raw response: " + body);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(body);

            // Handle OMDB API error responses
            if (json.has("Error")) {
                System.out.println("OMDB error: " + json.get("Error").asText());
                return null;
            }

            return json;
        } catch (Exception e) {
            System.out.println(" Exception while fetching/parsing OMDB movie by ID: " + e.getMessage());
            return null;
        }
    }


}
