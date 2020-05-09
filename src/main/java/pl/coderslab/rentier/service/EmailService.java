package pl.coderslab.rentier.service;

import org.apache.jasper.tagplugins.jstl.core.Url;
import pl.coderslab.rentier.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public interface EmailService {

    void sendPasswordReminderEmail(User user, String generatedToken, HttpServletRequest request);
    void sendActivationEmail(User user, String generatedToken, HttpServletRequest request) throws UnsupportedEncodingException;
    void sendEmailWithAttachment(User user, URL url, String fileName);
    String getURL(HttpServletRequest request);
}
