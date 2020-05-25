package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.utils.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenServiceImpl tokenService;
    private final EmailServiceImpl emailService;

    @Value("${rentier.tokenTypeActivation}")
    private int tokenTypeActivation;

    public RegisterServiceImpl(UserRepository userRepository, TokenRepository tokenRepository,
                               TokenServiceImpl tokenService, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public String registerUser(User user) {
        user.setActive(true);
        user.setVerified(false);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
        String generatedToken = tokenService.generateToken(30);
        tokenService.saveToken(user, generatedToken, tokenTypeActivation);
        return generatedToken;
    }


    @Override
    public void makeUserVerified(String token) {
        Token storedToken = tokenRepository.findTokenByTokenValue(token);
        userRepository.makeUserVerified(storedToken.getUser().getId());
    }


}
