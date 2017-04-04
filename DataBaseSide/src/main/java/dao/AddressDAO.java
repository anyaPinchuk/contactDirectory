package dao;


import entities.Address;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


public class AddressDAO extends AbstractDAO<Address> {

    @Override
    public List<Address> findAllById(Long contactId) throws GenericDAOException {
        return null;
    }

    @Override
    public Optional<? extends Address> findById(Long id) throws GenericDAOException {
            LOG.info("find Address starting by id {}", id);
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM address WHERE contact_id = ? LIMIT 1")) {
                statement.setLong(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                LOG.error("Address wasn't found", e);
                throw new GenericDAOException(e);
            } finally {
                connectionAwareExecutor.closeResultSet(resultSet);
            }
            return Optional.empty();
    }

    private Optional<Address> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String country = resultSet.getString("country");
        String city = resultSet.getString("city");
        String streetAddress = resultSet.getString("street_address");
        String index = resultSet.getString("index");
        Long contactId = resultSet.getLong("contact_id");
        return Optional.of(new Address(id, country, city, streetAddress, index, contactId));
    }

    @Override
    public int updateById(Long id, Address entity) throws GenericDAOException {
        LOG.info("update Address starting By Id {}", id);
        if (entity == null) return 0;
            try (PreparedStatement statement = connection.prepareStatement("UPDATE address SET country = ?, " +
                    "city = ?, street_address = ?, `index` = ? WHERE contact_id = ?")) {
                statement.setString(1, entity.getCountry());
                statement.setString(2, entity.getCity());
                statement.setString(3, entity.getStreetAddress());
                statement.setString(4, entity.getIndex());
                statement.setLong(5, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Address wasn't updated", e);
                throw new GenericDAOException(e);
            }
    }

    @Override
    public Long insert(Address entity) throws GenericDAOException {
        LOG.info("insert Address starting");
        if (entity == null) return 0L;
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO address (country, city, street_address," +
                    " `index`, contact_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getCountry());
                statement.setString(2, entity.getCity());
                statement.setString(3, entity.getStreetAddress());
                statement.setString(4, entity.getIndex());
                statement.setLong(5, entity.getContactId());
                int result = statement.executeUpdate();
                if (result == 0) {
                    throw new SQLException("Creating address failed, address wasn't added");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getLong(1);
                    else return 0L;
                }
            } catch (SQLException e) {
                LOG.error("Address wasn't inserted", e);
                if (e.getErrorCode() == 1062)
                    throw new UniqueDAOException(e);
                else throw new GenericDAOException(e);
            }
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        LOG.info("deleteById Address starting bi id {}", id);
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM address WHERE id = ?")) {
                statement.setLong(1, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Address wasn't deleted", e);
                throw new GenericDAOException(e);
            }
    }
}
