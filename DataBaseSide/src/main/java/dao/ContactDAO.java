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

public class ContactDAO extends AbstractDAO<Contact>{

    @Override
    public List<Contact> findAll() throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findAll Users starting");
            List<Contact> contacts = new LinkedList<>();
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.contact")) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String thirdName = resultSet.getString("thirdName");
                    Date birthDate = resultSet.getDate("dateOfBirth");
                    String sex = resultSet.getString("sex");
                    String citizenship = resultSet.getString("citizenship");
                    String status = resultSet.getString("maritalStatus");
                    String webSite = resultSet.getString("webSite");
                    String email = resultSet.getString("email");
                    String job = resultSet.getString("job");
                    Long address_id = resultSet.getLong("Address_id");
                    contacts.add(new Contact(id, name, surname, thirdName, birthDate, sex, citizenship,
                            status, webSite, email, job, address_id));
                }
                return contacts;
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Optional<? extends Contact> findById(Long id) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            //LOG.info("findById User starting");
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.contact WHERE id = " + id + " LIMIT 1")) {
                if (resultSet.next())
                    return buildEntityFromResult(resultSet);
            } catch (SQLException e) {
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
        Date birthDate = resultSet.getDate("dateOfBirth");
        String sex = resultSet.getString("sex");
        String citizenship = resultSet.getString("citizenship");
        String status = resultSet.getString("maritalStatus");
        String webSite = resultSet.getString("webSite");
        String email = resultSet.getString("email");
        String job = resultSet.getString("job");
        Long address_id = resultSet.getLong("Address_id");
        return Optional.of(new Contact(id, name, surname, thirdName, birthDate, sex, citizenship,
                status, webSite, email, job, address_id));
    }

    @Override
    public Optional<? extends Contact> findByField(Object field) throws GenericDAOException {
        return connectionAwareExecutor.submit(statement -> {
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts.contact WHERE email = '" + field + "' LIMIT 1")) {
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
                //LOG.info("updateById user starting");
                if (entity.getDateOfBirth()==null){
                    return statement.executeUpdate("UPDATE contacts.contact SET " +
                            "name = '" + entity.getName()
                            + "', surname = '" + entity.getSurname()
                            + "', thirdName = '" + entity.getThirdName()
                            + "', sex = '" + entity.getSex()
                            + "', citizenship = '" + entity.getCitizenship()
                            + "', maritalStatus = '" + entity.getMaritalStatus()
                            + "', webSite = '" + entity.getWebSite()
                            + "', email = '" + entity.getEmail()
                            + "', job = '" + entity.getJob()
                            + "', Address_id = '" + entity.getAddress_id()
                            + "' WHERE id = " + id);
                }
                else{
                    return statement.executeUpdate("UPDATE contacts.contact SET " +
                            "name = '" + entity.getName()
                            + "', surname = '" + entity.getSurname()
                            + "', thirdName = '" + entity.getThirdName()
                            + "', dateOfBirth = '" + entity.getDateOfBirth()
                            + "', sex = '" + entity.getSex()
                            + "', citizenship = '" + entity.getCitizenship()
                            + "', maritalStatus = '" + entity.getMaritalStatus()
                            + "', webSite = '" + entity.getWebSite()
                            + "', email = '" + entity.getEmail()
                            + "', job = '" + entity.getJob()
                            + "', Address_id = '" + entity.getAddress_id()
                            + "' WHERE id = " + id);
                }

            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }

    @Override
    public Long insert(Contact entity) throws GenericDAOException {
        if (entity == null) return 0L;
        return connectionAwareExecutor.submit(statement -> {
            try {
                //LOG.info("insert user starting");
                int result = statement.executeUpdate("INSERT INTO contacts.contact (name, surname, thirdName," +
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
                if (result == 0) {
                    throw new SQLException("Creating contact failed, contact wasn't added");
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
                return statement.executeUpdate("DELETE FROM contacts.contact WHERE id = '" + id + "'");
            } catch (SQLException e) {
                throw new GenericDAOException(e);
            }
        });
    }
}
