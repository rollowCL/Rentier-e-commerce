package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByOrderTypeId(Long id);
}