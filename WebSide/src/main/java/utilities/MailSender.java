package utilities;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.Arrays;

public class MailSender {

    public static void sendMail(String[] recipients, String subject, String message){
        Email email = new SimpleEmail();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("info.iposter@gmail.com",
                "aleanyamarina"));
        email.setDebug(false);
        email.setHostName("smtp.gmail.com");
        email.setSSLOnConnect(true);
        email.setStartTLSEnabled(true);
        try {
            email.setFrom("info.iposter@gmail.com");
            email.setSubject(subject);
            email.setMsg(message);
            for(String recipient : recipients){
                email.addTo(recipient);
            }
            email.send();
        } catch (EmailException e) {
            //log
        }

    }
}
