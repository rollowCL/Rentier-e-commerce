package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.controller.user.ForgotPasswordController;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.utils.BCrypt;
import pl.coderslab.rentier.utils.EmailUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final RentierProperties rentierProperties;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public RegisterServiceImpl(RentierProperties rentierProperties, UserRepository userRepository, TokenRepository tokenRepository) {
        this.rentierProperties = rentierProperties;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void registerUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
        String generatedToken = generateActivationToken(30);
        saveToken(user, generatedToken, rentierProperties.getTokenTypeActivation());
        sendActivationEmail(user, generatedToken);
    }

    @Override
    public void updateUserPassword(User user) {
        logger.info("Password before: " + user.getPassword());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        logger.info("Password after: " + user.getPassword());
        logger.info("Id: " + user.getId());
        userRepository.updateUserPassword(user.getId(), user.getPassword());
    }

    @Override
    public void sendActivationEmail(User user, String generatedToken) {

        String msgBody = "<h3>Potwierdzenie rejestracji</h3><br>" +
                "Witaj " + user.getFirstName() + " musisz aktywować konto klikając w link<br>" +
                "http://localhost:8080/activate?token=" + generatedToken;


        EmailUtil.createEmail(user.getEmail(),
                "Witamy w sklepie Rentier",
                msgBody,
                rentierProperties.getMailFrom(),
                rentierProperties.getMailPassword(),
                rentierProperties.getMailSMTP(),
                rentierProperties.getMailPort(),
                rentierProperties.getMailPersonal()
        );


    }

    @Override
    public void sendPasswordReminderEmail(User user, String generatedToken) {

        String msgBody = "<h3>Przyponienie hasła</h3><br>" +
                "http://localhost:8080/resetpassword?token=" + generatedToken;


        EmailUtil.createEmail(user.getEmail(),
                "Przypomnienie hasła w sklepie Rentier",
                msgBody,
                rentierProperties.getMailFrom(),
                rentierProperties.getMailPassword(),
                rentierProperties.getMailSMTP(),
                rentierProperties.getMailPort(),
                rentierProperties.getMailPersonal()
        );


    }

    @Override
    public String generateActivationToken(int len) {

        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        return sb.toString();
    }

    @Override
    public void saveToken(User user, String generatedToken, int tokenType) {

        Token token = new Token();
        token.setTokenType(tokenType);
        token.setCreateDate(LocalDateTime.now());
        token.setExpiryDate(token.getCreateDate().plusHours(1));
        token.setValid(true);
        token.setUser(user);
        token.setTokenValue(generatedToken);

        tokenRepository.save(token);

    }

    @Override
    public boolean validateToken(String token, int tokenType) {

        return tokenRepository.existsTokenByTokenValueEqualsAndValidTrueAndExpiryDateAfterAndTokenType
                (token, LocalDateTime.now(), tokenType);
    }

    @Override
    public void invalidateToken(String token) {

        tokenRepository.invalidateToken(token);
    }

    @Override
    public void makeUserVerified(String token) {
        Token storedToken = tokenRepository.findTokenByTokenValue(token);
        userRepository.makeUserVerified(storedToken.getUser().getId());
    }

    @Override
    public void resetPasswordProcess(String email) {

        Optional<User> user = userRepository.findByEmailAndActiveTrue(email);

        if (user.isPresent()) {

            String generatedToken = generateActivationToken(30);
            saveToken(user.get(), generatedToken, rentierProperties.getTokenTypePasswordReset());
            sendPasswordReminderEmail(user.get(), generatedToken);

        }


    }

    @Override
    public User getUserForToken(String token) {

        return tokenRepository.findTokenByTokenValue(token).getUser();
    }
}
