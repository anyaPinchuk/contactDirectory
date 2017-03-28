package commands;

import services.ContactService;
import services.MailService;
import utilities.LetterTemplate;
import utilities.MailSender;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SendMailCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {

    }

    @Override
    public void processPost() throws ServletException, IOException {
        String[] values = request.getParameterValues("chosenContacts");
        if (values != null) {
            Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            ContactService contactService = new ContactService();
            List<String> emails = contactService.findEmailsById(ids);
            if (emails!=null){
                request.setAttribute("emails", emails);
            }
            List<LetterTemplate> templates = LetterTemplate.getTemplates();
            request.setAttribute("templates", templates);
            forward("sendMail");
        }
        else {
            String[] emails = request.getParameterValues("emails");
            String subject = request.getParameter("subject");
            String templateName = request.getParameter("template");
            String content = request.getParameter("content");
            //valid
            MailService mailService = new MailService();
            mailService.sendEmail(emails, subject, templateName, content);
            response.sendRedirect("Contacts");
        }

    }
}