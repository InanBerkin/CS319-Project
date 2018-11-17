package server;

import server.models.User;

import java.util.HashMap;

public class ResetPasswordManager extends Manager {

    private HashMap<String, String> passwordResetCodes;

    ResetPasswordManager() {
        super();
        this.passwordResetCodes = new HashMap<>();
    }

    void sendPasswordResetCode(String email) {
        String code = generateCode(5);
        String subject = "Q-Bitz Password Reset Request";
        String mailBody = "The code required to reset password: " + code;
        mailSender.createEmailMessage(email, subject, mailBody);
        mailSender.sendEmail();

        passwordResetCodes.put(email, code);
    }

    boolean checkVerificationCode(String email, String code) {
        if (passwordResetCodes.get(email) == null)
            return false;

        if (passwordResetCodes.get(email) == code) {
            passwordResetCodes.remove(email, code);
            return true;
        }

        return false;
    }
}
