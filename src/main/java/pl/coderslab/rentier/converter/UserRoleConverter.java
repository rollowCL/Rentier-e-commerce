package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.UserRoleRepository;

import java.util.Optional;

public class UserRoleConverter implements Converter<String, UserRole> {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<UserRole> group = userRoleRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
