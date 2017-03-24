package dao;

import entities.PhoneNumber;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PhoneDAO extends AbstractDAO<PhoneNumber>{
    @Override
    public List<PhoneNumber> findAll() throws GenericDAOException {
        return null;
    }

    public List<PhoneNumber> findAllById(Long id_contact) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            LOG.info("findAll Phones starting");
            ResultSet resultSet = null;
            List<PhoneNumber> numbers = new LinkedList<>();
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM anya_pinchuk.phone_number" +
                    " WHERE contact_id = ?")) {
                statement.setLong(1, id_contact);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String countryCode = resultSet.getString("country_code");
                    String operatorCode = resultSet.getString("operator_code");
                    String number = resultSet.getString("number");
                    String numberType = resultSet.getString("phone_type");
                    String comment = resultSet.getString("comment");
                    numbers.add(new PhoneNumber(id, countryCode, operatorCode, number, numberType, comment, id_contact));
                }
                return numbers;
            } catch (SQLException e) {
                LOG.error("Phones weren't found", e);
                throw new GenericDAOException(e);
            }finally {
                connectionAwareExecutor.closeResultSet(resultSet);
            }
        });
    }

    @Override
    public Optional<? extends PhoneNumber> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            LOG.info("findById Phone starting");
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM anya_pinchuk.phone_number WHERE id = ? LIMIT 1")) {
                statement.setLong(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                LOG.error("Phone wasn't found", e);
                throw new GenericDAOException(e);
            }finally {
                connectionAwareExecutor.closeResultSet(resultSet);
            }
            return Optional.empty();
        });
    }

    private Optional<PhoneNumber> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String countryCode = resultSet.getString("country_code");
        String operatorCode = resultSet.getString("operator_code");
        String number = resultSet.getString("number");
        String numberType = resultSet.getString("phone_type");
        String comment = resultSet.getString("comment");
        Long contact_id = resultSet.getLong("contact_id");
        return Optional.of(new PhoneNumber(id, countryCode, operatorCode, number, numberType, comment, contact_id));
    }

    @Override
    public Optional<? extends PhoneNumber> findByField(Object field) throws GenericDAOException {
        return null;
    }

    @Override
    public int updateById(Long id, PhoneNumber entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE anya_pinchuk.phone_number SET country_code = ?, " +
                    "operator_code = ?, number = ?, phone_type = ?, contact_id = ? WHERE id = ?")) {
                LOG.info("updateById Phone starting");
                statement.setString(1, entity.getCountryCode());
                statement.setString(2, entity.getOperatorCode());
                statement.setString(3, entity.getNumber());
                statement.setString(4, entity.getNumberType());
                statement.setLong(5,entity.getContact_id());
                statement.setLong(6, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Phone wasn't updated", e);
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(PhoneNumber entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO anya_pinchuk.phone_number" +
                    " (country_code, operator_code, number, phone_type, contact_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){
                LOG.info("insert PhoneNumber starting");
                statement.setString(1, entity.getCountryCode());
                statement.setString(2, entity.getOperatorCode());
                statement.setString(3, entity.getNumber());
                statement.setString(4, entity.getNumberType());
                statement.setLong(5, entity.getContact_id());
                int result = statement.executeUpdate();
                if (result == 0) {
                    throw new SQLException("Creating phone failed, phone wasn't added");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getLong(1);
                    else return 0L;
                }
            } catch (SQLException e) {
                LOG.error("Phone wasn't inserted", e);
                if (e.getErrorCode() == 1062)
                    throw new UniqueDAOException(e);
                else throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM anya_pinchuk.phone_number WHERE id = ?")){
                LOG.info("deleteById Phone starting");
                statement.setLong(1, id);
                return statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Phone wasn't deleted", e);
                throw new GenericDAOException(e);
            }
        });
    }
}
