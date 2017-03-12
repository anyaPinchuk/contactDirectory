package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String company = request.getParameter("company");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String contactAddress = request.getParameter("address");
        String index = request.getParameter("index");
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        Date dateOfBirth = null;
        Contact contact;
        Address address;
        try {
            dateOfBirth = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (country.equals("") && city.equals("") && contactAddress.equals("") && index.equals("")) {
                contact = new Contact(null, name, surname, thirdName, dateOfBirth, sex, citizenship, status,
                        webSite, email, company, null);
                contactDAO.insert(contact);
            } else {
                address = new Address(null, country, city, contactAddress, index);
                Long address_id = addressDAO.insert(address);
                contact = new Contact(null, name, surname, thirdName, dateOfBirth, sex, citizenship, status,
                        webSite, email, company, null);
                if (address_id != 0) {
                    contact.setAddress_id(address_id);
                }
                contactDAO.insert(contact);
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing insert Contact in AddContactCommand");
            e.printStackTrace();
        }
        ContactsCommand contactsCommand = new ContactsCommand();
        contactsCommand.init(context, request, response);
        contactsCommand.processGet();
    }
}
