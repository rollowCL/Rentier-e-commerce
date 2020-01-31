package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}