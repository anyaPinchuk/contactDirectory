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
    public List<Address> findAll() throws GenericDAOException {
        return null;
    }

    @Override
    public List<Address> findAllById(Long contact_id) throws GenericDAOException {
        return null;
    }

    @Override
    public Optional<? extends Address> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            LOG.info("findById Address starting");
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM anya_pinchuk.address WHERE id = ? LIMIT 1")) {
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
        });
    }

    private Optional<Address> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String country = resultSet.getString("country");
        String city = resultSet.getString("city");
        String streetAddress = resultSet.getString("street_address");
        String index = resultSet.getString("index");
        return Optional.of(new Address(id, country, city, streetAddress, index));
    }

    @Override
    public Optional<? extends Address> findByField(Object field) throws GenericDAOException {
        return null;
    }

    @Override
    public int updateById(Long id, Address entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE anya_pinchuk.address SET country = ?, " +
                    "city = ?, street_address = ?, `index` = ? WHERE id = ?")) {
                LOG.info("updateById Address starting");
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
        });
    }

    @Override
    public Long insert(Address entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO anya_pinchuk.address (country, city, street_address," +
                    " `index`) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                LOG.info("insert Address starting");
                statement.setString(1, entity.getCountry());
                statement.setString(2, entity.getCity());
                statement.setString(3, entity.getStreetAddress());
                statement.setString(4, entity.getIndex());
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
        });
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM anya_pinchuk.address WHERE id = ?")) {
                LOG.info("deleteById Address starting");
                statement.setLong(1, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Address wasn't deleted", e);
                throw new GenericDAOException(e);
            }
        });
    }
}
