package dao;


import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.UniqueDAOException;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ContactDAO extends AbstractDAO<Contact> {

    @Override
    public List<Contact> findAll() throws GenericDAOException {
        LOG.info("findAll Contact starting");
        ResultSet resultSet = null;
        List<Contact> contacts = new LinkedList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact")) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                contacts.add(buildEntityFromResult(resultSet).get());
            }
            return contacts;
        } catch (SQLException e) {
            LOG.error("Contacts weren't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
    }

    public String findEmailById(Long id) throws GenericDAOException {
        LOG.info("findEmail by id starting");
        ResultSet resultSet = null;
        String email = "";
        try (PreparedStatement statement = connection.prepareStatement("SELECT email FROM contact WHERE id = ?")) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("email");
            }
            return email;
        } catch (SQLException e) {
            LOG.error("Contact wasn't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
    }

    public List<Contact> findByCriteria(Contact entity, Address address, String dateCriteria) throws GenericDAOException {
        LOG.info("findAll Contact starting");
        ResultSet resultSet = null;
        List<Contact> contacts = new LinkedList<>();
        String builder = "";
        builder = buildQuery(entity, address, dateCriteria);
        try (PreparedStatement statement = setParametersForSearch(connection, builder, entity, address)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                contacts.add(buildEntityFromResult(resultSet).get());
            }
            return contacts;
        } catch (SQLException e) {
            LOG.error("Contacts weren't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
    }

    private String buildQuery(Contact entity, Address address, String dateCriteria) {
        StringBuilder builder = new StringBuilder("SELECT * FROM contact c ");
        if (address != null) {
            builder.append("inner join address a on c.id = a.contact_id ");
            if (!address.getCountry().equals(""))
                builder.append("and a.country = ? ");
            if (!address.getCity().equals(""))
                builder.append("and a.city = ? ");
            if (!address.getStreetAddress().equals(""))
                builder.append("and a.street_address = ? ");
            if (!address.getIndex().equals(""))
                builder.append("and a.index = ? ");
        }
        builder.append("where ");
        if (!entity.getName().equals(""))
            builder.append("c.name = ? and ");
        if (!entity.getSurname().equals(""))
            builder.append("c.surname = ? and ");
        if (!entity.getThirdName().equals(""))
            builder.append("c.third_name = ? and ");
        if (entity.getDateOfBirth() != null) {
            builder.append("c.date_of_birth ");
            if (dateCriteria.equals("after"))
                builder.append(" > ? and ");
            else
                builder.append(" < ? and ");
        }
        if (!entity.getCitizenship().equals(""))
            builder.append("c.citizenship = ? and ");
        if (!entity.getGender().equals(""))
            builder.append("c.gender = ? and ");
        if (!entity.getMaritalStatus().equals(""))
            builder.append("c.marital_status = ? and ");
        builder.setLength(builder.length() - 4);
        return builder.toString();
    }

    private PreparedStatement setParametersForSearch(Connection connection, String builder, Contact entity,
                                                     Address address) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(builder);
        List<Object> parameters = new ArrayList<>();
        if (address != null) {
            parameters.add(address.getCountry());
            parameters.add(address.getCity());
            parameters.add(address.getStreetAddress());
            parameters.add(address.getIndex());
        }
        parameters.add(entity.getName());
        parameters.add(entity.getSurname());
        parameters.add(entity.getThirdName());
        if (entity.getDateOfBirth() != null) {
            parameters.add(entity.getDateOfBirth());
        }
        parameters.add(entity.getCitizenship());
        parameters.add(entity.getGender());
        parameters.add(entity.getMaritalStatus());
        int i = 1;
        for (Object arg : parameters) {
            if (arg instanceof Date) {
                statement.setDate(i, (Date) arg);
                i++;
            } else if (arg instanceof String) {
                if (!arg.equals("")) {
                    statement.setString(i, (String) arg);
                    i++;
                }
            }
        }
        return statement;
    }

    public List<Contact> findByParts(int startIndex, int totalCount) throws GenericDAOException {
        LOG.info("findByParts Contact starting");
        ResultSet resultSet = null;
        List<Contact> contacts = new LinkedList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact LIMIT ?, ?")) {
            statement.setInt(1, startIndex);
            statement.setInt(2, totalCount);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                contacts.add(buildEntityFromResult(resultSet).get());
            }
            return contacts;
        } catch (SQLException e) {
            LOG.error("Contacts weren't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
    }

    public int getCountRows() throws GenericDAOException {
        LOG.info("countRows Contact starting");
        ResultSet resultSet = null;
        int countRows;
        try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) FROM contact")) {
            resultSet = statement.executeQuery();
            resultSet.next();
            countRows = resultSet.getInt(1);
            return countRows;
        } catch (SQLException e) {
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Contact> findAllById(Long contact_id) throws GenericDAOException {
        return null;
    }

    @Override
    public Optional<? extends Contact> findById(Long id) throws GenericDAOException {
        LOG.info("findById Contact starting");
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact WHERE id = ? LIMIT 1")) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next())
                return buildEntityFromResult(resultSet);
        } catch (SQLException e) {
            LOG.error("Contact wasn't found", e);
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
        return Optional.empty();
    }

    private Optional<Contact> buildEntityFromResult(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String thirdName = resultSet.getString("third_name");
        java.sql.Date birthDate = resultSet.getDate("date_of_birth");
        String sex = resultSet.getString("gender");
        String citizenship = resultSet.getString("citizenship");
        String status = resultSet.getString("marital_status");
        String webSite = resultSet.getString("web_site");
        String email = resultSet.getString("email");
        String job = resultSet.getString("job");
        return Optional.of(new Contact(id, name, surname, thirdName, birthDate, sex, citizenship,
                status, webSite, email, job));
    }

    @Override
    public Optional<? extends Contact> findByField(Object field) throws GenericDAOException {
        return null;
    }


    @Override
    public int updateById(Long id, Contact entity) throws GenericDAOException {
        if (entity == null) return 0;
        StringBuilder builder = new StringBuilder("UPDATE contact SET name = ?, surname = ?, third_name = ?");
        if (entity.getDateOfBirth() != null) {
            builder.append(", date_of_birth = ?");
        }
        builder.append(", gender = ?").append(", citizenship = ?").append(", marital_status = ?")
                .append(", web_site = ?").append(", email = ?").append(", job = ?");
        builder.append(" WHERE id = ?");
        try (PreparedStatement statement = setParameters(connection, builder.toString(), entity)) {
            LOG.info("updateById Contact starting");
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Contact wasn't updated", e);
            throw new GenericDAOException(e);
        }
    }

    public PreparedStatement setParameters(Connection connection, String query, Contact entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        List<Object> parameters = new ArrayList<>();
        parameters.add(entity.getName());
        parameters.add(entity.getSurname());
        parameters.add(entity.getThirdName());
        if (entity.getDateOfBirth() != null) {
            parameters.add(entity.getDateOfBirth());
        }
        parameters.add(entity.getGender());
        parameters.add(entity.getCitizenship());
        parameters.add(entity.getMaritalStatus());
        parameters.add(entity.getWebSite());
        parameters.add(entity.getEmail());
        parameters.add(entity.getJob());
        if (entity.getId() != null)
            parameters.add(entity.getId());
        int i = 0;
        for (Object arg : parameters) {
            i++;
            if (arg instanceof Date) {
                statement.setDate(i, (Date) arg);
            } else if (arg instanceof Long) {
                statement.setLong(i, (Long) arg);
            } else if (arg instanceof String) {
                statement.setString(i, (String) arg);
            } else if (arg == null) {
                statement.setString(i, "");
            }

        }
        return statement;
    }

    @Override
    public Long insert(Contact entity) throws GenericDAOException {
        if (entity == null) return 0L;
        String query = "";
        if (entity.getDateOfBirth() != null) {
            query = "INSERT INTO anya_pinchuk.contact (name, surname, third_name, date_of_birth, gender, citizenship," +
                    " marital_status, web_site, email, job) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            query = "INSERT INTO anya_pinchuk.contact (name, surname, third_name, gender, citizenship, marital_status," +
                    " web_site, email, job) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        try (PreparedStatement statement = setParameters(connection, query, entity)) {
            LOG.info("insert Contact starting");
            int result = statement.executeUpdate();
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
    }

    @Override
    public int deleteById(Long id) throws GenericDAOException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM contact WHERE id = ?")) {
            LOG.info("deleteById Contact starting");
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Contact wasn't deleted", e);
            throw new GenericDAOException(e);
        }
    }

    public String findByEmail(String email) throws GenericDAOException {
        String name = "";
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM contact WHERE email = ? LIMIT 1")) {
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("name");
            }
            return name;
        } catch (SQLException e) {
            throw new GenericDAOException(e);
        } finally {
            connectionAwareExecutor.closeResultSet(resultSet);
        }
    }
}
