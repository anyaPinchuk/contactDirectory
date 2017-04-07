package services;

import dao.AddressDAO;
import dao.ContactDAO;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.ServiceException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService implements ServiceEntity {
    private static final Logger LOG = LoggerFactory.getLogger(ContactService.class);
    private ContactDAO contactDAO;
    private AddressDAO addressDAO;

    public ContactService() {
        contactDAO = new ContactDAO();
        addressDAO = new AddressDAO();
    }

    public List<Contact> findByParts(int start, int count) {
        List<Contact> contacts = null;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            contactDAO.setConnection(connection);
            contacts = contactDAO.findByParts(start, count);
        } catch (GenericDAOException e) {
            LOG.error("error while processing get contacts");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return contacts;
    }

    public Contact findById(Long contactId) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            contactDAO.setConnection(connection);
            return contactDAO.findById(contactId).isPresent() ? contactDAO.findById(contactId).get() : null;
        } catch (GenericDAOException e) {
            LOG.error("error while processing get contact by id ");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public int getCountRows() {
        int count = 0;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            count = contactDAO.getCountRows();
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing get count rows");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return count;
    }

    public List<Contact> findByCriteria(Contact contact, Address address, DateTime fromDate, DateTime toDate) {
        List<Contact> contacts = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            contactDAO.setConnection(connection);
            contacts = contactDAO.findByCriteria(contact, address, fromDate, toDate);
        } catch (GenericDAOException e) {
            LOG.error("error while processing get contacts by criteria");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return contacts;
    }

    public List<Contact> findByDate() {
        List<Contact> contacts = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            contactDAO.setConnection(connection);
            contacts = contactDAO.findByDate();
        } catch (GenericDAOException e) {
            LOG.error("error while processing get contacts ");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return contacts;
    }

    public List<String> findEmailsById(Long[] ids) {
        List<String> emails = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            contactDAO.setConnection(connection);
            for (Long id : ids) {
                emails.add(contactDAO.findEmailById(id));
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing get contact by id ");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return emails;
    }

    public void updateContact(Contact contact, Address address) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            contactDAO.updateById(contact.getId(), contact);
            addressDAO.setConnection(connection);
            addressDAO.updateById(contact.getId(), address);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing update Contact");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }

    }

    public Long insertContact(Contact contact, Address address) {
        Long id;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            addressDAO.setConnection(connection);
            id = contactDAO.insert(contact);
            address.setContactId(id);
            addressDAO.insert(address);
            connection.commit();
            return id;
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing insert Contact");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public Contact findAndDeleteById(Long id) {
        Connection connection = null;
        Contact contact;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            contact = contactDAO.findById(id).isPresent() ? contactDAO.findById(id).get() : new Contact();
            contactDAO.deleteById(id);
            connection.commit();
            return contact;
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing delete Contact");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public ContactDAO getContactDAO() {
        return contactDAO;
    }

    public void setContactDAO(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    public List<Contact> findContactsByEmails(String[] emails) {
        List<Contact> contacts = new ArrayList<>();
        Contact contact;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            contactDAO.setConnection(connection);
            for (String email : emails) {
                if((contact = contactDAO.findByEmail(email)) != null){
                    contacts.add(contact);
                }
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing get contacts by emails");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return contacts;
    }
}
