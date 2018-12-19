package server;

import server.models.User;

import java.util.ArrayList;
import java.util.Random;

public abstract class Manager {

    static final String ALPHANUMERIC = "abcdejghijklmnopqrstuxwyzABCDEFGHIJKLMNOPQRSTUXWYZ1234567890";

    MailSender mailSender;

    Manager() {
        this.mailSender = new MailSender("smtp.gmail.com", "info.qbitz@gmail.com", "qbitzteam");
        this.mailSender.setMailServerProperties(587, true,true);
    }

    String generateCode(int length) {
        StringBuilder result = new StringBuilder();
        Random generator = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = generator.nextInt(ALPHANUMERIC.length());
            result.append(ALPHANUMERIC.charAt(randomIndex));
        }

        return result.toString();
    }
}
