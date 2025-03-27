package movies.com.example.movies.dto;

import lombok.*;
import movies.com.example.movies.Enums.UserRole;

@Data

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationResponse {

    private String jwt;
    private long userId;
    private UserRole userRole;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    public AuthenticationResponse(String jwt, UserRole userRole) {
        this.jwt = jwt;
//        this.userId = userId;
        this.userRole = userRole;
    }
}
