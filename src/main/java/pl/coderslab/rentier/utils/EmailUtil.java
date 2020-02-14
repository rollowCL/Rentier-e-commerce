package pl.coderslab.rentier.utils;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     * @param fromEmail
     */
    public static void sendEmail(Session session, String toEmail, String subject, String body, String fromEmail,
                                 String personal) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, personal));

            msg.setReplyTo(InternetAddress.parse(fromEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setContent(body, "text/html");

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createEmail(String toEmail, String subject, String body, String fromEmail,
                                   String password, String smtpHost, String smtpPort, String personal){

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost); //SMTP Host
        props.put("mail.smtp.socketFactory.port", smtpPort); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", smtpPort); //SMTP Port

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        sendEmail(session, toEmail, subject, body, fromEmail, personal);
    }
}
