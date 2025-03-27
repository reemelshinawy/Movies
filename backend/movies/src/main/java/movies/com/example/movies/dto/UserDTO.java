package movies.com.example.movies.dto;

import lombok.*;
import movies.com.example.movies.Enums.UserRole;

@Getter
@Setter  // ✅ Ensures setter methods are generated
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    private String email;
    private UserRole userRole;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
