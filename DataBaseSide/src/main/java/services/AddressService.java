package services;

import dao.AddressDAO;
import entities.Address;
import entities.Attachment;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AddressService implements ServiceEntity {
    private AddressDAO addressDAO = new AddressDAO();
    private static final Logger LOG = Logger.getLogger(AddressService.class);

    public Address findById(Long id) {
        try (Connection connection = connectionAwareExecutor.connect()) {
            addressDAO.setConnection(connection);
            return addressDAO.findById(id).get();
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get address by id in AddressService");
        }
        return null;
    }

    public void insert(Address address, Long contactId) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            addressDAO.setConnection(connection);
            address.setContactId(contactId);
            addressDAO.insert(address);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing insert address in attachmentService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public void updateById(Long id, Address address) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            addressDAO.setConnection(connection);
            addressDAO.updateById(id, address);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing update address by id in AddressService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public AddressDAO getAddressDAO() {
        return addressDAO;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public void deleteById(Long address_id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            addressDAO.setConnection(connection);
            addressDAO.deleteById(address_id);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing delete address by id in AddressService");
            connectionAwareExecutor.rollbackConnection(connection);
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
