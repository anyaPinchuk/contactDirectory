package services;

import dao.AttachmentDAO;
import entities.Attachment;
import exceptions.GenericDAOException;
import exceptions.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AttachmentService implements ServiceEntity {
    private AttachmentDAO attachmentDAO = new AttachmentDAO();
    private static final Logger LOG = LoggerFactory.getLogger(ContactService.class);

    public List<Attachment> findAllById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            attachmentDAO.setConnection(connection);
            return attachmentDAO.findAllById(id);
        } catch (GenericDAOException e) {
            LOG.error("error while processing get all attachments by id");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public Long insertAttachment(Attachment attachment, Long contactId) {
        Long id = null;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            attachmentDAO.setConnection(connection);
            attachment.setContactId(contactId);
                try {
                    id = attachmentDAO.insert(attachment);
                } catch (GenericDAOException e) {
                    LOG.error("error while processing insert attachment");
                }
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("insert attachment in attachmentService failed");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return id;
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
            LOG.error("error while processing delete attachment by id ");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public Attachment findById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            attachmentDAO.setConnection(connection);
            return attachmentDAO.findById(id).isPresent() ? attachmentDAO.findById(id).get() : new Attachment();
        } catch (GenericDAOException e) {
            LOG.error("error while processing get attachment by id");
            throw new ServiceException();
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public void updateById(Long id, Attachment attachment) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            attachmentDAO.setConnection(connection);
            if (StringUtils.isEmpty(attachment.getComment())) {
                attachmentDAO.updateFileNameById(id, attachment.getFileName());
            } else attachmentDAO.updateById(id, attachment);
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing get attachment by id");
            throw new ServiceException();
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
