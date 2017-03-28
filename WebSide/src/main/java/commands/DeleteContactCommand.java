package commands;

import entities.Contact;
import services.AddressService;
import services.ContactService;
import services.PhotoService;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

public class DeleteContactCommand extends FrontCommand {
    private ContactService contactService = new ContactService();
    private PhotoService photoService = new PhotoService();
    private AddressService addressService = new AddressService();

    @Override
    public void processGet() throws ServletException, IOException {
    }

    @Override
    public void processPost() throws ServletException, IOException {
        String[] values = request.getParameterValues("chosenContacts");
        if (values != null) {
            Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            Arrays.stream(ids).forEach((Long id) -> {
                Contact contact = contactService.findById(id);
                contactService.deleteById(id);
                FileUploadDocuments.deleteDirectory(contact.getId(), false);
                FileUploadDocuments.deleteDirectory(contact.getId(), true);
            });
        }
        response.sendRedirect("Contacts");
    }
}
