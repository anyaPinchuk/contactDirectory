package services;

import dao.PhotoDAO;
import entities.Photo;
import exceptions.GenericDAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class PhotoService implements ServiceEntity {
    private PhotoDAO photoDAO = new PhotoDAO();
    private static final Logger LOG = LoggerFactory.getLogger(ContactService.class);

    public Photo findById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            photoDAO.setConnection(connection);
            return photoDAO.findById(id).isPresent() ? photoDAO.findById(id).get() : null;
        } catch (GenericDAOException e) {
            LOG.error("error while processing get Photo by id in PhotoService");
        }finally {
            connectionAwareExecutor.closeConnection(connection);
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

    public Long insert(String fileName, Long contactId) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            photoDAO.setConnection(connection);
            Long id = photoDAO.insert(new Photo(fileName, contactId));
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
