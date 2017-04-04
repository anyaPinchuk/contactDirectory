package services;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import utilities.LetterTemplate;
import utilities.MailSender;

import java.util.List;

public class MailService {

    public void sendEmail(String[] emails, String subject, String templateName, String content) {
        String message;
        ContactService contactService = new ContactService();
        List<String> names = contactService.findNamesByEmails(emails);
        if (CollectionUtils.isNotEmpty(names))
            for (int i = 0; i < emails.length; i++) {
                if (StringUtils.isNotEmpty(templateName.trim())) {
                    message = content;
                } else {
                    message = buildMessage(templateName, names.get(i));
                }
                MailSender.sendMail(emails[i], message, subject);
            }
    }

    private String buildMessage(String templateName, String name) {
        LetterTemplate letterTemplate = new LetterTemplate(templateName);
        ST messageTemplate = letterTemplate.getTemplate();
        messageTemplate.add("name", name);
        return messageTemplate.render();
    }
}
