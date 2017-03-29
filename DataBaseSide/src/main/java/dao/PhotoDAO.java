package dao;

import entities.Address;
import entities.Contact;
import entities.Photo;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class PhotoDAO extends AbstractDAO<Photo> {

    @Override
    public List<Photo> findAll() throws GenericDAOException {
        return null;
    }

    @Override
    public List<Photo> findAllById(Long contact_id) throws GenericDAOException {
        return null;
    }

    @Override
    public Optional<? extends Photo> findById(Long contactId) throws GenericDAOException {
        LOG.info("find photo starting By Id {}", contactId);
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM photo WHERE contact_id = ? LIMIT 1")) {
            statement.setLong(1, contactId);
            resultSet = statement.executeQuery();
            if (resultSet.next())
                return buildEntityFromResult(resultSet);
        } catch (SQLException e) {
            LOG.error("Photo wasn't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
        return Optional.empty();
    }

    private Optional<Photo> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        LOG.info("buildEntityFromResult starting");
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        Long contactId = resultSet.getLong("contact_id");
        return Optional.of(new Photo(id, name, contactId));
    }

    @Override
    public Optional<? extends Photo> findByField(Object field) throws GenericDAOException {
        LOG.info("find photo starting By field {}", field);
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM photo WHERE name = ? LIMIT 1")) {
            statement.setString(1, (String) field);
            resultSet = statement.executeQuery();
            if (resultSet.next())
                return buildEntityFromResult(resultSet);
        } catch (SQLException e) {
            LOG.error("Photo wasn't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
        return Optional.empty();
    }

    @Override
    public int updateById(Long id, Photo entity) throws GenericDAOException {
        LOG.info("update photo starting By Id {}", id);
        if (entity == null) return 0;
        try (PreparedStatement statement = connection.prepareStatement("UPDATE photo SET name = ? WHERE contact_id = ?")) {
            statement.setString(1, entity.getName());
            statement.setLong(2, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("photo wasn't updated", e);
            throw new GenericDAOException(e);
        }
    }

    @Override
    public Long insert(Photo entity) throws GenericDAOException {
        LOG.info("insert Photo starting");
        if (entity == null) return 0L;
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO photo (name, contact_id) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getContactId());
            int result = statement.executeUpdate();
            if (result == 0) {
                throw new SQLException("Creating photo failed, Photo wasn't added");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    return generatedKeys.getLong(1);
                else return 0L;
            }
        } catch (SQLException e) {
            LOG.error("photo wasn't inserted", e);
            if (e.getErrorCode() == 1062)
                throw new UniqueDAOException(e);
            else throw new GenericDAOException(e);
        }
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        LOG.info("deleteById photo starting by id {}", id);
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM photo WHERE id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("photo wasn't deleted", e);
            throw new GenericDAOException(e);
        }
    }
}
