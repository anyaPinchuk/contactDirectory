package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import dto.PhoneDTO;
import entities.Address;
import entities.Contact;
import entities.PhoneNumber;
import exceptions.GenericDAOException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddContactCommand extends FrontCommand {

    @Override
    public void processGet() throws ServletException, IOException {
        forward("addContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("insert contact starting ");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String thirdName = request.getParameter("thirdName");
        String date = request.getParameter("dateOfBirth");
        String sex = request.getParameter("sex");
        String citizenship = request.getParameter("citizenship");
        String status = request.getParameter("status");
        String webSite = request.getParameter("webSite");
        String email = request.getParameter("email");
        String company = request.getParameter("job");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String contactAddress = request.getParameter("address");
        String index = request.getParameter("index");
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        String[] inputs = request.getParameterValues("hiddens");
        List<PhoneNumber> numbers = new ArrayList<>();
        if (inputs != null){
            Arrays.stream(inputs).forEach(obj -> {
                String[] objects = obj.split(";");
                String comment = objects.length == 4 ? null : objects[4];
                numbers.add(new PhoneNumber(null, objects[0], objects[1], objects[2], objects[3], comment, null));
            });
        }
        Date dateOfBirth = null;
        Contact contact;
        Address address;
        Long contact_id;
        try {
            dateOfBirth = format.parse(date);
            date = format.format(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (country.equals("") && city.equals("") && contactAddress.equals("") && index.equals("")) {
                contact = new Contact(null, name, surname, thirdName, date, sex, citizenship, status,
                        webSite, email, company, null);
                contact_id = contactDAO.insert(contact);
            } else {
                address = new Address(null, country, city, contactAddress, index);
                Long address_id = addressDAO.insert(address);
                contact = new Contact(null, name, surname, thirdName, date, sex, citizenship, status,
                        webSite, email, company, null);
                if (address_id != 0) {
                    contact.setAddress_id(address_id);
                }
                contact_id = contactDAO.insertWithAddress(contact);
            }
            numbers.forEach(obj -> {
                obj.setContact_id(contact_id);
                try {
                    phoneDAO.insert(obj);
                } catch (GenericDAOException e) {
                    LOG.error("error while processing insert Phone in AddContactCommand");
                    e.printStackTrace();
                }
            });
        } catch (GenericDAOException e) {
            LOG.error("error while processing insert Contact in AddContactCommand");
            e.printStackTrace();
        }
        response.sendRedirect("Contacts");
    }
}
