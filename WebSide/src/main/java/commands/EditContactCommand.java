package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import converters.PhoneConverter;
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
import services.*;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EditContactCommand extends FrontCommand {
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;
    private PhoneConverter phoneConverter;
    private ContactService contactService;
    private AddressService addressService;
    private PhoneService phoneService;
    private AttachmentService attachmentService;
    private PhotoService photoService;

    public EditContactCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
        phoneConverter = new PhoneConverter();
        contactService = new ContactService();
        addressService = new AddressService();
        phoneService = new PhoneService();
        photoService = new PhotoService();
        attachmentService = new AttachmentService();
    }

    @Override
    public void processGet() throws ServletException, IOException {
        LOG.info("get contact by id starting ");
        ContactDTO contactDTO = null;
        String id_param = request.getParameter("id");
        if (id_param == null) return;
        Long id = Long.valueOf(id_param);
        try {
            Contact contact = contactService.findById(id);
            if (contact != null) {
                contactDTO = contactConverter.toDTO(Optional.of(contact)).orElseThrow(() ->
                        new GenericDAOException("contact wasn't converted"));
                Address address = addressService.findById(id);
                contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).orElseThrow(() ->
                        new GenericDAOException("address wasn't converted")));
                List<PhoneNumber> numberList = phoneService.findAllById(id);
                List<PhoneDTO> phoneDTOList = numberList.size() != 0 ? numberList.stream().map(number ->
                        phoneConverter.toDTO(Optional.of(number)).get()).collect(Collectors.toList()) : null;
                contactDTO.setPhoneDTOList(phoneDTOList);
                PhotoDTO photoDTO;
                Photo photo = photoService.findById(id);
                if (photo != null) {
                    photoDTO = new PhotoDTO(photo.getId(), photo.getName());
                    contactDTO.setPhoto(photoDTO);
                }
                List<Attachment> attachments = attachmentService.findAllById(contactDTO.getId());
                request.setAttribute("attachments", attachments);
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing find contact from EditContactCommand");
            forward("unknown");
            new MessageError(e.getMessage(), e);
        }
        if (contactDTO != null) {
            request.setAttribute("contact", contactDTO);
            forward("editContact");
        } else {
            forward("contacts");
        }
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("update contact starting ");
        PhoneService phoneService = new PhoneService();
        AttachmentService attachmentService = new AttachmentService();
        ContactService contactService = new ContactService();
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
                for (FileItem item : formItems) {
                    if (item.isFormField()) {
                        field = item.getString("UTF-8");
                        fieldName = item.getFieldName();
                        switch (fieldName) {
                            case "id": {
                                try {
                                    contact.setId(Long.parseLong(field));
                                    address.setId(contact.getId());
                                } catch (NumberFormatException e) {

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
                service.updatePhoto(contact.getId(), photoItem.getName());
                FileUploadDocuments.saveDocument(request, photoItem, contact.getId(), true);
            }
            phoneService.updatePhones(contact.getId(), numbersForUpdate);
            service.updateAttachments(attachmentsForUpdate, contact.getId());

        } catch (FileUploadException | GenericDAOException e) {
            LOG.error(e.getMessage());
        }
        if (documents.size() != 0) {
            documents.forEach(obj -> {
                if (!obj.getName().equals("")) {
                    FileUploadDocuments.saveDocument(request, obj, contact.getId(), false);
                }
            });
        }
        phoneService.insertPhones(numbersForInsert, contact.getId());
        attachmentService.insertAttachments(attachmentsForInsert, contact.getId());
        contactService.updateContact(contact, address);
        response.sendRedirect("Contacts");
    }
}
