package commands;

import entities.Contact;
import exceptions.MessageError;
import org.apache.commons.collections4.CollectionUtils;
import services.ContactService;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

public class DeleteContactCommand extends FrontCommand {
    private ContactService contactService = new ContactService();

    @Override
    public void processGet() throws ServletException, IOException {
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("delete contacts command starting");
        MessageError error = new MessageError();
        String[] values = request.getParameterValues("chosenContacts");
        if (values != null) {
            Long[] ids;
            try {
                ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
                Arrays.stream(ids).forEach((Long id) -> {
                    Contact contact = contactService.findAndDeleteById(id);
                    FileUploadDocuments.deleteDirectory(contact.getId(), false);
                    FileUploadDocuments.deleteDirectory(contact.getId(), true);
                });
                response.sendRedirect("contacts");
            } catch (NumberFormatException e) {
                error.addMessage("Wrong ids of chosen contacts");
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
                LOG.info("Wrong ids values of chosen contacts");
            }
        }

    }
}
