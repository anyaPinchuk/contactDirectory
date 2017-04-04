package commands;

import entities.*;
import exceptions.MessageError;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import services.AttachmentService;
import services.ContactService;
import services.PhoneService;
import utilities.FileUploadDocuments;
import utilities.Validator;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class AddContactCommand extends FrontCommand {

    @Override
    public void processGet() throws ServletException, IOException {
        forward("addContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("insert contact command starting ");
        MessageError error = new MessageError();
        Validator validator = new Validator(error);
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> formItems, documents = new ArrayList<>();
        List<PhoneNumber> numbersForInsert = new ArrayList<>();
        Contact contact = new Contact();
        Address address = new Address();
        List<Attachment> attachments = new ArrayList<>();
        try {
            formItems = upload.parseRequest(request);
            if (CollectionUtils.isNotEmpty(formItems)) {
                for (FileItem item : formItems) {
                    String field = item.getString("UTF-8");
                    if (item.isFormField() && StringUtils.isNotEmpty(field.trim())) {
                        String fieldName = item.getFieldName();
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
                                if (StringUtils.isNotEmpty(field.trim())) {
                                    DateTime dt = null;
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
                                    try {
                                        dt = formatter.parseDateTime(field);
                                    } catch (IllegalArgumentException e) {
                                        LOG.info("wrong date format");
                                        error.addMessage("Wrong date format");
                                    }
                                    contact.setDateOfBirth(dt == null ? null : new Date(dt.toDate().getTime()));
                                }
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
                    } else if (!item.isFormField()) {
                        documents.add(item);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(error.getMessages())) {
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
                return;
            }
            ContactService contactService = new ContactService();
            AttachmentService attachmentService = new AttachmentService();
            PhoneService phoneService = new PhoneService();
            Long contactId = 0L;
            if (validator.validContact(contact)) {
                contactId = contactService.insertContact(contact, address);
            }
            if (CollectionUtils.isNotEmpty(error.getMessages())) {
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
                return;
            }
            if (validator.validAttachments(attachments) && validator.validPhones(numbersForInsert)) {
                phoneService.insertPhones(numbersForInsert, contactId);
                if (CollectionUtils.isNotEmpty(documents) && contactId != 0 && attachments.size() == documents.size()) {
                    for (int i = 0; i < documents.size(); i++) {
                        if (StringUtils.isNotEmpty(documents.get(i).getName())) {
                            String fileName = FileUploadDocuments.saveDocument(documents.get(i), contactId, false);
                            attachments.get(i).setFileName(fileName);
                            attachmentService.insertAttachment(attachments.get(i), contactId);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(error.getMessages())) {
                request.getSession().setAttribute("messageList", error.getMessages());
                response.sendRedirect("errorPage");
            } else response.sendRedirect("contacts");
        } catch (Exception e) {
            error.addMessage("error while processing add contact command, please, check input data if it is correct");
            request.getSession().setAttribute("messageList", error.getMessages());
            forward("errorPage");
            LOG.error(e.getMessage());
        }
    }
}
