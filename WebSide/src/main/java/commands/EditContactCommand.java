package commands;

import converters.AddressConverter;
import converters.AttachmentConverter;
import converters.ContactConverter;
import converters.PhoneConverter;
import dao.PhoneDAO;
import dao.PhotoDAO;
import dto.AttachmentDTO;
import dto.ContactDTO;
import dto.PhoneDTO;
import dto.PhotoDTO;
import entities.*;
import exceptions.GenericDAOException;
import exceptions.MessageError;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import services.EntityService;
import services.InsertEntityService;
import services.UpdateEntityService;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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

            ///////////////////////////////////////////////////////////////////////

            List<PhoneNumber> numberList = phoneDAO.findAllById(contactDTO.getId());
            List<PhoneDTO> phoneDTOList = new ArrayList<>();
            numberList.forEach(obj -> {
                phoneDTOList.add(phoneConverter.toDTO(Optional.of(obj)).get());
            });
            contactDTO.setPhoneDTOList(phoneDTOList);

            ///////////////////////////////////////////////////

            Photo photo = photoDAO.findById(contact.getPhoto_id()).isPresent()
                    ? photoDAO.findById(contact.getPhoto_id()).get()
                    : null;
            PhotoDTO photoDTO = null;
            if (photo != null) {
                photoDTO = new PhotoDTO(photo.getId(), photo.getName());
                contactDTO.setPhoto(photoDTO);
            }

            /////////////////////////////////////////////

            List<Attachment> attachments = attachmentDAO.findAllById(contactDTO.getId());
            if (attachments != null) {
                request.setAttribute("attachments", attachments);
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
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> formItems, documents = new ArrayList<>();
        Contact contact = new Contact();
        List<PhoneNumber> numbersForUpdate = new ArrayList<>(), numbersForInsert = new ArrayList<>();
        List<Attachment> attachmentsForInsert = new ArrayList<>(), attachmentsForUpdate = new ArrayList<>();
        Address address = new Address();
        FileItem photoItem = null;
        UpdateEntityService service = new UpdateEntityService();
        try {
            formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                String field, fieldName;
                // iterates over form's fields
                for (FileItem item : formItems) {
                    if (item.isFormField()) {
                        field = item.getString("UTF-8");
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
                                if (field.equals("")) {
                                    contact.setDateOfBirth(null);
                                } else
                                    contact.setDateOfBirth(java.sql.Date.valueOf(field));
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
                            case "hiddenInfoForInsert": {
                                String[] objects = field.split(";");
                                String comment = objects.length == 3 ? "" : objects[3];
                                java.sql.Date date = java.sql.Date.valueOf(objects[2]);
                                attachmentsForInsert.add(new Attachment(null, date, objects[1], comment, null));
                                break;
                            }
                            case "hiddenInfoForUpdate": {
                                String[] objects = field.split(";");
                                String comment = objects.length == 3 ? "" : objects[3];
                                java.sql.Date date = java.sql.Date.valueOf(objects[2]);
                                Long id = Long.parseLong(objects[0]);
                                attachmentsForUpdate.add(new Attachment(id, date, objects[1], comment, null));
                                break;
                            }
                        }
                    } else {
                        if ((!item.getName().equals("") && !item.getFieldName().contains("attachment"))) {
                            photoItem = item;
                        } else if (item.getFieldName().contains("attachment")) {
                            documents.add(item);
                        }
                    }
                }
            }
            if (photoItem != null) {
                contact.setPhoto_id(service.updatePhoto(contact, photoItem.getName()));
                FileUploadDocuments.saveDocument(request, photoItem, null, true);
            }
            service.updatePhone(contact.getId(), numbersForUpdate);
            service.updateAttachment(attachmentsForUpdate, contact.getId());

        } catch (FileUploadException | GenericDAOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        if (documents.size() != 0) {
            documents.forEach(obj -> {
                if (!obj.getName().equals("")) {
                    FileUploadDocuments.saveDocument(request, obj, contact.getId(), false);
                }
            });
        }
        InsertEntityService insertEntityService = new InsertEntityService();
        insertEntityService.setContactId(contact.getId());
        insertEntityService.insertPhone(numbersForInsert);
        insertEntityService.insertAttachment(attachmentsForInsert);
        service.updateContact(contact, address);
        response.sendRedirect("Contacts");
    }
}
