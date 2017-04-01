package services;

import dao.PhoneDAO;
import entities.PhoneNumber;
import exceptions.GenericDAOException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PhoneService implements ServiceEntity {
    private PhoneDAO phoneDAO = new PhoneDAO();
    private static final Logger LOG = LoggerFactory.getLogger(ContactService.class);

    public List<PhoneNumber> findAllById(Long id) {
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            phoneDAO.setConnection(connection);
            return phoneDAO.findAllById(id);
        } catch (GenericDAOException e) {
            LOG.error("error while processing get all phones by id in PhoneService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
        return null;
    }

    public void insertPhones(List<PhoneNumber> numbersForInsert, Long contactId) {
        if (CollectionUtils.isEmpty(numbersForInsert) || contactId == 0) return;
        Connection connection = null;
        try {
            connection = connectionAwareExecutor.connect();
            connection.setAutoCommit(false);
            phoneDAO.setConnection(connection);
            numbersForInsert.forEach(obj -> {
                obj.setContactId(contactId);
                try {
                    phoneDAO.insert(obj);
                } catch (GenericDAOException e) {
                    LOG.error("error while processing insert phones in phoneService");
                }
            });
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }

    public void updatePhones(Long contact_id, List<PhoneNumber> phoneNumbersForUpdate) {
        Connection connection = null;
        try {
            List<PhoneNumber> numbers = phoneDAO.findAllById(contact_id);
            if (CollectionUtils.isEmpty(numbers)) return;
            connection = connectionAwareExecutor.connect();
            phoneDAO.setConnection(connection);
            connection.setAutoCommit(false);
            phoneNumbersForUpdate.forEach(obj -> {
                try {
                    if (numbers.contains(obj)) {
                        numbers.remove(obj);
                        phoneDAO.updateById(obj.getId(), obj);
                    }
                } catch (GenericDAOException e) {
                    LOG.error("error while processing update phones in phoneService");
                }
            });
            numbers.forEach(obj -> {
                try {
                    phoneDAO.deleteById(obj.getId());
                } catch (GenericDAOException e) {
                    LOG.error("error while processing delete phone by id in phoneService");
                }
            });
            connection.commit();
        } catch (GenericDAOException | SQLException e) {
            connectionAwareExecutor.rollbackConnection(connection);
            LOG.error("error while processing get phones in phoneService");
        } finally {
            connectionAwareExecutor.closeConnection(connection);
        }
    }
}
