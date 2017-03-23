package dao;

import entities.Attachment;
import entities.PhoneNumber;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.PreparedStatement;
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
        return connectionAwareExecutor.submit(connection -> {
            LOG.info("findAll Attachment starting");
            ResultSet resultSet = null;
            List<Attachment> attachments = new LinkedList<>();
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM anya_pinchuk.attachment" +
                    " WHERE contact_id = ?")) {
                statement.setLong(1, id_contact);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    java.sql.Date dateOfDownload = resultSet.getDate("date_of_download");
                    String fileName = resultSet.getString("file_name");
                    String comment = resultSet.getString("comment");
                    attachments.add(new Attachment(id, dateOfDownload, fileName, comment, id_contact));
                }
                return attachments;
            } catch (SQLException e) {
                LOG.error("Attachments weren't found", e);
                throw new GenericDAOException(e);
            }
            finally {
                connectionAwareExecutor.closeResultSet(resultSet);
            }
        });
    }

    @Override
    public Optional<? extends Attachment> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            LOG.info("findById Attachment starting");
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM anya_pinchuk.attachment WHERE id = ? LIMIT 1")) {
                statement.setLong(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                LOG.error("Attachment wasn't found", e);
                throw new GenericDAOException(e);
            }finally {
                connectionAwareExecutor.closeResultSet(resultSet);
            }
            return Optional.empty();
        });
    }

    private Optional<Attachment> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        java.sql.Date dateOfDownload = resultSet.getDate("date_of_download");
        String fileName = resultSet.getString("file_name");
        String comment = resultSet.getString("comment");
        Long contact_id = resultSet.getLong("contact_id");
        return Optional.of(new Attachment(id, dateOfDownload, fileName, comment, contact_id));
    }

    @Override
    public Optional<? extends Attachment> findByField(Object field) throws GenericDAOException {
        return null;
    }

    @Override
    public int updateById(Long id, Attachment entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE anya_pinchuk.attachment SET date_of_download = ?, " +
                    "file_name = ?, comment = ?, contact_id = ? WHERE id = ?")) {
                LOG.info("updateById Attachment starting");
                statement.setDate(1, entity.getDateOfDownload());
                statement.setString(2, entity.getFileName());
                statement.setString(3, entity.getComment());
                statement.setLong(4, entity.getContact_id());
                statement.setLong(5, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Attachment wasn't updated", e);
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(Attachment entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO anya_pinchuk.attachment" +
                    " (date_of_download, file_name, comment, contact_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){
                LOG.info("insert Attachment starting");
                statement.setDate(1, entity.getDateOfDownload());
                statement.setString(2, entity.getFileName());
                statement.setString(3, entity.getComment());
                statement.setLong(4, entity.getContact_id());
                int result = statement.executeUpdate();
                if (result == 0) {
                    throw new SQLException("Creating attachment failed, attachment wasn't added");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getLong(1);
                    else return 0L;
                }
            } catch (SQLException e) {
                LOG.error("Attachment wasn't inserted", e);
                if (e.getErrorCode() == 1062)
                    throw new UniqueDAOException(e);
                else throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM anya_pinchuk.attachment WHERE contact_id = ?")){
                LOG.info("deleteById Attachment starting");
                statement.setLong(1, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Attachment wasn't deleted", e);
                throw new GenericDAOException(e);
            }
        });
    }
}
