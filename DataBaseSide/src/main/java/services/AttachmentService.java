package services;

import dao.AttachmentDAO;
import entities.Attachment;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AttachmentService implements ServiceEntity{
    private AttachmentDAO attachmentDAO = new AttachmentDAO();
    private static final Logger LOG = Logger.getLogger(ContactService.class);

    public List<Attachment> findAllById(Long id) {
        try(Connection connection = connectionAwareExecutor.connect())  {
            attachmentDAO.setConnection(connection);
            return attachmentDAO.findAllById(id);
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get all attachments by id in attachmentService");
        }
        return null;
    }

    public void insertAttachments(List<Attachment> attachmentsForInsert, Long contactId) {
        if (attachmentsForInsert.size() == 0) return;
        attachmentsForInsert.forEach(obj -> {
            Connection connection = null;
            try {
                connection = connectionAwareExecutor.connect();
                connection.setAutoCommit(false);
                attachmentDAO.setConnection(connection);
                obj.setContact_id(contactId);
                attachmentDAO.insert(obj);
                connection.commit();
            } catch (GenericDAOException | SQLException e) {
                connectionAwareExecutor.rollbackConnection(connection);
                LOG.error("error while processing insert attachment in attachmentService");
            }finally {
                connectionAwareExecutor.closeConnection(connection);
            }
        });
    }

    public void deleteById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            attachmentDAO.setConnection(connection);
            attachmentDAO.deleteById(id);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing delete attachment by id in attachmentService");
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public Attachment findById(Long id) {
        try(Connection connection = connectionAwareExecutor.connect())  {
            attachmentDAO.setConnection(connection);
            return attachmentDAO.findById(id).get();
        } catch (GenericDAOException | SQLException e) {
            LOG.error("error while processing get attachment by id in attachmentService");
        }
        return null;
    }

    public void updateById(Long id, Attachment attachment) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            attachmentDAO.setConnection(connection);
            attachmentDAO.updateById(id, attachment);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing get attachment by id in attachmentService");
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
