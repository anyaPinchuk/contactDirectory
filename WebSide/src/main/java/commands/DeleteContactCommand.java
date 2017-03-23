package commands;

import dao.PhoneDAO;
import dao.PhotoDAO;
import entities.Address;
import entities.Attachment;
import entities.Contact;
import entities.PhoneNumber;
import exceptions.GenericDAOException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteContactCommand extends FrontCommand{
    private PhotoDAO photoDAO = new PhotoDAO();

    private PhoneDAO phoneDAO = new PhoneDAO();
    @Override
    public void processGet() throws ServletException, IOException {
    }

    @Override
    public void processPost() throws ServletException, IOException {
        String[] values = request.getParameterValues("chosenContacts");
        if (values !=null){
            Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
            Arrays.stream(ids).forEach((Long id) -> {
                try {
                    //delete photo and attachments if exist from disk
                    Contact contact = contactDAO.findById(id).get();
                    contactDAO.deleteById(id);
                    if (contact.getAddress_id() != 0){
                        addressDAO.deleteById(contact.getAddress_id());
                    }
                    if (contact.getPhoto_id() !=0){
                        photoDAO.deleteById(contact.getPhoto_id());
                    }
                } catch (GenericDAOException e) {
                    LOG.error("error while processing  delete by id in DeleteContactCommand");
                    e.printStackTrace();
                }
            });
        }
        response.sendRedirect("Contacts");
    }
}
