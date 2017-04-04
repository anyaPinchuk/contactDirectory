package commands;

import exceptions.MessageError;
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
        LOG.info("send mail command starting");
        MessageError error = new MessageError();
        String[] values = request.getParameterValues("chosenContacts");
        if (values != null) {
            Long[] ids;
            try {
                ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            } catch (NumberFormatException e) {
                error.addMessage("Wrong id values of chosen contacts");
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
                LOG.info("Wrong id values of chosen contacts");
                return;
            }
            ContactService contactService = new ContactService();
            List<String> emails = contactService.findEmailsById(ids);
            List<LetterTemplate> templates = LetterTemplate.getTemplates();
            if (CollectionUtils.isNotEmpty(emails)) {
                request.setAttribute("emails", emails);
                request.setAttribute("templates", templates);
                forward("sendMail");
            } else {
                error.addMessage("Emails were not found");
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
            }
        } else {
            String[] emails = request.getParameterValues("emails");
            String subject = request.getParameter("subject");
            String templateName = request.getParameter("template");
            String content = request.getParameter("content");
            MailService mailService = new MailService();
            if (CollectionUtils.isNotEmpty(Arrays.asList(emails)) && StringUtils.isNotEmpty(content.trim())) {
                mailService.sendEmail(emails, subject, templateName, content);
                response.sendRedirect("Contacts");
            } else {
                error.addMessage("Content was not specified");
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
            }
        }

    }
}