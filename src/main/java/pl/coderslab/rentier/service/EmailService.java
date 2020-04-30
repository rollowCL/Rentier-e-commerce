package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendPasswordReminderEmail(User user, String generatedToken);
    void sendActivationEmail(User user, String generatedToken) throws UnsupportedEncodingException;

}
