package pl.coderslab.rentier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.rentier.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
