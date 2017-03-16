package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import converters.PhoneConverter;
import dao.PhoneDAO;
import dto.ContactDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.Contact;
import entities.PhoneNumber;
import exceptions.GenericDAOException;
import exceptions.MessageError;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EditContactCommand extends FrontCommand {
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;
    private PhoneConverter phoneConverter;

    public EditContactCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
        phoneConverter = new PhoneConverter();
    }

    @Override
    public void processGet() throws ServletException, IOException {
        LOG.info("get contact by id starting ");
        ContactDTO contactDTO = null;
        PhoneDAO phoneDAO = new PhoneDAO();
        Long id = Long.valueOf(request.getParameter("id"));
        Long address_id;
        try {
            Contact contact = contactDAO.findById(id).get();
            address_id = contact.getAddress_id();
            contactDTO = contactConverter.toDTO(Optional.of(contact)).get();
            if (address_id!=0){
                Address address = addressDAO.findById(address_id).get();
                contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).get());
            }
            List<PhoneNumber> numberList = phoneDAO.findAllById(contactDTO.getId());
            List<PhoneDTO> phoneDTOList = new ArrayList<>();
            numberList.forEach(obj->{
                phoneDTOList.add(phoneConverter.toDTO(Optional.of(obj)).get());
            });
            contactDTO.setPhoneDTOList(phoneDTOList);
        } catch (GenericDAOException e) {
            LOG.error("error while processing find contact from EditContactCommand");
            new MessageError(e.getMessage(), e);
        }
        if (contactDTO != null) {
            request.setAttribute("contact", contactDTO);
        }
        forward("editContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("update contact starting ");
        Long id = Long.valueOf(request.getParameter("id"));
        Long address_id = Long.valueOf(request.getParameter("address_id"));
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
            date = format.format(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (country.equals("") && city.equals("") && contactAddress.equals("") && index.equals("")) {
                contact = new Contact(id, name, surname, thirdName, date, sex, citizenship, status,
                        webSite, email, company, null);
                contactDAO.updateById(id, contact);
            } else {
                address = new Address(id, country, city, contactAddress, index);
                if (address_id == 0) {
                    Long addressID = addressDAO.insert(address);
                    contact = new Contact(id, name, surname, thirdName, date, sex, citizenship, status,
                            webSite, email, company, addressID);
                } else {
                    addressDAO.updateById(address_id, address);
                    contact = new Contact(id, name, surname, thirdName, date, sex, citizenship, status,
                            webSite, email, company, address_id);
                }
                contactDAO.updateById(id, contact);
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing update Contact in editContactCommand");
            e.printStackTrace();
        }
        ContactsCommand contactsCommand = new ContactsCommand();
        contactsCommand.init(context, request, response);
        contactsCommand.processGet();

    }
}