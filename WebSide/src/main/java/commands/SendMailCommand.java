package commands;

import services.ContactService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SendMailCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {
        String[] values = request.getParameterValues("chosenContacts");
        if (values != null) {
            Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            ContactService contactService = new ContactService();
            List<String> emails = contactService.findEmailsById(ids);
            if (emails!=null){
                request.setAttribute("emails", emails);
            }
        }
        forward("sendMail");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        String[] emails = request.getParameterValues("emails");

        response.sendRedirect("Contacts");
    }
}
