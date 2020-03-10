package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.Role;
import pl.coderslab.rentier.repository.RoleRepository;

import java.util.Optional;

public class UserRoleConverter implements Converter<String, Role> {

    @Autowired
    private RoleRepository userRoleRepository;

    @Override
    public Role convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<Role> group = userRoleRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
