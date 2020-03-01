package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

public interface EmailService {

    void sendPasswordReminderEmail(User user, String generatedToken);
    void sendActivationEmail(User user, String generatedToken);

}
