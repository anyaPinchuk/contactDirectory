package commands;

import entities.Contact;
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
        String[] values = request.getParameterValues("chosenContacts");
        if (!CollectionUtils.isEmpty(Arrays.asList(values))) {
            Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            //проверить если не преобразуется
            Arrays.stream(ids).forEach((Long id) -> {
                Contact contact = contactService.findAndDeleteById(id);
                FileUploadDocuments.deleteDirectory(contact.getId(), false);
                FileUploadDocuments.deleteDirectory(contact.getId(), true);
            });
        }
        response.sendRedirect("Contacts");
    }
}
