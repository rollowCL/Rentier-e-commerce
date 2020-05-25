package pl.coderslab.rentier.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;

class RegisterServiceImplTest {


    @Test
    void makeUserVerified() {
        Long id = 1L;
        User user = new User();
        assertFalse("Should't be verified", user.isVerified());
        user.setVerified(true);
        assertTrue("Should be verified", user.isVerified());
    }
}
