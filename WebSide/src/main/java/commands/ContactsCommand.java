package commands;

import dao.ContactDAO;
import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.MessageError;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ContactsCommand extends FrontCommand {
    
    @Override
    public void process() throws ServletException, IOException {
        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contacts = null;
        try {
            contacts = contactDAO.findAll();
        }
        catch (GenericDAOException e){
            new MessageError(e.getMessage(), e);
        }
        if (contacts!=null) {
            request.setAttribute("contactList", contacts);
            forward("main");
        }
    }
}
