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
    private PhoneDAO phoneDAO = new PhoneDAO();

    public EditContactCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
        phoneConverter = new PhoneConverter();
    }

    @Override
    public void processGet() throws ServletException, IOException {
        LOG.info("get contact by id starting ");
        ContactDTO contactDTO = null;
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
            if (photo != null) {
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
        List<PhoneNumber> numbersForUpdate = new ArrayList<>();
        List<PhoneNumber> numbersForInsert = new ArrayList<>();
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
                                if (!field.equals("")) {
                                    contact.setAddress_id(Long.parseLong(field));
                                    address.setId(contact.getAddress_id());
                                }
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
                            case "hiddens": {
                                String[] objects = field.split(";");
                                String comment = objects.length == 4 ? "" : objects[4];
                                numbersForInsert.add(new PhoneNumber(null, objects[0], objects[1], objects[2], objects[3], comment, null));
                                break;
                            }
                            case "hiddensForUpdate": {
                                String[] objects = field.split(";");
                                String comment = objects.length == 5 ? "" : objects[5];
                                Long id = Long.parseLong(objects[0]);
                                numbersForUpdate.add(new PhoneNumber(id, objects[1], objects[2], objects[3], objects[4], comment, null));
                                break;
                            }
                        }
                    } else {
                        //проверка на то чтобы фотография уже добавлена к профилю и не изменилась
                        if (!item.getName().equals("")) {
                            String fileName = item.getName();
                            if (!(photoDAO.findByField(fileName).isPresent())) {
                                Long photo_id = photoDAO.insert(new Photo(item.getName(), FileUploadDocuments.getFileDirectory()));
                                contact.setPhoto_id(photo_id);
                                FileUploadDocuments.saveFile(request, item);
                            } else {
                                Photo photo = photoDAO.findByField(fileName).get();
                            }
                        }

                    }
                }
            }
        } catch (FileUploadException | GenericDAOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }

        if (numbersForUpdate.size() != 0) {
            numbersForUpdate.forEach(obj -> {
                obj.setContact_id(contact.getId());
            });
            updatePhone(contact.getId(), numbersForUpdate);
        }

        if (numbersForInsert.size() != 0) {
            numbersForInsert.forEach(obj -> {
                obj.setContact_id(contact.getId());
            });
            insertPhone(numbersForInsert);
        }
        ////////////////////////////////////////////////////////
        updateContact(contact, address);
        response.sendRedirect("Contacts");
    }

    private void insertPhone(List<PhoneNumber> numbersForInsert) {
        numbersForInsert.forEach(obj -> {
            try {
                phoneDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert phone in editContactCommand");
                e.printStackTrace();
            }
        });
    }

    public void updateContact(Contact contact, Address address) {
        Date dateOfBirth;
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        if (!contact.getDateOfBirth().equals("")) {
            try {
                dateOfBirth = format.parse(contact.getDateOfBirth());
                contact.setDateOfBirth(format.format(dateOfBirth));
            } catch (ParseException e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
            }
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

    public void updatePhone(Long contact_id, List<PhoneNumber> phoneNumbersForUpdate) {
        try {
            List<PhoneNumber> numbers = phoneDAO.findAllById(contact_id);
            phoneNumbersForUpdate.forEach(obj -> {
                try {
                    if (numbers.contains(obj)) {
                        phoneDAO.updateById(obj.getId(), obj);
                        numbers.remove(obj);
                    }
                } catch (GenericDAOException e) {
                    LOG.error("error while processing update phone by id in editContactCommand");
                    e.printStackTrace();
                }
            });
            numbers.forEach(obj -> {
                try {
                    phoneDAO.deleteById(obj.getId());
                } catch (GenericDAOException e) {
                    LOG.error("error while processing update phone by id in editContactCommand");
                    e.printStackTrace();
                }
            });
        } catch (GenericDAOException e) {
            LOG.error("error while processing find all phones by id in editContactCommand");
            e.printStackTrace();
        }
    }
}
