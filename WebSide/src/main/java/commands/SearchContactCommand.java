package commands;

import dto.AddressDTO;
import dto.ContactDTO;
import services.SearchService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SearchContactCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {
        List<Integer> pageList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            pageList.add(i);
        }
        request.setAttribute("pageList", pageList);
        forward("searchContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        List<ContactDTO> contactList;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName(request.getParameter("firstName"));
        contactDTO.setSurname(request.getParameter("surname"));
        contactDTO.setThirdName(request.getParameter("thirdName"));
        //validator date and other fields
        String date = request.getParameter("dateOfBirth");
        if (!date.equals("")){
           contactDTO.setDateOfBirth(Date.valueOf(date));
        }
        contactDTO.setCitizenship(request.getParameter("citizenship"));
        contactDTO.setGender(request.getParameter("gender"));
        contactDTO.setMaritalStatus(request.getParameter("status"));
        AddressDTO address = new AddressDTO();
        address.setCountry(request.getParameter("country"));
        address.setCity(request.getParameter("city"));
        address.setStreetAddress(request.getParameter("address"));
        address.setIndex(request.getParameter("indexCode"));
        contactDTO.setAddress(address);
        String dateCriteria = request.getParameter("dateCriteria");
        SearchService searchService = new SearchService();
        contactList = searchService.searchContacts(contactDTO, dateCriteria);
        request.setAttribute("contactList", contactList);
        forward("contacts");
    }
}
