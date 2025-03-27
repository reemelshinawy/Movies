package movies.com.example.movies.repositories;


import movies.com.example.movies.Enums.UserRole;
import movies.com.example.movies.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    List<User> findByUserRole(UserRole userRole);


}
