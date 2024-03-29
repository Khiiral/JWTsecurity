package securityproject.securityproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import securityproject.securityproject.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    Optional<User> findByFirstName(String firstName);

    Boolean existsByEmail(String email);
}
