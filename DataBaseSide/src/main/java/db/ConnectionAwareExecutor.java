package db;

import exceptions.GenericDAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class ConnectionAwareExecutor {
    private static final String DB_URL = "jdbc:mysql://";
    protected static final Logger LOG = LoggerFactory.getLogger(ConnectionAwareExecutor.class);

    private static Properties getEnvironmentProperties() throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(ConnectionAwareExecutor.class.getClassLoader().getResourceAsStream("environment.xml"));
        return properties;
    }
    public Connection connect() throws GenericDAOException {
        try {
            Properties properties = getEnvironmentProperties();
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
            LOG.error("error while connecting to DB");
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
