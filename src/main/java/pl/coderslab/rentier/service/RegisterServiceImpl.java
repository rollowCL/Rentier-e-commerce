package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.utils.BCrypt;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final RentierProperties rentierProperties;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenServiceImpl tokenService;
    private final EmailServiceImpl emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterServiceImpl(RentierProperties rentierProperties, UserRepository userRepository, TokenRepository tokenRepository, TokenServiceImpl tokenService, EmailServiceImpl emailService, BCryptPasswordEncoder passwordEncoder) {
        this.rentierProperties = rentierProperties;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String generatedToken = tokenService.generateToken(30);
        tokenService.saveToken(user, generatedToken, rentierProperties.getTokenTypeActivation());
        emailService.sendActivationEmail(user, generatedToken);
    }


    @Override
    public void makeUserVerified(String token) {
        Token storedToken = tokenRepository.findTokenByTokenValue(token);
        userRepository.makeUserVerified(storedToken.getUser().getId());
    }


}
