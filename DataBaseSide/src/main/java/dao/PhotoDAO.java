package dao;

import entities.Address;
import entities.Contact;
import entities.Photo;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class PhotoDAO extends AbstractDAO<Photo>{

    @Override
    public List<Photo> findAll() throws GenericDAOException {
        return null;
    }

    @Override
    public Optional<? extends Photo> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            LOG.info("findById photo starting");
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.photo WHERE id = " + id + " LIMIT 1")) {
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                LOG.error("Address wasn't found", e);
                throw new GenericDAOException(e);
            }
            return Optional.empty();
        });
    }

    private Optional<Photo> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String pathToFile = resultSet.getString("pathToFile");
        return Optional.of(new Photo(name, pathToFile));
    }

    @Override
    public Optional<? extends Photo> findByField(Object field) throws GenericDAOException {
            return connectionAwareExecutor.submit(statement -> {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.photo WHERE name = '"
                        + field + "' LIMIT 1")) {
                    if (resultSet.next())
                        return buildEntityFromResult(resultSet);
                } catch (SQLException e) {
                    throw new GenericDAOException(e);
                }
                return Optional.empty();
            });
    }

    @Override
    public int updateById(Long id, Photo entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(statement -> {
            try {
                LOG.info("updateById photo starting");
                return statement.executeUpdate("UPDATE contacts.photo SET " +
                        "name = '" + entity.getName()
                        + "', pathToFile = '" + entity.getPathToFile()
                        + "' WHERE id = " + id);

            } catch (SQLException e) {
                LOG.error("Address wasn't updated", e);
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(Photo entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                LOG.info("insert Address starting");
                int result = statement.executeUpdate("INSERT INTO contacts.photo (name, pathToFile" +
                        " ) VALUES ('"
                        + entity.getName()
                        + "','" + entity.getPathToFile()
                        + "')", Statement.RETURN_GENERATED_KEYS);
                if (result == 0) {
                    throw new SQLException("Creating photo failed, address wasn't added");
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
        });
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            try {
                LOG.info("deleteById photo starting");
                return statement.executeUpdate("DELETE FROM contacts.photo WHERE id = '" + id + "'");
            } catch (SQLException e) {
                LOG.error("photo wasn't deleted", e);
                throw new GenericDAOException(e);
            }
        });
    }
}
