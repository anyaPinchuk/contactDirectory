package services;

import entities.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import utilities.LetterTemplate;
import utilities.MailSender;

import java.util.List;

public class MailService {

    /**
     * send letter to users
     *
     * @param emails
     * @param subject
     * @param templateName
     * @param content      That is body of the letter
     */
    public void sendEmail(String[] emails, String subject, String templateName, String content) {
        String message;
        ContactService contactService = new ContactService();
        List<Contact> contacts = contactService.findContactsByEmails(emails);
        if (CollectionUtils.isNotEmpty(contacts))
            for (int i = 0; i < emails.length; i++) {
                if (StringUtils.isEmpty(templateName.trim())) {
                    message = content;
                } else {
                    message = buildMessage(templateName, contacts.get(i));
                }
                MailSender.sendMail(emails[i], message, subject);
            }
    }

    /**
     * builds message from template with defined contact parameters
     *
     * @param templateName
     * @param contact
     * @return created message
     */
    private String buildMessage(String templateName, Contact contact) {
        LetterTemplate letterTemplate = new LetterTemplate(templateName);
        ST messageTemplate = letterTemplate.getTemplate();
        messageTemplate.add("name", contact.getName());
        messageTemplate.add("surname", contact.getSurname());
        return messageTemplate.render();
    }
}
