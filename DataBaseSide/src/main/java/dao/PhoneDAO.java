package dao;

import entities.Contact;
import entities.PhoneNumber;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PhoneDAO extends AbstractDAO<PhoneNumber>{
    @Override
    public List<PhoneNumber> findAll() throws GenericDAOException {
        return null;
    }

    public List<PhoneNumber> findAllById(Long id_contact) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findAll Users starting");
            List<PhoneNumber> numbers = new LinkedList<>();
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.phonenumber" +
                    " WHERE Contact_id = " + id_contact )) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String countryCode = resultSet.getString("countryCode");
                    String operatorCode = resultSet.getString("operatorCode");
                    String number = resultSet.getString("number");
                    String numberType = resultSet.getString("numberType");
                    String comment = resultSet.getString("comment");
                    numbers.add(new PhoneNumber(id, countryCode, operatorCode, number, numberType, comment, id_contact));
                }
                return numbers;
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Optional<? extends PhoneNumber> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findById User starting");
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.phonenumber WHERE id = " + id + " LIMIT 1")) {
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
            return Optional.empty();
        });
    }

    private Optional<PhoneNumber> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String countryCode = resultSet.getString("countryCode");
        String operatorCode = resultSet.getString("operatorCode");
        String number = resultSet.getString("number");
        String numberType = resultSet.getString("numberType");
        String comment = resultSet.getString("comment");
        Long contact_id = resultSet.getLong("Contact_id");
        return Optional.of(new PhoneNumber(id, countryCode, operatorCode, number, numberType, comment, contact_id));
    }

    @Override
    public Optional<? extends PhoneNumber> findByField(Object field) throws GenericDAOException {
        return null;
    }

    @Override
    public int updateById(Long id, PhoneNumber entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("updateById user starting");

                    return statement.executeUpdate("UPDATE contacts.phonenumber SET " +
                            "countryCode = '" + entity.getCountryCode()
                            + "', operatorCode = '" + entity.getOperatorCode()
                            + "', number = '" + entity.getNumber()
                            + "', numberType = '" + entity.getNumberType()
                            + "', comment = '" + entity.getComment()
                            + "' WHERE id = " + id);
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(PhoneNumber entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("insert user starting");
                int result = statement.executeUpdate("INSERT INTO contacts.phonenumber (countryCode, operatorCode, number," +
                        " numberType, comment, Contact_id) VALUES ('"
                        + entity.getCountryCode()
                        + "','" + entity.getOperatorCode()
                        + "','" + entity.getNumber()
                        + "','" + entity.getNumberType()
                        + "','" + entity.getComment()
                        + "','" + entity.getContact_id()
                        + "')", Statement.RETURN_GENERATED_KEYS);
                if (result == 0) {
                    throw new SQLException("Creating phone failed, phone wasn't added");
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
                return statement.executeUpdate("DELETE FROM contacts.phonenumber WHERE id = '" + id + "'");
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }
}
