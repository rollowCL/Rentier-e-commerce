package pl.coderslab.rentier.service;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.rentier.InMemoryTestConfig;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {InMemoryTestConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class RegisterServiceImplTest {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterServiceImplTest.class);
    User user;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    TokenServiceImpl tokenService;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    RegisterServiceImpl registerService;

    @Before
    public void setUp() {

        user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("Test");
        user.setPassword2("Test");
        user.setEmail("test@o2.pl");
        user.setActive(true);
        user.setVerified(false);
        user.setPhone("123456789");
        user.setRegisterDate(LocalDateTime.now());

    }

    @Test
    public void should_makeUserVerified_ResultOK() {
        String token = registerService.registerUser(user);
        registerService.makeUserVerified(token);
        assertTrue(userRepository.findByUserName(user.getEmail()).isVerified());
    }

    @Test
    @Transactional
    public void should_registerUser_ResultOK() {
        assertNotNull(registerService.registerUser(user));
        User storedUser = userRepository.findByUserName(user.getEmail());
        assertTrue(storedUser.isActive());
        assertFalse(storedUser.isVerified());
    }


}
