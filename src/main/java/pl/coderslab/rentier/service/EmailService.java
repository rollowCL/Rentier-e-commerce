package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendPasswordReminderEmail(User user, String generatedToken, HttpServletRequest request);
    void sendActivationEmail(User user, String generatedToken, HttpServletRequest request) throws UnsupportedEncodingException;
    String getURL(HttpServletRequest request);
}
