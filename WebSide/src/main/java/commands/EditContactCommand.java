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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import services.*;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EditContactCommand extends FrontCommand {
    private static ContactConverter contactConverter = new ContactConverter();
    private static AddressConverter addressConverter = new AddressConverter();
    private static PhoneConverter phoneConverter = new PhoneConverter();
    private static ContactService contactService = new ContactService();
    private static AddressService addressService = new AddressService();
    private static PhoneService phoneService = new PhoneService();
    private static AttachmentService attachmentService = new AttachmentService();
    private static PhotoService photoService = new PhotoService();

    @Override
    public void processGet() throws ServletException, IOException {
        ContactDTO contactDTO = null;
        String paramId = request.getParameter("id");
        LOG.info("get contact by id {} starting in editContactCommand", paramId);
        if (!StringUtils.isNotEmpty(paramId.trim())) forward("unknown");
        try {
            Long id = Long.valueOf(paramId);
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
        } catch (NumberFormatException | GenericDAOException e) {
            LOG.error("error while parsing id or find contact from EditContactCommand");
            forward("unknown");
            new MessageError(e.getMessage(), e);
        }
        if (contactDTO != null) {
            request.setAttribute("contact", contactDTO);
            forward("editContact");
        } else {
            forward("unknown");
        }
    }

    @Override
    public void processPost() throws ServletException, IOException {
        LOG.info("update contact command starting ");
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
            if (!CollectionUtils.isEmpty(formItems)) {
                String field, fieldName;
                for (FileItem item : formItems) {
                    field = item.getString("UTF-8");
                    if (item.isFormField() && StringUtils.isNotEmpty(field.trim())) {
                        fieldName = item.getFieldName();
                        switch (fieldName) {
                            case "id": {
                                try {
                                    contact.setId(Long.parseLong(field));
                                    address.setId(contact.getId());
                                } catch (NumberFormatException e) {
                                    //to error page
                                    LOG.error("error while parsing id {}", field);
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
                    } else if (!item.isFormField()) {
                        if (StringUtils.isNotEmpty(item.getName())) {
                            if (!item.getFieldName().contains("attachment")) photoItem = item;
                            else if (item.getFieldName().contains("attachment")) documents.add(item);
                        }
                    }
                }
            }
            if (photoItem != null) {
                Long photoId = service.updatePhoto(contact.getId(), photoItem.getName());
                if (photoId != 0) {
                    FileUploadDocuments.saveDocument(request, photoItem, contact.getId(), null, true);
                }
            }
            phoneService.updatePhones(contact.getId(), numbersForUpdate);
            service.updateAttachments(attachmentsForUpdate, contact.getId());

        } catch (FileUploadException | GenericDAOException e) {
            LOG.error("error while uploading files or updating phones");
        }
        phoneService.insertPhones(numbersForInsert, contact.getId());
        List<Long> ids = attachmentService.insertAttachments(attachmentsForInsert, contact.getId());
        if (!CollectionUtils.isEmpty(documents) && !CollectionUtils.isEmpty(ids)) {
            for (int i = 0; i < documents.size(); i++) {
                if (StringUtils.isNotEmpty(documents.get(i).getName())) {
                    String fileName = FileUploadDocuments.saveDocument(request, documents.get(i), contact.getId(), ids.get(i), false);
                    attachmentService.updateById(ids.get(i), new Attachment(fileName));
                }
            }
        }
        if (contact.getName() != null && contact.getSurname() != null && contact.getEmail() != null) {
            contactService.updateContact(contact, address);
        }
        response.sendRedirect("contacts");
    }
}
