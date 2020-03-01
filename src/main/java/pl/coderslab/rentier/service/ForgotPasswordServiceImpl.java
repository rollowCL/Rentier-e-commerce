package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.utils.BCrypt;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);
    private final RentierProperties rentierProperties;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenServiceImpl tokenService;
    private final EmailServiceImpl emailService;

    public ForgotPasswordServiceImpl(RentierProperties rentierProperties, UserRepository userRepository, TokenRepository tokenRepository, TokenServiceImpl tokenService, EmailServiceImpl emailService) {
        this.rentierProperties = rentierProperties;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }


    @Override
    public void resetPasswordProcess(String email) {

        Optional<User> user = userRepository.findByEmailAndActiveTrue(email);

        if (user.isPresent()) {

            String generatedToken = tokenService.generateToken(30);
            tokenService.saveToken(user.get(), generatedToken, rentierProperties.getTokenTypePasswordReset());
            emailService.sendPasswordReminderEmail(user.get(), generatedToken);

        }


    }





}
