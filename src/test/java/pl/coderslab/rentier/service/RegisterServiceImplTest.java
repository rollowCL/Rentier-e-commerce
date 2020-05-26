package pl.coderslab.rentier.service;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.rentier.InMemoryTestConfig;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;


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


    @Test
    public void should_makeUserVerified_ResultOK() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("Test");
        user.setPassword2("Test");
        user.setEmail("test@o2.pl");
        user.setActive(true);
        user.setVerified(false);
        user.setPhone("123456789");
        user.setRegisterDate(LocalDateTime.now());
        String token = registerService.registerUser(user);
        registerService.makeUserVerified(token);
        assertTrue(userRepository.findByUserName(user.getEmail()).isVerified());
    }

    @Test
    public void should_registerUser_ResultOK() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("Test");
        user.setPassword2("Test");
        user.setEmail("test2@o2.pl");
        user.setActive(true);
        user.setVerified(false);
        user.setPhone("123456789");
        user.setRegisterDate(LocalDateTime.now());
        assertNotNull(registerService.registerUser(user));
        User storedUser = userRepository.findByUserName(user.getEmail());
        assertTrue(storedUser.isActive());
        assertFalse(storedUser.isVerified());
    }

}
