package services;

import dao.AddressDAO;
import entities.Address;
import exceptions.GenericDAOException;
import exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class AddressService implements ServiceEntity {
    private AddressDAO addressDAO = new AddressDAO();
    private static final Logger LOG = LoggerFactory.getLogger(AddressService.class);

    public Address findById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            addressDAO.setConnection(connection);
            return addressDAO.findById(id).isPresent() ? addressDAO.findById(id).get(): null;
        } catch (GenericDAOException e) {
            LOG.error("error while processing get address by id ");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
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
            LOG.error("error while processing delete address by id");
            connectionAwareExecutor.rollbackConnection(connection);
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
