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
import services.AddressService;
import services.ContactService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactsCommand extends FrontCommand {
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;
    private ContactService contactService;
    private AddressService addressService;
    private List<ContactDTO> contactsDTO = new ArrayList<>();


    public ContactsCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
        contactService = new ContactService();
        addressService = new AddressService();
    }

    @Override
    public void processGet() throws ServletException, IOException {
        int start = 0;
        int count = 10;
        int currentPage = 1;
        String pageFromClient = request.getParameter("page");
        int countRows = contactService.getCountRows();
        if (pageFromClient != null && !pageFromClient.equals("1")) {
            try{
                int temp = Integer.valueOf(pageFromClient);
                if (countRows / count >= temp - 1) {
                    currentPage = temp;
                    start += currentPage * count - count;
                }
            } catch (NumberFormatException e){

            }
        }
        List<Contact> contacts;
        List<Integer> pageList = new ArrayList<>();
        contacts = contactService.findByParts(start, count);

        if (contacts.size() != 0) {
            int pageCount = countRows % count == 0 ? countRows / count : countRows / count + 1;
            for (int i = 1; i <= pageCount; i++) {
                pageList.add(i);
            }
        }
        contacts.forEach(contact -> {
            ContactDTO contactDTO = contactConverter.toDTO(Optional.of(contact)).get();
            Address address = addressService.findById(contact.getId());
            if (address != null) {
                contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).get());
            }
            contactsDTO.add(contactDTO);
        });
        if (request.getSession().getAttribute("isSearch") != null) {
            contactsDTO = (List<ContactDTO>) request.getSession().getAttribute("contactList");
            int endIndex;
            if (currentPage == 1){
                endIndex = contactsDTO.size() < count ? start + contactsDTO.size() : count + start;
            } else endIndex = contactsDTO.size() % count != 0
                    ? start + (contactsDTO.size() - count * currentPage)
                    : count + start;
            contactsDTO = contactsDTO.subList(start, endIndex);
            request.getSession().setAttribute("contactList", contactsDTO);
            request.getSession().setAttribute("isSearch", null);
        } else {
            request.getSession().setAttribute("pageList", pageList);
            request.getSession().setAttribute("contactList", contactsDTO);
        }
        forward("contacts");
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
