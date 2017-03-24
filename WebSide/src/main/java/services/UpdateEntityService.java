package services;

import dao.PhoneDAO;
import entities.*;
import exceptions.GenericDAOException;
import utilities.FileUploadDocuments;

import java.util.List;

public class UpdateEntityService extends EntityService{

    public void updateAttachment(List<Attachment> listForUpdate, Long contact_id) throws GenericDAOException{
        if (listForUpdate.size() == 0) return;
        List<Attachment> attachments = attachmentDAO.findAllById(contact_id);
        listForUpdate.forEach(obj -> {
            obj.setContact_id(contact_id);
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
    }
    public void updatePhone(Long contact_id, List<PhoneNumber> phoneNumbersForUpdate) throws GenericDAOException {
        if (phoneNumbersForUpdate.size() == 0) return;
        List<PhoneNumber> numbers = phoneDAO.findAllById(contact_id);
        phoneNumbersForUpdate.forEach(obj -> {
            obj.setContact_id(contact_id);
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
    }

    public Long updatePhoto(Contact contact, String fileName) throws GenericDAOException {
        if (fileName == null) return null;
        Contact contact1 = contactDAO.findById(contact.getId()).get();
        if (contact1.getPhoto_id() != 0) {
            try {
                photoDAO.findById(contact1.getPhoto_id()).ifPresent(obj -> {
                    try {
                        FileUploadDocuments.deleteDocument(obj.getName(), true, null);
                        obj.setName(fileName);
                        photoDAO.updateById(obj.getId(), obj);
                    } catch (GenericDAOException e) {
                        LOG.error("error while processing update photo by id in editContactCommand");
                        e.printStackTrace();
                    }
                });
            } catch (GenericDAOException e) {
                LOG.error("error while processing find photo by id in editContactCommand");
                e.printStackTrace();
            }
        } else {
            try {
                return photoDAO.insert(new Photo(fileName));
            } catch (GenericDAOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void updateContact(Contact contact, Address address) {
        Long address_id = address.getId();
        try {
            if (address_id == null) {
                if (!(address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals(""))) {
                    address_id = addressDAO.insert(address);
                }
            } else {
                addressDAO.updateById(address_id, address);
            }
            contact.setAddress_id(address_id);
            contactDAO.updateById(contact.getId(), contact);
        } catch (GenericDAOException e) {
            LOG.error("error while processing update Contact in editContactCommand");
            e.printStackTrace();
        }

    }
}
