package server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class MailSender {

    private final String EMAIL_HOST;
    private final String EMAIL_USERNAME;
    private final String EMAIL_PASSWORD;

    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    MailSender(String emailHost, String emailUsername, String emailPassword) {
        this.EMAIL_HOST = emailHost;
        this.EMAIL_USERNAME = emailUsername;
        this.EMAIL_PASSWORD = emailPassword;
    }

    void setMailServerProperties(int smtpPort, boolean isAuth, boolean isSecure) {
        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", "" + smtpPort);
        emailProperties.put("mail.smtp.auth", isAuth ? "true" : "false");
        emailProperties.put("mail.smtp.starttls.enable", isSecure ? "true" : "false");
    }

    void createEmailMessage(String toAddress, String subject, String body) {

        try {
            mailSession = Session.getDefaultInstance(emailProperties, null);
            emailMessage = new MimeMessage(mailSession);

            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            emailMessage.setSubject(subject);
            emailMessage.setText(body);                    // for a text email
            //emailMessage.setContent(body, "text/html");  // for a html email
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    void sendEmail() {

        Transport transport;

        try {
            transport = mailSession.getTransport("smtp");
            transport.connect(EMAIL_HOST, EMAIL_USERNAME, EMAIL_PASSWORD);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
