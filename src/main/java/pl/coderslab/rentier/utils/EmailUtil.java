package pl.coderslab.rentier.utils;

import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
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
            msg.setHeader("Content-Type", "text/html; charset=iso-8859-2");
            msg.setHeader("Content-Language", "pl");
            msg.setFrom(new InternetAddress(fromEmail, personal));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "iso-8859-2");
            msg.setSentDate(new Date());
            msg.setContent(body, "text/html; charset=iso-8859-2");
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEmailWithAttachment(Session session, String toEmail, String subject, String fromEmail,
                                               String personal, URL url, String fileName) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setHeader("Content-Type", "text/html; charset=iso-8859-2");
            msg.setHeader("Content-Language", "pl");
            msg.setFrom(new InternetAddress(fromEmail, personal));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "iso-8859-2");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("Log");
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(url));
            messageBodyPart2.setFileName(fileName);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            msg.setContent(multipart);
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

    public static void createEmailWithAttachment(String toEmail, String subject, String fromEmail,
                                   String password, String smtpHost, String smtpPort, String personal, URL url, String fileName){

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
        sendEmailWithAttachment(session, toEmail, subject, fromEmail, personal, url, fileName);
    }
}
