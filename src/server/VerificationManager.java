package server;

import server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class VerificationManager extends Manager {

    ArrayList<User> tempUsers;

    VerificationManager() {
        super();
        this.tempUsers = new ArrayList<>();
    }

    void sendVerificationCode(User user) {
        String code = generateCode(5);
        String subject = "Q-Bitz Verification Code";
        String mailBody = "The verification code to complete registration: " + code;
        mailSender.createEmailMessage(user.getEmail(), subject, mailBody);
        mailSender.sendEmail();
        System.out.println("» Verification Mail sent to " + user.getEmail() + " - Code : " + code);

        user.setCode(code);
        tempUsers.add(user);
    }

    User checkVerificationCode(String email, String code) {
        for (int i = 0; i < tempUsers.size(); i++)
            if (tempUsers.get(i).getEmail().equals(email) && tempUsers.get(i).getCode().equals(code))
                return tempUsers.remove(i);

        return null;
    }

}
