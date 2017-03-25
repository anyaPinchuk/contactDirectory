package services;

import dao.AddressDAO;
import dao.ContactDAO;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.util.List;

public class ContactService {
    private static final Logger LOG = Logger.getLogger(ContactService.class);
    private ContactDAO contactDAO ;
    private AddressDAO addressDAO;

    public ContactService() {
        contactDAO = new ContactDAO();
        addressDAO = new AddressDAO();
    }

    public List<Contact> findByParts(int start, int count){
        LOG.info("find contacts starting");
        List<Contact> contacts = null;
        try {
            contacts = contactDAO.findByParts(start, count);
        } catch (GenericDAOException e) {
            LOG.error("error while processng get contacts in ContactService");
            e.printStackTrace();
        }
        return contacts;
    }

    public Contact findById(Long contact_id){
        try {
            return contactDAO.findById(contact_id)
                    .orElseThrow(()->new GenericDAOException("contact was not found"));
        } catch (GenericDAOException e) {
            LOG.error("error while processng get contact by id in AddressService");
            e.printStackTrace();
        }
        return null;
    }

    public int getCountRows(){
        int count = 0;
        try {
            count = contactDAO.getCountRows();
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
        return count;
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

    public Long insertContact(Contact contact, Address address) throws GenericDAOException {
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

    public ContactDAO getContactDAO() {
        return contactDAO;
    }

    public void setContactDAO(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    public void deleteById(Long id) {
        try {
            contactDAO.deleteById(id);
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
    }
}
