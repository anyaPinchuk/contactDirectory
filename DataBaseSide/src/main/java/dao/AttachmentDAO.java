package dao;

import entities.Attachment;
import entities.PhoneNumber;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AttachmentDAO extends AbstractDAO<Attachment>{
    @Override
    public List<Attachment> findAll() throws GenericDAOException {
        return null;
    }

    public List<Attachment> findAllById(Long id_contact) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findAll Users starting");
            List<Attachment> attachments = new LinkedList<>();
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.attachment" +
                    " WHERE Contact_id = " + id_contact )) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Date dateOfDownload = resultSet.getDate("dateOfDownload");
                    String fileName = resultSet.getString("fileName");
                    String comment = resultSet.getString("comment");
                    attachments.add(new Attachment(id, dateOfDownload, fileName, comment, id_contact));
                }
                return attachments;
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Optional<? extends Attachment> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findById User starting");
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.attachment WHERE id = " + id + " LIMIT 1")) {
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
            return Optional.empty();
        });
    }

    private Optional<Attachment> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Date dateOfDownload = resultSet.getDate("dateOfDownload");
        String fileName = resultSet.getString("fileName");
        String comment = resultSet.getString("comment");
        Long contact_id = resultSet.getLong("Contact_id");
        return Optional.of(new Attachment(id, dateOfDownload, fileName, comment, contact_id));
    }

    @Override
    public Optional<? extends Attachment> findByField(Object field) throws GenericDAOException {
        return null;
    }

    @Override
    public int updateById(Long id, Attachment entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("updateById user starting");
                return statement.executeUpdate("UPDATE contacts.attachment SET " +
                        "dateOfDownload = '" + entity.getDateOfDownload()
                        + "', fileName = '" + entity.getFileName()
                        + "', comment = '" + entity.getComment()
                        + "' WHERE id = " + id);
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(Attachment entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("insert user starting");
                int result = statement.executeUpdate("INSERT INTO contacts.attachment (dateOfDownload, fileName," +
                        " comment, Contact_id) VALUES ('"
                        + entity.getDateOfDownload()
                        + "','" + entity.getFileName()
                        + "','" + entity.getComment()
                        + "','" + entity.getContact_id()
                        + "')", Statement.RETURN_GENERATED_KEYS);
                if (result == 0) {
                    throw new SQLException("Creating attachment failed, attachment wasn't added");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getLong(1);
                    else return 0L;
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062)
                    throw new UniqueDAOException(e);
                else throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("deleteById user starting");
                return statement.executeUpdate("DELETE FROM contacts.attachment WHERE id = '" + id + "'");
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }
}
