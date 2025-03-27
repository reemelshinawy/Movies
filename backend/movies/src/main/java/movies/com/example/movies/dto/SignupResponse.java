package movies.com.example.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupResponse {
    public SignupResponse(String message, SignupRequest requestBody, UserDTO userData) {
        this.message = message;
        this.requestBody = requestBody;
        this.userData = userData;
    }

    private String message;
    private SignupRequest requestBody;
    private UserDTO userData;
}
