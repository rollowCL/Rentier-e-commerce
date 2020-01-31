package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
