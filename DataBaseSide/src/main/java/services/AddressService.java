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
            return addressDAO.findById(id).isPresent() ? addressDAO.findById(id).get(): null;
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get address by id in AddressService");
        }
        return null;
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
