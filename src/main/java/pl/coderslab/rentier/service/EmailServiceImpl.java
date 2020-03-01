package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.Token;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.utils.EmailUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final RentierProperties rentierProperties;
    private final TokenRepository tokenRepository;

    public EmailServiceImpl(RentierProperties rentierProperties,
                            TokenRepository tokenRepository) {
        this.rentierProperties = rentierProperties;
        this.tokenRepository = tokenRepository;
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
}
