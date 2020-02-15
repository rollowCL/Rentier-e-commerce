package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.utils.EmailUtil;

@Service
public class LoginRegisterServiceImpl implements LoginRegisterService {

    private final RentierProperties rentierProperties;

    public LoginRegisterServiceImpl(RentierProperties rentierProperties) {
        this.rentierProperties = rentierProperties;
    }

    @Override
    public void sendActivationEmail(User user) {

            String msgBody = "<h3>Potwierdzenie rejestracji</h3><br>" +
                    "Witaj " + user.getFirstName() + " musisz aktywować konto klikając w link<br>" +
                    "<a href>aktywacja</a>";


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
