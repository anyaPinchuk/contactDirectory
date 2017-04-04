package commands;

import dto.AddressDTO;
import dto.ContactDTO;
import exceptions.MessageError;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import services.SearchService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchContactCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {
        forward("searchContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("search contact command starting");
        MessageError error = new MessageError();
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName(request.getParameter("firstName"));
        contactDTO.setSurname(request.getParameter("surname"));
        contactDTO.setThirdName(request.getParameter("thirdName"));
        String date = request.getParameter("dateOfBirth");
        DateTime dt = null;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        if (StringUtils.isNotEmpty(date.trim())){
            try {
                dt = formatter.parseDateTime(date);
            } catch (IllegalArgumentException e) {
                error.addMessage("Wrong date format");
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
                LOG.info("wrong date format");
                return;
            }
        }
        contactDTO.setDateOfBirth(dt);
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
