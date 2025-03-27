package movies.com.example.movies.services.auth;


import movies.com.example.movies.dto.SignupRequest;
import movies.com.example.movies.dto.UserDTO;

public interface AuthService {




    UserDTO signUp(SignupRequest signUpRequest);

    Boolean hasUserWithEmail(String email);
}
