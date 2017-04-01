package services;

import dao.AttachmentDAO;
import entities.Attachment;
import exceptions.GenericDAOException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
            LOG.error("error while processing get all attachments by id in attachmentService");
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return null;
    }

    public List<Long> insertAttachments(List<Attachment> attachmentsForInsert, Long contactId) {
        List<Long> ids = new ArrayList<>();
        if (CollectionUtils.isEmpty(attachmentsForInsert) || contactId == 0) return ids;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            attachmentDAO.setConnection(connection);
            attachmentsForInsert.forEach(obj -> {
                obj.setContactId(contactId);
                try {
                    ids.add(attachmentDAO.insert(obj));
                } catch (GenericDAOException e) {
                    LOG.error("error while processing insert attachments in AttachmentService");
                }
            });
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing insert attachment in attachmentService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return ids;
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
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public Attachment findById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            attachmentDAO.setConnection(connection);
            return attachmentDAO.findById(id).isPresent() ? attachmentDAO.findById(id).get() : null;
        } catch (GenericDAOException e) {
            LOG.error("error while processing get attachment by id in attachmentService");
        }finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return null;
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
            LOG.error("error while processing get attachment by id in attachmentService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
