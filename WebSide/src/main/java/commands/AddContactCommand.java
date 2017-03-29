package commands;

import entities.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import services.AttachmentService;
import services.ContactService;
import services.PhoneService;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
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
            if (!CollectionUtils.isEmpty(formItems)) {
                String field, fieldName;
                // iterates over form's fields
                for (FileItem item : formItems) {
                    field = item.getString("UTF-8");
                    if (item.isFormField() && StringUtils.isNotEmpty(field.trim())) {
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
                            case "gender": {
                                contact.setGender(field);
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
                    } else if (!item.isFormField()){
                        documents.add(item);
                    }
                }
            }
            ContactService contactService = new ContactService();
            AttachmentService attachmentService = new AttachmentService();
            PhoneService phoneService = new PhoneService();
            Long contactId = 0L;
            if (StringUtils.isNotEmpty(contact.getName().trim()) && StringUtils.isNotEmpty(contact.getSurname().trim()) &&
                    StringUtils.isNotEmpty(contact.getEmail().trim())) {
                contactId = contactService.insertContact(contact, address);
            }
            phoneService.insertPhones(numbersForInsert, contactId);
            List<Long> ids = attachmentService.insertAttachments(attachments, contactId);
            if (!CollectionUtils.isEmpty(documents) && !CollectionUtils.isEmpty(ids)) {
                for(int i = 0; i < documents.size(); i++){
                    if (StringUtils.isNotEmpty(documents.get(i).getName())) {
                        String fileName = FileUploadDocuments.saveDocument(request, documents.get(i), contactId, ids.get(i), false);
                        attachmentService.updateById(ids.get(i), new Attachment(fileName));
                    }
                }
            }
            response.sendRedirect("Contacts");
        } catch (Exception e) {//error page
            LOG.error(e.getMessage());
        }
    }
}
