package services;

import dao.AddressDAO;
import dao.PhotoDAO;
import entities.Address;
import entities.Contact;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class PhotoService implements ServiceEntity {
    private PhotoDAO photoDAO = new PhotoDAO();
    private static final Logger LOG = Logger.getLogger(ContactService.class);

    public Photo findById(Long id) {
        try (Connection connection = connectionAwareExecutor.connect()) {
            photoDAO.setConnection(connection);
            return photoDAO.findById(id).get();
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get Photo by id in PhotoService");
        }
        return null;
    }

    public void updatePhoto(Long id, Photo photo) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            photoDAO.setConnection(connection);
            photoDAO.updateById(id, photo);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing update Photo by id in PhotoService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public Long insert(String fileName) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            photoDAO.setConnection(connection);
            Long id = photoDAO.insert(new Photo(fileName));
            connection.commit();
            return id;
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing insert Photo by id in PhotoService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return null;
    }

    public void deleteById(Long address_id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            photoDAO.setConnection(connection);
            photoDAO.deleteById(address_id);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing delete Photo by id in PhotoService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
