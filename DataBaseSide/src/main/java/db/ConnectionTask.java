package db;

import exceptions.GenericDAOException;

import java.sql.Statement;

@FunctionalInterface
public interface ConnectionTask<T> {
    T execute(Statement statement) throws GenericDAOException;
}
