package db;

import exceptions.GenericDAOException;

import java.sql.Connection;
import java.sql.Statement;

@FunctionalInterface
public interface ConnectionTask<T> {
    T execute(Connection connection) throws GenericDAOException;
}
