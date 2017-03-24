package services;

import entities.Address;
import entities.Attachment;
import entities.Contact;
import entities.PhoneNumber;
import exceptions.GenericDAOException;

import java.util.List;

public class InsertEntityService extends EntityService{
    private Long contactId;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public void insertAttachment(List<Attachment> attachmentsForInsert) {
        if (attachmentsForInsert.size() == 0) return;
        attachmentsForInsert.forEach(obj -> {
            try {
                obj.setContact_id(contactId);
                attachmentDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert attachment in InsertEntityService");
                e.printStackTrace();
            }
        });
    }

    public void insertPhone(List<PhoneNumber> numbersForInsert) {
        if (numbersForInsert.size() == 0) return;
        numbersForInsert.forEach(obj -> {
            try {
                obj.setContact_id(contactId);
                phoneDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert phone in InsertEntityService");
                e.printStackTrace();
            }
        });
    }

    public void insertContact(Contact contact, Address address) throws GenericDAOException {
        if (address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals("")
                && address.getIndex().equals("")) {
            contactId = contactDAO.insert(contact);
        } else {
            Long address_id = addressDAO.insert(address);
            if (address_id != 0) {
                contact.setAddress_id(address_id);
            }
            contactId = contactDAO.insertWithAddress(contact);
        }
    }
}
