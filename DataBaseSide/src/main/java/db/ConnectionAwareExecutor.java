package db;

import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static db.ManagerDB.DB_URL;


public class ConnectionAwareExecutor {

    protected static final Logger LOG = Logger.getLogger(ConnectionAwareExecutor.class);

    public Connection connect() throws GenericDAOException {
        try {
            Properties properties = ManagerDB.getEnvironmentProperties();
            String host = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String dbName = properties.getProperty("db_name");
            String useSSL = properties.getProperty("useSSL");
            String sqlUrl = DB_URL + host + ":" + port + "/" + dbName + "?useSSL=" + useSSL;

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection connection = DriverManager.getConnection(sqlUrl, user, password);

            DatabaseMetaData metaData = connection.getMetaData();
            LOG.info("dao Connected to " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            return connection;
        } catch (SQLException | IOException e) {
            throw new GenericDAOException(e);
        }
    }


    public void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void rollbackConnection(Connection connection) {
        try {
            if (connection != null)
                connection.rollback();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }


}
