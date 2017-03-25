package commands;

import dto.AddressDTO;
import dto.ContactDTO;
import entities.Address;
import entities.Contact;
import services.ContactService;
import services.SearchService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SearchContactCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {
        forward("searchContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        List<ContactDTO> contactList;
        ContactDTO contact = new ContactDTO();
        contact.setName(request.getParameter("firstName"));
        contact.setSurname(request.getParameter("surname"));
        contact.setThirdName(request.getParameter("thirdName"));
        //validator date and other fields
        String date = request.getParameter("dateOfBirth");
        if (!date.equals("")){
           // contact.setDateOfBirth(Date.valueOf(date));
        }
        contact.setCitizenship(request.getParameter("citizenship"));
        contact.setGender(request.getParameter("gender"));
        contact.setMaritalStatus(request.getParameter("status"));
        AddressDTO address = new AddressDTO();
        address.setCountry(request.getParameter("country"));
        address.setCity(request.getParameter("city"));
        address.setStreetAddress(request.getParameter("address"));
        address.setIndex(request.getParameter("index"));
        contact.setAddress(address);
        String dateCriteria = request.getParameter("dateCriteria");
        SearchService searchService = new SearchService();
        contactList = searchService.searchContacts(contact, dateCriteria);
        request.setAttribute("contactList", contactList);
        forward("contacts");
    }
}
