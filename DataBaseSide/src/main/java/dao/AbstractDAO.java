package dao;

import db.ConnectionAwareExecutor;
import exceptions.GenericDAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;


public abstract class AbstractDAO<T> {

    protected static final Logger LOG = LoggerFactory.getLogger("dao");
    protected ConnectionAwareExecutor connectionAwareExecutor = new ConnectionAwareExecutor();
    protected Connection connection;

    public abstract List<T> findAllById(Long contactId) throws GenericDAOException;

    public abstract Optional<? extends T> findById(Long id) throws GenericDAOException;

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
