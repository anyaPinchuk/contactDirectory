package services;

import dao.ContactDAO;
import entities.Contact;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.util.List;

public class ContactService {
    private static final Logger LOG = Logger.getLogger(ContactService.class);
    private ContactDAO contactDAO ;

    public ContactService() {
        contactDAO = new ContactDAO();
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

    public int getCountRows(){
        int count = 0;
        try {
            count = contactDAO.getCountRows();
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public ContactDAO getContactDAO() {
        return contactDAO;
    }

    public void setContactDAO(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }
}
