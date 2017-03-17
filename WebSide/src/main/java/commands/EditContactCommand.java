package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import converters.PhoneConverter;
import dao.PhoneDAO;
import dao.PhotoDAO;
import dto.ContactDTO;
import dto.PhoneDTO;
import dto.PhotoDTO;
import entities.Address;
import entities.Contact;
import entities.PhoneNumber;
import entities.Photo;
import exceptions.GenericDAOException;
import exceptions.MessageError;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EditContactCommand extends FrontCommand {
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;
    private PhoneConverter phoneConverter;
    private PhotoDAO photoDAO = new PhotoDAO();

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
            if (address_id != 0) {
                Address address = addressDAO.findById(address_id).get();
                contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).get());
            }
            List<PhoneNumber> numberList = phoneDAO.findAllById(contactDTO.getId());
            List<PhoneDTO> phoneDTOList = new ArrayList<>();
            numberList.forEach(obj -> {
                phoneDTOList.add(phoneConverter.toDTO(Optional.of(obj)).get());
            });
            contactDTO.setPhoneDTOList(phoneDTOList);
            Photo photo = photoDAO.findById(contact.getPhoto_id()).isPresent()
                    ? photoDAO.findById(contact.getPhoto_id()).get()
                    : null;
            PhotoDTO photoDTO = null;
            if (photo!=null){
                photoDTO = new PhotoDTO(photo.getId(), photo.getName(), photo.getPathToFile());
                contactDTO.setPhoto(photoDTO);
            }
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
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> formItems = null;
        Contact contact = new Contact();
        Address address = new Address();
        try {
            formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                String field, fieldName;
                // iterates over form's fields
                for (FileItem item : formItems) {
                    if (item.isFormField()) {
                        field = item.getString();
                        fieldName = item.getFieldName();
                        switch (fieldName) {
                            case "id": {
                                contact.setId(Long.parseLong(field));
                                break;
                            }
                            case "address_id": {
                                contact.setAddress_id(Long.parseLong(field));
                                address.setId(contact.getAddress_id());
                                break;
                            }
                            case "name": {
                                contact.setName(field);
                                break;
                            }
                            case "surname": {
                                contact.setSurname(field);
                                break;
                            }
                            case "thirdName": {
                                contact.setThirdName(field);
                                break;
                            }
                            case "dateOfBirth": {
                                contact.setDateOfBirth(field);
                                break;
                            }
                            case "sex": {
                                contact.setSex(field);
                                break;
                            }
                            case "citizenship": {
                                contact.setCitizenship(field);
                                break;
                            }
                            case "status": {
                                contact.setMaritalStatus(field);
                                break;
                            }
                            case "webSite": {
                                contact.setWebSite(field);
                                break;
                            }
                            case "email": {
                                contact.setEmail(field);
                                break;
                            }
                            case "job": {
                                contact.setJob(field);
                                break;
                            }
                            case "country": {
                                address.setCountry(field);
                                break;
                            }
                            case "city": {
                                address.setCity(field);
                                break;
                            }
                            case "address": {
                                address.setStreetAddress(field);
                                break;
                            }
                            case "index": {
                                address.setIndex(field);
                                break;
                            }
                        }
                    } else {
                        //проверка на то чтобы фотография уже добавлена к профилю и не изменилась
                        Photo photo = photoDAO.findById(contact.getPhoto_id()).get();
                        if (!(photo.getName().equals(item.getName()))){
                            Long photo_id = photoDAO.insert(new Photo(item.getName(), FileUploadDocuments.getFileDirectory()));
                            contact.setPhoto_id(photo_id);
                            FileUploadDocuments.saveFile(request, item);
                        }
                    }
                }
            }
        } catch (FileUploadException | GenericDAOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////
        updateContact(contact, address);
        response.sendRedirect("Contacts");
    }

    public void updateContact(Contact contact, Address address) {
        Date dateOfBirth;
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        try {
            dateOfBirth = format.parse(contact.getDateOfBirth());
            contact.setDateOfBirth(format.format(dateOfBirth));
        } catch (ParseException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        Long address_id = address.getId();
        try {
            if (address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals("")
                    && address.getIndex().equals("")) {
                contact.setAddress_id(null);
                contactDAO.updateById(contact.getId(), contact);
            } else {
                if (address_id == 0) {
                    Long addressID = addressDAO.insert(address);
                    contact.setAddress_id(addressID);
                } else {
                    addressDAO.updateById(address_id, address);
                    contact.setAddress_id(address_id);
                }
                contactDAO.updateById(contact.getId(), contact);
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing update Contact in editContactCommand");
            e.printStackTrace();
        }

    }
}
