package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenServiceImpl tokenService;
    private final EmailServiceImpl emailService;

    @Value("${rentier.tokenTypePasswordReset}")
    private int tokenTypePasswordReset;

    public ForgotPasswordServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, TokenServiceImpl tokenService, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }


    @Override
    public void resetPasswordProcess(String email, HttpServletRequest request) {

        Optional<User> user = userRepository.findByEmailAndActiveTrue(email);

        if (user.isPresent()) {
            tokenService.invalidateAllUserResetTokens(user.get());
            String generatedToken = tokenService.generateToken(30);
            tokenService.saveToken(user.get(), generatedToken, tokenTypePasswordReset);
            emailService.sendPasswordReminderEmail(user.get(), generatedToken, request);

        }


    }





}
