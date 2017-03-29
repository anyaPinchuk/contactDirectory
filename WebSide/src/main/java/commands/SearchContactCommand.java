package commands;

import dto.AddressDTO;
import dto.ContactDTO;
import org.apache.commons.lang3.StringUtils;
import services.SearchService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SearchContactCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {

        forward("searchContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName(request.getParameter("firstName"));
        contactDTO.setSurname(request.getParameter("surname"));
        contactDTO.setThirdName(request.getParameter("thirdName"));
        //validator date and other fields
        String date = request.getParameter("dateOfBirth");
        if (StringUtils.isNotEmpty(date.trim())) {
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
        List<ContactDTO> contactList = searchService.searchContacts(contactDTO, dateCriteria);
        List<Integer> pageList = new ArrayList<>();
        int pageCount = contactList.size() % 10 == 0 ? contactList.size() / 10 : contactList.size() / 10 + 1;
        for (int i = 1; i <= pageCount; i++) {
            pageList.add(i);
        }
        request.getSession().setAttribute("pageList", pageList);
        request.getSession().setAttribute("isSearch", true);
        request.getSession().setAttribute("contactList", contactList);
        response.sendRedirect("contacts?page=1");
    }
}
