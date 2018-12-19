package server;


import com.sun.org.apache.bcel.internal.classfile.Code;

import java.util.HashMap;

public class MainTest {

    public static void main(String[] args) {
 //       MailSender sender = new MailSender("smtp.gmail.com", "info.qbitz@gmail.com", "qbitzteam");
 //       sender.setMailServerProperties(587, true,true);
 //       sender.createEmailMessage("ztan.cankiri@gmail.com", "Q-Bitz Test Mail", "This is an example for auth message.");
 //       sender.sendEmail();

        HashMap<String, String> map = new HashMap<>();

        map.put("abcde", "12345");

        System.out.println(map.get("abcde"));

        map.remove("abcde", "12345");

        System.out.println(map.get("abcde"));
    }
}
