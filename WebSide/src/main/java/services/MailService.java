package services;

import utilities.MailSender;

public class MailService {

    public void sendEmail(String[] emails, String subject, String templateName){

        String message = buildMessage(templateName, );
        MailSender.sendMail(emails, subject, message);
    }

    public String buildMessage(){

    }
}
