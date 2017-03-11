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
    public void process() throws ServletException, IOException {
        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contacts = null;
        AddressDAO addressDAO = new AddressDAO();
        List<ContactDTO> contactsDTO = new ArrayList<>();
        try {
            contacts = contactDAO.findAll();
            contacts.forEach(contact ->{
                if (contact.getAddress_id()!=0 || contact.getAddress_id()!=null){
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
            new MessageError(e.getMessage(), e);
        }
        if (contacts!=null) {
            request.setAttribute("contactList", contactsDTO);
            forward("main");
        }
    }
}
