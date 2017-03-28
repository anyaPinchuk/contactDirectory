package services;

import org.stringtemplate.v4.ST;
import utilities.LetterTemplate;
import utilities.MailSender;

import java.util.List;

public class MailService {

    public void sendEmail(String[] emails, String subject, String templateName, String content) {
        String message = "";
        ContactService contactService = new ContactService();
        List<String> names = contactService.findNamesByEmails(emails);
        for (int i = 0; i < emails.length; i++) {
            if (templateName.equals("")) {
                message = content;
            } else {
                message = buildMessage(templateName, names.get(i));
            }
            MailSender.sendMail(emails[i], message, subject);
        }
    }

    public String buildMessage(String templateName, String name) {
        LetterTemplate letterTemplate = new LetterTemplate(templateName);
        ST messageTemplate = letterTemplate.getTemplate();
        messageTemplate.add("name", name);
        return messageTemplate.render();
    }
}
