package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.TokenRepository;
import pl.coderslab.rentier.utils.EmailUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final TokenRepository tokenRepository;

    @Value("${rentier.mailHostName}")
    private String mailHostName;

    @Value("${rentier.mailPort}")
    private String mailPort;

    @Value("${rentier.mailFrom}")
    private String mailFrom;

    @Value("${rentier.mailSMTP}")
    private String mailSMTP;

    @Value("${rentier.mailPassword}")
    private String mailPassword;

    @Value("${rentier.mailPersonal}")
    private String mailPersonal;

    public EmailServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }



    @Override
    public void sendPasswordReminderEmail(User user, String generatedToken, HttpServletRequest request) {
        String link = getURL(request) + "/resetpassword?token=" + generatedToken;

        String msgBody = "<h3>Przypomnienie hasła</h3><br>" +
                "<a href=\"" + link + "\">" + link + "</a>";


        EmailUtil.createEmail(user.getEmail(),
                "Przypomnienie hasła w sklepie Rentier",
                msgBody,
                mailFrom,
                mailPassword,
                mailSMTP,
                mailPort,
                mailPersonal
        );

    }

    @Override
    public void sendActivationEmail(User user, String generatedToken, HttpServletRequest request)  {
        String link = getURL(request) + "/activate?token=" + generatedToken;

        String msgBody = "<h3>Potwierdzenie rejestracji</h3><br>" +
                "Witaj " + user.getFirstName() + " musisz aktywować konto klikając w link<br>" +
                "<a href=\"" + link + "\">" + link + "</a>";


        EmailUtil.createEmail(user.getEmail(),
                "Witamy w sklepie Rentier",
                msgBody,
                mailFrom,
                mailPassword,
                mailSMTP,
                mailPort,
                mailPersonal
        );


    }

    @Override
    public String getURL(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.substring(0, url.lastIndexOf('/'));
    }

}
