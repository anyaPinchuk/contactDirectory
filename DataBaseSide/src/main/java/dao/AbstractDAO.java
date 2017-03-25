package dao;

import db.ConnectionAwareExecutor;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public abstract class AbstractDAO<T> {

    protected static final Logger LOG = Logger.getLogger("dao");
    protected ConnectionAwareExecutor connectionAwareExecutor = new ConnectionAwareExecutor();
    protected Connection connection;

    public abstract List<T> findAll() throws GenericDAOException;

    public abstract List<T> findAllById(Long contact_id) throws GenericDAOException;

    public abstract Optional<? extends T> findById(Long id) throws GenericDAOException;

    public abstract Optional<? extends T> findByField(Object field) throws GenericDAOException;

    public abstract int updateById(Long id, T entity) throws GenericDAOException;

    public abstract Long insert(T entity) throws GenericDAOException;

    public abstract int deleteById(Long id) throws GenericDAOException;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
