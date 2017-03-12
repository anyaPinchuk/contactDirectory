package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import dao.AddressDAO;
import dao.ContactDAO;
import dto.ContactDTO;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.MessageError;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactsCommand extends FrontCommand {
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;

    public ContactsCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
    }

    @Override
    public void processGet() throws ServletException, IOException {
        List<Contact> contacts = null;
        List<ContactDTO> contactsDTO = new ArrayList<>();
        LOG.info("get all contacts starting ");
        try {
            contacts = contactDAO.findAll();
            contacts.forEach(contact ->{
                if (contact.getAddress_id()!=0){
                    try {
                        Address address = addressDAO.findById(contact.getAddress_id())
                                .orElseThrow(()->new GenericDAOException("address was not found"));
                        ContactDTO contactDTO = contactConverter.toDTO(Optional.of(contact)).get();
                        contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).get());
                        contactsDTO.add(contactDTO);
                    } catch (GenericDAOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    ContactDTO contactDTO = contactConverter.toDTO(Optional.of(contact)).get();
                    contactsDTO.add(contactDTO);
                }
            });
        }
        catch (GenericDAOException e){
            LOG.error("error while processing all contacts from ContactsCommand");
            new MessageError(e.getMessage(), e);
        }
        if (contactsDTO!=null) {
            request.setAttribute("contactList", contactsDTO);
            forward("main");
        }
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
