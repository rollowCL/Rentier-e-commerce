package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.utils.BCrypt;
import pl.coderslab.rentier.utils.EmailUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class RegisterServiceImpl implements RegisterService {

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
        saveToken(user, generatedToken);
        sendActivationEmail(user, generatedToken);
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
    public String generateActivationToken(int len) {

        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        return sb.toString();
    }

    @Override
    public void saveToken(User user, String generatedToken) {

        Token token = new Token();
        token.setTokenType(rentierProperties.getTokenTypeActivation());
        token.setCreateDate(LocalDateTime.now());
        token.setExpiryDate(token.getCreateDate().plusMinutes(5));
        token.setValid(true);
        token.setUser(user);
        token.setTokenValue(generatedToken);

        tokenRepository.save(token);

    }

    @Override
    public boolean validateToken(String token) {

        return tokenRepository.existsTokenByTokenValueEqualsAndValidTrueAndExpiryDateAfter(token, LocalDateTime.now());
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
}
