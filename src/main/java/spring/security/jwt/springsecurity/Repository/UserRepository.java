package spring.security.jwt.springsecurity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.jwt.springsecurity.Model.OurUsers;

import java.util.Optional;

public interface UserRepository extends JpaRepository<OurUsers, Integer> {
    Optional<OurUsers> findByEmail(String email);
}
