package server;

import server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class VerificationManager {

    private static final String ALPHANUMERIC = "abcdejghijklmnopqrstuxwyzABCDEFGHIJKLMNOPQRSTUXWYZ1234567890";

    private MailSender mailSender;
    private ArrayList<User> tempUsers;

    VerificationManager() {
        this.tempUsers = new ArrayList<>();
        this.mailSender = new MailSender("smtp.gmail.com", "info.qbitz@gmail.com", "qbitzteam");
        this.mailSender.setMailServerProperties(587, true,true);
    }

    void sendVerificationCode(User user) {
        String code = generateCode(5);
        String subject = "Q-Bitz Verification Code";
        String mailBody = "The verification code to complete registration: " + code;
        mailSender.createEmailMessage(user.getEmail(), subject, mailBody);
        mailSender.sendEmail();

        user.setCode(code);
        tempUsers.add(user);
    }

    User checkVerificationCode(String email, String code) {

        for (int i = 0; i < tempUsers.size(); i++)
            if (tempUsers.get(i).getEmail().equals(email) && tempUsers.get(i).getCode().equals(code))
                return tempUsers.remove(i);

        return null;
    }

    private String generateCode(int length) {
        StringBuilder result = new StringBuilder();
        Random generator = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = generator.nextInt(ALPHANUMERIC.length());
            result.append(ALPHANUMERIC.charAt(randomIndex));
        }

        return result.toString();
    }

}
