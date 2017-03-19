package dao;


import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ContactDAO extends AbstractDAO<Contact> {

    @Override
    public List<Contact> findAll() throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            LOG.info("findAll Contact starting");
            List<Contact> contacts = new LinkedList<>();
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.contact")) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String thirdName = resultSet.getString("thirdName");
                    String birthDate = resultSet.getString("dateOfBirth");
                    String sex = resultSet.getString("sex");
                    String citizenship = resultSet.getString("citizenship");
                    String status = resultSet.getString("maritalStatus");
                    String webSite = resultSet.getString("webSite");
                    String email = resultSet.getString("email");
                    String job = resultSet.getString("job");
                    Long address_id = resultSet.getLong("Address_id");
                    Long photo_id = resultSet.getLong("photo_id");
                    contacts.add(new Contact(id, name, surname, thirdName, birthDate, sex, citizenship,
                            status, webSite, email, job, photo_id, address_id));
                }
                return contacts;
            } catch (SQLException e) {
                LOG.error("Contacts weren't found", e);
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Optional<? extends Contact> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            LOG.info("findById Contact starting");
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.contact WHERE id = " + id + " LIMIT 1")) {
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
                LOG.error("Contact wasn't found", e);
                throw new GenericDAOException(e);
            }
            return Optional.empty();
        });
    }

    private Optional<Contact> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String thirdName = resultSet.getString("thirdName");
        String birthDate = resultSet.getString("dateOfBirth");
        String sex = resultSet.getString("sex");
        String citizenship = resultSet.getString("citizenship");
        String status = resultSet.getString("maritalStatus");
        String webSite = resultSet.getString("webSite");
        String email = resultSet.getString("email");
        String job = resultSet.getString("job");
        Long address_id = resultSet.getLong("Address_id");
        Long photo_id = resultSet.getLong("photo_id");
        return Optional.of(new Contact(id, name, surname, thirdName, birthDate, sex, citizenship,
                status, webSite, email, job, photo_id, address_id));
    }

    @Override
    public Optional<? extends Contact> findByField(Object field) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.contact WHERE email = '"
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
    public int updateById(Long id, Contact entity) throws GenericDAOException {
        if (entity == null) return 0;
        return connectionAwareExecutor.submit(statement -> {
            try {
                String query = buildQueryForUpdate(entity.getDateOfBirth().equals(""), entity.getAddress_id() == null,
                        entity.getPhoto_id() == null, entity);
                LOG.info("updateById Contact starting");
                return statement.executeUpdate(query);
            } catch (SQLException e) {
                LOG.error("Contact wasn't updated", e);
                throw new GenericDAOException(e);
            }
        });
    }

    private String buildQueryForUpdate(boolean isDateNull, boolean isAddressNull, boolean isPhotoNull, Contact entity) {
        StringBuilder builder = new StringBuilder("UPDATE contacts.contact SET ");
        builder.append("name = '").append(entity.getName()).append("', surname = '").append(entity.getSurname())
                .append("', thirdName = '").append(entity.getThirdName());
        if (!isDateNull) {
            builder.append("', dateOfBirth = '").append(entity.getDateOfBirth());
        }
        builder.append("', sex = '").append(entity.getSex()).append("', citizenship = '").append(entity.getCitizenship())
                .append("', maritalStatus = '").append(entity.getMaritalStatus()).append("', webSite = '")
                .append(entity.getWebSite()).append("', email = '").append(entity.getEmail()).append("', job = '")
                .append(entity.getJob());
        if (!isAddressNull) {
            builder.append("', Address_id = '").append(entity.getAddress_id());
        }
        if (!isPhotoNull) {
            builder.append("', photo_id = '").append(entity.getPhoto_id());
        }
        builder.append("' WHERE id = ").append(entity.getId());
        return builder.toString();
    }

    @Override
    public Long insert(Contact entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                int result;
                LOG.info("insert Contact starting");
                if (entity.getDateOfBirth().equals("")) {
                    result = statement.executeUpdate("INSERT INTO contacts.contact (name, surname, thirdName," +
                            " sex, citizenship, maritalStatus, webSite, email, job) VALUES ('"
                            + entity.getName()
                            + "','" + entity.getSurname()
                            + "','" + entity.getThirdName()
                            + "','" + entity.getSex()
                            + "','" + entity.getCitizenship()
                            + "','" + entity.getMaritalStatus()
                            + "','" + entity.getWebSite()
                            + "','" + entity.getEmail()
                            + "','" + entity.getJob()
                            + "')", Statement.RETURN_GENERATED_KEYS);
                } else {
                    result = statement.executeUpdate("INSERT INTO contacts.contact (name, surname, thirdName," +
                            " dateOfBirth, sex, citizenship, maritalStatus, webSite, email, job) VALUES ('"
                            + entity.getName()
                            + "','" + entity.getSurname()
                            + "','" + entity.getThirdName()
                            + "','" + entity.getDateOfBirth()
                            + "','" + entity.getSex()
                            + "','" + entity.getCitizenship()
                            + "','" + entity.getMaritalStatus()
                            + "','" + entity.getWebSite()
                            + "','" + entity.getEmail()
                            + "','" + entity.getJob()
                            + "')", Statement.RETURN_GENERATED_KEYS);
                }
                if (result == 0) {
                    throw new SQLException("Creating contact failed, contact wasn't added");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getLong(1);
                    else return 0L;
                }
            } catch (SQLException e) {
                LOG.error("Contact wasn't inserted", e);
                if (e.getErrorCode() == 1062)
                    throw new UniqueDAOException(e);
                else throw new GenericDAOException(e);
            }
        });
    }

    public Long insertWithAddress(Contact entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                int result;
                LOG.info("insert Contact starting");
                if (entity.getDateOfBirth().equals("")) {
                    result = statement.executeUpdate("INSERT INTO contacts.contact (name, surname, thirdName," +
                            " sex, citizenship, maritalStatus, webSite, email, job, Address_id) VALUES ('"
                            + entity.getName()
                            + "','" + entity.getSurname()
                            + "','" + entity.getThirdName()
                            + "','" + entity.getSex()
                            + "','" + entity.getCitizenship()
                            + "','" + entity.getMaritalStatus()
                            + "','" + entity.getWebSite()
                            + "','" + entity.getEmail()
                            + "','" + entity.getJob()
                            + "','" + entity.getAddress_id()
                            + "')", Statement.RETURN_GENERATED_KEYS);
                } else {
                    result = statement.executeUpdate("INSERT INTO contacts.contact (name, surname, thirdName," +
                            " dateOfBirth, sex, citizenship, maritalStatus, webSite, email, job, Address_id) VALUES ('"
                            + entity.getName()
                            + "','" + entity.getSurname()
                            + "','" + entity.getThirdName()
                            + "','" + entity.getDateOfBirth()
                            + "','" + entity.getSex()
                            + "','" + entity.getCitizenship()
                            + "','" + entity.getMaritalStatus()
                            + "','" + entity.getWebSite()
                            + "','" + entity.getEmail()
                            + "','" + entity.getJob()
                            + "','" + entity.getAddress_id()
                            + "')", Statement.RETURN_GENERATED_KEYS);
                }
                if (result == 0) {
                    throw new SQLException("Creating contact failed, contact wasn't added");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getLong(1);
                    else return 0L;
                }
            } catch (SQLException e) {
                LOG.error("Contact wasn't inserted", e);
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
                LOG.info("deleteById Contact starting");
                return statement.executeUpdate("DELETE FROM contacts.contact WHERE id = '" + id + "'");
            } catch (SQLException e) {
                LOG.error("Contact wasn't deleted", e);
                throw new GenericDAOException(e);
            }
        });
    }
}
