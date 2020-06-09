package pl.coderslab.rentier.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import pl.coderslab.rentier.InMemoryTestConfig;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {InMemoryTestConfig.class},
        loader = AnnotationConfigContextLoader.class
)
public class TokenServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(TokenServiceImplTest.class);
    User user;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenServiceImpl tokenService;

    @Value("${rentier.tokenTypePasswordReset}")
    private int tokenTypePasswordReset;

    @Value("${rentier.tokenTypeActivation}")
    private int tokenTypeActivation;

    @BeforeEach
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
        userRepository.save(user);
    }

    @Test
    public void should_generateToken_Lenght0() {
        int len = 0;

        assertEquals(0, tokenService.generateToken(len).length());
    }

    @Test
    public void should_generateToken_ThrowException_Negative() {
        int len = -1;

        assertThrows(NegativeArraySizeException.class, () -> tokenService.generateToken(len));
    }

    @Test
    public void should_generateToken_LenthtPositive() {
        int len = 10;

        assertEquals(10, tokenService.generateToken(len).length());
    }

    @Test
    public void invalidateAllUserResetTokensTest() {

        Token token1 = new Token();
        token1.setCreateDate(LocalDateTime.now());
        token1.setExpiryDate(LocalDateTime.now().plusDays(1));
        token1.setUser(user);
        token1.setTokenType(tokenTypePasswordReset);
        token1.setValid(true);
        token1.setTokenValue("1234");

        Token token2 = new Token();
        token2.setCreateDate(LocalDateTime.now());
        token2.setExpiryDate(LocalDateTime.now().plusDays(1));
        token2.setUser(user);
        token2.setTokenType(tokenTypePasswordReset);
        token2.setValid(true);
        token2.setTokenValue("6666");

        tokenRepository.save(token1);
        tokenRepository.save(token2);

        tokenService.invalidateAllUserResetTokens(user);

        List<Token> tokenList = tokenRepository.findAll();

        assertThat(tokenList, hasItems(token1, token2));

        for (Token token : tokenList) {
            if (token.getUser().equals(user)) {
                assertFalse(token.getValid());
            }

        }

    }

    @Test
    public void saveToken() {
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
        userRepository.save(user);

        tokenService.saveToken(user, "9999", tokenTypePasswordReset);

        List<Token> tokenList = tokenRepository.findAll();

        for (Token tok : tokenList) {
            if (tok.getUser().equals(user)) {
                assertEquals("9999", tok.getTokenValue());
            }

        }

    }

    @Test
    public void should_validateToken_beTrue() {

        Token token = new Token();
        token.setCreateDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusDays(1));
        token.setUser(user);
        token.setTokenType(tokenTypePasswordReset);
        token.setValid(true);
        token.setTokenValue("7777");

        tokenRepository.save(token);

        assertTrue(tokenService.validateToken(token.getTokenValue(), tokenTypePasswordReset));

    }

    @Test
    public void should_validateToken_beFalse() {

        assertFalse(tokenService.validateToken("oiorjjasdoijsf0w9efuu", tokenTypePasswordReset));
        assertFalse(tokenService.validateToken("oiorjjasdoijsf0w9efuu", tokenTypeActivation));
    }


    @Test
    public void invalidateToken() {
        Token token = new Token();
        token.setCreateDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusDays(1));
        token.setUser(user);
        token.setTokenType(tokenTypePasswordReset);
        token.setValid(true);
        token.setTokenValue("8888445646464");
        tokenRepository.save(token);

        tokenService.invalidateToken(token.getTokenValue());

        assertFalse(tokenRepository.findTokenByTokenValue(token.getTokenValue()).getValid());
    }

    @Test
    public void getUserForToken() {

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
        userRepository.save(user);

        Token token = new Token();
        token.setCreateDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusDays(1));
        token.setUser(user);
        token.setTokenType(tokenTypePasswordReset);
        token.setValid(true);
        token.setTokenValue("234234234234");

        tokenRepository.save(token);

        assertEquals(user, tokenService.getUserForToken(token.getTokenValue()));


    }

    @Test
    public void should_getUserForToken_ThrowNullPointer() {

        assertThrows(NullPointerException.class, () -> tokenService.getUserForToken("103012930192301930"));

    }

}
