package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import dto.ContactDTO;
import entities.Address;
import entities.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import services.AddressService;
import services.ContactService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactsCommand extends FrontCommand {
    private ContactConverter contactConverter = new ContactConverter();
    private AddressConverter addressConverter = new AddressConverter();
    private ContactService contactService = new ContactService();
    private AddressService addressService = new AddressService();
    private List<ContactDTO> contactsDTO = new ArrayList<>();

    @Override
    public void processGet() throws ServletException, IOException {
        int start = 0;
        int count = 10;
        int currentPage = 1;
        String pageFromClient = request.getParameter("page");
        int countRows = contactService.getCountRows();
        if (StringUtils.isNotEmpty(pageFromClient) && !pageFromClient.equals("1")) {
            try {
                int temp = Integer.valueOf(pageFromClient);
                if (countRows / count >= temp - 1) {
                    currentPage = temp;
                    start += currentPage * count - count;
                }
            } catch (NumberFormatException e) {
                //
            }
        }
        List<Integer> pageList = new ArrayList<>();
        List<Contact> contacts = contactService.findByParts(start, count);

        if (!CollectionUtils.isEmpty(contacts)) {
            int pageCount = countRows % count == 0 ? countRows / count : countRows / count + 1;
            for (int i = 1; i <= pageCount; i++) {
                pageList.add(i);
            }
            contacts.forEach(contact -> {
                ContactDTO contactDTO = contactConverter.toDTO(Optional.of(contact)).get();
                Address address = addressService.findById(contact.getId());
                if (address != null) {
                    contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).get());
                }
                contactsDTO.add(contactDTO);
            });
        }
        if (request.getSession().getAttribute("isSearch") != null) {
            contactsDTO = (List<ContactDTO>) request.getSession().getAttribute("contactList");
            int endIndex;
            if (currentPage == 1) {
                endIndex = contactsDTO.size() < count ? start + contactsDTO.size() : count + start;
            } else endIndex = contactsDTO.size() % count != 0
                    ? start + (contactsDTO.size() - count * currentPage)
                    : count + start;
            contactsDTO = contactsDTO.subList(start, endIndex);
            request.getSession().setAttribute("isSearch", null);
        } else {
            request.getSession().setAttribute("pageList", pageList);
        }
        request.getSession().setAttribute("contactList", contactsDTO);
        forward("contacts");
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
