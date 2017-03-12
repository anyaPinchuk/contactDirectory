package dao;

import db.ConnectionAwareExecutor;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;


public abstract class AbstractDAO<T> {

    protected static final Logger LOG = Logger.getLogger("dao");
    protected ConnectionAwareExecutor connectionAwareExecutor = new ConnectionAwareExecutor();

    public abstract List<T> findAll() throws GenericDAOException;

    public abstract Optional<? extends T> findById(Long id) throws GenericDAOException;

    public abstract Optional<? extends T> findByField(Object field) throws GenericDAOException;

    public abstract int updateById(Long id, T entity) throws GenericDAOException;

    public abstract Long insert(T entity) throws GenericDAOException;

    public abstract int deleteById(Long id) throws GenericDAOException;

}
