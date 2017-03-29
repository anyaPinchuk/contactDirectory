package commands;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import services.ContactService;
import services.MailService;
import utilities.LetterTemplate;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SendMailCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {

    }

    @Override
    public void processPost() throws ServletException, IOException {
        String[] values = request.getParameterValues("chosenContacts");
        if (!CollectionUtils.isEmpty(Arrays.asList(values))) {
            Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            ContactService contactService = new ContactService();
            List<String> emails = contactService.findEmailsById(ids);
            List<LetterTemplate> templates = LetterTemplate.getTemplates();
            if (!CollectionUtils.isEmpty(emails)) {
                request.setAttribute("emails", emails);
            }
            request.setAttribute("templates", templates);
            forward("sendMail");
        } else {
            String[] emails = request.getParameterValues("emails");
            String subject = request.getParameter("subject");
            String templateName = request.getParameter("template");
            String content = request.getParameter("content");
            //valid
            MailService mailService = new MailService();
            if (!CollectionUtils.isEmpty(Arrays.asList(emails)) && StringUtils.isNotEmpty(subject.trim()) &&
                   StringUtils.isNotEmpty(content.trim())){
                mailService.sendEmail(emails, subject, templateName, content);
            }
            response.sendRedirect("Contacts");
        }

    }
}