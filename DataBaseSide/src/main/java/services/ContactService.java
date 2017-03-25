package services;

import dao.AddressDAO;
import dao.ContactDAO;
import db.ConnectionAwareExecutor;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ContactService implements ServiceEntity{
    private static final Logger LOG = Logger.getLogger(ContactService.class);
    private ContactDAO contactDAO;
    private AddressDAO addressDAO;

    public ContactService() {
        contactDAO = new ContactDAO();
        addressDAO = new AddressDAO();
    }

    public List<Contact> findByParts(int start, int count) {
        LOG.info("find contacts starting");
        List<Contact> contacts = null;
        try (Connection connection = connectionAwareExecutor.connect()) {
            contactDAO.setConnection(connection);
            contacts = contactDAO.findByParts(start, count);
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get contacts in ContactService");
            e.printStackTrace();
        }
        return contacts;
    }

    public Contact findById(Long contact_id) {
        try (Connection connection = connectionAwareExecutor.connect()) {
            contactDAO.setConnection(connection);
            return contactDAO.findById(contact_id).get();
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get contact by id in ContactService");
        }
        return null;
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
            LOG.error(e.getMessage());
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return count;
    }

    public List<Contact> findByCriteria(Contact contact, Address address, String dateCriteria){
        LOG.info("find contacts by criteria starting");
        List<Contact> contacts = null;
        try (Connection connection = connectionAwareExecutor.connect()) {
            contactDAO.setConnection(connection);
            contacts = contactDAO.findByCriteria(contact);
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get contacts in ContactService");
            e.printStackTrace();
        }
        return contacts;
    }

    public void updateContact(Contact contact, Address address) {
        Long address_id = address.getId();
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            addressDAO.setConnection(connection);
            if (address_id == null) {
                if (!(address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals(""))) {
                    address_id = addressDAO.insert(address);
                }
            } else {
                addressDAO.updateById(address_id, address);
            }
            contact.setAddress_id(address_id);
            contactDAO.updateById(contact.getId(), contact);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing update Contact in ContactService");
        }
        finally {
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
            if (address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals("")
                    && address.getIndex().equals("")) {
                id = contactDAO.insert(contact);
            } else {
                Long address_id = addressDAO.insert(address);
                if (address_id != 0) {
                    contact.setAddress_id(address_id);
                }
                id = contactDAO.insertWithAddress(contact);
            }
            connection.commit();
            return id;
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing insert Contact in ContactService");
            e.printStackTrace();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return null;
    }

    public void deleteById(Long id) {
        Connection connection = null;
        try  {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            contactDAO.deleteById(id);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing delete Contact in ContactService");
            e.printStackTrace();
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
}
