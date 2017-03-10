package dao;

import entities.Address;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

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
    public Optional<? extends Address> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findById User starting");
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.address WHERE id = " + id + " LIMIT 1")) {
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
            return Optional.empty();
        });
    }

    private Optional<Address> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String country = resultSet.getString("country");
        String city = resultSet.getString("city");
        String streetAddress = resultSet.getString("streetAddress");
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
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("updateById user starting");
                    return statement.executeUpdate("UPDATE contacts.address SET " +
                            "country = '" + entity.getCountry()
                            + "', city = '" + entity.getCity()
                            + "', streetAddress = '" + entity.getStreetAddress()
                            + "', `index` = '" + entity.getIndex()
                            + "' WHERE id = " + id);

            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(Address entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("insert user starting");
                int result = statement.executeUpdate("INSERT INTO contacts.address (country, city, streetAddress," +
                        " `index`) VALUES ('"
                        + entity.getCountry()
                        + "','" + entity.getCity()
                        + "','" + entity.getStreetAddress()
                        + "','" + entity.getIndex()
                        + "')", Statement.RETURN_GENERATED_KEYS);
                if (result == 0) {
                    throw new SQLException("Creating address failed, address wasn't added");
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
                return statement.executeUpdate("DELETE FROM contacts.address WHERE id = '" + id + "'");
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }
}
