package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import dto.AttachmentDTO;
import dto.PhoneDTO;
import entities.*;
import exceptions.GenericDAOException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddContactCommand extends FrontCommand {

    @Override
    public void processGet() throws ServletException, IOException {
        forward("addContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("insert contact starting ");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> formItems = null;
        List<FileItem> documents = new ArrayList<>();
        List<PhoneNumber> numbersForInsert = new ArrayList<>();
        Contact contact = new Contact();
        Address address = new Address();
        List<Attachment> attachments = new ArrayList<>();
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
                            case "hiddenInfoForInsert": {
                                String[] objects = field.split(";");
                                String comment = objects.length == 3 ? "" : objects[3];
                                java.sql.Date date = java.sql.Date.valueOf(objects[2]);
                                attachments.add(new Attachment(null, date, objects[1], comment, null));
                                break;
                            }
                            case "hiddens": {
                                String[] objects = field.split(";");
                                String comment = objects.length == 4 ? "" : objects[4];
                                numbersForInsert.add(new PhoneNumber(null, objects[0], objects[1], objects[2], objects[3], comment, null));
                                break;
                            }
                        }
                    } else {
                        documents.add(item);
                    }
                }
            }
            Long contact_id = insertContact(contact, address);
            numbersForInsert.forEach(obj -> {
                obj.setContact_id(contact_id);
                try {
                    phoneDAO.insert(obj);
                } catch (GenericDAOException e) {
                    LOG.error("error while processing insert Phone in AddContactCommand");
                    e.printStackTrace();
                }
            });

            if (documents.size() != 0) {
                documents.forEach(obj -> {
                    if (!obj.getName().equals("")) {
                        FileUploadDocuments.saveDocument(request, obj, contact_id, false);
                    }
                });
            }
            if (attachments.size() != 0) {
                insertAttachments(attachments, contact_id);
            }
            response.sendRedirect("Contacts");
        } catch (FileUploadException | GenericDAOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertAttachments(List<Attachment> attachments, Long contact_id) {
        attachments.forEach(obj -> {
            try {
                obj.setContact_id(contact_id);
                attachmentDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert attachment in AddContactCommand");
                e.printStackTrace();
            }
        });
    }

    private Long insertContact(Contact contact, Address address) throws GenericDAOException {
            if (address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals("")
                    && address.getIndex().equals("")) {
                return contactDAO.insert(contact);
            } else {
                Long address_id = addressDAO.insert(address);
                if (address_id != 0) {
                    contact.setAddress_id(address_id);
                }
                return contactDAO.insertWithAddress(contact);
            }
    }
}
