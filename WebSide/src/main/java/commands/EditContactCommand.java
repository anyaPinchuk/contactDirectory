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
    private AttachmentConverter attachmentConverter;
    private PhotoDAO photoDAO = new PhotoDAO();
    private PhoneDAO phoneDAO = new PhoneDAO();

    public EditContactCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
        phoneConverter = new PhoneConverter();
        attachmentConverter = new AttachmentConverter();
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
                photoDTO = new PhotoDTO(photo.getId(), photo.getName(), photo.getPathToFile());
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
        List<FileItem> formItems = null;
        List<FileItem> documents = new ArrayList<>();
        Contact contact = new Contact();
        List<PhoneNumber> numbersForUpdate = new ArrayList<>();
        List<PhoneNumber> numbersForInsert = new ArrayList<>();
        List<Attachment> attachmentsForInsert = new ArrayList<>();
        List<Attachment> attachmentsForUpdate = new ArrayList<>();
        Address address = new Address();
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
                                if(field.equals("")){
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
                        //проверка на то чтобы фотография уже добавлена к профилю и не изменилась
                        if ((!item.getName().equals("") && !item.getFieldName().contains("attachment"))) {
                            String fileName = item.getName();
                            if (!(photoDAO.findByField(fileName).isPresent())) {
                                Long photo_id = photoDAO.insert(new Photo(item.getName(), FileUploadDocuments.getFileDirectory(true)));
                                contact.setPhoto_id(photo_id);
                                FileUploadDocuments.saveDocument(request, item, null, true);
                            }
                        } else if (item.getFieldName().contains("attachment")) {
                            documents.add(item);
                        }
                    }
                }
            }
        } catch (FileUploadException | GenericDAOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
//        if (contact.getPhoto_id() != null) {
//            updatePhoto(contact);
//        }

        if (numbersForUpdate.size() != 0) {
            numbersForUpdate.forEach(obj -> {
                obj.setContact_id(contact.getId());
            });

        }
        updatePhone(contact.getId(), numbersForUpdate);
        if (numbersForInsert.size() != 0) {
            numbersForInsert.forEach(obj -> {
                obj.setContact_id(contact.getId());
            });
            insertPhone(numbersForInsert);
        }

        ///////////////////////////////

        if (attachmentsForUpdate.size() != 0) {
            updateAttachments(attachmentsForUpdate, contact.getId());
        }

        //////////////////////////////
        if (documents.size() != 0) {
            documents.forEach(obj -> {
                if (!obj.getName().equals("")) {
                    FileUploadDocuments.saveDocument(request, obj, contact.getId(), false);
                }
            });
        }
        if (attachmentsForInsert.size() != 0) {
            insertAttachments(attachmentsForInsert, contact.getId());

        }
        ////////////////////////////////////////////////////////
        updateContact(contact, address);
        response.sendRedirect("Contacts");
    }

    public void updatePhoto(Contact contact) {
        //TODO rewrite function, to call updateContact func
        try {
            contactDAO.findById(contact.getId()).ifPresent(o -> {
                if (!(o.getPhoto_id().equals(contact.getPhoto_id()))) {
                    try {
                        photoDAO.findById(o.getPhoto_id()).ifPresent(obj -> {
                            //if (FileUploadDocuments.deleteDocument(obj.getName(), true, null)) {
                            FileUploadDocuments.deleteDocument(obj.getName(), true, null);
                                Contact contact1 = new Contact();
                                contact1.setId(contact.getId());
                                contact1.setDateOfBirth(null);
                                contact1.setPhoto_id(contact.getPhoto_id());
                                try {
                                    contactDAO.updateById(contact1.getId(), contact1);
                                    photoDAO.deleteById(obj.getId());
                                } catch (GenericDAOException e) {
                                    LOG.error("error while processing update contact or delete photo by id in editContactCommand");
                                    e.printStackTrace();
                                }
                           // } else {
                                //LOG.info("photo was not deleted on disk");
                           // }
                        });
                    } catch (GenericDAOException e) {
                        LOG.error("error while processing find photo by id in editContactCommand");
                        e.printStackTrace();
                    }
                }
            });
        } catch (GenericDAOException e) {
            LOG.error("error while processing find contact by id in editContactCommand");
            e.printStackTrace();
        }
    }


    private void updateAttachments(List<Attachment> attachmentsForUpdate, Long contact_id) {
        try {
            List<Attachment> attachments = attachmentDAO.findAllById(contact_id);
            attachmentsForUpdate.forEach(obj -> {
                try {
                    if (attachments.contains(obj)) {
                        if (FileUploadDocuments.renameDocument(attachmentDAO.findById(obj.getId()).get().getFileName(),
                                obj.getFileName(), contact_id)) {
                            attachmentDAO.updateById(obj.getId(), obj);
                            attachments.remove(obj);
                        } else {
                            LOG.info("the attachment was not updated");
                        }
                    }
                } catch (GenericDAOException e) {
                    LOG.error("error while processing update attachment by id in editContactCommand");
                    e.printStackTrace();
                }
            });
            attachments.forEach(object -> {
                try {
                    attachmentDAO.deleteById(object.getId());
                    FileUploadDocuments.deleteDocument(object.getFileName(), false, contact_id);
                } catch (GenericDAOException e) {
                    LOG.error("error while processing delete attachment by id in editContactCommand");
                    e.printStackTrace();
                }
            });
        } catch (GenericDAOException e) {
            LOG.error("error while processing find all attachments in EditContactCommand");
            e.printStackTrace();
        }
    }

    private void insertAttachments(List<Attachment> attachmentsForInsert, Long id) {
        attachmentsForInsert.forEach(obj -> {
            try {
                obj.setContact_id(id);
                attachmentDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert attachment in EditContactCommand");
                e.printStackTrace();
            }
        });
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
